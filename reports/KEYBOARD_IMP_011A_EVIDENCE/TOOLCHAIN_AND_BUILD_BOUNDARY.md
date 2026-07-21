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

The source package intentionally does not claim build success. A later explicitly authorized checkpoint must establish a checksum-gated build path and collect package, signer, and APK evidence.
