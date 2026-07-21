# KEYBOARD-IMP-011A Toolchain and Build Boundary

## Source toolchain

```text
Android Gradle Plugin: 8.2.2
Kotlin Android plugin: 1.9.24
compileSdk:            34
targetSdk:             34
minSdk:                24
Java/JVM target:       17
```

These values are compatible with the accepted CORE_BASE toolchain profile while preserving a separate Keyboard app shell and identity.

## Verification executed

- XML parsing for manifest and IME metadata;
- static identity and boundary checks;
- standalone Kotlin compilation of `EditorContextClassifier.kt`;
- execution of 16 policy vectors;
- deterministic ZIP rebuild comparison;
- ZIP CRC, duplicate-entry, required-entry, and path-safety checks;
- source SHA-256 manifest generation.

## Verification not executed

No Android Gradle configuration, dependency resolution, resource merge, Kotlin Android compile, DEX, APK packaging, signing, emulator/device installation, or runtime launch was executed.

The source package intentionally does not claim build success.

## Current unified-router boundary

Current `main` now uses `.github/workflows/swrlz-apk-router.yml`, Java 17, Gradle 8.6, Android SDK setup, `scripts/ci/resolve_swrlz_source.py`, and `scripts/ci/build_swrlz_component.sh`. The helper can generate a Gradle wrapper for Keyboard archives that contain a Gradle settings file but no wrapper.

The current resolver does not recognize `SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip` under the `KEYBOARD_BASE` component naming contract. Therefore no truthful build claim can be made until a separately approved resolver-and-verification checkpoint extends the naming contract, selects the exact source path, and runs the router.

The active unified build request remains disabled and must stay disabled during the first explicit verification build.
