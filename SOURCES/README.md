# SWRLZ Sources

Upload the current active SWRLZ source set here. Keep this folder mobile-readable and uncluttered.

## Current naming standard

Use compact names that show the important difference early on Android file views:

```text
SRC_CF6_276_BOOTSYNC.zip
SHA_CF6_276_BOOTSYNC.txt
RPT_CF6_276_BOOTSYNC.md
```

Recommended pattern:

```text
SRC_<PATCH>_<VERSION>_<SHORT_OBJECTIVE>.zip
SHA_<PATCH>_<VERSION>_<SHORT_OBJECTIVE>.txt
RPT_<PATCH>_<VERSION>_<SHORT_OBJECTIVE>.md
```

Examples:

```text
SRC_CF5_276_ENGTEST.zip
SHA_CF5_276_ENGTEST.txt
RPT_CF5_276_ENGTEST.md

SRC_CF6_276_BOOTSYNC.zip
SHA_CF6_276_BOOTSYNC.txt
RPT_CF6_276_BOOTSYNC.md
```

## Folder hygiene rule

Top-level `SOURCES/` should normally contain only:

```text
OLD_PATCHES/
README.md
SRC_<current>.zip
SHA_<current>.txt
RPT_<current>.md
```

Older source ZIPs, SHA files, reports, upload notes, server bundles, and long-name legacy files should be moved into:

```text
SOURCES/OLD_PATCHES/
```

Do not delete old source outputs by default. Archive them as lineage receipts.

## Build request rule

`BUILD_REQUESTS/000_CURRENT.request` should point only at the current active source set. If the current source has not been uploaded yet, leave the request pointing at the latest valid uploaded source rather than creating a broken build target.

Example:

```text
source_zip=SOURCES/SRC_CF6_276_BOOTSYNC.zip
sha_file=SOURCES/SHA_CF6_276_BOOTSYNC.txt
release_name=APK_CF6_276_BOOTSYNC
commit_release_artifacts=true
```

## Assistant cleanup rule

Whenever a new SWRLZ source output is provided and uploaded, the assistant should archive the previously active source set from top-level `SOURCES/` into `SOURCES/OLD_PATCHES/`, then update `000_CURRENT.request` to the new current source set.

Core law: integrate, do not overwrite.
