# Portable Feature Capsule Architecture v1

- **Status:** Proposed architecture guide
- **Checkpoint:** CORE-ARCH-003A
- **Governing ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`

## Purpose

This guide explains how a useful feature can originate in any SWRLZ project, become a canonical portable package, and then attach to any compatible current or future project through a small adapter.

## Core rule

```text
features declare requirements
hosts declare provided services
integration succeeds when requirements are satisfied
```

A feature must not require every possible consumer to be predefined.

## Neutral canonical lane

Proposed future structure:

```text
SOURCES/SHARED_FEATURES/
└── <FEATURE>/
    ├── source/
    ├── packages/
    ├── OLD_PATCHES/
    ├── <FEATURE>_<VERSION>.zip
    ├── <FEATURE>_<VERSION>.sha256
    ├── docs/
    └── integrations/
```

This lane is proposed only; creating it requires a separate implementation authorization.

## Capsule structure

```text
feature-capsule/
├── portable-core/
│   ├── domain/
│   ├── models/
│   ├── state-machines/
│   ├── validation/
│   ├── serialization/
│   ├── migrations/
│   └── tests/
├── runtime-adapters/
│   ├── android/
│   ├── jvm/
│   └── server/
├── optional-ui/
├── contract/
├── docs/
├── manifest.yaml
└── checksum
```

### Portable core

Contains host-neutral rules and behavior. It must not depend directly on a host Activity, IME service, launcher surface, CLIENT UI, server endpoint, or navigation framework.

### Runtime adapters

Map platform facilities such as Android Keystore, JVM filesystem, WorkManager, server scheduling, notification systems, logging, or networking.

### Optional presentation

Contains host-selectable surfaces such as Compose settings, launcher tiles, keyboard warning strips, CLIENT diagnostics, or server admin views.

### Receiving-project adapter

Implements only the services the capsule requires. The adapter belongs to the receiving project and remains the least-authority boundary.

### Integration manifest

Records:

- feature ID and version;
- ZIP/SHA identity or repository module reference;
- runtime adapter;
- required and optional service mappings;
- enabled and disabled components;
- accepted permissions and platform components;
- storage namespace;
- route classes;
- compatibility result;
- source and integration lineage.

## Two supported integration paths

### Same repository

```text
implementation(project(":shared-features:<feature>"))
```

### Separate repository or source ZIP

```text
<FEATURE>_<VERSION>.zip
<FEATURE>_<VERSION>.sha256
```

The receiving project verifies the checksum, imports the capsule into a controlled location, supplies an adapter, and records the integration manifest.

## Extraction workflow

```text
feature exists inside CLIENT, CORE_BASE, SERVER, or another project
    ↓
bounded extraction checkpoint
    ↓
portable core + runtime adapters + descriptor
    ↓
canonical shared ZIP and sibling SHA
    ↓
originating project reintegrates canonical package
    ↓
other projects attach through local adapters
```

The canonical package becomes the reusable source of truth. The originating project must not silently continue maintaining a divergent private copy.

## Phoenix Firewall example

Phoenix Firewall may originate in one project but should be separated into:

- portable policy engine;
- Android adapter;
- JVM/server adapter;
- optional UI surfaces;
- required host services such as secure storage, audit sink, policy clock, scheduler, and optional network inspection.

A receiving project selects components by available services rather than by a hard-coded project name.

Example descriptor concepts:

```yaml
feature_id: swrlz.phoenix_firewall
version: 1.0.0
contract_version: 1
runtime_targets: [android, jvm]
required_services: [secure_storage, audit_sink, policy_clock]
optional_services: [network_inspector, notification_surface]
storage_namespace: phoenix_firewall
offline_mode: supported
remote_required: false
```

## Compatibility decision

```text
capsule packaged or imported
AND archive/checksum valid
AND runtime target supported
AND contract compatible
AND required host services mapped
AND required permissions/components accepted
AND migrations compatible
AND configured
AND available
AND trust-authorized
AND policy and Truth Firewall allowed
AND protocol compatible
= executable
```

Every failed gate must preserve its reason.

## Security rules

- No authority inferred from package inclusion, app identity, signature, source origin, or shared device identity.
- No silent permissions or platform components.
- No direct cross-capsule storage mutation.
- No silent local-to-remote fallback.
- No weakening of Truth Firewall objection, refusal, pause, or safer-alternative behavior.
- Optional feature failure must not crash unrelated host startup.
- Arbitrary runtime-downloaded executable code remains out of scope.

## Recommended implementation sequence

1. Accept the revised ADR and contract.
2. Define the capsule descriptor and host-service identifiers.
3. Define the integration-manifest schema.
4. Define canonical ZIP/SHA packaging and lineage rules.
5. Implement a tiny no-op portable capsule.
6. Attach it to two independent test hosts through separate adapters.
7. Verify extraction, transfer, compatibility failure reasons, lifecycle, and failure isolation.
8. Authorize `SOURCES/SHARED_FEATURES/` only after the reference flow is accepted.
9. Extract Phoenix Firewall in a separate checkpoint.
10. Reintegrate Phoenix into its originating project before attaching it elsewhere.

## Documentation and evidence gate

Each feature checkpoint must document origin, extraction commit, canonical ZIP/SHA, descriptor, adapters, integration manifests, permissions, migrations, compatibility results, build evidence, device/runtime evidence, rollback, and handoff state.

## Non-authorization

This guide does not authorize shared-feature directories, extraction, source modules, Gradle edits, app-lane changes, permissions, builds, workflows, releases, deployment, installation, dynamic plugins, or merge.