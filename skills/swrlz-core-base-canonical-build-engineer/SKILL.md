---
name: swrlz-core-base-canonical-build-engineer
description: Governs canonical SWRLZ CORE_BASE builds, portable feature capsules, checksum lineage, repository documentation, evidence, and handoffs.
---

# SWRLZ CORE_BASE Canonical Build Engineer

Manual invocation: `$swrlz-core-base-canonical-build-engineer`

## Mission

Operate canonical CORE_BASE source and builds while governing project-agnostic portable feature capsules that can originate in any SWRLZ project and attach to any compatible current or future host through narrow service adapters.

## Scope

In scope:

- `SOURCES/CORE_BASE/`;
- CORE_BASE build requests and workflows;
- architecture, lineage, evidence, and handoffs;
- portable feature-capsule ADRs, contracts, package rules, adapters, manifests, and documentation;
- future `SOURCES/SHARED_FEATURES/` planning when explicitly authorized.

Out of scope unless separately approved: CLIENT, SERVER, NODE_HOST, Keyboard, Launcher or other app implementation; shared-feature directories; source extraction; Gradle changes; builds; release; deployment; installation; merge; signing changes.

## Modes

- **REVIEW** — inspect repository, lineage, documentation, requests, workflows, archives, checksums, and feature origins.
- **PACKAGE** — create approved immutable ZIP/SHA packages.
- **ARCHITECT** — define project-agnostic feature contracts and service boundaries without implementing them.
- **EXTRACT** — separate portable logic from an originating project only when explicitly authorized.
- **INTEGRATE** — attach an accepted capsule to a receiving project through a bounded adapter and manifest only when explicitly authorized.
- **VERIFY** — inspect compatibility, builds, logs, checksums, runtime evidence, and provenance.
- **DOCUMENT** — update all durable documentation for every repository change.
- **HANDOFF** — produce continuation instructions that do not depend on verbal reconstruction.

## Mandatory process

1. Read the current build and architecture handoffs.
2. Read the governing ADRs and contracts before proposing implementation.
3. Improve accepted systems rather than create duplicate mechanisms.
4. Preserve canonical source, checksums, lineage, offline-first behavior, Truth Firewall behavior, local-versus-remote distinctions, and protocol discipline.
5. Never claim build, installation, launch, extraction, canonicalization, or integration without evidence.
6. Integrate; do not overwrite unrelated lanes or authoritative history.

## Portable feature-capsule governance

Before proposing a reusable feature, read:

1. `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`;
2. `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`;
3. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`;
4. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`;
5. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`.

Apply these rules:

- Features declare requirements; hosts declare provided services.
- Capsules target runtime classes and host-service contracts, not a closed list of named applications.
- CORE_BASE, CLIENT, SERVER, NODE_HOST, Keyboard, Launcher, and future projects are examples, not required descriptor entries.
- A capsule may originate in any project.
- Once extracted and accepted, its canonical reusable implementation should live in an authorized neutral shared-feature lane rather than remain owned by the originating app.
- The originating project should reintegrate the canonical capsule to prevent source drift.
- ZIP plus sibling SHA-256 is the initial cross-repository transport format.
- Same-repository projects may use direct module integration.
- Every receiving project supplies its own narrow adapter and integration manifest.
- Portable core logic, runtime adapters, and optional host presentation must remain separate.
- Package inclusion never grants trust, entitlement, enrollment, permissions, or execution authority.
- Required permissions and platform components must be declared and reviewed.
- Missing host services must produce explicit incompatibility reasons.
- Optional feature failure must not crash unrelated host startup.
- No silent storage sharing, authority escalation, permission addition, or local-to-remote fallback.
- Runtime-downloaded arbitrary executable plugins remain outside the initial model.

## Capsule descriptor requirements

A capsule descriptor must record, where applicable:

- feature ID and semantic version;
- contract version;
- source lineage, ZIP name, and SHA-256;
- runtime targets;
- required and optional host services;
- permissions and platform components;
- storage namespace and migration version;
- lifecycle and restart behavior;
- local, LAN, and remote behavior;
- protocol compatibility;
- failure policy;
- Truth Firewall and audit impact;
- portable-core, runtime-adapter, and optional-UI components.

It must not require edits whenever an unanticipated compatible project becomes a consumer.

## Extraction gate

Before extracting a feature from an existing project:

1. identify the originating repository path and commit;
2. classify portable logic, runtime adapters, and project-specific presentation;
3. define the capsule descriptor and migration boundary;
4. define canonical ZIP/SHA names and destination;
5. define reintegration into the originating project;
6. define rollback and retirement lineage;
7. obtain explicit source-modification authorization.

An extraction is incomplete until the originating project no longer maintains an undocumented divergent implementation.

## Receiving-project integration gate

Before attaching a capsule:

1. verify ZIP integrity and SHA-256 or exact module lineage;
2. validate runtime and contract compatibility;
3. map required host services;
4. declare optional services and enabled components;
5. review permissions and platform components;
6. assign storage namespace and migration plan;
7. preserve route distinctions and policy gates;
8. create the receiving-project integration manifest;
9. produce build and runtime evidence;
10. document the integration and rollback path.

## Documentation gate

Every file, directory, archive, checksum, workflow, request, skill, script, contract, generated artifact, adapter, integration manifest, migration, or materially changed path MUST be documented before completion.

Documentation must record:

- checkpoint, purpose, scope, exclusions, and approvals;
- exact repository paths;
- source origin and canonical lineage;
- ZIP/SHA identity;
- feature descriptor and host-service requirements;
- receiving-project adapter and integration manifest;
- permissions, components, storage, migrations, and routes;
- verified and unverified claims;
- evidence and recovery locations;
- handoff impact and cross-links.

## Documentation placement

- `docs/architecture/` — structural guides;
- `docs/architecture/adr/` — durable decisions;
- `docs/contracts/` — normative contracts;
- `docs/build/` — package, checksum, workflow, and artifact procedures;
- `docs/handoffs/` — continuation state;
- `reports/` — implementation and verification evidence;
- `SOURCES/CORE_BASE/OLD_PATCHES/` — CORE_BASE retirement lineage;
- future authorized `SOURCES/SHARED_FEATURES/<FEATURE>/OLD_PATCHES/` — feature retirement lineage;
- `skills/` — reusable operating procedures.

## Completion criteria

A checkpoint is not complete unless implementation or architecture deliverables, checksums where applicable, lineage, compatibility evidence, documentation, current handoff instructions, and skill guidance are present and no claim exceeds the evidence.

## Stop contract

Before every stop, state:

- what approval is waiting;
- what it authorizes;
- what it does not authorize;
- the expected result;
- the exact approval phrase.