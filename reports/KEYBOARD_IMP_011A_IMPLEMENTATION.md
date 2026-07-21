# KEYBOARD-IMP-011A Implementation Report

## Result

A standalone minimal SWRLZ Keyboard Android IME source project has been created under:

```text
SOURCES/KEYBOARD/source/
```

Accepted identity:

```text
namespace:     com.swrlz.keyboard.app
applicationId: com.swrlz.keyboard.app
versionCode:   1
versionName:   0.1.0
surfaceType:   keyboard
```

## Source verification

```text
static boundary checks: 26 passed / 0 failed
policy vectors:         16 passed / 0 failed
```

## Canonical source package

```text
SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip
SHA-256: 7281f083e4776004bd59f4973cc33a25e788b36c8175f42a15a4fc90ccd50442
Git blob: dd05d88b8bb9a60f3153db0f25f6fe88a771b73d
entries: 16
CRC integrity: PASS
unsafe paths: none
duplicate entries: none
deterministic rebuild: PASS
```

## Lineage

The existing `SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip` seed and checksum remain unchanged at the lane root. They are preserved as predecessor inheritance evidence and are not archived by this checkpoint.

## Build boundary

No APK, Android resource merge, Kotlin Android compile, DEX, package signing, workflow dispatch, release, deployment, or installation was performed.

## Concurrent current-main reconciliation

After implementation, current `main` advanced to `a03a572a25695c78c5fa9c91970183fb664d7d1b` and restored the unified `.github/workflows/swrlz-apk-router.yml` while retiring the former dedicated Keyboard workflow. This implementation branch remains unrebased and unmerged.

The unified build helper can build a wrapper-less Keyboard Gradle project, but the current source resolver recognizes only the historical `SWRLZ_KEYBOARD_BASE_CFvX.Y.Z` naming family. It rejects the new role-specific `SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip` filename. The next checkpoint must extend the resolver contract and explicitly select the new package before any build.

## Untouched lanes

- CORE_BASE;
- Launcher;
- CLIENT;
- SERVER/NODE_HOST;
- build workflows and resolver helpers;
- build requests;
- shared mature source.
