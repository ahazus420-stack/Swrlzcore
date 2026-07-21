# CORE-PLAN-005 No-Op Portable Feature Capsule Reference Implementation Plan

- **Status:** Planned; implementation not authorized
- **Checkpoint:** CORE-PLAN-005
- **Date:** 2026-07-21
- **Governing Constitution:** `docs/governance/SWRLZ_CONSTITUTION.md`
- **Accepted ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Accepted contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Evidence contract:** `docs/contracts/NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1.md`

## 1. Purpose

Define one bounded reference implementation that proves a project-agnostic portable feature capsule can be packaged once, attached to two independent host projects through different adapters, invoked without authority expansion, and verified through deterministic evidence.

The reference capsule performs no privileged or domain-changing work. Its sole purpose is to validate descriptor, compatibility, lifecycle, attachment, service translation, failure isolation, lineage, ZIP/SHA portability, and terminology requirements before Phoenix Firewall or any mature CLIENT/SERVER feature is extracted.

## 2. Proposed capsule identity

- Capsule ID: `swrlz.reference.noop`
- Capsule display name: `SWRLZ No-Op Reference Capsule`
- Capsule version: `0.1.0`
- Contract version: `1`
- Protocol/schema range: local reference schema `1`
- Runtime target: Kotlin/JVM-compatible host runtime
- Network behavior: none
- Remote dependency: none
- Android permissions: none
- Storage: optional isolated ephemeral state only
- Truth Firewall impact: none beyond explicit compatibility and lifecycle audit events

These values remain planned until a later implementation checkpoint accepts them.

## 3. Proposed descriptor schema

The reference descriptor should include:

```yaml
capsule_id: swrlz.reference.noop
capsule_version: 0.1.0
contract_version: 1
runtime_targets:
  - kotlin-jvm
required_services:
  - swrlz.audit_sink.v1
  - swrlz.clock.v1
optional_services:
  - swrlz.ephemeral_state.v1
permissions: []
network_routes: []
storage:
  namespace: swrlz.reference.noop
  schema_version: 1
lifecycle:
  initialize: required
  start: supported
  invoke: supported
  stop: required
failure_policy: optional-isolated
truth_firewall_impact: audit-only
```

The descriptor must not list named host projects as its compatibility boundary.

## 4. Initial host-service identifiers

### Required

- `swrlz.audit_sink.v1`
  - receives structured, content-free lifecycle and compatibility events;
  - must not record user content;
  - must preserve reason codes.

- `swrlz.clock.v1`
  - exposes monotonic or documented wall-clock timestamps;
  - must not imply remote time authority.

### Optional

- `swrlz.ephemeral_state.v1`
  - exposes a capsule-namespaced transient key/value boundary;
  - must not provide access to unrelated host storage;
  - absence must not block invocation.

## 5. Planned capsule behavior

The no-op capsule should:

1. inspect its descriptor;
2. validate required services;
3. initialize idempotently;
4. register an available state;
5. accept one typed `invoke()` request;
6. return a deterministic result such as `NoOpResult.Executed` with capsule ID, version, host-supplied timestamp, and no user content;
7. stop and release resources;
8. expose explicit incompatibility and lifecycle reason codes;
9. perform no networking, privileged operation, identity mutation, trust decision, or remote fallback.

## 6. Planned integration-manifest schema

Each host attachment should record:

```yaml
integration_id: <host-local-id>
host_identity: <host-local-reference>
capsule_id: swrlz.reference.noop
capsule_version: 0.1.0
capsule_source_sha256: <verified-sha256>
contract_version: 1
adapter_path: <host-local-adapter-path>
service_mappings:
  swrlz.audit_sink.v1: <host-audit-adapter>
  swrlz.clock.v1: <host-clock-adapter>
optional_service_mappings:
  swrlz.ephemeral_state.v1: <optional-host-state-adapter>
permissions_added: []
components_added: []
route_classes: []
storage_namespace: swrlz.reference.noop
rollback:
  detach_steps: <documented-host-local-procedure>
```

## 7. Two independent test hosts

The implementation checkpoint should use two deliberately different, minimal hosts so portability is demonstrated rather than assumed.

### Host A — Android/JVM reference host

- attaches the capsule through an Android-compatible Gradle module boundary;
- exposes audit and clock services through an Android adapter;
- adds no Android permissions or components;
- verifies process recreation or clean restart behavior where practical.

### Host B — plain JVM reference host

- attaches the same canonical capsule through a separate JVM project or test fixture;
- exposes independent audit and clock adapters;
- has no Android framework dependency;
- verifies that capsule logic does not require an Android application class, Activity, Context, or manifest.

Named mature app lanes such as CLIENT or SERVER are excluded from the no-op implementation checkpoint unless separately authorized.

## 8. Proposed canonical package layout

The later implementation checkpoint may create:

```text
SOURCES/SHARED_FEATURES/NOOP_REFERENCE/
├── source/
├── packages/
├── integrations/
│   ├── android-reference/
│   └── jvm-reference/
├── docs/
├── OLD_PATCHES/
├── SWRLZ_NOOP_REFERENCE_CAPSULE_v0.1.0.zip
└── SWRLZ_NOOP_REFERENCE_CAPSULE_v0.1.0.sha256
```

This path is not created by CORE-PLAN-005.

## 9. ZIP and checksum naming

Planned canonical package:

```text
SWRLZ_NOOP_REFERENCE_CAPSULE_v0.1.0.zip
SWRLZ_NOOP_REFERENCE_CAPSULE_v0.1.0.sha256
```

The checksum file must contain the SHA-256 of the exact ZIP bytes and filename. Archive integrity must be verified before extraction.

## 10. Compatibility reason codes

The implementation should define typed reason codes at minimum:

- `COMPATIBLE`
- `CONTRACT_VERSION_UNSUPPORTED`
- `RUNTIME_TARGET_UNSUPPORTED`
- `REQUIRED_SERVICE_MISSING`
- `SERVICE_VERSION_UNSUPPORTED`
- `DESCRIPTOR_INVALID`
- `SOURCE_CHECKSUM_MISMATCH`
- `INITIALIZATION_CONFLICT`
- `CAPSULE_QUARANTINED`
- `INTEGRATION_MANIFEST_INVALID`

Generic `disabled` or `failed` states are insufficient.

## 11. Failure-isolation tests

The implementation checkpoint must prove:

- missing required service rejects only the capsule;
- malformed descriptor rejects only the capsule;
- duplicate initialization is idempotent or returns an explicit conflict;
- invocation before initialization is rejected explicitly;
- stop after start succeeds;
- adapter exception enters unavailable or quarantined state without crashing unrelated host startup;
- optional service absence does not block execution;
- no silent remote or alternate route is attempted.

## 12. Constitutional terminology tests

Documentation, manifests, code comments, reports, and generated evidence must use accurate relationship language:

- projects **attach** or **compose** the capsule;
- hosts **expose** services and **host** lifecycle;
- adapters **translate** services;
- registries **register** availability;
- runtimes **invoke** behavior;
- packages **preserve** lineage.

The term `consume` is valid only for genuinely depleting operations and must not describe reusable module relationships.

## 13. Evidence and completion criteria

The implementation checkpoint is complete only when the evidence contract is satisfied for both hosts and proves:

- one canonical capsule source lineage;
- one verified ZIP/SHA package;
- two distinct adapters and integration manifests;
- no copied divergent capsule implementation;
- deterministic invocation result;
- compatibility and reason-code behavior;
- lifecycle and failure isolation;
- no permissions, components, networking, or authority expansion;
- constitutional terminology compliance;
- rollback and detach instructions;
- build and applicable runtime evidence for each host.

## 14. Recovery and rollback

Each host must be able to detach the capsule by removing its module/package reference, adapter, and integration manifest without modifying unrelated host behavior. Evidence and lineage records remain preserved after detachment.

## 15. Explicit exclusions

CORE-PLAN-005 does not authorize:

- creation of source modules or `SOURCES/SHARED_FEATURES/`;
- Gradle changes;
- CLIENT, SERVER, NODE_HOST, CORE_BASE, Keyboard, or Launcher changes;
- Phoenix Firewall implementation;
- extraction of an existing feature;
- workflow runs or builds;
- merge, release, deployment, or installation.

## 16. Recommended implementation checkpoint

A later implementation authorization should create only the no-op capsule, its stable contracts, two reference hosts/adapters, deterministic tests, canonical ZIP/SHA package, and required evidence. Mature app attachment and existing-feature extraction remain separate checkpoints.