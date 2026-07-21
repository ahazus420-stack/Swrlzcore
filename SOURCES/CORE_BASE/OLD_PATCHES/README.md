# CORE_BASE OLD_PATCHES

This directory preserves superseded CORE_BASE source archives, patches, and checkpoint evidence that are no longer active build inputs.

Migration into this directory is explicit and checkpoint-bound. Do not delete superseded material. Each migrated item must retain or receive:

- original filename;
- SHA-256 checksum;
- checkpoint identifier;
- source commit or source-lineage reference;
- superseded-by reference;
- migration date.

The active build source archive remains directly under `SOURCES/CORE_BASE/` with a sibling `.sha256` file. Items in `OLD_PATCHES/` are lineage evidence and must not be selected automatically for builds.
