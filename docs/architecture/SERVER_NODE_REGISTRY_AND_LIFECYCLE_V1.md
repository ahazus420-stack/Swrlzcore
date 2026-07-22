# SERVER Node Registry and Lifecycle Architecture v1

**Status:** Accepted architecture record; runtime acceptance pending build and device evidence.  
**Related checkpoints:** `INT-PRES-015A`, `INT-LIFE-016A`

## 1. Purpose

This document defines the authoritative relationship between CLIENT registration, SERVER persistence, live node presence, node-list presentation, and SERVER lifecycle control.

The design separates durable facts from temporary runtime observations:

```text
Durable registration record
+
Live presence observation
+
Trust and authorization state
=
Truthful node projection
```

Discovery reachability alone is never a registered, trusted, or connected node.

## 2. Authoritative components

```text
CLIENT Identity / Installation Store
        |
        v
Registration and Heartbeat Transport
        |
        v
SERVER Registration Verifier
        |
        v
Persistent Node Registry
        |
        +--> Presence State Reducer
        +--> Trust / Proof Projection
        +--> Mission Occupancy Projection
        |
        v
Shared Node Repository / Event Flow
        |
        +--> SERVER User Mode
        +--> SERVER Developer Mode
        +--> CLIENT Nodes Projection
```

No UI layer owns or mutates authoritative node truth directly.

## 3. Registration model

### 3.1 Automatic initiation

CLIENT initiates registration after it has discovered a compatible SERVER and confirmed a usable transport/status response.

### 3.2 Verification

SERVER verifies the registration request against:

- node and installation identity consistency;
- protocol compatibility;
- device metadata validity;
- previously persisted lineage;
- proof material where required or available;
- revocation/retirement state;
- lifecycle admission policy.

### 3.3 Persistence

On successful registration, SERVER creates or updates a durable node record. Existing registration timestamps and lineage are preserved. Re-registration must not silently create duplicate identities.

### 3.4 Security separation

```text
Registration = inventory membership
Presence = current reachability/activity
Proof = cryptographic evidence state
Trust = policy decision
Authorization = permitted action scope
```

These states are related but not interchangeable.

## 4. Persistent node registry

The registry is SERVER-local and authoritative. Registered nodes remain visible through restart and while offline.

Recommended entity domains:

### Identity

- node ID
- device ID
- installation ID
- display name
- device class/type
- application version
- protocol version

### Lineage

- first registered timestamp
- most recent registration timestamp
- predecessor/successor identity references where applicable
- retired/archived state

### Verification

- registration state
- identity state
- proof state
- trust state
- revocation state

### Presence

- last seen timestamp
- current connection state
- current route classification
- local versus remote distinction
- active mission ID when busy

### Capability projection

- declared capabilities
- accepted capabilities
- compatibility notes

## 5. Presence state machine

Primary path:

```text
DISCOVERED
-> CONNECTING
-> VERIFYING
-> REGISTERING
-> CONNECTED
-> BUSY
-> CONNECTED
-> OFFLINE
```

Representative exceptional states:

```text
REGISTRATION_FAILED
IDENTITY_FAILED
PROOF_FAILED
TRUST_PENDING
PROTOCOL_INCOMPATIBLE
REVOKED
RETIRED
```

A heartbeat timeout transitions an active node to `OFFLINE`; it does not delete its registration record.

## 6. Counts and list projections

### 6.1 Inventory counts

- Registered
- Retired/archived where exposed

### 6.2 Live counts

- Online
- Offline
- Busy
- Connecting
- Verifying
- Registering

`ONLINE` should be defined consistently, normally including connected and busy nodes while excluding transitional or stale nodes unless the UI explicitly groups them otherwise.

### 6.3 List priority

Recommended ordering:

1. connecting;
2. verifying;
3. registering;
4. connected;
5. busy;
6. recently offline;
7. older offline;
8. retired/archived in a separate projection.

## 7. State-driven visuals

The animated node blob is a projection of authoritative state.

Suggested semantics:

```text
CONNECTING   pulsing acquisition motion
VERIFYING    scanning transition
REGISTERING  assembly/binding transition
CONNECTED    stable active state
BUSY         mission-activity pulse
OFFLINE      dim static state
FAILED       restrained fault boundary
```

Visual treatment must never imply accepted proof, trust, or authorization unless those states are independently confirmed.

## 8. SERVER lifecycle model

Canonical lifecycle states:

```text
STOPPED
STARTING
RUNNING
DEGRADED
MAINTENANCE
RELOADING_CONFIGURATION
RESTARTING_SERVICES
SHUTTING_DOWN
FAILED
```

### 8.1 Runtime-active states

At minimum, these are runtime-active:

```text
STARTING
RUNNING
DEGRADED
MAINTENANCE
RELOADING_CONFIGURATION
RESTARTING_SERVICES
SHUTTING_DOWN
```

Therefore a degraded SERVER offers shutdown, not initialization.

### 8.2 Operator actions

- Initialize Server
- Enter Maintenance Mode
- Reload Configuration
- Restart Services
- Begin Shutdown Sequence

Each action is gated by lifecycle validity and must report progress or rejection truthfully.

## 9. Graceful shutdown

The shutdown coordinator performs:

1. close admission for new connections;
2. notify connected CLIENTS;
3. drain or safely cancel active work;
4. flush writes and queues;
5. persist runtime/session state;
6. finalize and rotate logs;
7. close encrypted stores and release resources;
8. stop workers/services;
9. close networking;
10. publish `STOPPED` only after completion.

The current stage must be observable by both User Mode and Developer Mode. Failure must retain the failed stage and reason rather than falsely claiming a clean stop.

## 10. Maintenance mode

Maintenance mode preserves the SERVER process and administrative observability while preventing normal admission.

Expected policy:

- reject new missions;
- reject or defer new enrollment;
- preserve registered nodes;
- retain explicitly permitted diagnostics/admin access;
- drain or deliberately cancel ongoing work;
- notify connected CLIENTS of reduced availability.

## 11. Reload and restart semantics

### Reload Configuration

Reloads validated configuration without discarding registry truth or installation identity. Invalid configuration must leave the last known-good configuration active where possible.

### Restart Services

Restarts bounded runtime services while preserving persistent storage and SERVER installation identity. CLIENTS receive a transient lifecycle notice and reconnect through normal verification.

Neither operation is equivalent to application data reset.

## 12. Failure and recovery

All lifecycle and registration failures should expose:

- machine-readable reason code;
- operator-readable explanation;
- affected component;
- whether local-only operation remains available;
- safe recovery action;
- timestamp and correlation/event ID where applicable.

## 13. Invariants

- Offline-first behavior remains preserved.
- The SERVER registry is durable and local-authoritative.
- Remote projections never overwrite local truth without validated reconciliation.
- Truth Firewall objections and trust boundaries remain armed.
- Protocol-version incompatibility is explicit.
- Retired identities retain lineage rather than disappearing.
- UI animation follows state; it does not manufacture state.
- Shutdown completion is evidence-based, not button-based.

## 14. Runtime acceptance evidence

This architecture is accepted only after evidence confirms:

- migration from the previous SERVER schema;
- automatic CLIENT registration;
- durable node persistence after restart;
- heartbeat online/offline behavior;
- transitional and busy-state projections;
- synchronized counts across interfaces;
- maintenance admission rejection;
- reload and restart preservation of identity and registry;
- complete graceful shutdown sequencing and notification;
- clean recovery after interrupted shutdown where supported.
