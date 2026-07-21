# CORE-BUILD-002A Current Handoff

Status: merged to `main`; CI build, physical-device installation, first launch, and minimal UI render verified
Date (UTC): 2026-07-21

## Read first

1. `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
2. `reports/CORE_BUILD_002A_FINAL_VERIFICATION.md`
3. `reports/CORE_BUILD_002A_DEVICE_INSTALL_AND_LAUNCH_VERIFICATION.md`
4. `BUILD_REQUESTS/000_CURRENT.request`
5. `.github/workflows/build-swrlz-core-android-foundation.yml`
6. `SOURCES/CORE_BASE/OLD_PATCHES/README.md`
7. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
8. `docs/handoffs/CORE_BASE_NEW_CHAT_HANDOFF_TEMPLATE.md`

## Scope

This handoff is dedicated to canonical CORE_BASE. Do not modify CLIENT, NODE_HOST, Keyboard, or Launcher without separate explicit authorization.

## Authoritative repository state

- Repository: `ahazus420-stack/Swrlzcore`
- Authoritative branch: `main`
- Completed checkpoint: `CORE-BUILD-002A`
- Merged PR: `#15`
- Merge commit: `56bf5eecc4388a3a47e81cd866448cb0c1b0a210`
- Device-evidence documentation commit: `f3d3c34fbdf87f3cea2001a5de2ab47d9cdf42b7`

## Canonical source and build contract

- Source tree: `SOURCES/CORE_BASE/source/`
- Source ZIP: `SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.zip`
- Source checksum path: `SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.sha256`
- Verified generated source SHA-256: `5aa7b7a993c0bbd508fdd5b86eed2cce74e2c056b4f32a3e38af1a5dfb7754e6`
- Request ID: `CORE-BUILD-002A`
- Workflow: `.github/workflows/build-swrlz-core-android-foundation.yml`
- Gradle task: `:app:assembleDebug`
- Application ID: `com.swrlz.core.app`
- Version: `1.0.0` (`versionCode=1`)

## Verified workflow evidence

- Run ID: `29790837016`
- Run number: `10`
- Job ID: `88512380173`
- Conclusion: `success`
- Artifact ID: `8480270199`
- Artifact name: `SWRLZ_CORE_BASE_CORE_REDUCE_003_BUILD_EVIDENCE`
- Artifact digest: `sha256:5c8af423ab4fb71acd19fe0970561d7371cf1f19d420bdf051805dbbed34b737`
- APK SHA-256: `38282980952e80458a8bccaf4b3daa206af41c9c652d7bc312f2608326d0d5e9`
- APK size: `8626772` bytes
- Inner artifact ZIP SHA-256: `2ddcdbbd0aa42d8defd4e2173e12059d824a5b84dcc24b5e4d47c384381837b8`
- Gradle: `8.6`
- JVM: Temurin OpenJDK `17.0.19`

## Verified physical-device evidence

- APK installation: verified
- Launcher registration as `SWRLZ Core`: verified
- First launch: verified
- Immediate startup crash: not observed
- Minimal Compose UI render with centered `SWRLZ Core` text: verified
- Evidence report: `reports/CORE_BUILD_002A_DEVICE_INSTALL_AND_LAUNCH_VERIFICATION.md`

## Reduction invariants

- `featurehome_absent=PASS`
- `designsystem_absent=PASS`
- `HomeScreen_absent=PASS`
- `FeatureRepository_absent=PASS`
- `CoreFeature_absent=PASS`

## Documentation inventory

- Build process: `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
- CI verification report: `reports/CORE_BUILD_002A_FINAL_VERIFICATION.md`
- Device verification report: `reports/CORE_BUILD_002A_DEVICE_INSTALL_AND_LAUNCH_VERIFICATION.md`
- Current handoff: `docs/handoffs/CORE_BUILD_002A_CURRENT_HANDOFF.md`
- New-chat template: `docs/handoffs/CORE_BASE_NEW_CHAT_HANDOFF_TEMPLATE.md`
- Skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- OLD_PATCHES contract: `SOURCES/CORE_BASE/OLD_PATCHES/README.md`

## Lineage

- Predecessor: original four-module CORE_BASE scaffold containing `app`, `core`, `designsystem`, and `featurehome`.
- Successor: reduced two-module CORE_BASE containing `app` and the empty `core` kernel boundary.
- Active source remains under `SOURCES/CORE_BASE/`.
- No active item has been migrated into `OLD_PATCHES` by this checkpoint.
- Preserve all verification reports and checksums; supersede by linkage rather than deletion.

## Claims that remain unverified

- extended runtime stability;
- process recreation and lifecycle recovery;
- background execution;
- upgrade/downgrade compatibility;
- permission flows;
- release build;
- release signing continuity;
- deployment or publication.

## Approval state

CORE-BUILD-002A is complete and authoritative on `main`. No release, publication, deployment, or installation automation was authorized. The manual installation and launch performed by the repository owner are documented as device evidence.

## Current architectural question

The next proposed checkpoint is a review-only architectural decision for a reusable CORE integrator/capability-module system that would let capabilities such as Phoenix Firewall be implemented once and composed into CORE_BASE, Keyboard, Launcher, and future SWRLZ applications without copying implementation code.

## Current gate

No implementation is authorized for the integrator architecture. The next bounded step should define and accept the module contract, host capability declarations, trust boundaries, lifecycle, version compatibility, storage ownership, UI contribution rules, and build composition model.

## Exact next approval phrase

`Approve CORE-ARCH-003 — Review and document the reusable CORE integrator architecture for shared capabilities such as Phoenix Firewall without implementing source code, modifying app lanes, or triggering builds`
