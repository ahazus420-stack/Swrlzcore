# CORE-BUILD-002A Current Handoff

Status: verification build succeeded; draft PR remains unmerged
Date (UTC): 2026-07-21

## Read first

1. `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
2. `reports/CORE_BUILD_002A_FINAL_VERIFICATION.md`
3. `BUILD_REQUESTS/000_CURRENT.request`
4. `.github/workflows/build-swrlz-core-android-foundation.yml`
5. `SOURCES/CORE_BASE/OLD_PATCHES/README.md`
6. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
7. `docs/handoffs/CORE_BASE_NEW_CHAT_HANDOFF_TEMPLATE.md`

## Scope

This handoff is dedicated to canonical CORE_BASE. Do not modify CLIENT, NODE_HOST, Keyboard, or Launcher without separate explicit authorization.

## Authoritative repository state

- Repository: `ahazus420-stack/Swrlzcore`
- Authoritative branch: `main`
- Checkpoint branch: `checkpoint/core-build-002a`
- Draft PR: `#15`
- PR state: open, draft, unmerged
- Verified workflow head source: PR merge reference for checkpoint head `60863df8acbc35a98bb59b0644599593ab66f351`
- Latest documentation commits follow the successful build and do not alter CORE_BASE source or workflow logic.

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

## Reduction invariants

- `featurehome_absent=PASS`
- `designsystem_absent=PASS`
- `HomeScreen_absent=PASS`
- `FeatureRepository_absent=PASS`
- `CoreFeature_absent=PASS`

## Documentation inventory

- Build process: `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
- Final verification report: `reports/CORE_BUILD_002A_FINAL_VERIFICATION.md`
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

- device installation;
- launch success;
- runtime UI behavior;
- release build;
- release signing continuity;
- deployment or publication.

## Approval state

Approval already granted covered reduction, bounded branch integration, workflow verification, documentation, and evidence inspection. It did not authorize merge, release, publication, deployment, installation, or another application lane.

## Current gate

The technical verification build succeeded. The next repository-state decision is whether to merge draft PR #15 into `main`. That is a separate authorization.

## Exact next approval phrase

`Approve CORE-BUILD-002A-MERGE — Merge verified draft PR #15 into main without releasing, publishing, deploying, or installing the APK`
