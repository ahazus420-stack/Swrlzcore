# BUILD-WF-015 — Unified Router Packaging Repair

**Mode:** IMPLEMENT
**Lifecycle state:** IMPLEMENTATION_VERIFIED
**Status:** COMPLETE AT BRANCH VERIFICATION LAYER

## Objective

Repair the unified Android router helper after a SERVER build reached `BUILD SUCCESSFUL` but failed during creation of the downloadable APK ZIP because a relative output path was evaluated after changing into the artifact directory.

## Approved scope

- Create `checkpoint/build-wf-015` from current `main`.
- Normalize source, work, and artifact arguments to absolute paths.
- Create the APK download ZIP through a safe temporary staging directory.
- Add a focused packaging regression test.
- Document the repair.
- Confirm the retired workflow YAML paths remain absent.
- Perform static and local verification only.

## Baseline

- Base branch: `main`
- Base commit: `a03a572a25695c78c5fa9c91970183fb664d7d1b`
- Repair branch: `checkpoint/build-wf-015`
- Original helper blob: `43dee18bed659a1ae3579aee39b7b5053cc811f9`

## Confirmed failure

The Android Gradle build itself completed successfully. The post-build helper then attempted to create:

```text
BUILD_ARTIFACTS/ROUTER/SERVER_CFv1.0.4_SWRLZ_DEBUG_APK_DOWNLOAD.zip
```

while its current directory was already the relative component artifact directory. `zip` therefore evaluated a non-existent nested relative path and exited with code 15. Signing, provenance normalization, artifact upload, and optional release handling did not run after that failure.

## Implementation

### `scripts/ci/build_swrlz_component.sh`

- Converts `SOURCE_ZIP`, `WORK_DIR`, and `ARTIFACT_DIR` to absolute paths before file checks, directory removal, extraction, Gradle execution, or packaging.
- Uses `RUNNER_TEMP`, then `TMPDIR`, then `/tmp` as the temporary root.
- Converts the temporary root to an absolute path.
- Creates a unique temporary staging directory with `mktemp -d`.
- Writes the bundle outside the artifact directory while `zip` reads the artifact directory.
- Moves the completed bundle into the artifact directory.
- Cleans the staging directory through an EXIT trap and explicit cleanup after success.
- Preserves the existing artifact, provenance, checksum, and GitHub output behavior.

Committed helper blob:

```text
8b6f0f5e4cd813579ecd5efac9d8ccba64dabda0
```

### `scripts/ci/test_build_swrlz_component_packaging.sh`

The focused test:

1. creates a synthetic SERVER project ZIP;
2. supplies a fake Gradle wrapper that writes a synthetic APK;
3. invokes the real helper with relative source, work, and artifact paths;
4. verifies the APK, APK checksum, build log, provenance, bundle, and bundle checksum;
5. verifies the ZIP contains the APK;
6. verifies GitHub output paths are absolute;
7. verifies no nested `relative-artifacts` directory appears inside the artifact directory.

Committed test blob:

```text
2de01b7e0a2996de86ef92398f8259864d39bf1b
```

### `scripts/ci/README.md`

Documents absolute path normalization, temporary bundle staging, and the focused packaging regression test.

## Verification performed

Commands executed locally against byte-identical candidate files:

```bash
bash -n build_swrlz_component.sh
bash -n test_build_swrlz_component_packaging.sh
bash test_build_swrlz_component_packaging.sh
```

Result:

```text
BUILD_LOG.txt
BUILD_PROVENANCE_REPORT.md
BUILD_PROVENANCE_REPORT.md.sha256
SERVER_TEST_SWRLZ_DEBUG.apk
SERVER_TEST_SWRLZ_DEBUG.apk.sha256
SERVER_TEST_SWRLZ_DEBUG_APK_DOWNLOAD.zip
SERVER_TEST_SWRLZ_DEBUG_APK_DOWNLOAD.zip.sha256
PASS: relative workflow paths were normalized and the APK download ZIP was created safely.
```

The local Git blob identities matched the committed branch blobs for both the helper and regression test.

## Retired workflow verification

Direct branch reads returned `404 Not Found` for all nine retired workflow paths:

```text
.github/workflows/build-swrlz-apk_target_client_latest.yml
.github/workflows/build-swrlz-android-project-e0af72.yml
.github/workflows/build-swrlz-server-apk.yml
.github/workflows/build-swrlz-core-android-foundation.yml
.github/workflows/build-swrlz-keyboard-base.yml
.github/workflows/build-swrlz-launcher-base.yml
.github/workflows/build-swrlz-apk_multi_target_ready.yml
.github/workflows/locate-stale-ui-source.yml
.github/workflows/server-contract-catchup-010d-promote-v103.yml
```

Historical workflow names and runs may remain visible in the GitHub Actions interface. Their visibility does not restore the deleted workflow definitions.

## Excluded work

This checkpoint did not:

- merge or update `main`;
- dispatch, rerun, or otherwise trigger a workflow;
- build an APK remotely;
- delete workflow history;
- modify CLIENT or SERVER source ZIPs;
- alter signing secrets or signer policy;
- address Node.js runtime deprecation warnings;
- release, publish, deploy, or install anything.

## Known limitation

The synthetic local test verifies the exact packaging failure mode and helper behavior, but only a GitHub-hosted workflow run after promotion can confirm end-to-end runner packaging and artifact upload.

## Current disposition

```text
IMPLEMENTATION_VERIFIED
→ MERGE_APPROVAL_PENDING
```

No successor implementation branch should begin before this branch is promoted, rejected, abandoned, or blocked.
