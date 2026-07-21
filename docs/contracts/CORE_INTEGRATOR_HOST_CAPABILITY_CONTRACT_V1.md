# Portable Feature Capsule and Host-Service Contract v1

- **Status:** Proposed
- **Version:** 1
- **Checkpoint:** CORE-ARCH-003A
- **Related ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`

## 1. Purpose

This contract defines how reusable SWRLZ features are packaged, transferred, and attached to compatible projects without copying whole applications, predefining every consumer, merging identities, or inheriting unrestricted authority.

## 2. Definitions

- **Feature capsule:** a portable versioned source package implementing this contract.
- **Host:** any compatible current or future project that attaches a capsule.
- **Host service:** a narrow capability supplied by a host, such as secure storage, audit, scheduling, networking, notifications, or policy evaluation.
- **Host adapter:** the receiving-project bridge that implements required host-service contracts.
- **Integration manifest:** the receiving-project declaration that selects a capsule version, maps required services, and enables optional components.
- **Runtime adapter:** capsule code specific to Android, JVM, server, or another accepted runtime.

## 3. Required capsule descriptor

Every capsule MUST declare:

- `featureId`;
- semantic `featureVersion`;
- `contractVersion`;
- source lineage and archive checksum identity;
- supported runtime targets;
- required host services;
- optional host services;
- required permissions and platform components;
- storage namespace and migration version;
- local, LAN, and remote behavior;
- lifecycle requirements;
- failure policy;
- protocol/schema compatibility range;
- Truth Firewall and audit impact;
- portable-core, runtime-adapter, and optional-UI components.

The descriptor MUST NOT require a closed list of named SWRLZ applications.

## 4. Host-service declaration

A host MUST expose an explicit set of services. Examples include secure storage, audit sink, policy clock, background scheduler, local networking, LAN networking, remote networking, notification surface, diagnostics, cryptographic operations, IPC, and platform-role services.

Absence of a required service MUST produce an explicit incompatible, unsupported, or unavailable result. A capsule MUST NOT infer a service from package name, signature matching, source origin, reflection, or device identity.

## 5. Integration rules

1. A capsule is attached only through an explicit receiving-project integration manifest.
2. Build-time inclusion and runtime authorization remain separate decisions.
3. The host MUST reject activation when contract, runtime, service, permission, storage, migration, or protocol requirements are incompatible.
4. The host MUST preserve the reason for rejection.
5. Permissions and components MUST be declared and reviewed; they MUST NOT appear silently.
6. The receiving project owns its adapter and presentation choices.
7. The capsule remains unaware of the receiving project name except for optional diagnostics supplied by the host.
8. Shared-repository consumers MAY use module dependencies; separate repositories or canonical ZIP lanes MUST support ZIP plus sibling SHA-256.

## 6. Capsule separation

A capsule SHOULD separate:

- portable logic and tests;
- runtime adapters;
- optional host-specific presentation;
- migrations;
- descriptor and documentation.

Portable logic MUST NOT depend directly on an Android Activity, IME service, launcher surface, CLIENT screen, server admin endpoint, or host navigation framework.

## 7. Lifecycle

```text
inspect descriptor
→ verify archive and checksum
→ validate runtime and host services
→ initialize
→ start
→ pause/resume where applicable
→ stop
→ migrate, update, or retire explicitly
```

Initialization MUST be idempotent or return an explicit conflict. Stop MUST release owned resources. Restart and process-death behavior MUST be documented.

## 8. Storage and migration

- Storage MUST be namespaced by feature identity and host installation identity.
- Cross-project storage sharing requires a separate accepted IPC or provider contract.
- Schema changes require versioned migrations.
- Failed migration MUST fail closed for protected data and preserve recovery evidence.
- Retirement MUST preserve lineage, checksums, superseded-by references, and rollback instructions.

## 9. Trust, routing, and offline behavior

- Packaging does not grant trust, entitlement, enrollment, or execution authority.
- Shared device identity or signing lineage does not grant unrestricted authorization.
- Offline-first operation is mandatory where the feature can function locally.
- Route classes MUST remain explicit: local, LAN, or remote.
- No silent local-to-remote fallback is allowed.
- Entitlement cannot override privacy, identity, trust, protocol, or Truth Firewall requirements.

## 10. Failure isolation

- Optional capsule failure MUST NOT crash unrelated host startup.
- Mandatory fail-closed capsules require a separate accepted declaration.
- Repeated failure SHOULD enter quarantine or unavailable state with reason code and audit evidence.
- One capsule MUST NOT directly mutate another capsule's storage, lifecycle, or policy state.

## 11. Extraction and canonicalization

When a reusable feature originates inside an existing project:

1. document the originating source and commit;
2. extract portable logic and required adapters in a bounded checkpoint;
3. create a canonical feature ZIP and sibling SHA-256;
4. place the accepted package in the neutral `SOURCES/SHARED_FEATURES/<FEATURE>/` lane when that lane is authorized;
5. create a receiving-project adapter and integration manifest;
6. reintegrate the canonical package into the originating project;
7. retire divergent local copies through documented lineage rather than deletion without evidence.

## 12. Integration manifest minimum

A receiving-project manifest MUST record:

- feature ID and version;
- archive and checksum identity or repository module reference;
- runtime adapter;
- required-service mappings;
- optional-service mappings;
- enabled and disabled components;
- permissions/components accepted by the host;
- storage namespace;
- route classes;
- compatibility result;
- source and integration lineage.

## 13. Evidence requirements

Implementation checkpoints MUST produce:

- source archive and SHA-256;
- descriptor and integration manifest;
- portable-core/runtime-adapter dependency graph;
- service compatibility results;
- permission and platform-component diff;
- extraction and reintegration lineage;
- failure-isolation and migration tests;
- offline and routing tests;
- Truth Firewall preservation evidence;
- build and on-device evidence for each receiving project.

## 14. Non-authorization

This contract does not authorize creation of `SOURCES/SHARED_FEATURES/`, source extraction, Gradle changes, app changes, permissions, builds, workflow runs, releases, deployments, installations, or dynamic executable plugin loading.