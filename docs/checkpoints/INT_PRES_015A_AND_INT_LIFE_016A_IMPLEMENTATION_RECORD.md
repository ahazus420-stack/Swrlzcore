# INT-PRES-015A + INT-LIFE-016A Implementation Record

**Recorded:** 2026-07-22  
**Status:** Source packages prepared; repository source ZIP upload, GitHub build, device test, migration test, and runtime evidence still pending.

## Scope

This record captures the approved implementation work for:

- `INT-PRES-015A` — verified automatic CLIENT registration, persistent SERVER node registry, live presence states, synchronized node counts, and state-driven node visuals.
- `INT-LIFE-016A` — state-aware SERVER lifecycle, maintenance mode, service restart, configuration reload, and graceful shutdown sequence.

## Source lineage

Prepared source packages:

- CLIENT `CFv2.0.6`, based on CLIENT `CFv2.0.5`.
- SERVER `CFv2.0.7`, based on SERVER `CFv2.0.6`.

At the time of this record, the repository contains delivery receipts through CLIENT `CFv2.0.5` and SERVER `CFv2.0.6`. The newer `CFv2.0.6` CLIENT and `CFv2.0.7` SERVER packages are not treated as repository-canonical until their ZIP, checksum, and receipt pairs are uploaded and verified.

## INT-PRES-015A requirements

### Automatic registration

After successful discovery and transport verification, CLIENT initiates registration without requiring a separate manual action.

Expected flow:

```text
CLIENT discovers SERVER
-> transport/status verification
-> registration request
-> SERVER validates identity and compatibility
-> SERVER persists node record
-> SERVER returns registration result
-> CLIENT begins presence heartbeat
```

### Registration verification boundary

Registration must validate, at minimum:

- stable node identity;
- device identity;
- installation identity;
- protocol compatibility;
- application/device metadata;
- declared capabilities;
- device-proof material when available.

Registration does not imply trust elevation or mission authorization.

```text
Registered != Trusted
Online != Authorized
Proof Presented != Proof Accepted
```

### Persistent SERVER registry

The SERVER registry is authoritative and durable across restarts. A registered node remains visible when offline.

Minimum persisted fields:

```text
nodeId
deviceId
installationId
displayName
deviceType
appVersion
protocolVersion
capabilities
firstRegisteredAt
lastSeenAt
registrationState
connectionState
identityState
proofState
trustState
currentMissionId
archivedOrRetiredState
```

### Presence states

Canonical live/transitional states:

```text
CONNECTING
VERIFYING
REGISTERING
CONNECTED
BUSY
OFFLINE
```

Failure and policy states remain distinct where implemented:

```text
REGISTRATION_FAILED
IDENTITY_FAILED
PROOF_FAILED
TRUST_PENDING
PROTOCOL_INCOMPATIBLE
REVOKED
RETIRED
```

### SERVER counts and projections

The SERVER UI must distinguish persistent inventory from live availability:

```text
REGISTERED
ONLINE
OFFLINE
BUSY
CONNECTING
VERIFYING
REGISTERING
```

All counts and node lists must derive from the same authoritative repository/state flow. User Mode and Developer Mode are projections; neither UI owns registry state.

### Nodes list visuals

Node blob animation and color transitions must reflect authoritative state only. Visuals must not imply connectivity, proof acceptance, trust, or mission authorization without corresponding evidence.

## INT-LIFE-016A requirements

### Lifecycle states

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

`DEGRADED` remains runtime-active. The primary action must therefore offer shutdown, not initialization.

### Operator actions

```text
INITIALIZE SERVER
ENTER MAINTENANCE MODE
RELOAD CONFIGURATION
RESTART SERVICES
BEGIN SHUTDOWN SEQUENCE
```

Actions are enabled only when valid for the authoritative lifecycle state.

### Graceful shutdown sequence

1. Stop accepting new CLIENT connections.
2. Notify connected CLIENTS that shutdown has begun.
3. Drain active operations or safely cancel them.
4. Flush pending writes and persistent queues.
5. Save runtime and session state.
6. Rotate and finalize logs.
7. Close encrypted stores and release resources.
8. Stop background workers and services.
9. Shut down networking.
10. Mark the SERVER stopped only after clean completion.

The UI must expose shutdown progress and the current stage. CLIENTS must transition truthfully to a SERVER-shutdown/offline state rather than disappearing.

### Maintenance mode

Maintenance mode must:

- reject new missions and new enrollment requests;
- preserve registered-node records;
- keep permitted diagnostics/administrative control available;
- allow existing operations to drain or be cancelled deliberately;
- advertise that normal work is unavailable.

Maintenance is not equivalent to stopped or offline.

## Data and protocol impact

Expected bounded changes include:

- CLIENT automatic registration and heartbeat behavior;
- SERVER registration, heartbeat, and node-list routes;
- persistent node-registry storage;
- explicit non-destructive database migration when schema advancement is required;
- lifecycle-aware admission rejection during maintenance and shutdown;
- lifecycle and presence events for synchronized UI projections.

Protocol-version discipline remains mandatory. Any incompatible wire-format change requires explicit version handling and must not be silently introduced.

## Architecture invariants

The implementation must preserve:

- offline-first behavior;
- stable identity and installation lineage;
- separate registration, proof, trust, authorization, and revocation states;
- Truth Firewall protections;
- local-versus-remote distinctions;
- durable node history, including retired/archived lineage;
- one authoritative state repository observed by User Mode and Developer Mode;
- no fake connected state derived only from discovery reachability.

## Verification still required

The following evidence remains pending:

- checksum verification after repository upload;
- GitHub Gradle/APK builds;
- installation and upgrade from the prior SERVER database schema;
- successful automatic CLIENT registration;
- persisted node visibility after SERVER restart;
- heartbeat-driven online/offline transitions;
- connecting, verifying, registering, connected, busy, and offline UI states;
- synchronized SERVER and CLIENT counts;
- maintenance-mode admission behavior;
- reload and service-restart behavior;
- graceful shutdown progress and CLIENT notification;
- clean persistence/log/network closure.

Until those tests pass, these checkpoints are source-complete but not runtime-accepted.

## Excluded authority

These checkpoints do not authorize:

- bypassing identity or device-proof validation;
- automatic trust elevation;
- destructive registry deletion;
- unrelated protocol redesign;
- provider-adapter work;
- deployment or release;
- claims of successful runtime behavior without evidence.
