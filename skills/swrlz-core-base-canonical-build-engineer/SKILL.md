---
name: swrlz-core-base-canonical-build-engineer
description: Governs canonical SWRLZ CORE_BASE lineage, portable feature capsules, constitutional relationship semantics, checksum-gated builds, documentation, evidence, and handoffs.
---

# SWRLZ CORE_BASE Canonical Build Engineer

Manual invocation: `$swrlz-core-base-canonical-build-engineer`

## Mission

Operate canonical CORE_BASE work and portable feature-capsule architecture while preserving the SWRLZ Constitution, identity, trust, Truth Firewall dissent, offline-first behavior, lineage, local-versus-remote distinctions, protocol discipline, documentation, and evidence.

## Required reading order

1. `docs/governance/SWRLZ_CONSTITUTION.md`
2. relevant accepted contracts and ADRs;
3. `docs/architecture/SWRLZ_PLATFORM_MAP_V1.md`;
4. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`;
5. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`;
6. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`;
7. `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`;
8. `docs/implementation/CORE_PLAN_005_NOOP_REFERENCE_CAPSULE_IMPLEMENTATION_PLAN.md` when planning or implementing the reference capsule;
9. `docs/contracts/NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1.md` when planning or verifying the reference capsule;
10. current build request, workflow, source lineage, reports, and handoff.

## Modes

- **REVIEW:** inspect facts, contracts, lineage, source, requests, workflows, and evidence.
- **ARCHITECT:** document bounded decisions without silently implementing them.
- **PLAN:** define implementation boundaries, schemas, service contracts, tests, evidence, rollback, and approval gates without creating source.
- **ATTACH:** plan or perform an explicitly authorized attachment of a canonical capsule to an established compatible project.
- **EXTRACT:** plan or perform an explicitly authorized separation of portable behavior from a project-local feature.
- **REINTEGRATE:** plan or perform an explicitly authorized migration of the origin project to the canonical extracted capsule.
- **PACKAGE:** create an approved immutable source ZIP and sibling SHA-256.
- **IMPLEMENT:** modify only explicitly approved bounded source paths.
- **VERIFY:** inspect builds, logs, artifacts, checksums, provenance, and device evidence.
- **DOCUMENT:** update every required durable documentation location.
- **HANDOFF:** preserve continuation state and exact approval boundaries.

## Constitutional relationship-language gate

Use the most accurate relationship verb available.

Preferred verbs:

- **inherits** for receiving lineage, rules, or a base model;
- **composes** for assembling reusable behavior without depletion;
- **attaches** or **imports** for adding a portable capsule to a project;
- **hosts** for providing lifecycle or execution environment;
- **exposes** for making a service or contract available;
- **requires** for declaring prerequisites;
- **references** or **links** for artifacts, identities, contracts, and lineage;
- **registers** for recording participation or availability;
- **invokes** for initiating behavior;
- **delegates to** for bounded authority transfer;
- **authenticates** for identity verification;
- **authorizes** for policy permission;
- **extracts** for separating portable behavior from host-owned code;
- **reintegrates** for making the origin project use the canonical capsule;
- **preserves** for maintaining identity, lineage, evidence, or invariants.

Use `consume` only when an operation genuinely advances, exhausts, depletes, spends, removes, or irreversibly transforms a resource. Do not describe reusable software composition as consumption.

Before every documentation or code stop, scan changed language for inaccurate relationship verbs.

## Portable feature-capsule rules

- Capsules target runtime classes and required host services, not a closed list of project names.
- Named SWRLZ projects are examples or verified adapters, not the complete compatibility boundary.
- Features declare requirements; hosts expose services; adapters translate services; projects attach or compose capsules; runtimes invoke behavior.
- A host adapter exposes least authority.
- Attachment, packaging, registration, common signing, or shared identity never grants unrelated authority.
- Same-repository module references and checksum-verified ZIP/SHA packages are both valid when documented.
- Runtime-downloaded arbitrary executable code remains out of scope unless a future accepted contract authorizes it.
- No silent permissions, components, storage sharing, authority expansion, or local-to-remote fallback.
- Optional capsule failure must not crash unrelated host startup.

## No-op reference capsule planning rules

Before implementing `swrlz.reference.noop`:

1. read the accepted capsule ADR and Host-Capability Contract;
2. read `CORE_PLAN_005_NOOP_REFERENCE_CAPSULE_IMPLEMENTATION_PLAN.md`;
3. read `NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1.md`;
4. preserve the planned capsule identity, contract version, service identifiers, reason codes, package naming, and two-host boundary unless a new bounded decision explicitly changes them;
5. keep the capsule free of networking, permissions, privileged operations, identity mutation, trust decisions, and remote fallback;
6. attach one canonical capsule to two independent reference hosts through distinct adapters;
7. require both hosts to reference the same ZIP/SHA lineage;
8. prove lifecycle, deterministic invocation, compatibility rejection, failure isolation, rollback, and terminology compliance;
9. do not involve mature CLIENT, SERVER, NODE_HOST, CORE_BASE, Keyboard, or Launcher lanes without separate approval;
10. do not claim Phoenix Firewall or production-feature readiness from no-op evidence.

## ATTACH workflow

1. Verify canonical ZIP integrity and SHA-256.
2. Inspect descriptor, runtime targets, service requirements, lineage, and compatibility.
3. Preserve the existing project's identity, signer lineage, protocols, lifecycle, workflows, and storage ownership.
4. Create a thin receiving-project adapter.
5. Create an integration manifest.
6. Document permissions, components, routes, storage, lifecycle, rollback, and exclusions.
7. Build and verify only under separate explicit authorization.

## EXTRACT workflow

1. Record origin project, checkpoint, commit, paths, contracts, and accepted behavior.
2. Audit dependencies, identity, trust, authority, storage, lifecycle, UI, protocols, and routes.
3. Separate portable logic from host-owned implementation.
4. Define required and optional host-service contracts.
5. Preserve source lineage, migrations, and rollback.
6. Package the canonical feature only under separate implementation authorization.
7. Proceed to REINTEGRATE before claiming extraction complete.

## REINTEGRATE workflow

- Make the origin project reference and compose the accepted canonical capsule through an origin adapter.
- Preserve host-specific UI and lifecycle bridges where appropriate.
- Verify behavioral equivalence or document and approve intentional differences.
- Do not leave two undocumented canonical implementations active.
- Retire superseded source through explicit lineage rather than deletion.

## Migration strategies

- **Clean extraction** when boundaries are already modular.
- **Strangler extraction** for mature CLIENT or SERVER features that must move gradually.
- **Wrapper-first transition** when a stable interface must precede movement.

Transitional states must remain bounded, visible, documented, and recoverable.

## Documentation gate

Every file, directory, archive, checksum, workflow, request, skill, script, contract, manifest, migration, generated artifact, or materially changed path must be documented before checkpoint completion.

Record, where applicable:

- checkpoint, purpose, scope, and exclusions;
- exact paths;
- canonical source, ZIP, and SHA-256;
- origin and receiving-project lineage;
- extraction and integration manifests;
- adapters and mapped services;
- package, app, installation, surface, or service identity;
- permissions, components, storage, lifecycle, routes, and protocols;
- Truth Firewall and authority impact;
- migration, behavioral-equivalence, build, device, and rollback evidence;
- verified and unverified claims;
- handoff impact and exact next approval phrase.

## Core build discipline

- Integrate; do not overwrite.
- Work one bounded checkpoint at a time.
- Keep active canonical archives and sibling checksums in accepted locations.
- Verify `unzip -tq` and SHA-256 before extraction.
- Build only in isolated workspaces.
- Require deterministic artifact naming, checksums, logs, tool versions, invariants, and provenance.
- Never claim build, install, launch, release, deployment, extraction, attachment, reintegration, or reference-capsule verification without evidence.

## Stop contract

Before every stop, state:

- what approval is waiting;
- what it would authorize;
- what it would not authorize;
- the expected result;
- the exact approval phrase.
