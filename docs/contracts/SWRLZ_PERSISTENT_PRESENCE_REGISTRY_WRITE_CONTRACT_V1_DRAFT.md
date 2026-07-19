# SWRLZ Persistent Presence Registry Write Contract v1 — DRAFT

- **Status:** Draft; not accepted, implemented, or verified
- **Checkpoint:** `INTEGRATION-FIX-011I`
- **Contract ID:** `SWRLZ-PRESENCE-WRITE-V1`
- **Protocol version:** `1`
- **Schema version:** `1`
- **Owner:** Shared CLIENT↔NODE_HOST contract
- **Implementation owner:** Android NODE_HOST owns authoritative persistence and server-side enforcement
- **CLIENT minimum version:** Not assigned until implementation acceptance
- **NODE_HOST minimum version:** Must be a successor to the installed read-only v1.0.3 compatibility build
- **Authority base:** repository `main` at `f35a9053cbd15c1d5f77b7dd6b5b07e0b778e181`

## 1. Purpose

Define the smallest offline-first write contract required for a SWRLZ CLIENT to:

1. create a presence group;
2. register a CLIENT device identity;
3. join a device to a group;
4. check in that device and maintain a bounded online lease;
5. read authoritative groups, devices, and online state from Android NODE_HOST.

This contract supplements the accepted read-only routes:

```http
GET /status
GET /presence/summary
GET /presence/groups
GET /presence/devices
```

It does not replace `GET /discovery/signature`, grant mission authority, create admin authority, or authorize remote/cloud execution.

## 2. Confirmed starting facts

### 2.1 Current CLIENT lineage

The current CLIENT registration flow already targets these legacy route names:

```http
POST /groups/create
POST /groups/join
POST /devices/checkin
```

The CLIENT stores local `group_id`, `group_key`, `device_id`, and `device_key` values and its auto-presence flow attempts group join followed by device check-in.

A historical, non-canonical CF10 report proposed `POST /devices/register` and richer identity metadata. That report is design evidence, not proof that the currently promoted CLIENT contains those additions.

### 2.2 Current Android NODE_HOST

The installed NODE_HOST v1.0.3 compatibility surface is intentionally read-only. It reports authoritative empty presence and has no persistent group, device, membership, registration, or check-in handler.

### 2.3 Root integration mismatch

The CLIENT exposes and invokes presence-write actions while NODE_HOST advertises only discovery/read behavior. Successful read health therefore coexists with failed registration and zero authoritative records.

## 3. Core laws

1. **NODE_HOST owns presence truth.** CLIENT local settings are proposals and credentials, not authoritative records.
2. **No fabricated rows.** Missing or failed writes remain empty or error state.
3. **Registration is not trust.** Creating a device or group does not grant mission execution authority.
4. **Group knowledge is not admin authority.** A `group_key` cannot authorize `/admin/*` routes.
5. **Device authentication is scoped.** A `device_key` authenticates one device record and its membership/check-in operations only.
6. **Pairing remains separate.** A valid pairing token is required before presence writes but does not by itself join a group or trust a mission source.
7. **Online is derived.** Online status comes from a server-clock lease, not a CLIENT boolean.
8. **Ghost Device history is preserved.** Superseded identities are retired with lineage rather than silently deleted.
9. **Offline-first remains first-class.** No paid service, cloud callback, or per-call billing dependency is permitted.
10. **Capabilities must be negotiated.** UI visibility alone is not authority and unsupported write controls must not execute calls.

## 4. Transport and exposure

- **Transport:** HTTP JSON over the NODE_HOST local-link listener, TCP `8787`.
- **Default bind:** loopback-only write availability.
- **LAN writes:** disabled by default; may be enabled only by explicit NODE_HOST configuration and still require a valid pairing token.
- **Public internet:** prohibited by this contract.
- **Content type:** `application/json; charset=utf-8`.
- **Cache policy:** all responses use `Cache-Control: no-store`.
- **Secret logging:** pairing tokens, group keys, device keys, and authorization headers must be redacted before request logging.

This protocol does not claim transport confidentiality. A future secure-transport contract may supersede LAN exposure rules without changing local registry semantics.

## 5. Capability advertisement

`GET /discovery/signature` must advertise exact implemented capabilities. The CLIENT must not infer write support from route names, app version, or successful read responses.

Canonical capability identifiers:

```text
discovery.v1
presence.read.v1
presence.group.create.v1
presence.group.join.v1
presence.device.register.v1
presence.device.checkin.v1
```

Optional aggregate identifier:

```text
presence.write.v1
```

`presence.write.v1` may be advertised only when all four write capabilities are enabled. CLIENT decisions must use the granular identifiers.

Example additive signature fields:

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "capabilities": [
    "discovery.v1",
    "presence.read.v1",
    "presence.group.create.v1",
    "presence.group.join.v1",
    "presence.device.register.v1",
    "presence.device.checkin.v1"
  ],
  "presenceContractVersion": 1,
  "presenceWriteTransport": "local-link-paired"
}
```

The installed NODE_HOST response containing only `"discovery"` must be treated as **write unsupported**. CLIENT must disable or mark unsupported the corresponding actions and must not send speculative POST requests.

## 6. Common request requirements

All write requests require:

| Requirement | Rule |
|---|---|
| Method | Exact POST route defined below |
| Accept | Must permit JSON |
| Content-Type | Must be JSON |
| Pairing token | `X-Swurlz-Pairing-Token`; required and validated |
| Protocol | Request body `protocolVersion: 1` or omitted by legacy adapter and normalized to `1` |
| Schema | Request body `schemaVersion: 1` or omitted by legacy adapter and normalized to `1` |
| Request correlation | Optional `X-SWRLZ-Request-Id`; server generates one when absent |
| CLIENT identity hint | `X-SWRLZ-Device-Node-Id` when available |
| Body size | Maximum 32 KiB |
| Unknown fields | Ignored for forward-compatible additive changes; preserved nowhere unless accepted by schema |

A pairing token is an access gate only. The route must still validate group/device credentials and policy.

## 7. Canonical data rules

### 7.1 Identifiers

- `group_id`: 1–64 characters, lowercase ASCII slug matching `[a-z0-9][a-z0-9._-]{0,63}`.
- `device_id`: 1–128 visible ASCII characters; stable within one CLIENT identity lineage.
- IDs are compared byte-for-byte after trim; NODE_HOST does not silently case-fold `device_id`.
- `requestId`, audit IDs, and internal record IDs use UUID strings.

### 7.2 Secrets

- `group_key`: 8–128 UTF-8 characters.
- `device_key`: 16–256 UTF-8 characters.
- NODE_HOST stores only a modern salted password/key hash or keyed verifier, never plaintext.
- Read routes and ordinary success responses never return either secret.
- A supplied key is accepted, rejected, or rotated through a separately accepted rotation contract; it is not echoed.

### 7.3 Timestamps

- UTC RFC 3339 with `Z` suffix.
- NODE_HOST clock is authoritative for `created_at`, `joined_at`, `last_seen_at`, and `lease_expires_at`.

### 7.4 Roles and capabilities

- `role` is a descriptive CLIENT role, not authorization.
- CLIENT-advertised capabilities are informational until NODE_HOST policy accepts them.
- Unknown roles are stored as descriptive strings only if length and character validation passes.

## 8. Persistent registry model

The storage engine is an implementation choice, but restart-persistent semantics are mandatory.

### 8.1 Group record

| Field | Type | Required | Notes |
|---|---|---:|---|
| `group_id` | string | yes | Natural key |
| `label` | string | yes | 1–96 display characters |
| `group_key_hash` | secret verifier | yes | Never serialized to CLIENT |
| `creator_device_id` | string | yes | Ownership lineage; not admin authority |
| `state` | enum | yes | `active`, `retired`, `blocked` |
| `created_at` | timestamp | yes | Server clock |
| `updated_at` | timestamp | yes | Server clock |

### 8.2 Device record

| Field | Type | Required | Notes |
|---|---|---:|---|
| `device_id` | string | yes | Natural key |
| `device_key_hash` | secret verifier | yes | Never serialized |
| `device_label` | string | yes | 1–96 characters |
| `role` | string | yes | Descriptive only |
| `lifecycle_state` | enum | yes | `active`, `retired`, `blocked` |
| `trust_state` | enum | yes | Separate from lifecycle: `untrusted`, `paired`, `trusted`, `blocked` |
| `identity_schema` | integer | no | Optional CLIENT identity schema |
| `device_uuid` | string | no | Optional declared identity |
| `device_anchor` | string | no | Optional redacted stable anchor |
| `fingerprint_hash` | string | no | Optional redacted hash |
| `profile_merge_key` | string | no | Optional redacted lineage hint |
| `client_version` | string | no | Last observed CLIENT version |
| `patch_label` | string | no | Last observed patch label |
| `capabilities` | string array | no | Last accepted CLIENT claims |
| `successor_device_id` | string | no | Required when retired into known successor lineage |
| `created_at` | timestamp | yes | Server clock |
| `updated_at` | timestamp | yes | Server clock |
| `last_seen_at` | timestamp | no | Last accepted check-in |

### 8.3 Membership record

Natural key: `(group_id, device_id)`.

| Field | Type | Required | Notes |
|---|---|---:|---|
| `group_id` | string | yes | Existing active group |
| `device_id` | string | yes | Existing active device |
| `membership_state` | enum | yes | `active`, `revoked`, `pending` |
| `joined_at` | timestamp | yes | Server clock |
| `updated_at` | timestamp | yes | Server clock |
| `last_seen_at` | timestamp | no | Updated by accepted check-in |
| `lease_expires_at` | timestamp | no | Derived from accepted check-in |

### 8.4 Audit record

Write decisions must append a bounded audit event containing request ID, event type, actor device, target IDs, timestamp, result, and reason code. Secret values and user content are prohibited.

## 9. Route: create group

```http
POST /groups/create
```

Required capability: `presence.group.create.v1`.

### Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "group_id": "kami-lab",
  "group_key": "<secret>",
  "label": "Kami Lab"
}
```

The creator is resolved from `X-SWRLZ-Device-Node-Id` or an additive `creator_device_id` field. One must be present.

### Behavior

- Validate pairing token, identifiers, label, and group secret.
- Store only the group-key verifier.
- Create one active group with creator lineage.
- Same `group_id`, same verified key, same creator: return idempotent success with `created: false`.
- Same `group_id` with a different key or creator: return conflict without revealing which secret differed.

### Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "created": true,
  "group": {
    "group_id": "kami-lab",
    "label": "Kami Lab",
    "creator_device_id": "android-a36adb3806cd",
    "state": "active",
    "created_at": "2026-07-19T00:00:00Z"
  }
}
```

## 10. Route: register device

```http
POST /devices/register
```

Required capability: `presence.device.register.v1`.

### Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "device_id": "android-a36adb3806cd",
  "device_key": "<secret>",
  "device_label": "Action Phone",
  "role": "Action Phone",
  "client_version": "0.2.7.6-cf8-admin-fallback",
  "patch_label": "2.7.6-CF8-ADMIN-FALLBACK",
  "capabilities": ["client.radar.v1"],
  "identity": {
    "schemaVersion": 1,
    "device_uuid": null,
    "device_anchor": null,
    "fingerprint_hash": null,
    "profile_merge_key": null
  }
}
```

The `identity` object and build metadata are optional in schema v1. Raw Android identifiers or hardware fingerprints are prohibited.

### Behavior

- Pairing token is required.
- A new device is stored with `lifecycle_state: active` and `trust_state: paired` or `untrusted` according to the pairing policy; never automatically `trusted`.
- Same device ID and matching key: update allowed descriptive metadata and return idempotent success.
- Same device ID and different key: conflict; do not overwrite or rotate silently.
- Similar anchor/fingerprint evidence may create a duplicate warning but must not auto-merge identities.

### Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "created": true,
  "device": {
    "device_id": "android-a36adb3806cd",
    "device_label": "Action Phone",
    "role": "Action Phone",
    "lifecycle_state": "active",
    "trust_state": "paired",
    "created_at": "2026-07-19T00:00:00Z"
  },
  "identityWarning": null
}
```

The response never returns `device_key`.

## 11. Route: join group

```http
POST /groups/join
```

Required capability: `presence.group.join.v1`.

### Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "group_id": "kami-lab",
  "group_key": "<secret>",
  "device_id": "android-a36adb3806cd",
  "device_key": "<secret>",
  "device_label": "Action Phone",
  "role": "Action Phone"
}
```

### Behavior

- Validate pairing token and group-key verifier.
- Validate the device key when the device exists.
- Canonical path: device already exists from `/devices/register`.
- Legacy compatibility path: when the device does not exist and `device_key`, label, and role are valid, NODE_HOST may atomically create the device and membership. The response must state `registrationMode: "legacy_atomic_join"`.
- Repeating the same valid membership returns success with `joined: false`.
- Joining does not change `trust_state` to `trusted`.

### Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "joined": true,
  "registrationMode": "existing_device",
  "membership": {
    "group_id": "kami-lab",
    "device_id": "android-a36adb3806cd",
    "membership_state": "active",
    "joined_at": "2026-07-19T00:00:00Z"
  },
  "device": {
    "device_id": "android-a36adb3806cd",
    "device_label": "Action Phone",
    "role": "Action Phone",
    "lifecycle_state": "active",
    "trust_state": "paired"
  }
}
```

## 12. Route: device check-in

```http
POST /devices/checkin
```

Required capability: `presence.device.checkin.v1`.

### Request

```json
{
  "protocolVersion": 1,
  "schemaVersion": 1,
  "group_id": "kami-lab",
  "device_id": "android-a36adb3806cd",
  "device_key": "<secret>"
}
```

### Behavior

- Validate pairing token, active device, active membership, and device-key verifier.
- Do not accept a check-in as implicit group creation.
- Update `last_seen_at` and the membership lease atomically.
- Default lease: 90 seconds.
- Recommended heartbeat interval: 30 seconds with bounded jitter of up to 5 seconds.
- Check-ins are ephemeral and must not be queued as missions while offline.
- After NODE_HOST restart, a device is offline until an accepted check-in establishes a fresh lease.

### Response

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "accepted": true,
  "device_id": "android-a36adb3806cd",
  "group_id": "kami-lab",
  "last_seen_at": "2026-07-19T00:00:00Z",
  "lease_seconds": 90,
  "lease_expires_at": "2026-07-19T00:01:30Z",
  "online": true
}
```

## 13. Read-route behavior after registry implementation

### `/presence/summary`

Returns counts and collections derived from persistent active records and current leases. `state` is `empty` only when no visible records exist; otherwise `state` is `active`.

### `/presence/groups`

Ordinary paired devices see groups they are authorized to view. It must not become an unauthenticated global group directory.

### `/presence/devices`

Ordinary paired devices see devices within their authorized group scope. Secrets, raw identity material, retired devices, and blocked devices are omitted from ordinary views.

### Online status

```text
online = active device
AND active membership
AND server_now < lease_expires_at
```

`online` is never accepted from a CLIENT request body.

## 14. Legacy CLIENT read adapter

The current CLIENT lineage may send `group_id` and `group_key` as query parameters to presence reads.

A temporary compatibility adapter may accept this form only when all conditions hold:

1. a valid pairing token is also present;
2. the request is on an approved local-link listener;
3. query strings are redacted from logs;
4. the group key is validated but never persisted from the read request;
5. the response includes `legacyAuth: true` and a deprecation warning.

New CLIENT implementations must move group authorization out of URLs. This adapter is compatibility behavior, not the long-term canonical authorization model.

## 15. CLIENT auto-presence state machine

The CLIENT must treat auto-presence as a multi-step result, not a generic success wrapper.

```text
CAPABILITY CHECK
  ├─ unsupported → stop; show unsupported reason; no POST
  └─ supported
       REGISTER DEVICE (when capability present)
          ├─ failure → preserve exact route/error; stop
          └─ success/idempotent
               JOIN GROUP
                  ├─ failure → preserve exact route/error; stop
                  └─ success/idempotent
                       CHECK IN
                          ├─ failure → registration exists but device offline
                          └─ success → refresh authoritative reads
```

Read health and write health remain separate. A successful Radar refresh must not overwrite the last registration failure.

## 16. Lifecycle and Ghost Device lineage

- Duplicate hints never trigger automatic merge.
- A superseded record becomes `retired`, is removed from ordinary active controls, and retains audit/history.
- When known, `successor_device_id` links the retired profile to the active profile.
- Hard deletion is outside this contract and requires a separate strongly confirmed administrative contract.
- Key rotation must not create a new visible device unless identity genuinely changes.

## 17. Trust and Truth Firewall boundaries

Presence writes may establish registry state only. They must not:

- set mission authorization to unrestricted;
- bypass `trusted_only` mission policy;
- create an admin session;
- infer trust from group membership;
- infer trust from online state;
- route to a paid or remote service when local writes fail;
- suppress a Truth Firewall objection, pause, refusal, or safer alternative;
- merge CLIENT identity with NODE_HOST installation identity.

## 18. Idempotency and retry

| Route | Idempotency key | Repeat behavior |
|---|---|---|
| `/groups/create` | `group_id` + verified key + creator | Same request returns `created: false` |
| `/devices/register` | `device_id` + verified key | Same request updates descriptive metadata only |
| `/groups/join` | `(group_id, device_id)` | Same valid membership returns `joined: false` |
| `/devices/checkin` | `(group_id, device_id)` | Refreshes lease; no duplicate record |

CLIENT retry rules:

- network timeout or `503`: bounded exponential backoff;
- `429`: respect `Retry-After`;
- `400`, `401`, `403`, `404`, `409`, `422`: no automatic rapid retry;
- no silent remote fallback;
- no retry loop faster than 10 seconds outside an explicit user action.

## 19. Error envelope

```json
{
  "ok": false,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "error": {
    "code": "GROUP_KEY_INVALID",
    "message": "Group credentials were rejected",
    "retryable": false
  }
}
```

| HTTP | Code | Meaning | CLIENT behavior |
|---:|---|---|---|
| 400 | `INVALID_JSON` | Malformed JSON | Show exact route; no retry |
| 400 | `INVALID_FIELD` | Field validation failed | Identify safe field name; no secret echo |
| 401 | `PAIRING_REQUIRED` | Pairing token absent | Prompt pairing/setup |
| 401 | `PAIRING_TOKEN_INVALID` | Pairing token rejected | Stop and re-pair |
| 401 | `DEVICE_KEY_INVALID` | Device credential rejected | Stop; offer identity repair, not auto-rotate |
| 403 | `GROUP_KEY_INVALID` | Group credential rejected | Stop; preserve group ID, clear no secrets automatically |
| 403 | `LAN_WRITE_DISABLED` | Write attempted on disabled LAN listener | Explain local-only policy |
| 403 | `DEVICE_BLOCKED` | Device policy block | Stop; no retry |
| 403 | `POLICY_BLOCKED` | Trust/policy rejected operation | Preserve reason |
| 404 | `GROUP_NOT_FOUND` | Group absent | Offer create/join correction |
| 404 | `DEVICE_NOT_FOUND` | Device absent | Register when capability exists |
| 409 | `GROUP_CONFLICT` | Existing group conflicts with request | Require owner/admin resolution |
| 409 | `DEVICE_KEY_CONFLICT` | Device ID exists under another key | Identity repair/rotation checkpoint |
| 409 | `IDENTITY_CONFLICT` | Duplicate/lineage evidence needs decision | Do not auto-merge |
| 422 | `PROTOCOL_VERSION_UNSUPPORTED` | Unsupported protocol | Stop; show compatibility state |
| 422 | `SCHEMA_VERSION_UNSUPPORTED` | Unsupported schema | Stop; show compatibility state |
| 429 | `RATE_LIMITED` | Write throttled | Bounded retry after server delay |
| 503 | `REGISTRY_UNAVAILABLE` | Persistent registry unavailable | Preserve read health; retry later |
| 503 | `PERSISTENCE_UNAVAILABLE` | Durable commit failed | Never report success |

Unknown errors remain visible as unknown; CLIENT must not relabel them as `/status` failures.

## 20. Transaction and durability requirements

- A success response is emitted only after durable commit.
- Group/device/membership changes that form one logical operation are atomic.
- Process death or restart must not produce a success response for an uncommitted write.
- A failed audit append must fail the write unless an accepted degraded-audit policy exists.
- Existing read-only responses remain available when the registry is empty.

## 21. Verification vectors

| Vector | Input | Expected result |
|---|---|---|
| V1 | Installed read-only NODE_HOST advertises no write capability | CLIENT marks controls unsupported and sends no POST |
| V2 | Valid paired group creation | Durable group; no secret in response/log |
| V3 | Repeat identical group creation | `200`, `created: false` |
| V4 | Same group ID with conflicting key | `409 GROUP_CONFLICT` |
| V5 | Valid device registration | Durable active device, not mission-trusted |
| V6 | Repeat register with matching key | Idempotent metadata update |
| V7 | Register with conflicting key | `409 DEVICE_KEY_CONFLICT` |
| V8 | Valid group join | Durable active membership |
| V9 | Legacy join for absent device | Atomic device+membership; explicit `legacy_atomic_join` |
| V10 | Valid check-in | Online lease set from server time |
| V11 | No check-in for 90 seconds | Device becomes offline without deleting record |
| V12 | NODE_HOST restart | Records persist; device offline until fresh check-in |
| V13 | Blocked device check-in | `403 DEVICE_BLOCKED`; no lease update |
| V14 | Duplicate anchor hint | Conflict/warning; no automatic merge |
| V15 | Retire old profile | Hidden from ordinary roster; lineage preserved |
| V16 | Successful reads after failed write | Read health remains green; write failure remains visible |
| V17 | Secret inspection | No plaintext secret in registry export, logs, reads, or audit |
| V18 | LAN writes disabled | `403 LAN_WRITE_DISABLED` |
| V19 | Unsupported protocol/schema | `422` explicit compatibility error |
| V20 | Offline failure | No cloud fallback, paid call, or mission queue substitution |

## 22. Compatibility class

- **Change class:** additive, capability-negotiated protocol-v1 extension.
- Existing read-only NODE_HOST remains valid and must advertise no write capabilities.
- Existing CLIENT may continue to read; its write controls must be capability-gated in the implementation checkpoint.
- Legacy route names are preserved to minimize CLIENT endpoint churn.
- Legacy query-secret reads are transitional and explicitly deprecated.

## 23. Out of scope

This draft does not define or authorize:

- source implementation;
- APK builds or workflows;
- promotion to `main`;
- releases, deployment, or installation;
- public-internet exposure;
- TLS or certificate provisioning;
- admin group rename/delete;
- device-key rotation;
- hard deletion;
- mission submission or execution;
- queue semantics;
- remote hosted-node synchronization;
- billing, entitlement enforcement, or paid AI.

## 24. Acceptance decisions required before implementation

An acceptance checkpoint must explicitly confirm:

1. the four legacy-compatible write route names;
2. granular capability identifiers;
3. loopback-default and explicit LAN-write policy;
4. 90-second online lease and 30-second heartbeat recommendation;
5. legacy atomic registration during group join;
6. transitional query-secret read adapter;
7. persistent storage and audit ownership in NODE_HOST;
8. no automatic identity merge;
9. separation of registration, pairing, trust, admin, and mission authority.

Until that acceptance occurs, this document is design evidence only.