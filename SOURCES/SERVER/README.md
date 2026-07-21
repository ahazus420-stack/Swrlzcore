# SERVER Source Lane

This lane contains the current active Android NODE_HOST source set and the minimum metadata required to verify and build it.

## Active source set

- `SERVER_CFv1.0.3_SWRLZ.zip`
- `SERVER_CFv1.0.3_SWRLZ.sha256`

The active source ZIP and its matching checksum must remain paired at the top of this lane. A version-specific Markdown report may remain beside them when one exists.

## Naming contract

- `SERVER_CFvX.Y.Z_SWRLZ.zip`
- `SERVER_CFvX.Y.Z_SWRLZ.sha256`
- `SERVER_CFvX.Y.Z_SWRLZ.md`

## Hygiene and lineage

Keep only the active complete source set at this lane root. Move superseded sources, legacy transport bundles, old discovery packages, and their documentation into:

- `SOURCES/SERVER/OLD_PATCHES/`

Do not delete lineage by default. Archived files retain their original names and Git history unless a separately approved collision-resolution checkpoint requires otherwise.

## Build selection

`.github/workflows/build-swrlz-server-apk.yml` selects the newest versioned `SERVER_CFv*_SWRLZ.zip` from this lane, or an explicitly requested versioned archive inside this lane. It no longer falls back to the legacy unversioned `SWRLZ_NODE_HOST_COMPLETE.zip` transport.

## Preserved anomalies

Unpaired or oddly suffixed v1.0.4 checksum artifacts are not an active complete source set. They remain untouched pending a separate explicit disposition checkpoint.

Core law: integrate, do not overwrite.
