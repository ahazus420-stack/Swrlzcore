#!/usr/bin/env python3
"""Materialize the exact INTEGRATION-FIX-011T SERVER v1.1.1 source ZIP."""
from __future__ import annotations

import argparse
import base64
import hashlib
import io
import json
import shutil
import tarfile
import tempfile
from pathlib import Path
from zipfile import ZIP_DEFLATED, ZipFile, ZipInfo

BASE_SHA = "f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f"
BUNDLE_SHA = "396613db4e1d4e660382c4d63fd5a8f45aad3cadafa7b552a25d11590af22d79"
OUTPUT_SHA = "63ef4e92e4a582be8a9a81dcc193fc0608f9b8e14362f9a688be72668f4211c5"
PREFIX = "SWRLZ_NODE_HOST/"
EXPECTED_ENTRIES = 72


def sha_bytes(value: bytes) -> str:
    return hashlib.sha256(value).hexdigest()


def sha_file(path: Path) -> str:
    return sha_bytes(path.read_bytes())


def safe_extract(archive: tarfile.TarFile, destination: Path) -> None:
    root = destination.resolve()
    for member in archive.getmembers():
        target = (destination / member.name).resolve()
        if root != target and root not in target.parents:
            raise SystemExit(f"unsafe replacement path: {member.name}")
        if not member.isfile() and not member.isdir():
            raise SystemExit(f"unsupported replacement member: {member.name}")
    for member in archive.getmembers():
        target = destination / member.name
        if member.isdir():
            target.mkdir(parents=True, exist_ok=True)
            continue
        target.parent.mkdir(parents=True, exist_ok=True)
        source = archive.extractfile(member)
        if source is None:
            raise SystemExit(f"replacement member could not be read: {member.name}")
        with source, target.open("wb") as output:
            shutil.copyfileobj(source, output)


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


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", type=Path, default=Path("SERVER_CFv1.1.0_SWRLZ.zip"))
    parser.add_argument("--output", type=Path, default=Path("SERVER_CFv1.1.1_SWRLZ.zip"))
    args = parser.parse_args()

    here = Path(__file__).resolve().parent
    manifest = json.loads((here / "candidate-manifest.json").read_text(encoding="utf-8"))
    part_paths = sorted((here / "transport").glob("part-*.b64"))
    if not part_paths:
        raise SystemExit("replacement transport parts are missing")
    encoded = "".join(path.read_text(encoding="ascii") for path in part_paths)
    bundle = base64.b64decode(encoded, validate=True)
    if sha_bytes(bundle) != BUNDLE_SHA:
        raise SystemExit("replacement transport checksum mismatch")

    base = args.base.resolve()
    if sha_file(base) != BASE_SHA:
        raise SystemExit("SERVER v1.1.0 base checksum mismatch")

    expected = {entry["path"]: entry["sha256"] for entry in manifest["changedPaths"]}
    with tempfile.TemporaryDirectory(prefix="swrlz-011t-") as temporary:
        temp = Path(temporary)
        replacement_root = temp / "replacements"
        replacement_root.mkdir()
        with tarfile.open(fileobj=io.BytesIO(bundle), mode="r:gz") as archive:
            safe_extract(archive, replacement_root)
        replacement_project = replacement_root / "SWRLZ_NODE_HOST"
        actual = {
            p.relative_to(replacement_project).as_posix(): sha_file(p)
            for p in replacement_project.rglob("*")
            if p.is_file()
        }
        if actual != expected:
            raise SystemExit("replacement path or content limiter mismatch")

        extracted = temp / "base"
        with ZipFile(base) as source:
            bad = source.testzip()
            if bad:
                raise SystemExit(f"base ZIP integrity failure: {bad}")
            if len(source.infolist()) != manifest["base"]["entries"]:
                raise SystemExit("base entry-count mismatch")
            source.extractall(extracted)
        project = extracted / "SWRLZ_NODE_HOST"
        for relative in sorted(expected):
            target = project / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(replacement_project / relative, target)

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
