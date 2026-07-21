# CORE-IMP-009A Lineage and Source Identity

## Repository checkpoint lineage

- Repository: `ahazus420-stack/Swrlzcore`
- Checkpoint branch: `checkpoint/core-imp-009a`
- Implementation branch base: `00bb23d7f4fed389a7b24e7ec2127399da0d924f`
- Required accepted ancestor: `ef62e870e30143912be992972aed89849f186448`
- Current `main` at checkpoint close: `961e92907acb6a3158f6da982902f07acbfba019`

The implementation base descends from the required accepted ancestor. During this checkpoint, `main` advanced through separately authorized source-lineage work. The capsule branch was not rebased or merged; this record reconciles evidence only.

## Current active mature source declarations

### CLIENT

- ZIP: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip`
- Sibling checksum: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.sha256`
- Declared SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`

### SERVER/NODE_HOST

- ZIP: `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.zip`
- Sibling checksum: `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.sha256`
- Declared SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`

`SERVER-LINEAGE-FIX-014` resolved the former same-name ambiguity by canonicalizing the accepted v1.0.4 successor from duplicate-download transport names without rewriting either binary blob. The active pair is now canonical and unsuffixed at the SERVER lane root.

## Preserved SERVER predecessor lineage

SERVER v1.0.3 is preserved under:

- `SOURCES/SERVER/OLD_PATCHES/SERVER_CFv1.0.3_SWRLZ.zip`
- `SOURCES/SERVER/OLD_PATCHES/SERVER_CFv1.0.3_SWRLZ.sha256`
- Declared predecessor SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`

The accepted SERVER lineage record states that v1.0.4 ZIP integrity passed, duplicate entries were absent, deterministic ordering and timestamps passed, and the canonical paths reuse the previously verified blobs byte-for-byte.

## Download-suffix discipline

A terminal Android/browser duplicate-download suffix such as `(1)` or `(2)` is transport metadata, not source-version identity. Candidate normalization must fail closed unless canonical naming, ZIP integrity, checksum association, digest agreement, and ambiguity checks all pass. Repository storage remains canonical and unsuffixed.

## Preserved origin hash records

- `DiscoveryProtocol.kt`: `36c248b3d2ad5cee53d8a048607b41aa3fea32ae9578afbe5773ae06cee21a97`
- `NodeCompatibilityProtocol.kt`: `43325599ffa1fb97cf7c508b5eef600aad5e4247cdcf384924e655867754c319`

No mature source was copied, modified, attached, built, or invoked by CORE-IMP-009A.

## Verification boundary

CORE-IMP-009A independently verified the standalone capsule package and tests. It did not re-stream the mature CLIENT or SERVER ZIP bytes to perform a new independent SHA-256 calculation. Current mature-source identities are therefore grounded in their repository sibling checksums and accepted lineage evidence.

Any later SERVER REINTEGRATE or CLIENT ATTACH checkpoint must repeat exact ZIP/checksum, archive-integrity, path-safety, and origin-file verification before modifying either mature host.
