# sCore Launcher Source Lane

This directory is the canonical source lane for the standalone sCore Launcher Android application.

Current source naming:

```text
SCORE_LAUNCHER_CFvX.Y.Z_SWRLZ.zip
SCORE_LAUNCHER_CFvX.Y.Z_SWRLZ.sha256
```

Keep only the current accepted source set in this directory. Move superseded ZIPs, checksums, reports, and notes into `OLD_PATCHES/` without deleting lineage evidence.

The source ZIP must contain a complete standalone Gradle build root. The sCore Launcher workflow verifies the matching SHA-256 before extraction and uploads APK/build evidence as GitHub Actions artifacts.

This lane is infrastructure-ready but contains no implementation source yet. It does not contain CLIENT, SERVER, or Keyboard source material.