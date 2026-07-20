# INTEGRATION-FIX-011T-C Execution Blocker

- Status: `BLOCKED_FAIL_CLOSED`
- Approval: accepted
- Branch: `checkpoint/server-device-proof-011t-b`
- Approved overlay source: `BUILD_REQUESTS/INTEGRATION_FIX_011T_B/files`
- Intended canonical input: `SOURCES/SERVER/SERVER_CFv1.0.3_SWRLZ.zip`
- Original archive mutation: none
- Gradle/APK/workflow execution: none

## Blocker

The GitHub connector confirmed the canonical ZIP blob exists, but returned its binary body only as a response-truncated base64 stream. The active runtime cannot safely download the public repository binary directly, and the connector blob decoder attempts UTF-8 decoding and fails on ZIP bytes.

Because the complete canonical bytes could not be materialized, this checkpoint did not fabricate, partially reconstruct, or claim a successor archive or SHA-256.

## Completed bounded work

A deterministic, fail-closed application harness was committed at:

`BUILD_REQUESTS/INTEGRATION_FIX_011T_C/APPLY_011T_B_TO_SERVER_ARCHIVE.py`

The harness:

1. verifies the canonical SHA-256 before extraction;
2. rejects ZIP CRC failures and duplicate paths;
3. discovers exactly one Android NODE_HOST project root;
4. locks the overlay to exactly seven approved paths;
5. applies no other file changes;
6. emits a deterministic ZIP with fixed entry timestamps and ordering;
7. emits the successor SHA-256 and JSON changed-path manifest;
8. revalidates the successor ZIP CRC;
9. does not invoke Gradle, workflows, APK tooling, install, release, or deploy operations.

## Resume condition

Execution can resume without expanding authority when the exact canonical SERVER ZIP bytes are available in the active runtime or through a connector action that returns a reusable binary file reference.
