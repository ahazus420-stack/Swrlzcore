# SWRLZ Sources

Upload the active SWRLZ source sets here. Keep each lane mobile-readable and uncluttered.

## Lane structure

```text
SOURCES/
├── CLIENT/
│   ├── OLD_PATCHES/
│   └── README.md
├── SERVER/
│   ├── OLD_PATCHES/
│   └── README.md
└── README.md
```

## Current naming standard

Use compact names that show the important difference early on Android file views:

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

Each lane should normally contain only its current active source outputs plus its own archive folder:

```text
SOURCES/CLIENT/
SOURCES/CLIENT/OLD_PATCHES/
SOURCES/SERVER/
SOURCES/SERVER/OLD_PATCHES/
```

Older source ZIPs, SHA files, reports, upload notes, and legacy files should be moved into the lane’s `OLD_PATCHES/` folder.

Do not delete old source outputs by default. Archive them as lineage receipts.

## Build request rule

`BUILD_REQUESTS/000_CURRENT.request` should point only at the current active lane. The workflow should determine the newest versioned source inside that lane automatically rather than storing a filename here.

Example:

```text
target=CLIENT
commit_release_artifacts=true
```

## Assistant cleanup rule

Whenever a new SWRLZ source output is provided and uploaded, the assistant should archive the previously active source set from the relevant lane into that lane’s `OLD_PATCHES/`, then keep the request file pointing at the lane rather than the file name.

Core law: integrate, do not overwrite.
