# SOURCES / OLD_PATCHES

This folder stores archived source ZIPs that have already been superseded by newer source uploads.

## Repo hygiene rule

- Keep `SOURCES/` clean for the current active source ZIP, SHA file, and patch/report files.
- Move older source ZIPs into `SOURCES/OLD_PATCHES/` before uploading a new current source ZIP.
- Do not delete old source ZIPs by default; preserve them as build lineage receipts.
- `BUILD_REQUESTS/000_CURRENT.request` may temporarily point to an archived ZIP if that archived ZIP is still the active build target, but new update work should upload the new ZIP into `SOURCES/` and then update the request.

Core law: Integrate, do not overwrite.
