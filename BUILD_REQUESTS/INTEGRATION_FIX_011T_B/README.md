# INTEGRATION-FIX-011T-B SERVER proof-infrastructure source overlay

This directory is a deterministic, non-mutating SERVER source candidate rooted at contract commit `fcd619a08d03a6c9c4e3ef08afb4e42341bc813a`.

Apply the files beneath `files/` to the matching paths inside the canonical SERVER Android project only after a later approval authorizes source-candidate packaging or promotion.

Included:

- Android Keystore AES-GCM master-key wrapper;
- application-private atomic encrypted proof sidecar;
- HKDF-SHA256 derivation bound to `serverInstallationId` and `deviceId`;
- locked binding states;
- paired local-LAN authorization primitive;
- timestamp, nonce, request-ID, body-hash, HMAC, and replay verification;
- read-only/non-mutating device resolution;
- safe `PROOF_ENROLLMENT_REQUIRED` handling for known legacy devices.

Excluded:

- CLIENT changes;
- registration-time binding;
- legacy enrollment endpoint;
- membership restoration or mutation;
- group creation/join;
- heartbeat or admin routes;
- registry migration;
- Gradle/APK builds;
- workflow execution;
- merge or promotion.
