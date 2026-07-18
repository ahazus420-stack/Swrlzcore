# SWRLZ Board Keyboard Source Lane

This directory is the canonical source lane for the standalone SWRLZ Keyboard Android application.

Current source naming:

```text
KEYBOARD_CFvX.Y.Z_SWRLZ.zip
KEYBOARD_CFvX.Y.Z_SWRLZ.sha256
```

Keep only the current accepted source set in this directory. Move superseded ZIPs, checksums, reports, and notes into `OLD_PATCHES/` without deleting lineage evidence.

The source ZIP must contain a complete standalone Gradle build root. The Keyboard workflow verifies the matching SHA-256 before extraction and uploads APK/build evidence as GitHub Actions artifacts.

This lane does not contain CLIENT, SERVER, or sCore Launcher source material.