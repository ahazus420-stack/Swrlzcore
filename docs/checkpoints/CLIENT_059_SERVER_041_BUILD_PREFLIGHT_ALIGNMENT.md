# CLIENT CFv2.0.59 + SERVER CFv2.0.41 — Build Preflight Alignment

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub Android compilation and device acceptance pending.

## Scope

This checkpoint records a deliberate pre-build audit of the prepared CLIENT `CFv2.0.58` and SERVER `CFv2.0.40` source packages before another Forge upload.

The audit covered:

- Gradle and Kotlin plugin configuration;
- Compose compiler configuration;
- Android Activity Compose API availability;
- package declarations and source paths;
- manifest and XML parsing;
- image-resource integrity;
- launcher, Theme Armor, and bubble resource references;
- exhaustive navigation handling for SERVER Forge destinations;
- Android API-floor guards and core-library desugaring;
- explicit Compose import regressions;
- final ZIP and SHA-256 integrity.

## CLIENT finding and repair

The CLIENT root build applied:

```text
Kotlin Android plugin: 1.9.24
Kotlin serialization plugin: 1.9.24
Compose Compiler Gradle plugin: 2.0.0
```

The Compose Compiler Gradle plugin is the Kotlin 2.0+ setup and is intended to match the Kotlin plugin version. CLIENT `CFv2.0.59` aligns all three plugins at `2.0.0`.

No protocol, Forge transaction, upload-log, connector-continuity, bubble-authority, or Dragon Kamileon behavior changes in this repair.

## SERVER findings and repairs

### Activity Compose API floor

The new SERVER Forge uses `rememberLauncherForActivityResult`, while `CFv2.0.40` depended on:

```kotlin
implementation("androidx.activity:activity-compose:1.2.0")
```

The API was introduced under that name in Activity Compose 1.3.0. SERVER `CFv2.0.41` advances the dependency to the already-used CLIENT baseline:

```kotlin
implementation("androidx.activity:activity-compose:1.9.2")
```

### Obsolete Compose compiler extension setting

SERVER already aligns Kotlin Android, Compose, serialization, and KSP at `2.0.20`, but retained the pre-Kotlin-2 compiler setting:

```kotlin
composeOptions {
    kotlinCompilerExtensionVersion = "1.5.15"
}
```

That block was removed so the Kotlin 2.0.20 Compose Compiler Gradle plugin remains the single compiler authority.

No SERVER protocol, node authority, Forge transaction, build monitoring, bubble, launcher, or Dragon Kamileon behavior changes in this repair.

## Static audit results

The resulting CLIENT and SERVER sources passed the available preflight checks:

- all manifests and XML resources parse;
- all PNG/JPEG/WebP resources open and verify;
- Kotlin package declarations match their source paths;
- local resource references resolve;
- SERVER `SwurlzerTab`, `HostTab`, and `BubbleSection` additions handle Forge in their exhaustive navigation paths;
- no explicit `androidx.compose.foundation.layout.weight` import exists;
- SERVER's API-26 same-folder picker hint remains guarded;
- SERVER Java-time usage remains covered by core-library desugaring;
- both final ZIPs pass compressed-data integrity testing;
- both final ZIP digests match their sibling SHA-256 receipts.

A full local Android build was not possible because the preparation environment has no Android SDK and only incomplete Gradle wrapper downloads. GitHub Actions remains the authoritative compilation gate.

## Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.59_SWRLZ.zip
versionCode: 86
versionName: 2.0.59-build-preflight-alignment-v1
SHA-256: f04f075e1a2d3df24d7e9abf75c2fef4a038da99475f3123bc1f8402680554bb
```

### SERVER

```text
Package: SERVER_CFv2.0.41_SWRLZ.zip
versionCode: 42
versionName: 2.0.41-build-preflight-alignment-v1
SHA-256: 1dd25aafa22f2c94af5e4fc1c4eb0c62ac8bf003cdad3b95a719945e08042c49
```

## Evidence classification

- source-package hashes and ZIP integrity: locally verified;
- CLIENT Kotlin/Compose mismatch: source and official-tooling-contract verified;
- SERVER Activity Compose API mismatch: source and official release-history verified;
- SERVER obsolete compiler-extension block: source and official-tooling-contract verified;
- XML, image, package-path, resource-reference, enum-routing, and API-guard checks: static verified;
- clean GitHub Android builds: pending;
- installed runtime and visual acceptance: pending.

## Acceptance gate

1. Upload CLIENT `CFv2.0.59` and confirm one Source Package Integrity run.
2. Confirm the CLIENT APK Router completes Gradle configuration, Kotlin compilation, packaging, and artifact publication.
3. Install with the same signing certificate and verify the existing Forge, logs, connector continuity, bubbles, version footers, and Dragon Kamileon theme.
4. Upload SERVER `CFv2.0.41` and confirm one Source Package Integrity run.
5. Confirm SERVER Forge compiles through its Activity Result launcher integration and produces an artifact.
6. Install and validate Forge from SERVER User Mode, Developer Mode, and the SERVER bubble.
7. If either build fails, export the exact workflow logs and the in-app Forge upload log for the next bounded repair.
