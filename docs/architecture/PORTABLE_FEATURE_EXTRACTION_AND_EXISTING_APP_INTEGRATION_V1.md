# Portable Feature Extraction and Existing-App Integration v1

- **Status:** Proposed architecture guide
- **Checkpoint:** CORE-ARCH-003B-LANGUAGE
- **Governing ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Constitutional basis:** `docs/governance/SWRLZ_CONSTITUTION.md`

## Purpose

This guide defines how established SWRLZ applications attach portable feature capsules without discarding prior work, and how existing CLIENT, SERVER, NODE_HOST, Core, Keyboard, Launcher, or future-project features are extracted into canonical reusable modules without creating duplicate implementations.

## Constitutional relationship language

Use relationship verbs that describe the real technical relationship:

- a project **attaches**, **imports**, **references**, or **composes** a capsule;
- a host **exposes** services and **hosts** lifecycle;
- an adapter **translates** existing services into capsule contracts;
- a registry **registers** capsule availability;
- a runtime **invokes** capsule behavior;
- an origin project **reintegrates** the accepted capsule;
- source packages **preserve** lineage.

The term `consume` is reserved for operations that genuinely advance, exhaust, deplete, spend, remove, or irreversibly transform a resource. Reusable software composition does not deplete the module.

## Operating modes

### ATTACH

Attach an accepted canonical capsule to an already-built application while preserving the application's package identity, signer lineage, lifecycle, storage, workflows, protocols, and accepted contracts.

Required sequence:

1. verify capsule ZIP integrity and SHA-256;
2. inspect descriptor, contract version, runtime targets, and lineage;
3. map required host services to existing application services;
4. create a thin receiving-project adapter;
5. create a local integration manifest;
6. declare permissions, components, storage, routing, and lifecycle changes explicitly;
7. validate compatibility before invocation;
8. build and verify the receiving application in a separate approved checkpoint;
9. record integration evidence and rollback instructions.

### EXTRACT

Convert an existing project-local feature into a portable capsule.

Required sequence:

1. identify origin project, checkpoint, paths, and accepted behavior;
2. audit dependencies, authority, identity, storage, protocols, UI, lifecycle, and routes;
3. separate portable domain logic from host-owned implementation;
4. define required and optional host-service contracts;
5. move portable behavior behind those contracts;
6. create the originating-project adapter;
7. preserve original lineage and migration evidence;
8. package the canonical source ZIP and sibling SHA-256;
9. proceed to REINTEGRATE before declaring extraction complete.

### REINTEGRATE

Make the originating application reference and compose the accepted canonical capsule rather than retaining a drifting second implementation.

Required sequence:

1. replace origin-local entry points with adapter-backed capsule invocation;
2. preserve host-specific UI, lifecycle bridges, and storage adapters where appropriate;
3. migrate existing state explicitly;
4. verify behavioral equivalence or document every intentional difference;
5. preserve rollback to the prior origin checkpoint;
6. retire superseded source through explicit lineage rather than deletion.

## Migration strategies

### Clean extraction

Use when feature boundaries are already modular. Separate host dependencies, define contracts, move portable logic, and reintegrate the capsule.

### Strangler extraction

Preferred for mature CLIENT and SERVER features. Extract bounded components gradually while old entry points delegate to the capsule until the project-local implementation can be retired safely.

### Wrapper-first transition

Use when immediate extraction is too risky. Place a stable capsule-compatible interface around existing behavior, then move implementation behind that interface through later bounded checkpoints. This is transitional and must not become a permanent duplicate canonical implementation.

## Required extraction manifest

An extraction manifest records:

- feature identity and proposed capsule version;
- origin project, checkpoint, commit, and paths;
- accepted behavior and governing contracts;
- portable candidates;
- host-owned dependencies;
- identity, trust, authorization, and Truth Firewall boundaries;
- storage schemas and migrations;
- protocol and route dependencies;
- permissions and lifecycle assumptions;
- planned canonical lane, ZIP, and checksum names;
- reintegration and rollback plan.

## Required integration manifest

A receiving-project integration manifest records:

- capsule ID, version, ZIP, SHA-256, and lineage;
- receiving project identity and checkpoint;
- adapter path;
- mapped required and optional host services;
- enabled and excluded components;
- storage namespace and migration state;
- permissions and components;
- local, LAN, and remote route behavior;
- lifecycle attachment points;
- compatibility result and reason codes;
- build, device, rollback, and retirement evidence.

## Behavioral-equivalence gate

The originating application must demonstrate, where applicable:

- equivalent accepted inputs, outputs, and policy decisions;
- preserved identity, trust, and authorization semantics;
- preserved Truth Firewall objection, refusal, pause, and safer-alternative behavior;
- preserved offline operation;
- preserved local, LAN, and remote distinctions;
- valid storage migration and recovery;
- protocol compatibility;
- no silent permission or authority expansion;
- no silent remote fallback.

Intentional changes must be classified, approved, documented, and evidenced.

## Canonical ownership after extraction

After acceptance, the neutral shared-feature lane becomes the canonical implementation. The origin and receiving projects retain only their adapters, integration manifests, host-specific presentation, lifecycle bridges, and approved migration code.

No extraction is complete while two undocumented canonical implementations remain active.

## Proposed migration toolkit

A later implementation checkpoint may create templates and validators for:

- extraction manifests;
- capsule descriptors;
- host-service contracts;
- integration manifests;
- dependency and authority audits;
- storage migration reviews;
- behavioral-equivalence evidence;
- ZIP and SHA-256 packaging;
- terminology validation against the SWRLZ Constitution.

This guide does not authorize scripts, source extraction, Gradle changes, app modifications, builds, workflows, merges, releases, deployments, or installations.
