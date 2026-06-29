# SWRLZ-Core 2.7.6 Network Discovery Real Source Patch Report

## Input verified

- Input source: `SWRLZ_CORE_2.7.4_COCKPIT_SHELL_SOURCE-2 (1).zip`
- Input SHA256: `62f43e5f6611184a9ee87b668b2f436aaf588035c888e8f482492a9a3a978cce`

## Output

- Source ZIP: `SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY_SOURCE.zip`
- SHA256: `dc86d24f09ebd843b22deb16c572b4c1ce40a36884740e4635c809cd6f6ca30f`

## Build identity target

- versionName: `0.2.7.6-network-discovery`
- versionCode: `22`
- Patch: `2.7.6-NETWORK-DISCOVERY`
- Roadmap: `2.7.6`
- Build line: `Network Discovery / discovery signature / local node scan / saved server verify / Admin Registry bridge`
- Source: `SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY_SOURCE.zip`

## Functional changes

- Added `NetworkDiscoveryScreen.kt`
- Added `Network` top tab route
- Added Home `NETWORK DISCOVERY` button
- Added More drawer `Network Discovery` entry
- Added `/discovery/signature` probe
- Added local candidate scan
- Added save-to-Core-Node config action
- Updated `core/version.json`
- Updated `android/app/build.gradle.kts` BuildConfig identity fields

## Truth note

This is a real repacked source ZIP produced from the uploaded 2.7.4 source.
It is not merely a GitHub Actions workflow patch.
