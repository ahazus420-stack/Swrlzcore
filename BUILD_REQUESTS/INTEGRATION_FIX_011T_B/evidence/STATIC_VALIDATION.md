# INTEGRATION-FIX-011T-B Static Validation Evidence

- Validation type: deterministic source inspection only
- Root contract commit: `fcd619a08d03a6c9c4e3ef08afb4e42341bc813a`
- Candidate branch: `checkpoint/server-device-proof-011t-b`
- Gradle invoked: no
- APK built: no
- Workflow triggered: no
- Canonical SERVER ZIP modified: no

## Required infrastructure markers

- Android Keystore provider: present
- AES-GCM authenticated encryption: present
- HKDF-SHA256 extract/expand: present
- salt binds `serverInstallationId` and `deviceId`: present
- binding states `BOUND`, `ENROLLMENT_REQUIRED`, `REVOKED`, `UNAVAILABLE`, `CONFLICT`: present
- application-private `AtomicFile` sidecar: present
- associated authenticated data binds version/server/device/state: present
- paired local-LAN authorization: present
- body SHA-256 verification: present
- HMAC-SHA256 verification: present
- timestamp and nonce validation: present
- request-ID replay ledger: present
- non-mutating exact-device registry lookup: present
- known legacy device returns `ACTION_REQUIRED / PROOF_ENROLLMENT_REQUIRED`: present
- retired/replaced/blocked lineage is preserved: present

## Mutation boundary inspection

The candidate contains no registration route, enrollment endpoint, membership write, group create/join, heartbeat mutation, administrator route, registry migration, CLIENT source, Gradle invocation, workflow change, APK, release, or deployment material.

## Result

`PASS` for the approved source-candidate and static-validation scope only.

This receipt is not build evidence and does not establish runtime or compiler correctness. A later build checkpoint must remain separately approval-gated.
