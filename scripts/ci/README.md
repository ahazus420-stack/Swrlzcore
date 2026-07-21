# SWRLZ Android CI Router Helpers

## `resolve_swrlz_source.py`

Resolves source ZIP/checksum pairs for CLIENT, SERVER, CORE_BASE, KEYBOARD_BASE, and LAUNCHER_BASE.

It accepts terminal duplicate-download suffixes with or without a preceding space:

```text
NAME.zip
NAME(1).zip
NAME (2).zip
NAME(204).zip
```

The suffix is transport metadata only. Version ordering uses the canonical source identity, and artifact names never inherit the suffix. Alias files that normalize to one identity must be byte-identical. Checksum verification compares the actual calculated hash against the first valid SHA-256 value, independent of the filename written inside the checksum file.

## `test_resolve_swrlz_source.py`

Static unit tests for normalization, version selection, checksum pairing, provenance, and fail-closed ambiguity behavior.

## `build_swrlz_component.sh`

Shared build dispatcher. It preserves CLIENT's project build script, standard Gradle builds for SERVER/CORE_BASE, wrapper regeneration for KEYBOARD_BASE/LAUNCHER_BASE, and CORE_REDUCE_003 reduction checks.

The dispatcher normalizes the source, work, and artifact arguments to absolute paths before changing directories. APK download bundles are created in a temporary staging directory under `RUNNER_TEMP`, or the platform temporary directory when `RUNNER_TEMP` is unavailable, and are then moved into the component artifact directory.

## `test_build_swrlz_component_packaging.sh`

Focused synthetic regression test for the post-build packaging path. It invokes the real dispatcher with relative workflow-style paths, uses a fake Gradle wrapper to create a synthetic SERVER APK, and verifies APK copying, provenance, ZIP creation, checksums, absolute GitHub outputs, and the absence of nested artifact directories.
