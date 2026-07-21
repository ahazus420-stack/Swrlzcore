# SOURCE-LANE-HYGIENE-001C Verification Receipt

- Status: `STATIC_HYGIENE_VALIDATION_PASS`
- Branch: `checkpoint/source-lane-hygiene-001c`
- Parent checkpoint: `checkpoint/workflow-surface-cleanup-001b`
- Operation: move-only archival; no file-byte changes

## Active identity resolution

- SERVER active identity: `SERVER_CFv1.0.4_SWRLZ`
  - repository ZIP path: `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(2).zip`
  - repository Git blob: `281db43117a18a2d4cd38fe6af5944d29a973a63`
  - locally verified Git blob: `281db43117a18a2d4cd38fe6af5944d29a973a63`
  - SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`
  - checksum match: PASS
- CLIENT active identity: `CLIENT_CFv1.0.1_SWRLZ`
  - repository Git blob: `ab30a195a319bd1aff7c244475886cb71877af9f`
  - locally verified Git blob: `ab30a195a319bd1aff7c244475886cb71877af9f`
  - SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`
  - checksum match: PASS

## Older SERVER evidence

- `SERVER_CFv1.0.1_SWRLZ`
  - ZIP blob: `b0e64f8cc4c319a81d17fc9c8cf041fad5edfbe9`
  - SHA-256 file: `c6c4a802a30743d94df80cc98fec9e409860be9b8e23dd58ff04988a569530a7`
  - version report records the same current SHA-256 and ZIP-integrity PASS
- `SERVER_CFv1.0.2_SWRLZ`
  - ZIP blob: `64f6d8bc47280648897b22f14ad7810856fea16d`
  - SHA-256 file: `9d278bed4944eee20d6b9dc1ea89ba9363c30a7e7daa5b91b44836c386abd200`
  - version report records the same current SHA-256
  - successful APK provenance records checksum status `verified` for the same source path and SHA-256
- `SERVER_CFv1.0.3_SWRLZ`
  - repository ZIP blob: `925546a6ed513b463603a56b41836f43608ed8b0`
  - locally verified ZIP blob: `925546a6ed513b463603a56b41836f43608ed8b0`
  - SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`
  - checksum match: PASS

## Older CLIENT evidence

- `CLIENT_CFv1.0.0_SWRLZ`
  - ZIP blob: `a799fa6147685e9db1cff3c8851555d8102a3015`
  - SHA-256 file: `8c6933bd122e7d099a6b9576d53eb52da3a9cbfe9e0a58302cec6fe0c2760e89`
  - successful GitHub Actions summary identifies this exact source ZIP and SHA file

## Collision and preservation checks

- Destination ZIP collisions for SERVER v1.0.1, v1.0.2, v1.0.3: NONE
- Destination ZIP collision for CLIENT v1.0.0: NONE
- Destination README collisions: NONE
- Active Android-suffixed SERVER files: PRESERVED
- `SWRLZ_NODE_HOST_COMPLETE.zip`: PRESERVED
- `README_SWRLZ_NODE_HOST.md`: PRESERVED
- Canonical lane `README.md` files: PRESERVED
- Existing `OLD_PATCHES` contents: UNCHANGED
- Workflow changes: NONE
- Build-request changes: NONE
- Build/workflow execution: NONE

## Move contract

Every archived file is inserted at its destination using its existing Git blob SHA and removed from its former top-level path in the same tree commit. This is byte-preserving by construction.
