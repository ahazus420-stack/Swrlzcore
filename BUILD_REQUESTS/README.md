# SWRLZ Build Requests

The only request file consumed by the active Android build router is:

```text
BUILD_REQUESTS/000_CURRENT.request
```

The active workflow is:

```text
.github/workflows/swrlz-apk-router.yml
SWRLZ APK Router — Manual / Auto
```

## Schema 2

```text
schema=2
enabled=false
request_id=
target=CLIENT
source_mode=lane_latest
source_zip=
build_variant=debug
commit_release_artifacts=false
```

Rules:

- `enabled=false` is the safe resting state.
- `target` must be one of `CLIENT`, `SERVER`, `CORE_BASE`, `KEYBOARD_BASE`, or `LAUNCHER_BASE`.
- `source_mode=lane_latest` lets the shared resolver choose the newest complete canonical source identity from the target lane.
- Duplicate-download suffixes such as `(1)`, ` (2)`, and `(204)` are accepted as transport aliases but are removed from canonical artifact identity.
- Automatic request builds may upload GitHub Actions artifacts but may not commit release artifacts. Repository commits require manual dispatch with `commit_release_artifacts=true`.
- Historical requests belong under `BUILD_REQUESTS/OLD_REQUESTS/` or remain in a clearly marked retained staging directory when moving the full evidence set would obscure lineage.
- Directories other than `000_CURRENT.request` are not scanned as build inputs.

## Manual operation

Use the single Actions entry:

```text
SWRLZ APK Router — Manual / Auto
```

Choose the component, optional explicit ZIP path, and build variant. The explicit ZIP must remain at the selected active lane root; `OLD_PATCHES` is never an automatic or explicit build source.
