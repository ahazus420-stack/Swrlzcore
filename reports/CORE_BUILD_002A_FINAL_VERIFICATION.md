# CORE-BUILD-002A Final Verification Report

Status: verified build success
Date (UTC): 2026-07-21
Checkpoint: CORE-BUILD-002A
Repository: `ahazus420-stack/Swrlzcore`
Checkpoint branch: `checkpoint/core-build-002a`
Draft PR: `#15`

## Scope

This report records repository-backed verification of the reduced canonical CORE_BASE Android application. It does not authorize merge, release, publication, deployment, installation, signing changes, or work on CLIENT, NODE_HOST, Keyboard, or Launcher.

## Authoritative inputs

- Canonical source tree: `SOURCES/CORE_BASE/source/`
- Immutable source archive: `SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.zip`
- Source checksum file: `SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.sha256`
- Build request: `BUILD_REQUESTS/000_CURRENT.request`
- Workflow: `.github/workflows/build-swrlz-core-android-foundation.yml`
- Application ID: `com.swrlz.core.app`
- Version name: `1.0.0`
- Version code: `1`
- Gradle task: `:app:assembleDebug`

## Workflow evidence

- Workflow run ID: `29790837016`
- Workflow run number: `10`
- Job ID: `88512380173`
- Job: `build-core-base`
- Conclusion: `success`
- Artifact ID: `8480270199`
- Artifact name: `SWRLZ_CORE_BASE_CORE_REDUCE_003_BUILD_EVIDENCE`
- Artifact size: `16542702` bytes
- Artifact digest: `sha256:5c8af423ab4fb71acd19fe0970561d7371cf1f19d420bdf051805dbbed34b737`

## Verified checksums

- Generated source archive SHA-256: `5aa7b7a993c0bbd508fdd5b86eed2cce74e2c056b4f32a3e38af1a5dfb7754e6`
- Debug APK SHA-256: `38282980952e80458a8bccaf4b3daa206af41c9c652d7bc312f2608326d0d5e9`
- Inner build-artifact ZIP SHA-256: `2ddcdbbd0aa42d8defd4e2173e12059d824a5b84dcc24b5e4d47c384381837b8`
- Outer GitHub artifact ZIP SHA-256: `5c8af423ab4fb71acd19fe0970561d7371cf1f19d420bdf051805dbbed34b737`

The inner build-artifact ZIP and APK checksum files were checked successfully. The outer artifact digest matched GitHub's reported digest.

## Build result

The workflow completed `:app:assembleDebug` successfully.

- Build duration reported by Gradle: `2m 7s`
- Gradle: `8.6`
- JVM: Temurin OpenJDK `17.0.19`
- APK size: `8626772` bytes

## Reduction invariants

All required invariant checks passed:

- `featurehome_absent=PASS`
- `designsystem_absent=PASS`
- `HomeScreen_absent=PASS`
- `FeatureRepository_absent=PASS`
- `CoreFeature_absent=PASS`

## Lineage and documentation

- `SOURCES/CORE_BASE/OLD_PATCHES/` remains the explicit lineage-retirement lane and is not an automatic build-input path.
- The active source archive remains directly under `SOURCES/CORE_BASE/` with its sibling checksum.
- The canonical build process is documented in `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`.
- Future-chat requirements are documented in `docs/handoffs/CORE_BASE_NEW_CHAT_HANDOFF_TEMPLATE.md`.
- Operating requirements are documented in `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`.

## Unverified claims

The following remain unverified and must not be claimed:

- installation on a physical or virtual Android device;
- first launch success;
- runtime UI behavior beyond successful compilation and packaging;
- release signing continuity;
- release build success;
- merge readiness beyond the successful verification build.

## Approval boundary

This verification did not authorize or perform:

- merge of PR #15;
- release or publication;
- deployment or installation;
- CLIENT, NODE_HOST, Keyboard, or Launcher changes;
- package, version, signer, protocol, or identity changes.

## Recovery

If later verification exposes a defect, continue from `checkpoint/core-build-002a`, preserve this evidence, and apply one bounded repair. Do not replace or delete this report; supersede it with a linked follow-up report when necessary.
