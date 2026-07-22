#!/usr/bin/env python3
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path


def sha256(path: Path) -> str:
    h = hashlib.sha256()
    with path.open('rb') as fh:
        for chunk in iter(lambda: fh.read(1024 * 1024), b''):
            h.update(chunk)
    return h.hexdigest()


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument('zip_path', type=Path)
    parser.add_argument('--checksum', type=Path)
    parser.add_argument('--manifest', type=Path)
    args = parser.parse_args()

    zip_path = args.zip_path
    checksum = args.checksum or zip_path.with_suffix('.sha256')
    manifest = args.manifest or zip_path.with_suffix('.manifest.json')

    if not zip_path.is_file():
        raise SystemExit(f'Missing ZIP: {zip_path}')
    if not checksum.is_file():
        raise SystemExit(f'Missing checksum: {checksum}')
    if not manifest.is_file():
        raise SystemExit(f'Missing manifest: {manifest}')

    parts = checksum.read_text(encoding='utf-8').strip().split(maxsplit=1)
    if len(parts) != 2:
        raise SystemExit('Malformed checksum file')
    declared_hash, declared_name = parts
    actual_hash = sha256(zip_path)
    payload = json.loads(manifest.read_text(encoding='utf-8'))

    failures: list[str] = []
    if declared_name != zip_path.name:
        failures.append(f'checksum basename {declared_name!r} != {zip_path.name!r}')
    if declared_hash != actual_hash:
        failures.append(f'checksum hash {declared_hash} != {actual_hash}')
    if payload.get('zip') != zip_path.name:
        failures.append('manifest ZIP basename mismatch')
    if payload.get('sha256') != actual_hash:
        failures.append('manifest SHA-256 mismatch')
    if payload.get('size_bytes') != zip_path.stat().st_size:
        failures.append('manifest size mismatch')
    if payload.get('verified') is not True:
        failures.append('manifest verified flag is not true')

    if failures:
        raise SystemExit('Package verification failed: ' + '; '.join(failures))

    print(json.dumps({
        'zip': zip_path.name,
        'sha256': actual_hash,
        'size_bytes': zip_path.stat().st_size,
        'checksum': checksum.name,
        'manifest': manifest.name,
        'verified': True,
    }, indent=2))
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
