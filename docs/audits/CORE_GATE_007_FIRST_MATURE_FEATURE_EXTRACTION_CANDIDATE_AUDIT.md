# CORE-GATE-007 — First Mature Feature Extraction Candidate Audit

- **Status:** Complete — recommendation only; implementation not authorized
- **Date:** 2026-07-21
- **Checkpoint:** CORE-GATE-007
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/core-gate-007`
- **Governing Constitution:** `docs/governance/SWRLZ_CONSTITUTION.md`
- **Accepted architecture:** ADR-0003 and `CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Reference proof:** `SOURCES/SHARED_FEATURES/NOOP_REFERENCE/`

## Objective

Audit canonical CLIENT and SERVER/NODE_HOST evidence and recommend the safest first bounded mature feature to EXTRACT into the portable feature-capsule system without modifying either application.

## Evidence basis and limitation

The audit used canonical source ZIP lineage, checksum records, changed-path manifests, accepted contracts, implementation reports, deterministic verification receipts, and on-device findings. The CLIENT and SERVER source archives remain authoritative. This checkpoint did not extract, rewrite, build, or execute either mature application.

Current relevant lineage:

- CLIENT candidate: `CLIENT_CFv1.0.1_SWRLZ.zip`
- CLIENT SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`
- SERVER successor evidence exists for `SERVER_CFv1.0.4_SWRLZ.zip`; the current accepted proof-key successor lineage must be revalidated at extraction start because two documented v1.0.4 checksum identities exist in historical artifacts.
- SERVER discovery/compatibility source paths preserved through the icon-only v1.0.4 candidate include `DiscoveryProtocol.kt`, `NodeCompatibilityProtocol.kt`, `NodeRuntime.kt`, and `NodeIdentityStore.kt`.

The checksum ambiguity above is not resolved by this audit and must be closed before any SERVER source movement.

## Candidate inventory

### Candidate A — CLIENT verified-admin route policy

Origin evidence:

- `android/app/src/main/java/sh/swurlz/core/net/AdminRoutePolicy.kt`
- related host paths: `Prefs.kt`, `Models.kt`, `Api.kt`, `CommandsScreen.kt`, and `NetworkDiscoveryScreen.kt`

Strengths:

- small, deterministic policy surface;
- already compiled and exercised through Kotlin/JVM smoke assertions;
- authority distinction is explicit: UI mode does not equal authenticated authority;
- likely clean extraction from Android UI and network execution.

Weaknesses:

- primarily CLIENT-side route-selection policy;
- SERVER must enforce authorization independently and should not compose CLIENT presentation policy;
- proving attachment to two genuinely different mature hosts would be artificial at this stage;
- secure-store state and UI normalization remain host-owned.

Assessment: **good later CLIENT capsule, not the first cross-project extraction proof**.

### Candidate B — SERVER device-proof cryptographic subsystem

Origin evidence includes:

- `security/AndroidKeystoreProofCipher.kt`
- `security/DeviceProofModels.kt`
- `security/HkdfSha256.kt`
- `security/PairedLanAuthorizer.kt`
- `security/ProofBindingSidecar.kt`
- `security/RequestProofVerifier.kt`
- `service/NonMutatingDeviceResolution.kt`

Strengths:

- cohesive security domain;
- meaningful future reuse across SERVER and other trusted hosts;
- explicit device/install binding and non-mutating resolution boundaries.

Weaknesses:

- Android Keystore, encrypted sidecar storage, authorization, identity, legacy enrollment, and migration are high-risk boundaries;
- extraction mistakes could weaken trust or create false authorization confidence;
- current build evidence is blocked before Kotlin compilation in one recorded environment;
- not appropriate as the first mature extraction after only a no-op reference proof.

Assessment: **defer until the extraction system has one lower-risk production feature success**.

### Candidate C — shared discovery contract and compatibility codec

Origin SERVER paths:

- `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt`
- `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/NodeCompatibilityProtocol.kt`

Related SERVER host-owned boundaries:

- `NodeRuntime.kt` — sockets, binding, listener lifecycle, runtime health, route dispatch;
- `NodeIdentityStore.kt` — durable node and installation identity;
- Android app/service lifecycle and local diagnostics.

Related CLIENT paths:

- `android/app/src/main/java/sh/swurlz/core/net/Api.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`
- discovery models currently represented through CLIENT-local model/parsing surfaces.

Portable candidate boundary:

- protocol/schema constants;
- discovery response and compatibility models;
- canonical field names and sentinel values;
- serialization and parsing;
- structural validation;
- compatibility result and reason codes;
- protocol/schema range evaluation;
- deterministic test vectors.

Host-owned boundaries that must remain outside:

- HTTP clients and servers;
- sockets, addresses, ports, interface selection, timeouts, and retries;
- identity creation, persistence, rotation, and restoration;
- trust establishment, pairing, authorization, tokens, and proof keys;
- UI, saved-server preferences, navigation, notifications, and lifecycle;
- runtime capability truth and route availability.

Strengths:

- naturally attaches to both CLIENT and SERVER through distinct adapters;
- primarily pure Kotlin and deterministic;
- current protocol logic has focused compilation and test evidence;
- directly addresses an existing duplication/drift risk between producer and parser;
- protocol version discipline, sentinel preservation, and unknown-field tolerance can be evidenced without networking;
- extraction does not require Android permissions, storage migration, sockets, or credentials.

Risks:

- SERVER and CLIENT currently have different responsibilities and possibly different local models;
- careless unification could merge producer authority with CLIENT interpretation;
- serialization-library assumptions may introduce runtime coupling;
- compatibility must preserve protocol/schema version `1` and existing sentinel behavior;
- discovery identifies a candidate node and must never imply trust or authorization.

Assessment: **recommended first mature extraction candidate**.

## Recommendation

EXTRACT a bounded capsule provisionally identified as:

```text
capsule_id: swrlz.discovery.contract
initial_version: 0.1.0
contract_version: 1
runtime_targets:
  - kotlin-jvm
```

The capsule should be a **shared discovery contract and codec**, not a networking feature and not a trust feature.

### Required service model

The portable core should require no host services for pure parsing, serialization, and compatibility evaluation. Optional diagnostic output may be exposed through the existing generic audit boundary only if separately justified.

### Origin and first attachments

1. Treat the SERVER `DiscoveryProtocol.kt` and `NodeCompatibilityProtocol.kt` behavior as one origin evidence set, not automatically as the final canonical API.
2. Create the capsule from accepted wire contracts and both implementations rather than copying one side wholesale.
3. REINTEGRATE SERVER through a narrow producer/validator adapter.
4. ATTACH CLIENT through a narrow parser/compatibility adapter.
5. Preserve SERVER authority for actual identity, capabilities, health, and listener truth.
6. Preserve CLIENT authority for candidate scanning, user intent, caching, presentation, and recovery.

## Proposed migration strategy

**Strangler extraction with a clean portable core.**

Reason:

- the portable codec itself can be clean and pure;
- mature CLIENT and SERVER entry points should delegate gradually;
- existing local models and endpoint behavior remain in place until equivalence is proven;
- rollback is straightforward because old entry points remain available during the bounded transition.

Wrapper-first may be used only if direct model substitution creates excessive compile impact. A permanent wrapper around duplicated parsing is not acceptable.

## Behavioral-equivalence gates

Before the extraction can be called complete, evidence must prove:

- exact preservation of `swrlz-local-node` and `discovery-signature` sentinels where still contractually required;
- protocol and schema version `1` compatibility;
- canonical field names, required fields, nullability, and enum handling;
- unknown additive fields remain nonfatal;
- malformed, unsupported, or incompatible payloads return typed reason codes;
- CLIENT candidate recognition behavior remains equivalent;
- SERVER serialized output remains equivalent for accepted vectors;
- discovery remains identity advertisement only and grants no trust or authority;
- no network, permission, storage, or lifecycle behavior moves into the capsule;
- no silent local-to-remote fallback;
- origin implementations are retired or delegated through explicit lineage after REINTEGRATE.

## Required extraction manifest fields

The future extraction manifest must record:

- exact CLIENT and SERVER source archive filenames and verified SHA-256 values at checkpoint start;
- exact origin paths and file hashes;
- accepted discovery and compatibility contracts;
- wire vectors captured before extraction;
- portable versus host-owned dependency classification;
- serializer/library decision;
- service and adapter boundaries;
- protocol/schema compatibility range;
- rollback and supersession plan;
- Truth Firewall, identity, trust, and authority non-expansion statement.

## Alternatives rejected for the first extraction

- **Phoenix Firewall:** high authority and policy impact; not appropriate before a simpler mature extraction succeeds.
- **Device-proof subsystem:** cryptography, Keystore, migration, and authorization risk are too high for the first production extraction.
- **CLIENT AdminRoutePolicy:** technically clean but does not naturally prove two-sided mature attachment.
- **NODE_HOST runtime/lifecycle:** Android service, sockets, diagnostics, and listener health are host infrastructure, not portable contract logic.
- **Presence registry:** persistence, lineage, and authoritative-state semantics require a separately accepted extraction design.

## Repository actions performed

- Created documentation-only branch `checkpoint/core-gate-007` from current `main`.
- Added this audit and the checkpoint handoff.

## Explicitly not performed

- No CLIENT or SERVER source modification.
- No capsule source or `SOURCES/SHARED_FEATURES/DISCOVERY_*` lane created.
- No ZIP or checksum generated.
- No Gradle or workflow change.
- No build, workflow trigger, merge, release, deployment, or installation.

## Recommended next checkpoint

`CORE-PLAN-008 — Discovery Contract Capsule Extraction Plan`

Define the exact extraction manifest, canonical wire vectors, portable API, serializer boundary, CLIENT and SERVER adapters, strangler sequence, behavioral-equivalence tests, lineage, and rollback without modifying mature source.
