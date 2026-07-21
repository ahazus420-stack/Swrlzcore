# CORE-PLAN-008 — `swrlz.discovery.contract` Extraction Plan

- **Status:** Complete plan; implementation not authorized
- **Date:** 2026-07-21
- **Checkpoint:** CORE-PLAN-008
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/core-plan-008`
- **Parent checkpoint:** CORE-GATE-007
- **Governing Constitution:** `docs/governance/SWRLZ_CONSTITUTION.md`
- **Accepted architecture:** ADR-0003 and `CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Candidate audit:** `docs/audits/CORE_GATE_007_FIRST_MATURE_FEATURE_EXTRACTION_CANDIDATE_AUDIT.md`
- **Extraction manifest:** `docs/manifests/SWRLZ_DISCOVERY_CONTRACT_EXTRACTION_MANIFEST_V1.md`
- **Portable API and vectors:** `docs/contracts/SWRLZ_DISCOVERY_CONTRACT_PORTABLE_API_AND_WIRE_VECTORS_V1.md`
- **Evidence and rollback:** `docs/evidence/SWRLZ_DISCOVERY_CONTRACT_EQUIVALENCE_AND_ROLLBACK_PLAN_V1.md`

## 1. Objective

Define an implementation-ready, bounded extraction plan for a project-agnostic discovery contract capsule without modifying mature CLIENT or SERVER/NODE_HOST source.

Provisional capsule identity:

```text
capsule_id: swrlz.discovery.contract
capsule_version: 0.1.0
contract_version: 1
runtime_targets:
  - kotlin-jvm
```

The capsule will preserve one canonical protocol-v1/schema-v1 discovery model and codec for SERVER production and CLIENT interpretation. It is not a networking, pairing, trust, identity-storage, authorization, mission, or UI feature.

## 2. Facts

### 2.1 Accepted wire baseline

The current discovery contract defines:

- HTTP route owned by NODE_HOST: `GET /discovery/signature`;
- discovery port owned by NODE_HOST runtime: `8787`;
- protocol version: `1`;
- schema version: `1`;
- exact sentinels:
  - `swrlz-local-node`;
  - `discovery-signature`;
- required success fields:
  - `ok`;
  - `service`;
  - `endpoint`;
  - `protocolVersion`;
  - `schemaVersion`;
  - `nodeId`;
  - `installationId`;
  - `displayName`;
  - `hostVersion`;
  - `port`;
  - `capabilities`;
  - `trust`;
- required trust fields:
  - `policy = pairing_required`;
  - `missionAuthorization = trusted_only`.

Discovery identifies a candidate node only. It does not establish trust or authorize missions.

### 2.2 Mature source boundaries

SERVER origin evidence:

```text
SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt
SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/NodeCompatibilityProtocol.kt
```

SERVER host-owned boundaries that remain outside the capsule:

```text
NodeRuntime.kt
NodeIdentityStore.kt
Android service and application lifecycle
listener sockets and interface selection
HTTP request parsing and response status/headers
runtime capability truth
persistent identity and host version retrieval
```

CLIENT attachment evidence:

```text
android/app/src/main/java/sh/swurlz/core/net/Api.kt
android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt
related CLIENT-local models and preferences
```

CLIENT host-owned boundaries that remain outside the capsule:

```text
candidate address generation and scanning
HTTP client, timeout, retry, and route behavior
saved endpoint and observation persistence
identity-change UX and trust reassessment workflow
Compose presentation and navigation
notifications and local recovery
```

### 2.3 Current lineage evidence

CLIENT candidate:

```text
CLIENT_CFv1.0.1_SWRLZ.zip
SHA-256: 9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7
```

Preserved SERVER discovery source hashes recorded in earlier evidence:

```text
DiscoveryProtocol.kt:
36c248b3d2ad5cee53d8a048607b41aa3fea32ae9578afbe5773ae06cee21a97

NodeCompatibilityProtocol.kt:
43325599ffa1fb97cf7c508b5eef600aad5e4247cdcf384924e655867754c319
```

Historical records contain two distinct archives named `SERVER_CFv1.0.4_SWRLZ.zip`:

```text
795fe420c43e0d1ad32502869499fede042609bd29bbb2bdeb09cedfcdabee70
32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6
```

The implementation checkpoint must resolve the repository-authoritative SERVER ZIP and sibling checksum before reading or modifying mature source. Filename equality is not sufficient lineage evidence.

## 3. Requirements

The capsule must:

1. remain Kotlin/JVM and Android-framework independent;
2. require no host services for parsing, validation, compatibility evaluation, or canonical serialization;
3. preserve protocol-v1/schema-v1 field names and semantics;
4. preserve the exact service and endpoint sentinels;
5. preserve UUIDv4-prefixed node and installation ID validation;
6. ignore unknown additive fields when protocol `1` remains supported;
7. tolerate higher additive schema versions when all required v1 fields validate;
8. ignore unknown capabilities for execution while allowing diagnostic retention;
9. fail closed for unknown trust policy or mission-authorization values;
10. return typed reason codes rather than generic failure strings;
11. contain no transport route, socket, HTTP client/server, retry, address, permission, lifecycle, identity-storage, trust mutation, token, proof-key, or mission logic;
12. preserve deterministic canonical wire vectors and package lineage;
13. allow SERVER and CLIENT to attach through distinct, thin adapters;
14. avoid a permanent duplicate canonical implementation after accepted REINTEGRATE.

## 4. Serializer boundary decision

### Decision

Use a capsule-internal JSON backend abstraction with an initial `kotlinx.serialization` implementation.

```text
DiscoveryContractCodec
        ↓
internal DiscoveryJsonBackend
        ↓
kotlinx.serialization Json implementation
```

The public capsule API must not expose `JsonElement`, serializer descriptors, Android types, or library-specific exceptions.

Recommended JSON configuration:

```text
ignoreUnknownKeys = true
explicitNulls = false
isLenient = false
coerceInputValues = false
encodeDefaults = true
```

Wire enum values should first decode as raw strings. Validation then maps them to supported semantic values or typed incompatibility reason codes. This preserves fail-closed behavior and produces stable evidence instead of allowing library exceptions to erase the reason.

### Why this boundary

- avoids maintaining an unsafe hand-written general JSON parser;
- keeps the library implementation replaceable;
- permits deterministic field order through declaration order for canonical vectors;
- allows unknown-field tolerance;
- avoids coupling CLIENT and SERVER adapters to a serializer library;
- preserves one canonical codec implementation.

### Non-decision

The exact dependency version and Gradle coordinates remain implementation-checkpoint details and must be selected against the verified CLIENT and SERVER toolchains. This plan does not modify either build graph.

## 5. Portable API shape

The exact planned API is specified in `SWRLZ_DISCOVERY_CONTRACT_PORTABLE_API_AND_WIRE_VECTORS_V1.md`.

The public surface contains:

- immutable wire and validated models;
- protocol constants;
- `encodeSuccess`;
- `encodeErrorBody`;
- `decode`;
- `validate`;
- `evaluateCompatibility`;
- typed errors and nonfatal warnings;
- deterministic canonical vectors.

The API does not accept a URL, socket, HTTP request, Android `Context`, identity store, preference store, token, or trust object.

## 6. Adapter plan

### 6.1 SERVER producer/validator adapter

Planned host-local adapter responsibility:

1. receive verified host-owned `nodeId`, `installationId`, display name, host version, active port, capabilities, and fixed discovery policy;
2. create a capsule `DiscoverySuccessInput`;
3. ask the capsule to validate and encode the success body;
4. map capsule encoding failure to a host-owned bounded `500` or `503` response;
5. attach host-owned HTTP status and headers;
6. retain method, path, body, `Accept`, listener, interface, timeout, and lifecycle enforcement in NODE_HOST.

Planned strangler location:

```text
DiscoveryProtocol.kt
```

`DiscoveryProtocol.kt` should become a host HTTP adapter and delegate only body model validation and serialization to the capsule. `NodeRuntime.kt` and `NodeIdentityStore.kt` remain authoritative host code.

`NodeCompatibilityProtocol.kt` stays outside this capsule because `/status` and presence routes are separate compatibility surfaces.

### 6.2 CLIENT parser/compatibility adapter

Planned host-local adapter responsibility:

1. receive HTTP status, content type, response body, source URL, and observation time from `Api.kt`;
2. reject non-`200` or incompatible content type at the host boundary;
3. pass only the JSON body to the capsule codec;
4. map validated discovery identity and compatibility result into CLIENT-local persistence and UI models;
5. compare saved endpoint identity using host-owned policy:
   - same node and installation: update observation;
   - same node, different installation: require trust reassessment;
   - different node: treat as another node;
6. keep candidate scanning, retry, saved-server state, UI, and trust transitions outside the capsule.

Planned attachment locations:

```text
Api.kt
NetworkDiscoveryScreen.kt
CLIENT-local models and preferences
```

### 6.3 Legacy bridge

During one explicitly bounded compatibility window, CLIENT may retain legacy substring recognition only for identified pre-contract NODE_HOST versions.

The bridge must:

- be isolated behind a named `LegacyDiscoveryBridge` or equivalent host-local adapter;
- never create or transfer trust;
- never masquerade as structured protocol-v1 identity evidence;
- return a distinct compatibility state;
- have an explicit retirement checkpoint.

No silent fallback from structured parsing to substring acceptance is permitted for a payload claiming protocol-v1 fields.

## 7. Strangler extraction sequence

### Stage 0 — lineage gate

Before implementation:

- resolve exact repository-authoritative CLIENT and SERVER ZIPs and checksums;
- verify archive integrity and path safety;
- record exact origin file hashes;
- verify accepted contract status and repository copy;
- stop if lineage is ambiguous.

### Stage 1 — freeze wire evidence

Capture the current SERVER canonical success bytes, error-body bytes, and CLIENT recognition behavior as immutable vectors. Record semantic and byte-equivalence expectations separately because JSON member order is not a protocol requirement, while the first migration should preserve existing canonical bytes where practical.

### Stage 2 — implement capsule only

Create `SOURCES/SHARED_FEATURES/DISCOVERY_CONTRACT/` with:

- pure Kotlin source;
- descriptor;
- public contract API;
- internal JSON backend;
- deterministic vectors;
- negative tests;
- ZIP/SHA package and evidence.

No mature source changes occur in this stage.

### Stage 3 — SERVER shadow verification

Create a narrow NODE_HOST adapter. In tests only, produce the body through both the old serializer and capsule and compare:

- exact canonical bytes for accepted v1 vectors;
- parsed semantic equality for vectors where escaping or ordering may differ;
- identical fail-closed outcomes.

Do not emit duplicate runtime responses or mutate host identity.

### Stage 4 — SERVER REINTEGRATE

After equivalence passes, make the SERVER HTTP adapter use the capsule as the canonical discovery body codec. Preserve the old implementation through rollback lineage until acceptance. Do not change listener or identity behavior.

### Stage 5 — CLIENT attach and shadow verification

Attach the capsule through the CLIENT parser adapter. In tests, compare structured capsule decisions with existing recognition behavior for accepted and rejected vectors. Preserve only the explicitly named legacy bridge for pre-contract nodes.

### Stage 6 — CLIENT structured persistence

Persist endpoint, node ID, installation ID, protocol/schema versions, known capabilities, observation time, and compatibility state through host-owned storage. This is a CLIENT checkpoint because persistence and trust reassessment are not capsule responsibilities.

### Stage 7 — behavioral-equivalence and device evidence

Build and verify CLIENT and SERVER independently under separate approval. Then perform local-link device testing without treating discovery as proof of trust.

### Stage 8 — retirement

Retire duplicated discovery serialization and parsing only after:

- both mature hosts compose the canonical capsule;
- equivalence evidence is accepted;
- rollback packages remain available;
- lineage records identify superseded paths;
- legacy bridge retirement is separately approved.

## 8. Checkpoint decomposition

Recommended future execution checkpoints:

1. `CORE-IMP-009A` — implement and test the standalone discovery contract capsule only;
2. `SERVER-REINT-009B` — attach and reintegrate SERVER body serialization through a thin adapter;
3. `CLIENT-ATTACH-009C` — attach CLIENT structured parsing and compatibility evaluation;
4. `CLIENT-MIG-009D` — add identity-aware persistence and legacy-bridge policy;
5. `DISCOVERY-VER-009E` — independent builds, device verification, and equivalence acceptance;
6. `DISCOVERY-RETIRE-009F` — retire superseded duplicate logic through explicit lineage.

These checkpoints must not be collapsed into one broad source rewrite.

## 9. Security and authority boundary

The capsule may state what a discovery response claims. It cannot prove that the remote host owns the advertised identity.

The capsule must not:

- create, store, rotate, restore, or delete node identity;
- establish pairing or trust;
- verify device proof;
- accept or authorize missions;
- infer trust from endpoint, signature, capability, host version, or successful decoding;
- select local, LAN, remote, cellular, VPN, or public routes;
- perform network fallback;
- expose secrets, tokens, proof keys, or device keys;
- weaken Truth Firewall objection, refusal, pause, or safer-alternative behavior.

## 10. Completion criteria for this plan

CORE-PLAN-008 is complete when repository documentation defines:

- exact extraction scope;
- portable and host-owned boundaries;
- planned public API;
- serializer abstraction and initial backend decision;
- canonical positive and negative vectors;
- SERVER and CLIENT adapter responsibilities;
- strangler and REINTEGRATE stages;
- equivalence evidence;
- lineage gate;
- rollback and retirement;
- next bounded implementation checkpoint.

## 11. Repository actions performed

Documentation-only records are added on `checkpoint/core-plan-008`, derived from `checkpoint/core-gate-007` so the unmerged audit remains in ancestry.

## 12. Explicitly not performed

- No CLIENT source modification.
- No SERVER/NODE_HOST source modification.
- No discovery capsule source or shared-feature directory.
- No Gradle or workflow change.
- No ZIP or checksum generation.
- No build or test execution.
- No workflow trigger.
- No merge, release, deployment, or installation.

## 13. Recommended next checkpoint

`CORE-IMP-009A — Standalone Discovery Contract Capsule Implementation`

Implement only the project-agnostic capsule core, serializer abstraction, canonical vectors, deterministic tests, descriptor, ZIP/SHA package, and evidence. Do not attach it to CLIENT or SERVER yet.