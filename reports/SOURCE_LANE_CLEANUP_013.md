# SOURCE-LANE-CLEANUP-013

## Purpose

Complete the remaining SERVER source-lane hygiene work that was outside the narrow version-set scope of SOURCE-LANE-CLEANUP-012.

## Base

- Repository: `ahazus420-stack/Swrlzcore`
- Base branch: `main`
- Base commit: `00bb23d7f4fed389a7b24e7ec2127399da0d924f`
- Checkpoint branch: `checkpoint/source-lane-cleanup-013`

## Active source retained unchanged

- `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.zip`
- `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.sha256`
- Declared ZIP SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`

## Legacy files archived

The following lane-root files are relocated into `SOURCES/SERVER/OLD_PATCHES/` while preserving their existing Git blob contents:

- `SWRLZ_NODE_HOST_COMPLETE.zip`
- `README_SWRLZ_NODE_HOST.md`
- `README_SERVER.md`

## Duplicate root files removed

The following root copies are removed because byte-identical copies already exist in `SOURCES/SERVER/OLD_PATCHES/`:

- `SERVER_0.7.3_DISCOVERY_FULL.zip`
  - shared Git blob: `6eb7001ea18a3b0a3d8da3edc382e47b0b2899d9`
- `SHA_SERVER_0.7.3_DISCOVERY_FULL.txt`
  - shared Git blob: `98bcac15066298d025072e0c2f136c7ed8d7905f`

No archived lineage copy is deleted or rewritten.

## README consolidation

`SOURCES/SERVER/README.md` becomes the single authoritative lane README. It records:

- the active v1.0.3 ZIP/checksum pair;
- the versioned naming contract;
- the `OLD_PATCHES` lineage rule;
- versioned-only workflow source selection;
- the separate status of unresolved v1.0.4 anomalies.

The former `README_SERVER.md` is preserved in `OLD_PATCHES` rather than deleted.

## Workflow hygiene

`.github/workflows/build-swrlz-server-apk.yml` is changed only to remove legacy unversioned transport behavior:

- remove `SWRLZ_NODE_HOST_COMPLETE.zip` from push-trigger paths;
- remove automatic fallback selection of that ZIP;
- remove checksum-verification logic specific to that fallback.

Explicit or automatic selection of versioned `SERVER_CFv*_SWRLZ.zip` sources remains intact.

## Explicitly untouched

- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(1).sha256`
- any other `(1)` or `(2)` v1.0.4 anomaly visible in the lane
- CLIENT sources
- release artifacts
- update manifests
- APKs
- runtime source contents inside the active ZIP

## Operation guarantees

- No build performed.
- No workflow dispatched.
- No release, publication, deployment, or installation performed.
- No update to `main` performed.
- No v1.0.4 anomaly modified.
- No checkpoint branch deletion performed.
