# Portable Feature Capsule Architecture v1

- **Status:** Proposed architecture guide
- **Checkpoint:** CORE-ARCH-003 / CORE-ARCH-003A / CORE-ARCH-003B-LANGUAGE
- **Governing ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Existing-app guide:** `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`

## Purpose

This guide explains how SWRLZ can design a useful feature once, package it as a project-agnostic capsule, attach it to compatible existing or future projects, and extract already-crafted CLIENT or SERVER features without discarding prior work.

## Core rule

```text
feature declares requirements
        +
host exposes services
        +
thin adapter translates services
        +
integration manifest records attachment
        =
feature composed into a compatible project
```

Named projects are examples and evidence targets, not the complete compatibility boundary.

## Proposed neutral canonical lane

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

This lane is proposed only and is not created by the architecture checkpoint.

## Capsule contents

A portable capsule may contain:

- portable domain logic and deterministic state machines;
- stable service contracts;
- descriptor and compatibility metadata;
- runtime-neutral orchestration;
- optional Android, JVM, server, or other adapters;
- optional typed UI contributions;
- migrations, tests, documentation, lineage, ZIP, and SHA-256 evidence.

It should not directly depend on an Activity, IME service, launcher surface, host navigation, server framework, project-local database, or package name unless isolated behind a runtime or receiving-project adapter.

## Host-service model

A capsule asks whether services exist, not whether it is inside a particular named project.

Typical services include:

- secure storage;
- audit output;
- policy clock;
- scheduler or background execution;
- route inspection;
- notifications;
- diagnostics;
- local, LAN, or remote networking;
- identity and trust references;
- Android-role or server-runtime facilities.

The host exposes only the services and authority approved for that integration.

## Attachment methods

### Same repository

A project may reference a shared module directly through its build graph.

### Separate repository or canonical source package

A project may import a checksum-verified source ZIP and attach it through a local adapter and integration manifest.

Both methods must preserve capsule identity, version, source lineage, checksum, contract compatibility, and rollback evidence.

## Mature application integration

Established CLIENT, SERVER, NODE_HOST, Core, Keyboard, Launcher, or future projects do not need wholesale redesign. They retain their accepted identities, workflows, protocols, storage, lifecycle, and contracts. Attachment adds only:

- the capsule package or module reference;
- a thin compatibility adapter;
- an integration manifest;
- explicit permission, component, storage, route, and lifecycle changes;
- verification and rollback evidence.

## Existing-feature extraction

```text
project-local feature
        ↓ dependency and authority audit
portable core + host-owned boundaries
        ↓ service contracts
canonical capsule package
        ↓ origin adapter
origin project reintegrates capsule
        ↓
other compatible projects attach capsule
```

The origin project must become the first verified host of the extracted canonical capsule, or follow a clearly temporary bounded delegation path.

## ATTACH, EXTRACT, and REINTEGRATE

- **ATTACH** adds a canonical capsule to an established compatible project.
- **EXTRACT** separates portable behavior from an established project-local feature.
- **REINTEGRATE** makes the origin project reference and compose the canonical capsule.

Detailed procedures are defined in `PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`.

## Migration strategies

- **Clean extraction:** direct separation where boundaries already exist.
- **Strangler extraction:** gradual movement from mature code while existing entry points delegate to the capsule.
- **Wrapper-first transition:** establish a stable interface first, then move implementation behind it through later checkpoints.

Transitional states must remain documented, bounded, and recoverable.

## Constitutional relationship language

Use accurate verbs:

- projects **attach**, **import**, **reference**, or **compose** capsules;
- hosts **expose** services and **host** lifecycle;
- adapters **translate** host services;
- registries **register** availability;
- runtimes **invoke** behavior;
- origin projects **reintegrate** canonical capsules;
- packages and records **preserve** lineage.

Do not use `consume` for reusable software composition. The Constitution reserves it for genuinely depleting or irreversible operations.

## Execution decision

```text
attached or packaged
AND descriptor compatible
AND required services exposed
AND integration manifest valid
AND entitled
AND configured
AND available
AND trust-authorized
AND policy and Truth Firewall allowed
AND protocol compatible
= executable
```

Every failure preserves a reason code.

## Security model

- No initial arbitrary runtime executable loading.
- No authority inferred from attachment, package inclusion, common signature, or shared device identity.
- No silent permission or component additions.
- No direct capsule-to-capsule storage mutation.
- No silent local-to-remote fallback.
- No weakening of Truth Firewall objection, refusal, pause, or safer-alternative behavior.
- Optional capsule failure must not crash unrelated host startup.

## Recommended implementation sequence

1. accept ADR, contract, extraction guide, and terminology rules;
2. audit existing repository and project dependency graphs;
3. define descriptor and integration-manifest schemas;
4. define host-service contracts;
5. implement a tiny no-op reference capsule;
6. attach it to two independent test hosts through distinct adapters;
7. verify compatibility, lifecycle, failure isolation, ZIP/SHA portability, and terminology checks;
8. select one mature CLIENT or SERVER feature for extraction planning;
9. perform EXTRACT and REINTEGRATE in separate approved checkpoints;
10. attach the resulting capsule to another compatible project.

## Documentation and evidence gate

Every implementation checkpoint updates contracts, manifests, dependency graphs, permissions, checksums, lineage, migration state, behavioral-equivalence evidence, build/device evidence, handoffs, and skills. One successful compile is not sufficient.

## Non-authorization

This guide does not authorize source modules, extraction, shared-feature directories, Gradle edits, app changes, permissions, builds, workflows, merge, release, deployment, or installation.
