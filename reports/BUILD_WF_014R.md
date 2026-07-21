# BUILD-WF-014R — Refreshed Unified APK Router Staging Report

## Status

Staged on a refreshed checkpoint branch created from current `main`. No workflow dispatch, APK build, pull-request operation, merge, release, publication, deployment, or installation was performed.

## Base and branch

- Repository: `ahazus420-stack/Swrlzcore`
- Base branch: `main`
- Base commit: `961e92907acb6a3158f6da982902f07acbfba019`
- Checkpoint branch: `checkpoint/build-wf-014r`

## Refresh order

The nine legacy workflow files were removed from the refreshed branch before `BUILD_REQUESTS/000_CURRENT.request` was changed. Every retirement commit used `[skip ci]`. This removes the trigger-order ambiguity recorded by BUILD-WF-014.

## Replayed verified implementation

The following implementation files reuse the exact Git blobs from `checkpoint/build-wf-014`:

- `.github/workflows/swrlz-apk-router.yml` — `1716d58cd34d3efd392385aa67a969cfbc81b81c`
- `scripts/ci/resolve_swrlz_source.py` — `57cf94d3a14bb3fbc9af0312f362e7b26f34df26`
- `scripts/ci/test_resolve_swrlz_source.py` — `1f9f0251a9a532da0cc6a09d70669817121708c7`
- `scripts/ci/build_swrlz_component.sh` — `43dee18bed659a1ae3579aee39b7b5053cc811f9`
- `scripts/ci/README.md` — `282437febbca808d36e3a6861e6554ff3d2fca06`

No implementation content was rewritten during the refresh.

## Active workflow surface

One clearly named workflow remains:

```text
SWRLZ APK Router — Manual / Auto
.github/workflows/swrlz-apk-router.yml
```

Manual choices:

- CLIENT
- SERVER
- CORE_BASE
- KEYBOARD_BASE
- LAUNCHER_BASE

The shared resolver accepts arbitrary terminal duplicate-download suffixes such as `(1)`, `(2)`, and higher integers while retaining the original transport filename in provenance and keeping the canonical artifact identity unsuffixed.

## SERVER v1.0.4 retention

The refreshed branch starts from and retains the canonical SERVER v1.0.4 lineage:

- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.zip`
- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.sha256`
- Declared SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`

`SOURCES/SERVER/README.md` now names `.github/workflows/swrlz-apk-router.yml` as the build-selection workflow and preserves the fail-closed suffix-normalization contract. `OLD_PATCHES` remains excluded from automatic source selection.

## BUILD_REQUESTS cleanup

- `BUILD_REQUESTS/000_CURRENT.request` uses schema 2 and remains `enabled=false`.
- The verified CORE-BUILD-002A request is preserved byte-for-byte as `BUILD_REQUESTS/OLD_REQUESTS/CORE_BUILD_002A.request`.
- The retained `SERVER_CONTRACT_CATCHUP_010D` staging directory is marked historical and is not referenced by an active workflow.
- Automatic request builds cannot commit release artifacts.

## Static verification

Fresh repository-state checks for BUILD-WF-014R verify:

- the refreshed branch base is current `main` at `961e92907acb6a3158f6da982902f07acbfba019`;
- all nine legacy workflow paths are absent before the request-file change;
- the router and helper blob SHAs exactly match the previously reviewed BUILD-WF-014 implementation;
- the resolver test-suite blob is byte-identical to the eight-test suite previously recorded as `Ran 8 tests / OK`;
- `000_CURRENT.request` is disabled;
- automatic router push triggers are restricted to active lane-root files on `main`;
- no `OLD_PATCHES` path is a trigger or resolver search lane;
- canonical SERVER v1.0.4 source/checksum paths remain retained from the base;
- the refreshed diff contains no source ZIP, APK, update manifest, runtime implementation, package identity, signer policy, protocol, or trust change.

Because the connector supplies repository Git objects rather than a runnable checkout, BUILD-WF-014R does not claim a fresh local execution of the Python test process, YAML parser, or Bash parser. Instead, it verifies exact blob identity with the already passing static implementation and separately verifies the refreshed repository topology and configuration. No GitHub Actions execution was used as verification.

## Explicitly not performed

- No workflow dispatch.
- No APK compilation or packaging.
- No pull request opened, updated, closed, or merged.
- No update to `main`.
- No source ZIP content change or lineage rewrite.
- No release, publication, deployment, or installation.
