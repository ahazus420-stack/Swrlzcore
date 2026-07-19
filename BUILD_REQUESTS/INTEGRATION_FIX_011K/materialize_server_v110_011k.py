#!/usr/bin/env python3
"""Materialize the exact INTEGRATION-FIX-011K SERVER v1.1.0 source ZIP.

The temporary branch retains a text-safe, lossless replacement bundle because
this checkpoint does not promote a binary into the canonical SERVER lane.
"""
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

BASE_SHA = "127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5"
BUNDLE_SHA = "8e2f07bd5fc2632dbf22280b95198c1a3cf15e975818f2e5b076880ae40108c6"
OUTPUT_SHA = "f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f"
PREFIX = "SWRLZ_NODE_HOST/"


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
    archive.extractall(destination)


def deterministic_zip(root: Path, output: Path) -> None:
    with ZipFile(output, "w", compression=ZIP_DEFLATED, compresslevel=9, strict_timestamps=True) as result:
        for path in sorted(p for p in root.rglob("*") if p.is_file()):
            relative = path.relative_to(root).as_posix()
            info = ZipInfo(PREFIX + relative, date_time=(1980, 1, 1, 0, 0, 0))
            info.compress_type = ZIP_DEFLATED
            info.create_system = 3
            mode = 0o755 if relative in {"gradlew", "scripts/test_presence_registry_011k.py"} else 0o644
            info.external_attr = (mode & 0xFFFF) << 16
            info.flag_bits = 0x800
            result.writestr(info, path.read_bytes(), compress_type=ZIP_DEFLATED, compresslevel=9)


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", type=Path, default=Path("SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.zip"))
    parser.add_argument("--output", type=Path, default=Path("SERVER_CFv1.1.0_SWRLZ.zip"))
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
        raise SystemExit("canonical SERVER v1.0.3 checksum mismatch")

    expected = {entry["path"]: entry["sha256"] for entry in manifest["changedPaths"]}
    with tempfile.TemporaryDirectory(prefix="swrlz-011k-") as temporary:
        temp = Path(temporary)
        replacement_root = temp / "replacements"
        replacement_root.mkdir()
        with tarfile.open(fileobj=io.BytesIO(bundle), mode="r:gz") as replacement_archive:
            safe_extract(replacement_archive, replacement_root)
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
                raise SystemExit(f"canonical ZIP integrity failure: {bad}")
            source.extractall(extracted)
        project = extracted / "SWRLZ_NODE_HOST"
        for relative in sorted(expected):
            target = project / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(replacement_project / relative, target)

        output = args.output.resolve()
        output.parent.mkdir(parents=True, exist_ok=True)
        deterministic_zip(project, output)

    if sha_file(output) != OUTPUT_SHA:
        raise SystemExit(f"candidate checksum mismatch: {sha_file(output)}")
    with ZipFile(output) as candidate:
        bad = candidate.testzip()
        if bad:
            raise SystemExit(f"candidate ZIP integrity failure: {bad}")
        if len(candidate.infolist()) != 69:
            raise SystemExit("candidate entry-count mismatch")
    print(f"PASS {output} {OUTPUT_SHA}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
