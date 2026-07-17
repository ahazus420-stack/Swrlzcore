# SWRLZ-Core 2.7.6 CODEFIX3 Scan Trim / Loopback Toggle

## Purpose

Reduce wasted local discovery scanning and UI lag by avoiding the `127.0.0.x` range sweep by default.

## Identity

- versionName: `0.2.7.6-codefix3-scan-trim`
- versionCode: `25`
- Patch: `2.7.6-CODEFIX3-DISCOVERY-SCAN-TRIM`
- Source: `SRC_2.7.6_CF3_SCAN_TRIM.zip`

## Behavior

SCAN LOCAL now:

1. Tries the typed Node URL first.
2. Tries the saved Core Node URL second.
3. Reads the Android client's active non-loopback IPv4 address.
4. Derives the active LAN prefix from the client IP.
5. Sweeps `x.x.x.1` through `x.x.x.254` on the selected port.
6. Tests only `127.0.0.1`/`localhost` by default for same-device mode.
7. Skips `127.0.0.2-254` unless the saved `LOOPBACK RANGE` toggle is enabled.
8. Probes `/discovery/signature` and accepts only a valid `swrlz-local-node` signature.

## Why

For normal Android use, `127.0.0.1` points to the same device and extra `127.0.0.x` scans are usually unnecessary. The server phone should be found on the same router/hotspot subnet as the client, such as `10.24.230.x` or `192.168.1.x`.

## Output naming

The build helper now uses short mobile-readable output names when `SOURCE_ZIP` starts with `SRC_`:

- Source: `SRC_2.7.6_CF3_SCAN_TRIM.zip`
- APK: `APK_2.7.6_CF3_SCAN_TRIM_debug.apk`
- Bundle: `BUNDLE_2.7.6_CF3_SCAN_TRIM_APK_DOWNLOAD.zip`
