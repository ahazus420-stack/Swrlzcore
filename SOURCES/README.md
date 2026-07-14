# SWRLZ Sources

Upload the current active SWRLZ source set here. Keep this folder mobile-readable and uncluttered.

## Current naming standard

Use compact names that show the important difference early on Android file views:

```text
CLIENT_CFv1.0.0_SWRLZ.zip
CLIENT_CFv1.0.0_SWRLZ.sha256
CLIENT_CFv1.0.0_SWRLZ.md
```

```text
SERVER_CFv1.0.0_SWRLZ.zip
SERVER_CFv1.0.0_SWRLZ.sha256
SERVER_CFv1.0.0_SWRLZ.md
```

Recommended pattern:

```text
CLIENT_CFvX.Y.Z_SWRLZ.zip
CLIENT_CFvX.Y.Z_SWRLZ.sha256
CLIENT_CFvX.Y.Z_SWRLZ.md
```

```text
SERVER_CFvX.Y.Z_SWRLZ.zip
SERVER_CFvX.Y.Z_SWRLZ.sha256
SERVER_CFvX.Y.Z_SWRLZ.md
```

## Folder hygiene rule

Top-level `SOURCES/` should normally contain only:

```text
CLIENT/
SERVER/
OLD_PATCHES/
README.md
```

Older source ZIPs, SHA files, reports, upload notes, and legacy files should be moved into:

```text
SOURCES/OLD_PATCHES/
```

Each lane should also keep its own archived lineage inside its own folder when the lane structure is present:

```text
SOURCES/CLIENT/OLD_PATCHES/
SOURCES/SERVER/OLD_PATCHES/
```

Do not delete old source outputs by default. Archive them as lineage receipts.

## Build request rule

`BUILD_REQUESTS/000_CURRENT.request` should point only at the current active lane. The workflow should determine the newest versioned source inside that lane automatically rather than storing a filename here.

Example:

```text
target=CLIENT
commit_release_artifacts=true
```

## Assistant cleanup rule

Whenever a new SWRLZ source output is provided and uploaded, the assistant should archive the previously active source set from the relevant lane into `OLD_PATCHES/`, then keep the request file pointing at the lane rather than the file name.

Core law: integrate, do not overwrite.
