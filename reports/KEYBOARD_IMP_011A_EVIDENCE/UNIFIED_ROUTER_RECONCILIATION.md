# KEYBOARD-IMP-011A Unified Router Reconciliation

## Concurrent repository change

After the Keyboard source implementation commit was created, current `main` advanced to:

```text
a03a572a25695c78c5fa9c91970183fb664d7d1b
```

That separately authorized change restored the unified workflow:

```text
.github/workflows/swrlz-apk-router.yml
```

and retired the former dedicated Keyboard build workflow. The active unified build request remains disabled.

This Keyboard implementation branch was not rebased or merged. No workflow file, resolver, helper, or build request from current `main` was imported into the branch.

## Current router facts

The unified router:

- exposes the `KEYBOARD_BASE` component route;
- allows a manual explicit `source_zip` path;
- verifies source identity and checksum through `scripts/ci/resolve_swrlz_source.py`;
- extracts and builds through `scripts/ci/build_swrlz_component.sh`;
- generates a Gradle 8.6 wrapper for Keyboard/Launcher archives that contain settings files but no wrapper;
- can build `:app:assembleDebug` and collect APK/provenance artifacts;
- leaves `BUILD_REQUESTS/000_CURRENT.request` disabled unless a separate automatic build is authorized.

## Confirmed resolver mismatch

The current `KEYBOARD_BASE` resolver naming contract accepts only:

```text
SWRLZ_KEYBOARD_BASE_CFv<major>.<minor>.<patch>.zip
```

The new role-specific source is:

```text
SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip
```

Therefore both lane-latest resolution and explicit-source resolution reject the new filename before checksum verification. This is a naming-contract mismatch, not a source or package-integrity failure.

The old seed also has version `1.0.1`, while the new role-specific IME begins at `0.1.0`. Automatic version ranking must not silently select the old Core-shaped seed as the active Keyboard implementation. The first verification build must use an exact explicit source path and must keep automatic requests disabled.

## Required bounded repair before build

A later verification checkpoint should:

1. extend only the `KEYBOARD_BASE` resolver naming contract to recognize the role-specific `SWRLZ_KEYBOARD_IME_CFvX.Y.Z_SOURCE` identity;
2. add resolver tests proving exact explicit selection, checksum agreement, lane-root enforcement, duplicate-suffix discipline, and rejection of ambiguity;
3. preserve the historical `SWRLZ_KEYBOARD_BASE_CFv1.0.1` seed as lineage but prevent it from being mistaken for the new active implementation;
4. invoke the unified router manually against the exact new source path;
5. collect source-resolution, build, APK, manifest, package, version, and signer evidence;
6. keep `BUILD_REQUESTS/000_CURRENT.request` disabled and `commit_release_artifacts=false`.

## Boundary

This reconciliation is documentation/evidence only. It performs no resolver or workflow change and no build or workflow dispatch.
