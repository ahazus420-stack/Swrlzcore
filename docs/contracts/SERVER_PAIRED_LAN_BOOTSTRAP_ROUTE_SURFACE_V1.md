# SERVER Paired-LAN Bootstrap Route Surface v1

- **Checkpoint:** `INTEGRATION-FIX-011S`
- **Status:** Approved behavioral and route contract; implementation separately approval-gated
- **Protocol version:** `1`
- **Schema version:** `1`
- **Applies to:** SWRLZ CLIENT ↔ Android NODE_HOST local-LAN bootstrap lane
- **Depends on:**
  - `CLIENT_AUTOMATIC_BOOTSTRAP_GROUP_RESTORATION_RADAR_DEV_MODE_V1.md`
  - `SERVER_NODE_HOST_COMPATIBILITY_SURFACE_V1.md`
  - `INTEGRATION_FIX_011R_SERVER_ROUTE_LOCK_REPORT.md`

## 1. Purpose

Define the additive SERVER surface required for a normal paired CLIENT to progress truthfully from verified SERVER discovery through device resolution, optional idempotent registration, authoritative membership restoration, atomic group creation-and-join, heartbeat, and group-scoped Radar.

The intended flow is:

```text
verify SERVER
→ authorize paired local-LAN request
→ resolve stable CLIENT identity
→ register only if SERVER reports unknown
→ restore SERVER-authoritative membership
→ require explicit choice if multiple memberships are ambiguous
→ start heartbeat
→ expose permitted group-scoped Radar
```

This contract does not grant administrator, mission, trust-root, or Truth Firewall authority.

## 2. Scope and transport boundary

### 2.1 Local-LAN only

This contract applies only to a CLIENT and SERVER communicating on an explicitly local network path.

It is not a hosted/public-internet authentication contract. Public or hosted transport requires a separately approved transport and credential model.

### 2.2 Fail-closed default

Non-loopback writes remain rejected unless all applicable conditions are satisfied:

1. the candidate passed `/discovery/signature` validation;
2. protocol/schema are compatible;
3. the remote address is accepted by local-LAN policy;
4. the request presents valid SERVER-issued pairing authorization;
5. device-bound operations present valid device proof when the device is already known;
6. the route-specific authorization and payload validation pass;
7. no identity, lineage, membership, or trust ambiguity requires user action.

Network reachability, a caller-supplied device ID, Dev Mode, cached state, or a local admin toggle is never authorization.

### 2.3 Loopback compatibility

Existing loopback behavior may remain available for SERVER-local diagnostics. Supporting paired LAN requests must not weaken the loopback or local-access gate.

## 3. Common request rules

### 3.1 Required version fields

Every bootstrap write request contains:

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1
}
```

An incompatible version fails closed with a structured error. This contract remains additive under protocol/schema `1 / 1`; implementation review must stop for explicit approval if a version change is proven necessary.

### 3.2 Common headers

Paired LAN writes use:

```text
Content-Type: application/json
x-swrlz-pairing-token: <SERVER-issued pairing authorization>
x-swrlz-request-id: <unique request identifier>
```

Known-device operations additionally use:

```text
x-swrlz-device-id: <stable device ID>
x-swrlz-device-timestamp: <CLIENT timestamp>
x-swrlz-device-nonce: <unique nonce>
x-swrlz-device-proof: <device-bound proof>
```

Exact header casing is not significant.

### 3.3 Device-proof canonical form

For the current symmetric device-key lane, the proof is defined as HMAC-SHA256 over UTF-8 canonical text:

```text
<METHOD>\n
<PATH>\n
<PROTOCOL_VERSION>\n
<SCHEMA_VERSION>\n
<DEVICE_ID>\n
<TIMESTAMP>\n
<NONCE>\n
<REQUEST_ID>\n
<SHA256_HEX_OF_EXACT_REQUEST_BODY>
```

The HMAC key is the stable CLIENT device key already bound to the SERVER device record. The transmitted proof is lowercase hexadecimal.

SERVER must:

- compare proofs in constant-time where practical;
- reject stale timestamps outside a bounded clock-skew window;
- reject replayed `(device_id, nonce)` combinations inside a bounded replay window;
- reject a proof that does not match the stored device key;
- never interpret a self-signed proof for an unknown device as SERVER-recognized identity.

A future asymmetric identity lane requires a separately approved contract; it must not be silently substituted here.

### 3.4 Pairing and device proof are distinct

```text
pairing authorization
=
permission to attempt a local SERVER operation
```

```text
device proof
=
proof that a known CLIENT possesses the key bound to that device record
```

Both are required for known-device LAN writes. Pairing alone does not prove device ownership. Device proof alone does not establish pairing or route authority.

### 3.5 Unknown-device first binding

An unknown device cannot present proof verifiable against an existing SERVER record. Initial registration therefore requires valid pairing authorization and a complete stable identity payload.

On successful first registration, SERVER binds the stable device ID to the supplied device key under existing identity, lineage, and conflict rules. Repeating the same valid registration is idempotent. Reusing the same device ID with a different key fails closed into identity conflict or an explicitly approved lineage-repair flow.

### 3.6 Request size and parsing

- JSON body maximum remains 32 KiB unless separately revised.
- Unknown required fields, malformed JSON, duplicate security-critical keys, invalid encoding, or ambiguous identity fields fail closed.
- Secrets must not be written to ordinary logs.

### 3.7 Idempotency

`x-swrlz-request-id` is required for mutating bootstrap writes.

For a repeated request with the same request ID, authenticated actor, route, and logically identical body, SERVER returns the original logical result or an equivalent idempotent result. It must not create duplicate devices, groups, or memberships.

Reusing a request ID with a different route or body fails with `REQUEST_ID_CONFLICT`.

Exact retention mechanics are implementation-specific but must survive ordinary retry races for the bounded operation.

## 4. Common response envelope

Successful responses use:

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 0
}
```

Errors use:

```json
{
  "ok": false,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "error": {
    "code": "...",
    "message": "...",
    "actionRequired": false,
    "retryable": false
  }
}
```

HTTP status codes should distinguish malformed input, unauthenticated/unauthorized requests, not-found conditions, conflicts, rate limits, and SERVER failures. CLIENT behavior is governed by the structured error code and must not infer success from HTTP `200` alone.

## 5. Device resolution

### 5.1 Route

```text
POST /devices/resolve
```

### 5.2 Purpose

Resolve the connecting CLIENT's stable identity without creating or mutating a device record.

### 5.3 Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "identity": {
    "device_id": "stable-device-id",
    "device_key_fingerprint": "sha256-hex",
    "device_uuid": "optional",
    "device_anchor": "optional",
    "fingerprint_hash": "optional",
    "profile_merge_key": "optional",
    "device_label": "optional",
    "role": "optional",
    "client_version": "optional",
    "patch_label": "optional"
  }
}
```

Known devices include valid device-proof headers. An unknown device may still send a self-generated proof, but SERVER treats it only as unverified request material until a record is found and the proof validates.

### 5.4 Response states

`resolution` is exactly one of:

```text
KNOWN
UNKNOWN
RETIRED
REPLACED
BLOCKED
IDENTITY_CONFLICT
ACTION_REQUIRED
```

Example known response:

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 42,
  "resolution": "KNOWN",
  "device": {
    "device_id": "...",
    "device_label": "...",
    "role": "...",
    "status": "active"
  },
  "membershipCount": 1,
  "lineage": null
}
```

Example unknown response:

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 42,
  "resolution": "UNKNOWN",
  "device": null,
  "membershipCount": 0,
  "lineage": null
}
```

### 5.5 Resolution rules

- `KNOWN` requires matching stable identity and valid device proof.
- `UNKNOWN` means no matching active, retired, replaced, blocked, or conflicting record was found under the submitted identity evidence.
- A known device with invalid proof does not become `UNKNOWN`; it fails closed as `IDENTITY_CONFLICT` or `DEVICE_PROOF_INVALID`.
- `RETIRED` and `REPLACED` preserve Ghost/Legacy lineage and provide only the minimum lineage pointer needed for recovery.
- `BLOCKED` remains blocked.
- Resolution never creates a device, membership, session, group, or authority record.
- Responses are limited to the submitted identity and must not permit registry enumeration.

## 6. Idempotent registration

### 6.1 Route

```text
POST /devices/register
```

The existing route is retained and hardened for paired LAN use.

### 6.2 Preconditions

CLIENT calls registration only after `/devices/resolve` returns `UNKNOWN` and valid pairing authorization is present.

### 6.3 Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "identity": {
    "device_id": "...",
    "device_key": "...",
    "device_label": "...",
    "role": "...",
    "device_uuid": "optional",
    "device_anchor": "optional",
    "fingerprint_hash": "optional",
    "profile_merge_key": "optional",
    "client_version": "optional",
    "patch_label": "optional",
    "capabilities": []
  }
}
```

The device key is sensitive and must be redacted from logs. This local-LAN first-binding lane is not public/hosted-ready.

### 6.4 Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 43,
  "created": true,
  "device": {},
  "identityWarning": null
}
```

### 6.5 Registration rules

- Same device ID and same device key resolve idempotently to the same active record.
- Same device ID with a different key fails closed.
- Registration does not silently replace, delete, or reactivate Ghost/Legacy records.
- Registration does not grant group membership, administrator authority, mission authority, or trust beyond the registration record.
- A transient read or heartbeat failure must not cause repeat registration.

## 7. Authoritative membership restoration

### 7.1 Route

```text
POST /devices/membership/restore
```

### 7.2 Preconditions

- valid pairing authorization;
- device resolution is `KNOWN`;
- valid device proof;
- active, non-blocked device record.

### 7.3 Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "device_id": "..."
}
```

### 7.4 Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 44,
  "membershipState": "SINGLE",
  "memberships": [
    {
      "group_id": "...",
      "group_label": "...",
      "membership_status": "active",
      "joined_at": "..."
    }
  ],
  "selectedMembership": {
    "group_id": "...",
    "group_label": "...",
    "membership_status": "active",
    "joined_at": "..."
  },
  "selectionRequired": false
}
```

`membershipState` is exactly one of:

```text
NONE
SINGLE
MULTIPLE
CONFLICT
```

### 7.5 Deterministic zero/one/multiple behavior

#### `NONE`

- return an empty membership list;
- `selectedMembership = null`;
- `selectionRequired = false`;
- CLIENT may present Create Group and Join Group actions.

#### `SINGLE`

- return exactly one active membership;
- select it automatically;
- CLIENT may enter `ACTIVE` after successful heartbeat.

#### `MULTIPLE`

- return every active membership the device is permitted to know;
- do not select by lexical order, recency guess, nearby SERVER, CLIENT cache, or first database row;
- `selectedMembership = null` unless SERVER already possesses explicit authoritative current-membership metadata;
- otherwise `selectionRequired = true` and CLIENT enters `ACTION_REQUIRED` with a clear group-selection action;
- no destructive leave or automatic group switch occurs.

#### `CONFLICT`

- use when membership data violates registry invariants or cannot be resolved safely;
- fail closed into `ACTION_REQUIRED`;
- preserve existing records and lineage for repair.

### 7.6 Cache reconciliation

After a successful restore response, SERVER membership wins over CLIENT cache. CLIENT updates its last-known cache but must retain stale-state labeling until the SERVER response is accepted.

Temporary SERVER absence must not erase cached membership or create a replacement identity.

## 8. Atomic create-and-join

### 8.1 Route

```text
POST /groups/create-and-join
```

### 8.2 Purpose

Create a new group and add the authenticated creating device as an active member in one atomic SERVER transaction.

### 8.3 Preconditions

- valid pairing authorization;
- known active device;
- valid device proof;
- no unresolved identity or membership conflict.

### 8.4 Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "creator_device_id": "...",
  "group_name": "short user-visible name"
}
```

Normal CLIENT UI does not require the user to supply an internal group ID, registry key, or database identifier.

### 8.5 SERVER transaction

SERVER must atomically:

1. normalize and validate `group_name`;
2. reject invalid or conflicting names truthfully;
3. generate the internal group identity and required group credential material;
4. create the group;
5. add the creator device as an active member;
6. persist both records;
7. advance registry revision once for the logical transaction;
8. return the authoritative group and membership.

If any step fails, neither a group nor membership may remain committed.

### 8.6 Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 45,
  "created": true,
  "joined": true,
  "group": {
    "group_id": "...",
    "group_label": "..."
  },
  "membership": {
    "group_id": "...",
    "device_id": "...",
    "membership_status": "active",
    "joined_at": "..."
  },
  "groupCredential": {
    "group_key": "..."
  }
}
```

`groupCredential` is secret material. CLIENT stores it in secure storage and must not show it in normal UI or logs. A later contract may replace symmetric group credentials; that change must be explicit.

### 8.7 Idempotency

Repeating the same request ID returns the same group/membership result. It must not create a second group with a modified suffix or a duplicate membership.

A different request ID using an already-conflicting normalized name returns `GROUP_NAME_CONFLICT`.

## 9. Existing explicit join route

```text
POST /groups/join
```

The existing join route remains available under paired LAN authorization and device-proof rules. It is not redefined here beyond these requirements:

- joining an existing active membership is idempotent;
- the resulting authoritative membership is returned;
- CLIENT must not use join as a recurring reconnect operation;
- user-facing short-name resolution and admission policy require a separately accepted contract if the existing group-key model is changed.

## 10. Heartbeat/check-in

### 10.1 Route

```text
POST /devices/checkin
```

The existing route remains and is hardened for paired LAN device proof.

### 10.2 Preconditions

- known active device;
- valid pairing authorization and device proof;
- active restored membership;
- group/device relationship matches SERVER registry.

### 10.3 Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "group_id": "...",
  "device_id": "..."
}
```

Known-device LAN check-in uses proof headers; it should not require transmitting the raw device key in the JSON body. Existing loopback compatibility may remain separately supported.

### 10.4 Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "...",
  "serverTime": "...",
  "registryRevision": 46,
  "accepted": true,
  "device_id": "...",
  "group_id": "...",
  "last_seen_at": "...",
  "lease_seconds": 90,
  "heartbeat_recommended_seconds": 30,
  "lease_expires_at": "...",
  "online": true
}
```

SERVER-observed lease state remains authoritative. CLIENT must not claim global online state after a rejected or unconfirmed heartbeat.

## 11. Radar read scope after bootstrap

After successful membership restoration and heartbeat, a normal non-admin CLIENT may request only:

- current SERVER connectivity state;
- its selected/current group;
- itself;
- devices visible within that group;
- online/offline/last-seen state where authorized;
- compatibility or action-required state.

It must not receive unrelated groups, unrelated devices, global totals, raw registry records, pairing secrets, trust keys, admin sessions, or mission authority.

Device proof must bind any device-scoped LAN read. A caller-supplied identity hint alone is not authorization for hosted/public use.

## 12. Required structured error codes

Implementations must distinguish at least:

```text
PROTOCOL_INCOMPATIBLE
SCHEMA_INCOMPATIBLE
MALFORMED_REQUEST
REQUEST_TOO_LARGE
REQUEST_ID_REQUIRED
REQUEST_ID_CONFLICT
PAIRING_REQUIRED
PAIRING_INVALID
PAIRING_EXPIRED
LAN_WRITE_DISABLED
LOCAL_NETWORK_REQUIRED
DEVICE_ID_REQUIRED
DEVICE_UNKNOWN
DEVICE_PROOF_REQUIRED
DEVICE_PROOF_INVALID
DEVICE_PROOF_STALE
DEVICE_PROOF_REPLAYED
DEVICE_KEY_MISMATCH
DEVICE_BLOCKED
DEVICE_RETIRED
DEVICE_REPLACED
IDENTITY_CONFLICT
REGISTRATION_REJECTED
GROUP_NAME_INVALID
GROUP_NAME_CONFLICT
GROUP_NOT_FOUND
GROUP_CREDENTIAL_INVALID
MEMBERSHIP_NONE
MEMBERSHIP_MULTIPLE
MEMBERSHIP_CONFLICT
MEMBERSHIP_INACTIVE
CHECKIN_REJECTED
RATE_LIMITED
INTERNAL_ERROR
```

Errors must identify the failed operation accurately. A read failure must not be presented as registration failure, and a heartbeat failure must not trigger identity regeneration.

## 13. Acceptance scenarios

### 13.1 Known device with one membership

1. CLIENT verifies saved SERVER.
2. Paired LAN gate accepts request.
3. `/devices/resolve` returns `KNOWN` after device-proof validation.
4. `/devices/membership/restore` returns `SINGLE`.
5. `/devices/checkin` succeeds.
6. CLIENT enters `ACTIVE` and shows only permitted group Radar data.

### 13.2 Unknown paired device

1. Resolve returns `UNKNOWN`.
2. CLIENT calls registration once.
3. SERVER binds the stable identity.
4. Repeated request resolves idempotently to the same device.
5. Membership restore returns `NONE` until user creates or joins a group.

### 13.3 Identity conflict

1. Submitted device ID matches a SERVER record but key proof does not.
2. SERVER returns `DEVICE_PROOF_INVALID` or `IDENTITY_CONFLICT`.
3. SERVER does not return `UNKNOWN` and does not create a duplicate device.
4. CLIENT enters `ACTION_REQUIRED`.

### 13.4 Multiple memberships

1. SERVER returns all permitted active memberships.
2. Without explicit current-membership metadata, `selectionRequired = true`.
3. CLIENT does not choose silently or alter membership.
4. User receives one clear selection action.

### 13.5 Atomic group creation

1. CLIENT submits a valid short name.
2. SERVER creates group and creator membership atomically.
3. Retry with the same request ID returns the same logical result.
4. Any transaction failure leaves neither partial group nor partial membership.

### 13.6 SERVER temporarily offline

1. CLIENT preserves stable identity and last-known membership cache.
2. Cache is visibly stale.
3. No duplicate registration, group erasure, or identity rotation occurs.
4. Reconnect repeats resolve/restore and reconciles to SERVER truth.

### 13.7 Pairing or proof rejection

1. SERVER fails closed.
2. CLIENT reports the actual pairing/proof error.
3. Dev Mode does not bypass it.
4. No admin, mission, or trust authority is inferred.

## 14. Implementation ordering recommendation

A later source checkpoint should implement in this order:

1. common paired-LAN authorization and proof verification;
2. non-mutating `/devices/resolve`;
3. LAN-hardening of idempotent `/devices/register`;
4. `/devices/membership/restore`;
5. atomic `/groups/create-and-join`;
6. proof-hardened `/devices/checkin`;
7. group-scoped read enforcement and acceptance tests.

Each slice remains separately approval-gated. A source checkpoint must stop if implementation reveals a required protocol/schema change or storage migration not already authorized.

## 15. Non-goals

This contract does not authorize or define:

- CLIENT or SERVER source implementation;
- enabling LAN writes in an APK;
- a database or registry migration;
- a new protocol/schema version;
- hosted/public-internet transport;
- general remote administration;
- admin session, role, promotion, or revocation endpoints;
- mission execution authority;
- APK build, workflow execution, installation, release, deployment, merge, rebase, or `main` promotion;
- weakening identity, pairing, trust, Truth Firewall, lineage, privacy, or local-versus-remote distinctions.
