# INTEGRATION-FIX-011R — SERVER Route and Payload Surface Lock Report

- **Checkpoint:** `INTEGRATION-FIX-011R`
- **Status:** Review complete; implementation separately approval-gated
- **Branch reviewed:** `checkpoint/server-presence-registry-011k`
- **SERVER candidate:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **SERVER candidate SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- **Protocol / schema:** `1 / 1`
- **Scope:** CLIENT automatic bootstrap, device resolution, registration, membership restoration, heartbeat, Radar reads, and administrative prerequisites

## 1. Evidence boundary

The reviewed SERVER candidate is retained on the temporary checkpoint branch and is derived from canonical base `SERVER_CFv1.0.3_SWRLZ.zip` with SHA-256 `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`.

The candidate contains 69 entries and changes exactly 11 internal source paths. Source-contract validation reports:

- four pairing-gated write routes;
- three registry-backed presence read routes;
- 90-second SERVER-derived online lease;
- 30-second heartbeat recommendation;
- Android Keystore-backed local pairing authority;
- LAN writes fail closed;
- protocol/schema remain `1 / 1`.

The exact candidate completed Android `clean assembleDebug` validation. The validation APK was a debug validation artifact, not a stable-signed release.

This review did not modify CLIENT or SERVER source and did not build, run a workflow, migrate storage, install, release, deploy, merge, rebase, or promote anything to `main`.

## 2. Locked existing route inventory

### 2.1 Discovery and status

| Route | Result | Bootstrap suitability |
|---|---|---|
| `GET /discovery/signature` | Present | Usable for discovery, SERVER identity, and protocol compatibility |
| `GET /status` | Present | Usable for basic health/status |

`GET /discovery/signature` establishes that a candidate is a compatible SWRLZ SERVER. It does not establish pairing, device ownership, registration, membership, administrator authority, mission authority, or trust beyond the declared compatibility surface.

### 2.2 Registry-backed reads

| Route | Result | Bootstrap suitability |
|---|---|---|
| `GET /presence/summary` | Present | Partial; does not restore authoritative membership |
| `GET /presence/groups` | Present | Partial; scoped group display |
| `GET /presence/devices` | Present | Partial; scoped device display |

The existing reads expose registry-backed group/device and online-state information after pairing and scope selection. They do not expose a normative device-resolution result or a complete authoritative membership record for the connecting device.

A device identity supplied only as a scope-selection hint is not sufficient proof of device ownership for public or hosted transport. Local pairing remains a separate gate.

### 2.3 Existing write routes

| Route | Result | Limitation |
|---|---|---|
| `POST /devices/register` | Present and idempotent for the same valid identity | Loopback-only; no preceding normative device-resolution route |
| `POST /groups/create` | Present | Loopback-only; does not atomically join creator |
| `POST /groups/join` | Present and idempotent for existing membership | Loopback-only; requires group identity/key already known |
| `POST /devices/checkin` | Present | Loopback-only; requires existing registration and membership |

All four writes remain fail-closed for non-loopback callers in the reviewed candidate.

## 3. Route-level findings

### 3.1 Device registration

The registration mutation accepts stable CLIENT identity material and preserves idempotent identity behavior. It can truthfully return whether the device record was created and the resulting SERVER-side device record.

The missing prerequisite is a normative lookup/resolve operation that answers whether the stable connecting identity is:

- known and active;
- unknown;
- retired or replaced with lineage;
- blocked;
- in identity conflict;
- known but not currently a member of any group.

The CLIENT must not infer `unknown` from an empty or scope-limited presence response because that can produce repeated or misleading registration attempts.

### 3.2 Group creation

The existing route creates a group but does not atomically add the creator as a member. This does not satisfy the accepted CLIENT contract, which requires create-and-join as one SERVER transaction with one authoritative result.

A two-call CLIENT sequence would create an externally visible partial state if creation succeeds and joining fails. The correction belongs on SERVER, not in UI retry choreography.

### 3.3 Group joining

The existing join route is suitable for an explicit user action once paired LAN writes are safely authorized. It is idempotent when membership already exists and can return the resulting membership.

It is not a reconnect-time restoration operation because the CLIENT must already know the target group and its required authorization material.

### 3.4 Membership restoration

No existing route returns the complete SERVER-authoritative membership state for the connecting registered device.

The external surface lacks a normative result equivalent to either:

```text
memberships: [...]
```

or:

```text
current_group_id
```

The registry may retain more than one active membership. The contract must therefore define deterministic behavior for zero, one, and multiple active memberships instead of allowing CLIENT cache to choose silently.

### 3.5 Heartbeat/check-in

The existing check-in route has sound lease semantics after registration and membership are known. It returns a SERVER-observed last-seen time, 90-second lease, 30-second recommendation, lease expiry, and online state.

It cannot be the first bootstrap write because it requires a valid device, group, membership, and device key.

### 3.6 Paired LAN writes

The reviewed candidate correctly fails closed for LAN writes. Automatic CLIENT bootstrap on another device is therefore blocked until the SERVER can accept a paired, device-authenticated LAN request without treating network reachability or a caller-supplied device ID as authority.

Enabling writes must remain conditional on:

- successful SWRLZ discovery-signature verification;
- valid SERVER pairing authorization;
- request freshness/replay control where applicable;
- device proof for device-bound operations;
- route-specific authorization;
- fail-closed behavior on ambiguity.

### 3.7 Administrator prerequisites

No reviewed SERVER route implements SERVER-authoritative admin session login, session verification, device promotion/demotion, role retrieval, or session revocation.

The approved `011O-A` contract remains a separate later implementation lane. Bootstrap route catch-up must not silently grant administrative or mission authority.

## 4. Locked capability matrix

| Capability | Locked result |
|---|---|
| Discovery signature | Present and usable |
| Saved SERVER verification | Present and usable |
| Basic SERVER status | Present and usable |
| Device identity resolution | Absent |
| Unknown-device determination | Absent |
| Idempotent registration mutation | Present, loopback-only |
| Group creation | Present, loopback-only, not atomic create+join |
| Group joining | Present, loopback-only, idempotent |
| Authoritative membership restoration | Absent / insufficient |
| Heartbeat/check-in | Present, loopback-only, dependent on known membership |
| Group-scoped Radar reads | Partially usable |
| Device-authenticated hosted/public scoping | Insufficient |
| Admin session/role verification | Absent |
| SERVER-side admin promotion/revocation | Absent |

## 5. Safe CLIENT implementation boundary

The current SERVER surface safely supports only the early CLIENT states:

```text
LOCAL_BOOT
→ TRY_SAVED_SERVER
→ DISCOVERING
→ SERVER_VERIFIED
```

The CLIENT must not represent the following as complete against the current route surface:

```text
IDENTIFYING_DEVICE
AUTO_REGISTERING
RESTORING_MEMBERSHIP
ACTIVE
```

Progress beyond `SERVER_VERIFIED` requires the additive contract in `docs/contracts/SERVER_PAIRED_LAN_BOOTSTRAP_ROUTE_SURFACE_V1.md` and a separately approved SERVER implementation checkpoint.

## 6. Required additive SERVER contract

The next SERVER slice must define and later implement:

1. paired, fail-closed LAN write authorization;
2. device-authenticated stable identity resolution;
3. idempotent registration linked to resolution and Ghost/Legacy lineage;
4. authoritative membership restoration for zero, one, or multiple memberships;
5. atomic create-and-join;
6. heartbeat using the restored membership result;
7. clear errors that distinguish pairing, identity, registration, membership, authorization, and protocol failures;
8. no implicit administrator, mission, trust, or Truth Firewall elevation.

## 7. Non-goals

This report does not authorize:

- CLIENT or SERVER source changes;
- endpoint creation;
- enabling LAN writes;
- database or schema migration;
- protocol-version change;
- APK build or workflow execution;
- installation, release, deployment, merge, rebase, or `main` promotion;
- admin or mission authorization;
- weakening identity, pairing, lineage, trust, or Truth Firewall controls.
