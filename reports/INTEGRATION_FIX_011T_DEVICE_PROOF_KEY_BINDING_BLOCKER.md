# INTEGRATION-FIX-011T — Device-Proof Key Binding Blocker

- **Checkpoint:** `INTEGRATION-FIX-011T`
- **Status:** Stopped before SERVER source modification
- **Branch:** `checkpoint/server-presence-registry-011k`
- **Reviewed source candidate:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **Reviewed source SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- **Protocol / schema:** `1 / 1`

## 1. Authorized slice

The approved slice authorized implementation of:

1. paired local-LAN authorization infrastructure;
2. timestamp, nonce, request-ID, replay, and device-proof validation;
3. non-mutating `POST /devices/resolve`;
4. source-candidate and static-validation evidence only.

The approval explicitly excluded registration changes and database/storage migration.

## 2. Source materialization verification

The exact SERVER v1.1.0 candidate was materialized from canonical base:

```text
SERVER_CFv1.0.3_SWRLZ.zip
127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5
```

using the checksum-bound 011K replacement bundle:

```text
8e2f07bd5fc2632dbf22280b95198c1a3cf15e975818f2e5b076880ae40108c6
```

The deterministic result matched:

```text
SERVER_CFv1.1.0_SWRLZ.zip
f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f
143721 bytes
69 ZIP entries
```

## 3. Confirmed source fact

The persistent `DeviceRecord` stores only:

```text
deviceKeyVerifier
```

The verifier is created using PBKDF2-HMAC-SHA256. The raw stable device key is accepted during registration and check-in, but it is not persisted in recoverable form.

The current verifier API can answer:

```text
Does this supplied raw key match the stored verifier?
```

It cannot recover the raw key or produce an HMAC for an arbitrary request.

## 4. Contract requirement

The accepted 011S contract defines known-device proof as HMAC-SHA256 over the canonical request text, keyed by the stable CLIENT device key already bound to the SERVER record.

To validate that proof, SERVER must possess HMAC-capable verification material equivalent to that device key. A one-way PBKDF2 password verifier is insufficient for this operation.

## 5. Blocking conflict

A correct implementation therefore requires at least one separately approved change:

1. persist the device key or a contractually equivalent HMAC verification key in Android-Keystore-protected storage during registration/rebinding; or
2. replace the symmetric HMAC lane with an asymmetric public-key signature lane; or
3. revise the request contract to transmit raw device credentials, which is not recommended and would weaken the approved separation between proof and secret transport.

Option 1 necessarily changes registration/key-binding behavior and introduces persistent proof-key storage. Those changes were explicitly outside the 011T authorization.

## 6. Stop decision

No SERVER source was modified because continuing would have silently crossed the approved boundaries:

- `NO-REGISTRATION-CHANGE`
- `NO-DATABASE-MIGRATION`
- protocol and security-contract discipline

A partial implementation that returned `UNKNOWN` for known devices whose proof could not be checked was rejected because it could trigger duplicate registration and violate Ghost/Legacy lineage requirements.

## 7. Static validation result

```text
record_has_verifier=true
record_has_raw_key=false
register_hashes_raw_key=true
verifier_is_pbkdf2=true
verifier_api_only_verify=true
protocol_receives_raw_registration_key=true
RESULT=BLOCKED_WITHOUT_PROOF_KEY_BINDING_STORAGE_OR_PROTOCOL_CHANGE
```

## 8. Boundary confirmation

This checkpoint did not:

- modify CLIENT or SERVER implementation;
- create `/devices/resolve`;
- enable LAN writes;
- change registration, membership, group, heartbeat, or admin behavior;
- migrate registry/storage;
- run Gradle, build an APK, or trigger a workflow;
- rebase, merge, promote to `main`, install, release, or deploy.
