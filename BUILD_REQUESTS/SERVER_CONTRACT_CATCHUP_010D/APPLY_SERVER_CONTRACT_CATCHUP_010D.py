#!/usr/bin/env python3
from __future__ import annotations

import argparse
import hashlib
import json
import shutil
import stat
import subprocess
import sys
import tempfile
import zipfile
from pathlib import Path, PurePosixPath

from icon_rle import load as load_icon, scale as scale_icon, write_png

BASE_SHA = "9d278bed4944eee20d6b9dc1ea89ba9363c30a7e7daa5b91b44836c386abd200"
OUTPUT_NAME = "SERVER_CFv1.0.3_SWRLZ.zip"
FIXED_TIME = (2026, 7, 18, 0, 0, 0)
OLD_ROUTE = """            method = request.method; path = request.path
            DiscoveryProtocol.handle(request, activeIdentity, hostVersion)"""
NEW_ROUTE = """            method = request.method; path = request.path
            if (NodeCompatibilityProtocol.handles(request.path)) {
                val runtime = _state.value
                NodeCompatibilityProtocol.handle(
                    request = request,
                    snapshot = NodeCompatibilitySnapshot(
                        phase = runtime.phase.name,
                        identityReady = runtime.identityReady,
                        discoveryHealthy = runtime.isDiscoveryHealthy,
                        privateHealthy = runtime.isPrivateHealthy,
                        nodeId = activeIdentity?.nodeId,
                        installationId = activeIdentity?.installationId,
                        hostVersion = hostVersion,
                        lanUrls = runtime.discoveryListeners
                            .asSequence()
                            .filter { !it.required && it.status == ListenerStatus.LISTENING }
                            .map { listener -> \"http://${listener.address}:${listener.port}\" }
                            .toList(),
                    ),
                )
            } else {
                DiscoveryProtocol.handle(request, activeIdentity, hostVersion)
            }"""


def digest(path: Path) -> str:
    value = hashlib.sha256()
    with path.open("rb") as source:
        for block in iter(lambda: source.read(1024 * 1024), b""):
            value.update(block)
    return value.hexdigest()


def extract(source: Path, destination: Path) -> None:
    with zipfile.ZipFile(source) as archive:
        if bad := archive.testzip():
            raise RuntimeError(f"ZIP integrity failed at {bad}")
        for info in archive.infolist():
            raw = info.filename.replace("\\", "/")
            member = PurePosixPath(raw)
            mode = (info.external_attr >> 16) & 0xFFFF
            if not raw or raw.startswith("/") or member.is_absolute() or ".." in member.parts:
                raise RuntimeError(f"unsafe ZIP member: {raw}")
            if stat.S_ISLNK(mode):
                raise RuntimeError(f"symlink rejected: {raw}")
        archive.extractall(destination)


def root_of(extracted: Path) -> Path:
    roots = [path.parent for path in extracted.rglob("gradlew") if ".gradle" not in path.parts]
    if len(roots) != 1:
        raise RuntimeError(f"expected one Gradle root, found {len(roots)}")
    return roots[0]


def replace_once(text: str, old: str, new: str, label: str) -> str:
    if text.count(old) != 1:
        raise RuntimeError(f"{label}: expected exactly one marker")
    return text.replace(old, new, 1)


def patch(root: Path) -> None:
    runtime = root / "app/src/main/java/sh/swrlz/nodehost/service/NodeRuntime.kt"
    text = runtime.read_text(encoding="utf-8")
    for marker in ("RuntimeEventStore", "NodeRuntimeState", "_state.value", "ListenerStatus.LISTENING"):
        if marker not in text:
            raise RuntimeError(f"v1.0.2 invariant missing: {marker}")
    runtime.write_text(replace_once(text, OLD_ROUTE, NEW_ROUTE, "compatibility routing"), encoding="utf-8")

    gradle = root / "app/build.gradle.kts"
    text = gradle.read_text(encoding="utf-8")
    text = replace_once(text, "versionCode = 1", "versionCode = 2", "versionCode")
    text = replace_once(text, 'versionName = "1.0"', 'versionName = "1.0.3"', "versionName")
    gradle.write_text(text, encoding="utf-8")


def install(request: Path, root: Path) -> None:
    staged = request / "files"
    for source in sorted(staged.rglob("*")):
        if source.is_file():
            target = root / source.relative_to(staged)
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, target)

    width, height, palette, pixels = load_icon(request / "assets/glitch_dragon_core_rle")
    resources = root / "app/src/main/res"
    adaptive = resources / "drawable-nodpi/ic_launcher_adaptive_foreground.png"
    adaptive.parent.mkdir(parents=True, exist_ok=True)
    for old in adaptive.parent.glob("ic_launcher_adaptive_foreground.*"):
        old.unlink()
    write_png(adaptive, 432, 432, palette, scale_icon(width, height, pixels, 432, 432))

    for density, size in {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}.items():
        directory = resources / f"mipmap-{density}"
        directory.mkdir(parents=True, exist_ok=True)
        scaled = scale_icon(width, height, pixels, size, size)
        for stem in ("ic_launcher", "ic_launcher_round"):
            for old in directory.glob(stem + ".*"):
                old.unlink()
            write_png(directory / f"{stem}.png", size, size, palette, scaled)


def verify(root: Path) -> None:
    subprocess.run([sys.executable, str(root / "scripts/test_server_contract_catchup_010d.py"), str(root)], check=True)


def pack(root: Path, output: Path) -> None:
    excluded = {"build", ".gradle", ".idea", ".kotlin", ".git", "__pycache__"}
    files = [
        path for path in root.rglob("*")
        if path.is_file()
        and not any(part in excluded for part in path.relative_to(root).parts)
        and path.name != "local.properties"
    ]
    files.sort(key=lambda path: path.relative_to(root).as_posix())
    with zipfile.ZipFile(output, "w", zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
        for path in files:
            relative = Path(root.name) / path.relative_to(root)
            info = zipfile.ZipInfo(relative.as_posix(), FIXED_TIME)
            info.compress_type = zipfile.ZIP_DEFLATED
            executable = path.name == "gradlew" or path.parent.name == "scripts"
            info.external_attr = ((0o755 if executable else 0o644) & 0xFFFF) << 16
            archive.writestr(info, path.read_bytes())


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("base_zip", type=Path)
    parser.add_argument("request_dir", type=Path)
    parser.add_argument("output_dir", type=Path)
    arguments = parser.parse_args()
    base, request, output_dir = arguments.base_zip.resolve(), arguments.request_dir.resolve(), arguments.output_dir.resolve()
    if (actual := digest(base)) != BASE_SHA:
        raise RuntimeError(f"base SHA mismatch: {actual}")

    with tempfile.TemporaryDirectory(prefix="swrlz-010d-") as temporary:
        extracted = Path(temporary) / "extracted"
        extract(base, extracted)
        root = root_of(extracted)
        patch(root)
        install(request, root)
        verify(root)
        output_dir.mkdir(parents=True, exist_ok=True)
        output = output_dir / OUTPUT_NAME
        pack(root, output)
        output_sha = digest(output)
        (output_dir / "SERVER_CFv1.0.3_SWRLZ.sha256").write_text(f"{output_sha}  {OUTPUT_NAME}\n", encoding="utf-8")
        receipt = {
            "checkpoint": "SERVER-CONTRACT-CATCHUP-010D",
            "baseSha256": BASE_SHA,
            "output": OUTPUT_NAME,
            "outputSha256": output_sha,
            "routes": ["/status", "/presence/summary", "/presence/groups", "/presence/devices"],
            "presenceTruth": "authoritative-empty",
            "icon": "Glitch Dragon Core RLE launcher derivative",
            "iconMasterSha256": "2183730b2823b47709f074a531999f83043f128cc424a5aefd97846f52688256",
            "apkBuilt": False,
        }
        (output_dir / "SERVER_CONTRACT_CATCHUP_010D_RECEIPT.json").write_text(json.dumps(receipt, indent=2) + "\n", encoding="utf-8")
        print(json.dumps(receipt, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
