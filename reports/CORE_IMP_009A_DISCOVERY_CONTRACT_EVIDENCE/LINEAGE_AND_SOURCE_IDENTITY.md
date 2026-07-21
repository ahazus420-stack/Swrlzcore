# CORE-IMP-009A Lineage and Source Identity

## Repository base

- Repository: `ahazus420-stack/Swrlzcore`
- Checkpoint branch: `checkpoint/core-imp-009a`
- Branch base: `00bb23d7f4fed389a7b24e7ec2127399da0d924f`
- Required ancestor: `ef62e870e30143912be992972aed89849f186448`
- Relationship: current base descends from the required ancestor through source-lane cleanup only.

## Active mature source declarations

### CLIENT

- ZIP: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip`
- Sibling checksum: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.sha256`
- Declared SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`

### SERVER

- ZIP: `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.zip`
- Sibling checksum: `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.sha256`
- Declared SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`

The source-lane cleanup record identifies SERVER v1.0.3 as the newest complete repository pair.

## Preserved anomaly

`SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(1).sha256` remains unpaired and declares:

```text
32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6  SERVER_CFv1.0.4_SWRLZ.zip
```

It was not selected, renamed, moved, deleted, or treated as active source.

## Origin hash records preserved from CORE-PLAN-008

- `DiscoveryProtocol.kt`: `36c248b3d2ad5cee53d8a048607b41aa3fea32ae9578afbe5773ae06cee21a97`
- `NodeCompatibilityProtocol.kt`: `43325599ffa1fb97cf7c508b5eef600aad5e4247cdcf384924e655867754c319`

No mature source was modified or copied into this capsule.

## Verification limitation

The GitHub connector exposed the active ZIP blobs as binary content but its blob reader attempted UTF-8 decoding and failed. Therefore this checkpoint independently confirmed repository path identity, exact sibling checksum declarations, and current active-pair selection, but did not re-stream the mature ZIP bytes to recompute their SHA-256 values or inspect every archive entry.

This limitation does not create a competing source identity and does not authorize later mature-host attachment. SERVER REINTEGRATE and CLIENT ATTACH must repeat byte-level ZIP/checksum, archive-integrity, path-safety, and origin-file verification before touching either mature source lane.
