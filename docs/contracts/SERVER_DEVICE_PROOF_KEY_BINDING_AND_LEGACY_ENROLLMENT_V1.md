# SERVER Device-Proof Key Binding and Legacy Enrollment v1

- **Checkpoint:** `INTEGRATION-FIX-011T-A`
- **Status:** Accepted documentation contract; implementation separately approval-gated
- **Protocol version:** `1`
- **Schema version:** `1`
- **Applies to:** SWRLZ CLIENT ↔ Android NODE_HOST local-LAN bootstrap lane
- **Depends on:**
  - `SERVER_PAIRED_LAN_BOOTSTRAP_ROUTE_SURFACE_V1.md`
  - `INTEGRATION_FIX_011T_DEVICE_PROOF_KEY_BINDING_BLOCKER.md`
  - `SERVER_NODE_HOST_COMPATIBILITY_SURFACE_V1.md`

## 1. Purpose

Resolve the proven key-material mismatch between:

```text
011S device-proof requirement
=
HMAC-SHA256 using device-key-derived proof material
```

and:

```text
current SERVER persistence
=
PBKDF2 verifier only
```

without storing plaintext device keys, weakening Ghost/Legacy lineage, silently misclassifying known devices as unknown, or changing the authoritative presence-registry schema.

The bounded model is:

```text
stable CLIENT device key
→ derive SERVER-specific proof key
→ encrypt proof key with Android Keystore
→ store encrypted sidecar binding
→ validate future request HMACs without retransmitting raw device key
```

## 2. Security and authority boundaries

This contract does not grant:

- administrator authority;
- mission authority;
- trust-root authority;
- Truth Firewall authority;
- public/hosted transport authority;
- permission to bypass pairing, device state, lineage, or membership checks.

A valid proof demonstrates possession of device-bound proof material only. Route-specific authorization remains independently required.

Dev Mode, local cache, network reachability, a caller-supplied device ID, or a local UI toggle never establishes proof binding.

## 3. Existing verifier remains authoritative for raw-key equality

The existing PBKDF2-HMAC-SHA256 `deviceKeyVerifier` remains in the persistent device record and remains the authoritative mechanism for answering:

```text
Does this supplied raw device key match the key originally bound to this record?
```

It is not used directly as an HMAC request-signing key and must not be treated as recoverable device-key material.

No presence-registry field is replaced or repurposed by this contract.

## 4. Derived proof-key algorithm

### 4.1 Inputs

For device-proof binding version `1`, both CLIENT and SERVER derive:

```text
IKM = UTF-8 bytes of the stable CLIENT device key
salt = SHA256(
  "SWRLZ-DEVICE-PROOF-SALT-V1\n" +
  serverInstallationId + "\n" +
  deviceId
)
info = UTF-8 bytes of "SWRLZ-DEVICE-PROOF-KEY-V1"
length = 32 bytes
```

### 4.2 Derivation

The proof key is:

```text
proofKey = HKDF-SHA256(IKM, salt, info, 32)
```

The output is server-specific and device-specific. A proof key bound to one SERVER installation must not authorize requests to another SERVER installation.

### 4.3 Raw-key handling

The raw device key:

- may exist transiently in memory during a newly approved registration or explicit legacy enrollment;
- must be cleared from mutable buffers where practical after derivation and verification;
- must not be persisted in plaintext;
- must not be written to logs, diagnostics, crash reports, telemetry, or ordinary preferences;
- must not be returned by SERVER responses.

## 5. Android Keystore protection

### 5.1 Master key

SERVER protects stored proof keys with an Android Keystore-backed non-exportable AES-GCM master key.

Recommended alias:

```text
swrlz.device-proof.master.v1
```

Required characteristics:

- AES-256 where supported;
- GCM authenticated encryption;
- non-exportable key material;
- application-private use;
- hardware-backed protection when available, without silently refusing otherwise-supported devices solely because hardware backing is unavailable;
- no user-authentication requirement that would prevent unattended heartbeat verification unless separately approved.

### 5.2 Sidecar storage

Proof bindings are stored outside the authoritative presence-registry schema in an application-private sidecar.

Logical record:

```json
{
  "bindingVersion": 1,
  "deviceId": "stable-device-id",
  "serverInstallationId": "server-installation-id",
  "state": "BOUND",
  "ciphertext": "base64",
  "nonce": "base64",
  "createdAt": "timestamp",
  "updatedAt": "timestamp",
  "keyFingerprint": "sha256-hex-of-proof-key",
  "source": "NEW_REGISTRATION | LEGACY_ENROLLMENT | REBIND"
}
```

The sidecar must:

- be application-private;
- use atomic replacement semantics;
- authenticate ciphertext and associated binding metadata;
- reject duplicate active bindings for one `(serverInstallationId, deviceId, bindingVersion)` tuple;
- preserve explicit revoked or unavailable state rather than silently deleting evidence;
- never contain plaintext device keys or group credentials.

Associated authenticated data should bind at least:

```text
bindingVersion
serverInstallationId
deviceId
state
```

## 6. Proof-binding states

SERVER represents each known device proof binding as exactly one of:

```text
BOUND
ENROLLMENT_REQUIRED
REVOKED
UNAVAILABLE
CONFLICT
```

### `BOUND`

A valid encrypted proof key exists and can be decrypted under the active Keystore master key.

### `ENROLLMENT_REQUIRED`

The device record exists, but no usable proof-key binding exists. This is the required state for pre-contract legacy devices.

### `REVOKED`

The proof binding was explicitly revoked. Requests must fail closed until a separately authorized rebind succeeds.

### `UNAVAILABLE`

The binding exists but cannot currently be used because of Keystore invalidation, sidecar corruption, unsupported cryptography, or another recoverability failure.

### `CONFLICT`

Multiple, contradictory, mismatched, or lineage-unsafe bindings were detected.

No state except `BOUND` permits normal known-device HMAC authorization.

## 7. Resolution behavior

`POST /devices/resolve` must never return `UNKNOWN` merely because proof material is absent or unavailable for a known device.

For an exact known device ID under valid pairing authorization:

| Device record | Binding state | Resolution behavior |
|---|---|---|
| active | `BOUND` and proof valid | `KNOWN` |
| active | `ENROLLMENT_REQUIRED` | `ACTION_REQUIRED` with `PROOF_ENROLLMENT_REQUIRED` |
| active | `REVOKED` | fail closed with `PROOF_KEY_REVOKED` |
| active | `UNAVAILABLE` | fail closed with `PROOF_KEY_UNAVAILABLE` |
| active | `CONFLICT` | `IDENTITY_CONFLICT` or `PROOF_BINDING_CONFLICT` |
| retired/replaced/blocked | any | preserve existing lineage/status result; do not reactivate |
| no matching record | none | `UNKNOWN` |

Responses for unproven devices expose only the minimum information necessary for recovery. They must not enumerate registry data, memberships, groups, admin state, or unrelated lineage.

## 8. New-device binding during registration

A future source implementation may bind proof material during successful registration because the raw stable device key is already present for verifier creation.

The logical operation must be atomic from the caller's perspective:

1. validate pairing, identity, lineage, request ID, and registration payload;
2. verify no conflicting device record or proof binding exists;
3. create the PBKDF2 verifier;
4. derive the SERVER-specific proof key;
5. stage the Keystore-encrypted sidecar binding;
6. persist the device record and proof binding as one logical success;
7. return registration success only after both are durable.

If proof binding fails, registration must not report success and must not leave an active partially bound device that would later be mistaken for a normal `BOUND` device.

Repeating the same valid registration request is idempotent and returns the same logical device and proof-binding result.

Registration does not grant group, administrator, mission, or trust authority.

## 9. Legacy enrollment

### 9.1 Required behavior

An existing device with no proof sidecar enters:

```text
PROOF_ENROLLMENT_REQUIRED
```

It remains the same registered device with the same lineage and memberships. SERVER must not:

- return `UNKNOWN`;
- create a duplicate device;
- rotate identity automatically;
- erase membership;
- treat enrollment as registration;
- infer administrator or mission authority.

### 9.2 Future route contract

A separately implemented route may use:

```text
POST /devices/proof/enroll
```

This document defines behavior only; it does not create the endpoint.

### 9.3 Preconditions

Legacy enrollment requires:

- compatible protocol/schema `1 / 1`;
- valid SERVER pairing authorization;
- exact known active device ID;
- device state not blocked, retired, replaced, or conflicted;
- one-time enrollment challenge issued by SERVER;
- unique request ID;
- explicit user-visible enrollment action;
- a transport permitted by Section 9.4;
- raw key equality verified against the existing PBKDF2 verifier before binding.

### 9.4 Transport restriction

The raw stable device key must not be sent over an unencrypted LAN connection merely because pairing succeeded.

Legacy enrollment is allowed only through one of:

1. SERVER-local or loopback enrollment where the secret does not traverse the LAN; or
2. a mutually verified encrypted local transport separately accepted for SWRLZ; or
3. a later asymmetric enrollment mechanism approved by a new contract.

Pairing tokens, same-subnet status, or discovery validation alone do not make plaintext secret transport acceptable.

### 9.5 Enrollment request shape

Over an accepted secure enrollment transport, the logical request is:

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "device_id": "stable-device-id",
  "enrollmentChallenge": "server-issued-one-time-value",
  "device_key": "raw-stable-device-key"
}
```

The raw key is accepted only for verifier equality and proof-key derivation. It is discarded after the operation.

### 9.6 Enrollment transaction

SERVER must:

1. validate pairing, transport, request ID, challenge freshness, and device status;
2. verify the raw device key against `deviceKeyVerifier`;
3. derive the proof key using Section 4;
4. reject an unexpected existing non-equivalent binding;
5. encrypt and persist the sidecar binding;
6. consume the one-time challenge;
7. record a non-secret audit event;
8. return `BOUND` only after durable success.

A failed attempt does not alter the existing device record, lineage, memberships, or authority.

### 9.7 Idempotency and replay

- Repeating the same authenticated request ID and challenge after a successful enrollment returns the existing successful logical result.
- Reusing the request ID with different material fails with `REQUEST_ID_CONFLICT`.
- Reusing an enrollment challenge for a different request fails closed.
- Repeated failed raw-key attempts are rate-limited and audited without logging the submitted secret.

## 10. Proof-key rotation and revocation

Proof-key rotation or rebind is not automatic.

A future rebind requires:

- explicit action;
- valid current proof or separately approved recovery authority;
- lineage-safe identity confirmation;
- new request ID and challenge;
- durable replacement of the encrypted sidecar;
- revocation of the prior binding;
- non-secret audit evidence.

Deleting or invalidating a sidecar must not delete the device record. The device transitions to `REVOKED`, `UNAVAILABLE`, or `ENROLLMENT_REQUIRED` as appropriate.

## 11. Keystore loss and recovery

If the Android Keystore master key is invalidated, missing, or cannot decrypt an existing sidecar:

```text
known device
→ PROOF_KEY_UNAVAILABLE
→ ACTION_REQUIRED
```

SERVER must not:

- reinterpret the device as unknown;
- accept pairing alone as device proof;
- regenerate proof material from the PBKDF2 verifier;
- silently reset the device;
- discard Ghost/Legacy lineage.

Recovery requires a separately authorized re-enrollment or rebind path.

## 12. Request proof after binding

After binding, the `011S` HMAC canonical request format remains authoritative.

CLIENT derives the same proof key using Section 4 and signs requests with that derived proof key rather than transmitting the raw stable device key.

SERVER decrypts the bound proof key only for the minimum duration necessary to verify the HMAC and must avoid retaining it in long-lived ordinary objects or logs.

Proof comparison is constant-time where practical. Timestamp, nonce, request-ID, body-hash, route, and replay rules from `011S` remain unchanged.

## 13. Structured error codes

The implementation must distinguish at least:

```text
PROOF_ENROLLMENT_REQUIRED
PROOF_ENROLLMENT_NOT_ALLOWED
PROOF_ENROLLMENT_CHALLENGE_REQUIRED
PROOF_ENROLLMENT_CHALLENGE_INVALID
PROOF_ENROLLMENT_CHALLENGE_EXPIRED
PROOF_KEY_BINDING_FAILED
PROOF_KEY_UNAVAILABLE
PROOF_KEY_REVOKED
PROOF_BINDING_CONFLICT
PROOF_KEYSTORE_INVALIDATED
PROOF_STORAGE_CORRUPT
DEVICE_KEY_MISMATCH
RATE_LIMITED
INTERNAL_ERROR
```

These are additive under protocol/schema `1 / 1` because they refine structured outcomes without changing the existing version envelope.

## 14. Audit requirements

Audit records may include:

- device ID;
- SERVER installation ID;
- binding version;
- event type;
- result;
- timestamp;
- request ID;
- actor/transport classification;
- non-secret failure code.

Audit records must not include:

- raw device key;
- derived proof key;
- ciphertext plaintext;
- HMAC proof value;
- pairing token;
- group credentials;
- admin session secrets.

Required event types include:

```text
PROOF_BIND_CREATED
PROOF_BIND_REUSED_IDEMPOTENTLY
PROOF_BIND_REJECTED
PROOF_BIND_REVOKED
PROOF_BIND_UNAVAILABLE
PROOF_BIND_CONFLICT
PROOF_ENROLLMENT_REQUIRED
```

## 15. Protocol and schema decision

This contract remains under:

```text
protocolVersion = 1
schemaVersion = 1
```

Reasoning:

- routes and structured error codes are additive;
- the authoritative presence-registry record schema is unchanged;
- proof-key persistence is an application-private sidecar with its own `bindingVersion`;
- existing discovery and compatibility envelopes remain valid.

Implementation must stop for explicit approval if source work proves that a registry migration, incompatible payload change, or protocol-version change is actually required.

## 16. Acceptance scenarios

### 16.1 New registration

1. SERVER receives a separately authorized registration request.
2. PBKDF2 verifier and derived proof key are created.
3. Proof key is Keystore-encrypted into the sidecar.
4. Registration succeeds only after both records are durable.
5. Subsequent signed resolve returns `KNOWN`.

### 16.2 Existing legacy device

1. SERVER finds the existing device record.
2. No proof sidecar exists.
3. Resolve returns `ACTION_REQUIRED` / `PROOF_ENROLLMENT_REQUIRED`.
4. SERVER does not return `UNKNOWN` or create a duplicate device.
5. Existing membership and lineage remain untouched.

### 16.3 Successful legacy enrollment

1. User explicitly starts enrollment over an allowed secure transport.
2. SERVER validates pairing, challenge, device status, request ID, and raw-key equality.
3. SERVER derives and encrypts the proof key.
4. Binding becomes `BOUND`.
5. The same device record, lineage, and memberships remain authoritative.

### 16.4 Wrong raw key

1. PBKDF2 verification fails.
2. SERVER returns `DEVICE_KEY_MISMATCH`.
3. No sidecar is created or modified.
4. Device remains `ENROLLMENT_REQUIRED`.
5. Attempt is rate-limited and audited without secret logging.

### 16.5 Keystore invalidation

1. Existing sidecar cannot be decrypted.
2. SERVER returns `PROOF_KEY_UNAVAILABLE` or `PROOF_KEYSTORE_INVALIDATED`.
3. Device is not treated as unknown.
4. Pairing alone does not authorize the request.
5. Explicit recovery is required.

### 16.6 Ghost/Legacy device

1. Submitted identity resolves to retired or replaced lineage.
2. SERVER preserves the lineage outcome.
3. Enrollment does not silently reactivate or replace the record.
4. Recovery requires a separately approved lineage-repair action.

### 16.7 SERVER replacement

1. CLIENT connects to a different SERVER installation ID.
2. The prior SERVER-specific proof key is not valid for the new SERVER.
3. CLIENT does not reuse proof binding across installations.
4. New pairing/registration/enrollment follows the new SERVER's authority.

## 17. Recommended implementation order

A later source checkpoint should proceed in this order:

1. Keystore master-key wrapper;
2. encrypted sidecar store and binding-state model;
3. deterministic HKDF derivation helper and tests;
4. non-mutating proof-binding lookup;
5. `PROOF_ENROLLMENT_REQUIRED` resolution behavior;
6. new-registration binding integration under separate approval;
7. secure legacy enrollment route under separate approval;
8. HMAC request verification and replay cache;
9. acceptance and failure-path tests.

Each slice remains separately approval-gated.

## 18. Non-goals

This contract does not authorize:

- SERVER or CLIENT source changes;
- creation of a functional endpoint;
- registry/database migration;
- LAN-write enablement;
- transmission of raw device keys over plaintext LAN transport;
- automatic identity rotation or duplicate registration;
- admin, mission, trust-root, or Truth Firewall elevation;
- Gradle or APK build;
- workflow execution;
- rebase, merge, or `main` promotion;
- installation, release, or deployment.
