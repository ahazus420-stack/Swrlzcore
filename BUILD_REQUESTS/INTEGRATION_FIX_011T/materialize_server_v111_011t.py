#!/usr/bin/env python3
"""Materialize the exact INTEGRATION-FIX-011T SERVER v1.1.1 source ZIP."""
from __future__ import annotations

import argparse
import hashlib
import json
import tempfile
from pathlib import Path
from zipfile import ZIP_DEFLATED, ZipFile, ZipInfo

BASE_SHA = "f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f"
OUTPUT_SHA = "63ef4e92e4a582be8a9a81dcc193fc0608f9b8e14362f9a688be72668f4211c5"
PREFIX = "SWRLZ_NODE_HOST/"
EXPECTED_ENTRIES = 72


def sha_bytes(value: bytes) -> str:
    return hashlib.sha256(value).hexdigest()


def sha_file(path: Path) -> str:
    return sha_bytes(path.read_bytes())


def deterministic_zip(root: Path, output: Path) -> None:
    executable = {"gradlew", "scripts/test_presence_registry_011k.py", "scripts/test_paired_lan_resolve_011t.py"}
    with ZipFile(output, "w", compression=ZIP_DEFLATED, compresslevel=9, strict_timestamps=True) as result:
        for path in sorted(p for p in root.rglob("*") if p.is_file()):
            relative = path.relative_to(root).as_posix()
            info = ZipInfo(PREFIX + relative, date_time=(1980, 1, 1, 0, 0, 0))
            info.compress_type = ZIP_DEFLATED
            info.create_system = 3
            mode = 0o755 if relative in executable else 0o644
            info.external_attr = (mode & 0xFFFF) << 16
            info.flag_bits = 0x800
            result.writestr(info, path.read_bytes(), compress_type=ZIP_DEFLATED, compresslevel=9)


def replacement_bytes(here: Path, relative: str) -> bytes:
    direct = here / "replacements" / "SWRLZ_NODE_HOST" / relative
    fragment_base = here / "replacement_fragments" / "SWRLZ_NODE_HOST"
    fragments = sorted(fragment_base.glob(relative + ".part-*"))
    if direct.is_file() and fragments:
        raise SystemExit(f"ambiguous direct and fragmented replacement: {relative}")
    if direct.is_file():
        return direct.read_bytes()
    if fragments:
        return b"".join(path.read_bytes() for path in fragments)
    raise SystemExit(f"replacement evidence is missing: {relative}")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", type=Path, default=Path("SERVER_CFv1.1.0_SWRLZ.zip"))
    parser.add_argument("--output", type=Path, default=Path("SERVER_CFv1.1.1_SWRLZ.zip"))
    args = parser.parse_args()

    here = Path(__file__).resolve().parent
    manifest = json.loads((here / "candidate-manifest.json").read_text(encoding="utf-8"))
    expected = {entry["path"]: entry["sha256"] for entry in manifest["changedPaths"]}
    replacements = {relative: replacement_bytes(here, relative) for relative in expected}
    actual = {relative: sha_bytes(value) for relative, value in replacements.items()}
    if actual != expected:
        raise SystemExit("replacement path or content limiter mismatch")

    base = args.base.resolve()
    if sha_file(base) != BASE_SHA:
        raise SystemExit("SERVER v1.1.0 base checksum mismatch")

    with tempfile.TemporaryDirectory(prefix="swrlz-011t-") as temporary:
        extracted = Path(temporary) / "base"
        with ZipFile(base) as source:
            bad = source.testzip()
            if bad:
                raise SystemExit(f"base ZIP integrity failure: {bad}")
            if len(source.infolist()) != manifest["base"]["entries"]:
                raise SystemExit("base entry-count mismatch")
            source.extractall(extracted)
        project = extracted / "SWRLZ_NODE_HOST"
        for relative, value in sorted(replacements.items()):
            target = project / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(value)

        output = args.output.resolve()
        output.parent.mkdir(parents=True, exist_ok=True)
        deterministic_zip(project, output)

    actual_output_sha = sha_file(output)
    if actual_output_sha != OUTPUT_SHA:
        raise SystemExit(f"candidate checksum mismatch: {actual_output_sha}")
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
