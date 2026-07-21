# SOURCE-LANE-CLEANUP-012

## Purpose

Apply the repository source-lane hygiene rule without deleting lineage: keep only the newest complete active CLIENT and SERVER source sets at their lane roots and relocate superseded complete source outputs into each lane's `OLD_PATCHES/` directory.

## Base

- Repository: `ahazus420-stack/Swrlzcore`
- Base branch: `main`
- Base commit: `ef62e870e30143912be992972aed89849f186448`
- Checkpoint branch: `checkpoint/source-lane-cleanup-012`

## Active source sets retained

### CLIENT

- `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip`
- `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.sha256`
- Declared ZIP SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`

### SERVER

- `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.zip`
- `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.sha256`
- Declared ZIP SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`

SERVER v1.0.3 remains the newest complete repository source set because no matching SERVER v1.0.4 source ZIP exists at either expected root filename checked during this audit.

## Superseded outputs relocated

### CLIENT v1.0.0

- `CLIENT_CFv1.0.0_SWRLZ.zip`
- `CLIENT_CFv1.0.0_SWRLZ.sha256`
- Declared ZIP SHA-256: `8c6933bd122e7d099a6b9576d53eb52da3a9cbfe9e0a58302cec6fe0c2760e89`

Destination: `SOURCES/CLIENT/OLD_PATCHES/`

### SERVER v1.0.1

- `SERVER_CFv1.0.1_SWRLZ.zip`
- `SERVER_CFv1.0.1_SWRLZ.sha256`
- `SERVER_CFv1.0.1_SWRLZ.md`
- Declared ZIP SHA-256: `c6c4a802a30743d94df80cc98fec9e409860be9b8e23dd58ff04988a569530a7`

Destination: `SOURCES/SERVER/OLD_PATCHES/`

### SERVER v1.0.2

- `SERVER_CFv1.0.2_SWRLZ.zip`
- `SERVER_CFv1.0.2_SWRLZ.sha256`
- `SERVER_CFv1.0.2_SWRLZ.md`
- Declared ZIP SHA-256: `9d278bed4944eee20d6b9dc1ea89ba9363c30a7e7daa5b91b44836c386abd200`

Destination: `SOURCES/SERVER/OLD_PATCHES/`

## Preserved anomaly

`SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(1).sha256` remains untouched. It is an unpaired, oddly suffixed checksum and was not classified as either a complete active source set or a superseded complete source output. Resolving, renaming, archiving, or deleting it requires a separate explicit decision.

## Operation guarantees

- Move-only semantics implemented by reusing each existing Git blob SHA at the archive path, then removing the old lane-root path in the same tree.
- No file contents changed.
- No lineage deleted.
- No build, workflow dispatch, release, deployment, installation, merge, or `main` update performed.
