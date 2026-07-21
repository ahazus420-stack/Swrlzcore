# BUILD-WF-014 — Unified APK Router Staging Report

## Status

Staged on checkpoint branch only. No workflow dispatch, APK build, merge, release, publication, deployment, or installation is authorized or performed by this checkpoint.

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

Automatic builds are limited to active lane-root source/checksum paths on `main` and the disabled-by-default `BUILD_REQUESTS/000_CURRENT.request`. `OLD_PATCHES` is not a trigger or source search path.

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

## Explicitly not performed

- No workflow dispatch.
- No GitHub Actions run triggered from the checkpoint branch; the router push trigger is restricted to `main`.
- No APK compilation or packaging.
- No source ZIP content change.
- No canonical source/checksum rename or movement.
- No application ID, package, version, signer, protocol, identity, runtime, trust, or update-policy change.
- No merge, release, publication, deployment, or installation.
