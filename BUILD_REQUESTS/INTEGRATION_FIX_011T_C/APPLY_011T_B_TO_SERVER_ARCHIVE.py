#!/usr/bin/env python3
"""Deterministically apply the approved 011T-B overlay to a canonical SERVER ZIP.

No Gradle, APK, workflow, install, release, or deployment behavior exists here.
The script fails closed on checksum, layout, duplicate-path, or unexpected-path errors.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import os
import shutil
import stat
import tempfile
import zipfile
from pathlib import Path

OVERLAY_ROOT = Path("BUILD_REQUESTS/INTEGRATION_FIX_011T_B/files")
EXPECTED_OVERLAY_PATHS = (
    "app/src/main/java/sh/swrlz/nodehost/security/AndroidKeystoreProofCipher.kt",
    "app/src/main/java/sh/swrlz/nodehost/security/DeviceProofModels.kt",
    "app/src/main/java/sh/swrlz/nodehost/security/HkdfSha256.kt",
    "app/src/main/java/sh/swrlz/nodehost/security/PairedLanAuthorizer.kt",
    "app/src/main/java/sh/swrlz/nodehost/security/ProofBindingSidecar.kt",
    "app/src/main/java/sh/swrlz/nodehost/security/RequestProofVerifier.kt",
    "app/src/main/java/sh/swrlz/nodehost/service/NonMutatingDeviceResolution.kt",
)
FIXED_ZIP_DT = (2026, 7, 20, 0, 0, 0)


def sha256(path: Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b""):
            h.update(chunk)
    return h.hexdigest()


def parse_expected_sha(path: Path) -> str:
    token = path.read_text(encoding="utf-8").strip().split()[0].lower()
    if len(token) != 64 or any(c not in "0123456789abcdef" for c in token):
        raise SystemExit(f"Invalid SHA-256 file: {path}")
    return token


def locate_project_root(extracted: Path) -> Path:
    candidates = []
    for p in extracted.rglob("settings.gradle*"):
        root = p.parent
        if (root / "app" / "src" / "main" / "java" / "sh" / "swrlz" / "nodehost").is_dir():
            candidates.append(root)
    if len(candidates) != 1:
        raise SystemExit(f"Expected exactly one SERVER project root, found {len(candidates)}")
    return candidates[0]


def deterministic_zip(source_root: Path, output_zip: Path) -> None:
    entries = sorted(p for p in source_root.rglob("*") if p.is_file())
    with zipfile.ZipFile(output_zip, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as zf:
        for path in entries:
            rel = path.relative_to(source_root).as_posix()
            info = zipfile.ZipInfo(rel, FIXED_ZIP_DT)
            mode = stat.S_IMODE(path.stat().st_mode)
            info.external_attr = (mode & 0xFFFF) << 16
            info.compress_type = zipfile.ZIP_DEFLATED
            zf.writestr(info, path.read_bytes())


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--input-zip", required=True, type=Path)
    ap.add_argument("--input-sha256", required=True, type=Path)
    ap.add_argument("--output-zip", required=True, type=Path)
    ap.add_argument("--manifest", required=True, type=Path)
    args = ap.parse_args()

    expected = parse_expected_sha(args.input_sha256)
    actual = sha256(args.input_zip)
    if actual != expected:
        raise SystemExit(f"Canonical archive checksum mismatch: expected {expected}, got {actual}")

    actual_overlay = tuple(sorted(p.relative_to(OVERLAY_ROOT).as_posix() for p in OVERLAY_ROOT.rglob("*") if p.is_file()))
    if actual_overlay != tuple(sorted(EXPECTED_OVERLAY_PATHS)):
        raise SystemExit("Approved overlay path set differs from the locked seven-path set")

    with tempfile.TemporaryDirectory(prefix="swrlz-011t-c-") as td:
        extracted = Path(td) / "extracted"
        extracted.mkdir()
        with zipfile.ZipFile(args.input_zip) as zf:
            bad = zf.testzip()
            if bad:
                raise SystemExit(f"Canonical ZIP CRC failure: {bad}")
            names = zf.namelist()
            if len(names) != len(set(names)):
                raise SystemExit("Canonical ZIP contains duplicate paths")
            zf.extractall(extracted)

        project_root = locate_project_root(extracted)
        changed = []
        for rel in EXPECTED_OVERLAY_PATHS:
            src = OVERLAY_ROOT / rel
            dst = project_root / rel
            dst.parent.mkdir(parents=True, exist_ok=True)
            before = sha256(dst) if dst.exists() else None
            shutil.copyfile(src, dst)
            after = sha256(dst)
            changed.append({"path": rel, "before_sha256": before, "after_sha256": after})

        args.output_zip.parent.mkdir(parents=True, exist_ok=True)
        deterministic_zip(extracted, args.output_zip)

    output_sha = sha256(args.output_zip)
    args.output_zip.with_suffix(args.output_zip.suffix + ".sha256").write_text(
        f"{output_sha}  {args.output_zip.name}\n", encoding="utf-8"
    )
    args.manifest.parent.mkdir(parents=True, exist_ok=True)
    args.manifest.write_text(json.dumps({
        "checkpoint": "INTEGRATION-FIX-011T-C",
        "input_archive": args.input_zip.name,
        "input_sha256": actual,
        "output_archive": args.output_zip.name,
        "output_sha256": output_sha,
        "changed_paths": changed,
        "path_count": len(changed),
        "build_executed": False,
        "workflow_triggered": False,
    }, indent=2) + "\n", encoding="utf-8")

    with zipfile.ZipFile(args.output_zip) as zf:
        bad = zf.testzip()
        if bad:
            raise SystemExit(f"Successor ZIP CRC failure: {bad}")
    print(output_sha)


if __name__ == "__main__":
    main()
