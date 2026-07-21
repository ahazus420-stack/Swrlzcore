# BUILD-WF-014 — Unified APK Router Staging Report

## Status

Staged on checkpoint branch only. No manual workflow dispatch, local APK build, merge, release, publication, deployment, or installation was performed by this checkpoint.

## Base

- Repository: `ahazus420-stack/Swrlzcore`
- Base branch: `main`
- Base commit: `e6621447b1e666a5c121a6a69045be8e87c506ab`
- Checkpoint branch: `checkpoint/build-wf-014`

## Active workflow surface

The nine former workflow entries are replaced by one clearly named action:

```text
SWRLZ APK Router — Manual / Auto
.github/workflows/swrlz-apk-router.yml
```

Manual component choices:

- CLIENT
- SERVER
- CORE_BASE
- KEYBOARD_BASE
- LAUNCHER_BASE

Automatic builds are limited to active lane-root source/checksum paths on `main` and the disabled-by-default `BUILD_REQUESTS/000_CURRENT.request`. `OLD_PATCHES` is not a trigger or source search path. CORE source-tree-only edits do not trigger a build until the canonical archive/checksum pair is updated, preventing stale-archive compilation.

## Source-name normalization

The shared resolver accepts arbitrary terminal duplicate-download suffixes:

```text
NAME.zip
NAME(1).zip
NAME (2).zip
NAME(204).zip
```

The suffix:

- does not affect semantic version ordering;
- is removed from canonical artifact identity;
- remains recorded in source-resolution provenance;
- may differ between the ZIP and checksum alias;
- cannot conceal conflicting bytes under one canonical identity.

Resolution fails closed when aliases disagree, checksums disagree, the source hash does not match, a checksum is missing, an explicit source leaves the active lane root, or two canonical identities tie for newest order.

## Preserved component behavior

- CLIENT: project-owned `scripts/install_android_sdk_and_build.sh`, backend `.env` creation from repository secrets, APK collection, and provenance.
- SERVER: Gradle-root discovery, debug/release task selection, APK collection, and provenance.
- CORE_BASE: Gradle build plus `CORE_REDUCE_003` reduction invariants when that canonical identity is selected.
- KEYBOARD_BASE and LAUNCHER_BASE: Gradle wrapper regeneration at 8.6 when the source archive does not contain a wrapper.
- All lanes: optional stable development re-signing when the complete four-secret signing set exists; partial signing-secret configuration fails closed.

## BUILD_REQUESTS cleanup

- The former multi-section CORE-BUILD-002A request is preserved exactly as `BUILD_REQUESTS/OLD_REQUESTS/CORE_BUILD_002A.request`.
- `000_CURRENT.request` is replaced by schema 2 and rests at `enabled=false`.
- Only `000_CURRENT.request` is consumed by the router.
- Automatic request builds cannot commit release artifacts.
- The retained `SERVER_CONTRACT_CATCHUP_010D` staged evidence directory is no longer referenced by an active workflow.

## Static verification

The resolver test suite contains eight tests covering all five component contracts, arbitrary suffix normalization, version precedence, explicit provenance, cross-suffix checksum pairing, conflicting aliases, missing checksums, and checksum mismatch behavior.

Local checkpoint-authoring result:

```text
Ran 8 tests
OK
```

YAML parsing and Bash syntax validation also passed locally. These are static authoring checks, not GitHub Actions execution and not an APK build.

## Workflow-execution evidence boundary

No workflow was manually dispatched and no APK was built in the local checkpoint-authoring environment.

An early checkpoint commit changed `BUILD_REQUESTS/000_CURRENT.request` before every legacy path-triggered workflow had been removed from the checkpoint branch. A combined-status query for that request-change commit returned no statuses. However, the available connector cannot conclusively enumerate push-triggered GitHub Actions runs for the branch. Therefore this report does not claim that zero automatic runs occurred. The Actions UI should be checked for branch `checkpoint/build-wf-014` before any refreshed staging or promotion decision.

## Concurrent main advancement

While BUILD-WF-014 was being staged, `main` advanced from the recorded base to `961e92907acb6a3158f6da982902f07acbfba019` through SERVER-LINEAGE-FIX-014. That separate work canonicalized SERVER v1.0.4 and archived v1.0.3 without modifying workflows.

The checkpoint branch is intentionally not merged or rebased because BUILD-WF-014 did not authorize either operation. Before a pull request or promotion decision, the branch must be refreshed from current `main`, the canonical SERVER v1.0.4 pair must be retained, and `SOURCES/SERVER/README.md` must be reconciled so its build-selection reference names `.github/workflows/swrlz-apk-router.yml` rather than the retired dedicated SERVER workflow.

## Explicitly not performed

- No manual workflow dispatch.
- No local APK compilation or packaging.
- No source ZIP content change.
- No canonical source/checksum rename or movement.
- No application ID, package, version, signer, protocol, identity, runtime, trust, or update-policy change.
- No merge, rebase, release, publication, deployment, or installation.
