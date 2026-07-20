#!/usr/bin/env python3
"""Materialize the exact INTEGRATION-FIX-011T-B SERVER v1.1.1 source ZIP."""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import shutil
import tempfile
from pathlib import Path
from zipfile import ZIP_DEFLATED, ZipFile, ZipInfo

BASE_SHA = "f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f"
OUTPUT_SHA = "762d72e445a3a9fcb48da11905dbc0261b206060b55760dfa96fefbf1e9486e4"
PREFIX = "SWRLZ_NODE_HOST/"
EXPECTED_ENTRIES = 73
HUNK = re.compile(r"^@@ -(\d+)(?:,(\d+))? \+(\d+)(?:,(\d+))? @@")


def sha_bytes(value: bytes) -> str:
    return hashlib.sha256(value).hexdigest()


def sha_file(path: Path) -> str:
    return sha_bytes(path.read_bytes())


def patch_path(value: str) -> str | None:
    value = value.strip()
    if value == "/dev/null":
        return None
    if value.startswith("a/") or value.startswith("b/"):
        value = value[2:]
    path = Path(value)
    if path.is_absolute() or ".." in path.parts:
        raise SystemExit(f"unsafe patch path: {value}")
    return path.as_posix()


def apply_patch(project: Path, patch_file: Path) -> set[str]:
    lines = patch_file.read_text(encoding="utf-8").splitlines(keepends=True)
    index = 0
    changed: set[str] = set()
    while index < len(lines):
        if not lines[index].startswith("--- "):
            raise SystemExit(f"invalid patch header in {patch_file.name}: {lines[index]!r}")
        old_path = patch_path(lines[index][4:])
        index += 1
        if index >= len(lines) or not lines[index].startswith("+++ "):
            raise SystemExit(f"missing new-file header in {patch_file.name}")
        new_path = patch_path(lines[index][4:])
        index += 1
        target_path = new_path or old_path
        if target_path is None:
            raise SystemExit(f"delete-only patches are unsupported: {patch_file.name}")
        target = project / target_path
        original = [] if old_path is None else target.read_text(encoding="utf-8").splitlines(keepends=True)
        output: list[str] = []
        original_index = 0
        while index < len(lines) and lines[index].startswith("@@ "):
            match = HUNK.match(lines[index])
            if not match:
                raise SystemExit(f"invalid hunk header in {patch_file.name}: {lines[index]!r}")
            old_start = int(match.group(1))
            desired_index = max(0, old_start - 1)
            if desired_index < original_index:
                raise SystemExit(f"overlapping hunk in {patch_file.name}")
            output.extend(original[original_index:desired_index])
            original_index = desired_index
            index += 1
            while index < len(lines) and not lines[index].startswith("@@ ") and not lines[index].startswith("--- "):
                line = lines[index]
                index += 1
                if line.startswith("\\ No newline at end of file"):
                    continue
                if not line:
                    raise SystemExit(f"empty patch record in {patch_file.name}")
                marker, text = line[0], line[1:]
                if marker == " ":
                    if original_index >= len(original) or original[original_index] != text:
                        raise SystemExit(f"patch context mismatch in {patch_file.name}:{target_path}")
                    output.append(original[original_index])
                    original_index += 1
                elif marker == "-":
                    if original_index >= len(original) or original[original_index] != text:
                        raise SystemExit(f"patch removal mismatch in {patch_file.name}:{target_path}")
                    original_index += 1
                elif marker == "+":
                    output.append(text)
                else:
                    raise SystemExit(f"invalid patch record {marker!r} in {patch_file.name}")
        output.extend(original[original_index:])
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text("".join(output), encoding="utf-8", newline="")
        changed.add(target_path)
    return changed


def deterministic_zip(root: Path, output: Path) -> None:
    with ZipFile(output, "w", compression=ZIP_DEFLATED, compresslevel=9, strict_timestamps=True) as result:
        for path in sorted(p for p in root.rglob("*") if p.is_file()):
            relative = path.relative_to(root).as_posix()
            info = ZipInfo(PREFIX + relative, date_time=(1980, 1, 1, 0, 0, 0))
            info.compress_type = ZIP_DEFLATED
            info.create_system = 3
            mode = 0o755 if relative == "gradlew" or relative.startswith("scripts/") else 0o644
            info.external_attr = (mode & 0xFFFF) << 16
            info.flag_bits = 0x800
            result.writestr(info, path.read_bytes(), compress_type=ZIP_DEFLATED, compresslevel=9)


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", type=Path, default=Path("SERVER_CFv1.1.0_SWRLZ.zip"))
    parser.add_argument("--output", type=Path, default=Path("SERVER_CFv1.1.1_SWRLZ.zip"))
    args = parser.parse_args()

    here = Path(__file__).resolve().parent
    manifest = json.loads((here / "candidate-manifest.json").read_text(encoding="utf-8"))
    patch_paths = sorted((here / "patches").glob("*.patch"))
    if not patch_paths:
        raise SystemExit("patch transport is missing")
    expected_transport = {Path(item["path"]).name: item["sha256"] for item in manifest["transport"]["parts"]}
    actual_transport = {path.name: sha_file(path) for path in patch_paths}
    if actual_transport != expected_transport:
        raise SystemExit("patch transport checksum mismatch")

    base = args.base.resolve()
    if sha_file(base) != BASE_SHA:
        raise SystemExit("SERVER v1.1.0 base checksum mismatch")
    expected = {entry["path"]: entry["sha256"] for entry in manifest["changedPaths"]}

    with tempfile.TemporaryDirectory(prefix="swrlz-011tb-") as temporary:
        extracted = Path(temporary) / "base"
        with ZipFile(base) as source:
            bad = source.testzip()
            if bad:
                raise SystemExit(f"base ZIP integrity failure: {bad}")
            source.extractall(extracted)
        project = extracted / "SWRLZ_NODE_HOST"
        changed: set[str] = set()
        for patch in patch_paths:
            changed.update(apply_patch(project, patch))
        if changed != set(expected):
            raise SystemExit(f"patch changed-path limiter mismatch: {sorted(changed)}")
        actual = {relative: sha_file(project / relative) for relative in expected}
        if actual != expected:
            raise SystemExit("patched content checksum mismatch")

        output = args.output.resolve()
        output.parent.mkdir(parents=True, exist_ok=True)
        deterministic_zip(project, output)

    if sha_file(output) != OUTPUT_SHA:
        raise SystemExit(f"candidate checksum mismatch: {sha_file(output)}")
    with ZipFile(output) as candidate:
        bad = candidate.testzip()
        if bad:
            raise SystemExit(f"candidate ZIP integrity failure: {bad}")
        if len(candidate.infolist()) != EXPECTED_ENTRIES:
            raise SystemExit("candidate entry-count mismatch")
    print(f"PASS {output} {OUTPUT_SHA}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
