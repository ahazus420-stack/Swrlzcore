# INTEGRATION-FIX-011T-C Completion Receipt

- Status: `COMPLETE_STATIC_ARCHIVE_VALIDATION_PASS`
- Branch: `checkpoint/server-device-proof-011t-b`
- Canonical input: `SERVER_CFv1.0.3_SWRLZ.zip`
- Canonical SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`
- Successor candidate: `SERVER_CFv1.0.4_SWRLZ.zip`
- Successor SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`
- ZIP CRC/integrity: `PASS`
- Duplicate entries: `NONE`
- Deterministic path ordering and fixed timestamps: `PASS`
- Approved changed paths: `7`
- Unapproved archive-content changes: `NONE`
- Original canonical archive mutation: `NONE`
- Gradle/APK/workflow execution: `NONE`

## Applied paths

1. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/AndroidKeystoreProofCipher.kt`
2. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/DeviceProofModels.kt`
3. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/HkdfSha256.kt`
4. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/PairedLanAuthorizer.kt`
5. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/ProofBindingSidecar.kt`
6. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/security/RequestProofVerifier.kt`
7. `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/NonMutatingDeviceResolution.kt`

The successor candidate was generated from the checksum-verified uploaded canonical archive. Validation compared every non-directory archive entry and confirmed that only these seven approved paths differ.

This receipt is static archive evidence only. It is not compiler, Gradle, APK, runtime, endpoint, or deployment evidence.
