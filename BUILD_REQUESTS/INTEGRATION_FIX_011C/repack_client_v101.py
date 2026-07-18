#!/usr/bin/env python3
from __future__ import annotations

import argparse
import hashlib
import shutil
import subprocess
import tempfile
import zipfile
from pathlib import Path

FIXED_TIME = (2026, 7, 18, 0, 0, 0)
EXPECTED_BASE_SHA256 = "8c6933bd122e7d099a6b9576d53eb52da3a9cbfe9e0a58302cec6fe0c2760e89"
EXPECTED_OUTPUT_SHA256 = "9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7"


def sha256(path: Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            h.update(chunk)
    return h.hexdigest()


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--base", required=True, type=Path)
    parser.add_argument("--patch", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()

    if sha256(args.base) != EXPECTED_BASE_SHA256:
        raise SystemExit("canonical CLIENT v1.0.0 checksum mismatch")

    with tempfile.TemporaryDirectory(prefix="swrlz-client-v101-") as raw:
        work = Path(raw)
        with zipfile.ZipFile(args.base) as archive:
            base_infos = {item.filename: item for item in archive.infolist()}
            archive.extractall(work)

        subprocess.run(
            ["patch", "--batch", "--forward", "-p1", "-i", str(args.patch.resolve())],
            cwd=work,
            check=True,
        )

        project = work / "CLIENT_CFv1.0.0_SWRLZ"
        subprocess.run(
            ["python3", str(project / "scripts/test_cf8_admin_fallback.py"), str(project)],
            check=True,
        )

        existing_names = sorted(base_infos)
        extracted_names: list[str] = []
        for path in work.rglob("*"):
            name = path.relative_to(work).as_posix()
            if path.is_dir():
                name += "/"
            extracted_names.append(name)
        new_names = sorted(set(extracted_names) - set(existing_names))

        args.output.parent.mkdir(parents=True, exist_ok=True)
        with zipfile.ZipFile(
            args.output,
            "w",
            compression=zipfile.ZIP_DEFLATED,
            compresslevel=6,
        ) as archive:
            for name in existing_names + new_names:
                source = work / name.rstrip("/")
                is_directory = name.endswith("/")
                info = zipfile.ZipInfo(name, FIXED_TIME)
                info.create_system = 3
                if name in base_infos:
                    info.external_attr = base_infos[name].external_attr
                else:
                    mode = 0o40755 if is_directory else (
                        0o100755
                        if name.startswith("CLIENT_CFv1.0.0_SWRLZ/scripts/")
                        else 0o100644
                    )
                    info.external_attr = (mode << 16) | (0x10 if is_directory else 0)
                info.compress_type = zipfile.ZIP_STORED if is_directory else zipfile.ZIP_DEFLATED
                if is_directory:
                    archive.writestr(info, b"")
                else:
                    archive.writestr(
                        info,
                        source.read_bytes(),
                        compress_type=zipfile.ZIP_DEFLATED,
                        compresslevel=6,
                    )

    actual = sha256(args.output)
    if actual != EXPECTED_OUTPUT_SHA256:
        raise SystemExit(f"candidate checksum mismatch: {actual}")
    print(f"CLIENT v1.0.1 deterministic reconstruction: PASS {actual}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
