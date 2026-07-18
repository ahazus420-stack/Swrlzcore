#!/usr/bin/env python3
from pathlib import Path
import sys

root = Path(sys.argv[1]).resolve()
service = root / "app/src/main/java/sh/swrlz/nodehost/service"
protocol = (service / "NodeCompatibilityProtocol.kt").read_text()
runtime = (service / "NodeRuntime.kt").read_text()
manifest = (root / "app/src/main/AndroidManifest.xml").read_text()

for path in ["/status", "/presence/summary", "/presence/groups", "/presence/devices"]:
    assert path in protocol
for marker in [
    'groups\\\":[]',
    'devices\\\":[]',
    'online_count\\\":0',
    "server_version",
    "node_name",
    "authoritative",
    "data_source",
]:
    assert marker in protocol
assert "NodeCompatibilityProtocol.handle" in runtime
assert "DiscoveryProtocol.handle" in runtime
assert 'android:icon="@mipmap/ic_launcher"' in manifest
assert 'android:roundIcon="@mipmap/ic_launcher_round"' in manifest

resources = root / "app/src/main/res"
assert (resources / "drawable-nodpi/ic_launcher_adaptive_foreground.png").is_file()
for density in ["mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"]:
    assert (resources / f"mipmap-{density}/ic_launcher.png").is_file()
    assert (resources / f"mipmap-{density}/ic_launcher_round.png").is_file()
print("SERVER-CONTRACT-CATCHUP-010D source and icon checks: PASS")
