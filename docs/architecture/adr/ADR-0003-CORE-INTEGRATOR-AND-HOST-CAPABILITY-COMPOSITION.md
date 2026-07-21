# ADR-0003: Portable Feature Capsules and Host-Service Composition

- **Status:** Proposed for acceptance
- **Date:** 2026-07-21
- **Checkpoint:** CORE-ARCH-003A
- **Constitutional basis:** Integrate; do not overwrite. Preserve offline-first behavior, Truth Firewall dissent, lineage, local-versus-remote distinctions, and protocol-version discipline.
- **Related decisions:** ADR-0001 Shared Core Capabilities and Distinct Android App Shells; ADR-0002 Modular Capability and Entitlement Gates
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`

## Context

SWRLZ needs to move useful features between independently evolving projects without copying entire applications or requiring every future consumer to be predefined. A feature may originate in CLIENT, CORE_BASE, SERVER, Launcher, Keyboard, NODE_HOST, or a future project. Once extracted, its canonical reusable form must be portable and attachable to any compatible host.

## Decision

SWRLZ will use **portable feature capsules**: versioned reusable source packages that declare runtime targets, required and optional host services, permissions, storage, lifecycle, routing, compatibility, lineage, migrations, and evidence requirements.

Features declare requirements. Hosts declare provided services. Integration succeeds when those requirements are satisfied.

A capsule MUST target runtime classes and service contracts rather than a closed list of named applications. Named SWRLZ projects are examples, not a registry of allowed consumers.

```text
feature capsule
    + receiving-project adapter
    + receiving-project integration manifest
    = attached capability
```

A neutral canonical lane is proposed:

```text
SOURCES/SHARED_FEATURES/<FEATURE>/
├── source/
├── packages/
├── OLD_PATCHES/
├── <FEATURE>_<VERSION>.zip
├── <FEATURE>_<VERSION>.sha256
├── docs/
└── integrations/
```

Creation of that lane is not authorized by this ADR.

## Requirements

1. A capsule MUST be project-agnostic and MUST NOT require the originating project to remain present.
2. A capsule MUST declare `featureId`, semantic version, contract version, runtime targets, source lineage, checksum, required services, optional services, permissions/components, storage namespace, migration version, lifecycle, routing, failure policy, protocol compatibility, Truth Firewall impact, and audit impact.
3. A host MUST provide a local integration manifest mapping capsule requirements to host-provided services.
4. A capsule MUST NOT infer authority from package name, app identity, signing key, device identity, reflection, or source origin.
5. Portable core logic MUST remain separate from runtime adapters and host-specific presentation.
6. A feature extracted from an existing project MUST receive a canonical shared package; the originating project SHOULD then reintegrate that canonical implementation rather than retain a divergent copy.
7. ZIP plus sibling SHA-256 is the initial portable distribution format. Repository module integration MAY be used when projects share a repository.
8. Runtime-downloaded arbitrary executable code remains outside the initial architecture.
9. Optional capsule failure MUST be isolated from unrelated host startup unless an accepted contract marks the capsule mandatory and fail-closed.
10. Local, LAN, and remote routes MUST remain explicit; no silent remote fallback is allowed.
11. Truth Firewall behavior MUST remain active and MUST NOT be weakened by composition, entitlement, or host selection.
12. Storage MUST be namespaced and migration-controlled.

## Feature structure

A capsule SHOULD separate:

- portable domain logic, models, state machines, validation, serialization, migrations, and tests;
- runtime adapters such as Android Keystore, JVM filesystem, WorkManager, server scheduling, notifications, and logging;
- optional host presentation such as Compose screens, launcher tiles, keyboard warnings, CLIENT diagnostics, or server admin surfaces.

## Extraction and reintegration

```text
project-local implementation
    → bounded extraction checkpoint
    → canonical SHARED_FEATURES package + SHA
    → receiving-project adapter and manifest
    → originating project reintegrates canonical package
```

The extracted canonical package becomes the source of truth. Independent copied descendants are not accepted as the normal maintenance model.

## Consequences

### Positive

- features can move between current and future projects without predeclaring consumers;
- one canonical implementation can serve Android and JVM hosts through adapters;
- fixes, migrations, and trust requirements propagate through versioned packages;
- receiving projects retain their own identity, lifecycle, permissions, and authority;
- offline ZIP/SHA transfer remains supported.

### Costs and risks

- service contracts and compatibility validation require discipline;
- runtime adapters and receiving-project manifests require maintenance;
- extraction from legacy project-local code may require refactoring;
- careless adapters could expose excessive host authority;
- canonicalization and reintegration must be documented to prevent drift.

## Rejected alternatives

- **Whole-project cloning:** rejected because fixes and trust behavior drift.
- **Closed host registry:** rejected because future projects should not require edits to the feature package merely to become consumers.
- **One unrestricted profile per named app:** rejected because requirements should be capability-based.
- **Runtime-downloaded executable plugins:** rejected initially because of signing, loading, permission, supply-chain, lifecycle, and policy complexity.

## Implementation boundary

This ADR documents architecture only. It does not authorize `SOURCES/SHARED_FEATURES/`, source extraction, Gradle changes, app-lane changes, permissions, builds, workflows, releases, deployment, installation, or merge.

## Verification expectations

Future evidence should demonstrate portable ZIP/SHA packaging, project-agnostic descriptors, receiving-project manifests, service compatibility validation, adapter least authority, source-lineage traceability, originating-project reintegration, failure isolation, migration tests, offline behavior, Truth Firewall preservation, and no silent authority expansion.