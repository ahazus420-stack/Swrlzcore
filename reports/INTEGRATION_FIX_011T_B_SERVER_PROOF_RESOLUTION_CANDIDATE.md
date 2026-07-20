# INTEGRATION-FIX-011T-B — SERVER v1.1.1 Proof-Sidecar and Resolution Candidate

- **Checkpoint:** `INTEGRATION-FIX-011T-B`
- **Status:** Source candidate and static validation complete; Android/Gradle build not run
- **Branch:** `checkpoint/server-presence-registry-011k`
- **Base candidate:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **Base SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- **Successor candidate:** `SERVER_CFv1.1.1_SWRLZ.zip`
- **Successor SHA-256:** `762d72e445a3a9fcb48da11905dbc0261b206060b55760dfa96fefbf1e9486e4`
- **Successor size / entries:** `159733 bytes / 73 entries`
- **Protocol / schema:** `1 / 1`
- **Application version:** `1.1.1` / version code `4`

## 1. Facts

The deterministic successor changes exactly ten internal SERVER paths:

```text
API.md
INTEGRATION_FIX_011T_B_IMPLEMENTATION_REPORT.md
app/build.gradle.kts
app/src/main/java/sh/swrlz/nodehost/service/DeviceProofBindingStore.kt
app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt
app/src/main/java/sh/swrlz/nodehost/service/NodeRuntime.kt
app/src/main/java/sh/swrlz/nodehost/service/PresenceProtocol.kt
app/src/main/java/sh/swrlz/nodehost/service/PresenceRegistry.kt
app/src/main/java/sh/swrlz/nodehost/service/PresenceRequestSecurity.kt
scripts/test_device_proof_011tb.py
```

The candidate implements:

- Android Keystore AES-GCM proof-key protection using alias `swrlz.device-proof.master.v1`;
- an application-private `AtomicFile` proof-binding sidecar outside the presence registry;
- binding states `BOUND`, `ENROLLMENT_REQUIRED`, `REVOKED`, `UNAVAILABLE`, and `CONFLICT`;
- HKDF-SHA256 derivation scoped to SERVER installation ID and stable device ID;
- HMAC-SHA256 canonical-request verification using the exact request-body SHA-256;
- five-minute timestamp freshness, nonce/request-ID validation, and a bounded ten-minute replay cache;
- canonical `x-swrlz-pairing-token` handling, retaining the historical misspelled header only as a loopback compatibility alias;
- same-prefix IPv4 local-LAN classification;
- non-mutating `POST /devices/resolve`;
- `ACTION_REQUIRED / PROOF_ENROLLMENT_REQUIRED` for an active known legacy device without a usable binding;
- lineage-preserving retired, replaced, blocked, conflict, and unknown outcomes.

Existing mutating routes remain loopback-only and function-equivalent to the v1.1.0 base. The candidate does not populate proof bindings through registration and does not expose a legacy enrollment endpoint.

## 2. Requirements satisfied

The candidate satisfies the approved `011T-B` source scope by providing the proof-store, derivation, request-authentication, replay, paired-LAN resolution, and legacy action-required infrastructure without changing CLIENT code or authoritative presence-registry records.

Device resolution remains non-mutating. A missing sidecar for a known device is never interpreted as an unknown device, preventing duplicate registration and preserving Ghost/Legacy lineage.

A valid proof establishes possession of device-bound proof material only. It does not establish group membership, administrator authority, mission authority, trust-root authority, or Truth Firewall authority.

## 3. Assumptions and validation limits

- Android Keystore and Android framework behavior were source-checked but not exercised on an Android runtime.
- Kotlin source/type checks used minimal local platform stubs; they are not a substitute for an Android Gradle compile.
- The sidecar is empty on existing installations until a separately approved binding or enrollment checkpoint runs.
- Same-prefix LAN classification depends on Android network-interface address and prefix metadata available at runtime.
- No claim is made that an APK has been produced or that this candidate has run on a physical device.

## 4. Static validation evidence

Static validation passed for:

- exact v1.1.0 base checksum and entry count;
- ten-path change limiter;
- absence of registry migration;
- protocol/schema `1 / 1` preservation;
- successor version `1.1.1` / version code `4`;
- non-mutating resolution;
- unchanged loopback-only mutation exposure;
- AtomicFile and Android Keystore AES-GCM invariants;
- deterministic HKDF vectors and HMAC behavior;
- exact-body hashing, timestamp, nonce, request-ID, and replay rules;
- legacy `PROOF_ENROLLMENT_REQUIRED` behavior;
- deterministic successor ZIP reproduction and integrity.

Validator:

```text
scripts/test_device_proof_011tb.py
0b4f2c92a9461fad2207f9adb5e7dd2e882298d1f6e849980a3c629630fa0fbd
```

Transparent source transport:

```text
10 ordered UTF-8 unified-diff patch files
71,577 total patch bytes
per-file SHA-256 values locked in candidate-manifest.json
```

The materializer validates every patch checksum, applies each hunk against the exact v1.1.0 base, verifies the ten resulting source-file SHA-256 values, and then creates the deterministic successor ZIP.

## 5. Recommendations

1. Perform one isolated Android `clean assembleDebug` validation against this exact checksum-bound candidate.
2. Do not begin registration-time proof binding until Android compilation and source validation are both accepted.
3. Keep legacy enrollment separately approval-gated because it requires an explicitly secure enrollment transport.
4. Add runtime tests for Keystore invalidation, sidecar corruption, replay rejection, and LAN-address classification before release consideration.

## 6. Boundary confirmation

This checkpoint did not:

- modify CLIENT source;
- bind proof material during registration;
- create `POST /devices/proof/enroll`;
- implement membership restoration, atomic create-and-join, or heartbeat changes;
- create administrator/session routes;
- migrate the presence registry or database;
- run Gradle or build an APK;
- trigger GitHub Actions;
- rebase, merge, or promote to `main`;
- install, release, or deploy anything.
