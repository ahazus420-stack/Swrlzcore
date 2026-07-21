# CORE-ARCH-003 Current Handoff

- **Status:** Project-agnostic portable feature-capsule architecture and Contract v1 accepted; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative branch:** `main`
- **Checkpoint branch:** `checkpoint/core-arch-003`
- **Latest acceptance checkpoint:** `CORE-GATE-004B-CONSTITUTION-SKILL`

## Read first

1. `docs/governance/SWRLZ_CONSTITUTION.md`
2. `skills/swrlz-constitution-guardian/SKILL.md`
3. `docs/architecture/SWRLZ_PLATFORM_MAP_V1.md`
4. `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`
5. `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`
6. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
7. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
8. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
9. `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
10. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
11. `docs/handoffs/CORE_BUILD_002A_CURRENT_HANDOFF.md`

## Accepted architecture direction

- Portable feature capsules target runtime classes and required host services, not a closed list of named projects.
- Features may originate in any mature SWRLZ project.
- Existing applications attach capsules through thin adapters and local integration manifests without wholesale restructuring.
- A neutral `SOURCES/SHARED_FEATURES/` lane remains proposed but is not created.
- Canonical capsules may attach through same-repository module references or checksum-verified ZIP/SHA packages.
- Packaging, attachment, registration, or shared identity does not grant authority.
- Runtime-downloaded arbitrary executable code remains outside the initial architecture.
- ADR-0003 and `CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1` are accepted on this branch.

## Existing-project workflows

- **ATTACH:** add a canonical capsule to an established compatible project.
- **EXTRACT:** separate portable behavior from a project-local feature after dependency, authority, storage, lifecycle, route, and protocol audit.
- **REINTEGRATE:** make the origin project reference and compose the accepted canonical capsule.

Supported migration strategies:

- clean extraction;
- strangler extraction;
- wrapper-first transition.

Extraction is incomplete while two undocumented canonical implementations remain active.

## Constitutional terminology gate

Use accurate relationship verbs:

- projects **attach**, **import**, **reference**, or **compose** capsules;
- hosts **expose** services and **host** lifecycle;
- adapters **translate** services;
- registries **register** availability;
- runtimes **invoke** behavior;
- origin projects **reintegrate** canonical capsules;
- packages **preserve** lineage.

`Consume` is reserved for genuinely depleting or irreversible operations and must not describe reusable software composition.

## Constitution Guardian skill

Repository paths:

- `skills/swrlz-constitution-guardian/SKILL.md`
- `skills/swrlz-constitution-guardian/agents/openai.yaml`

The skill governs constitutional authority order, relationship semantics, Truth Firewall preservation, identity and authority separation, route transparency, lineage, evidence, bounded approvals, portable feature-capsule workflows, documentation gates, and stop requirements across future SWRLZ chats.

## Documentation created or changed

- Accepted ADR: `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- Accepted contract: `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- Architecture guide: `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
- Existing-app extraction guide: `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
- ADR index: `docs/architecture/adr/README.md`
- CORE_BASE operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- Constitution Guardian skill and agent descriptor.
- This handoff.

## Explicit non-implementation state

No source module, shared-feature directory, extraction, adapter, Gradle configuration, application source, permission, workflow, build request, APK, release, deployment, installation, or merge was created or performed by this checkpoint.

## Approval state

Approval granted for `CORE-GATE-004B-CONSTITUTION-SKILL` authorized adding the Constitution Guardian skill and accepting the constitutionally aligned portable feature-capsule architecture. It did not authorize implementation, app-lane changes, builds, workflows, merge, release, deployment, or installation.

## Current gate

The next bounded checkpoint should define the no-op reference capsule implementation plan: descriptor schema, host-service identifiers, integration manifest, ZIP/SHA naming, two independent test hosts, terminology validation, evidence requirements, and rollback boundaries.

## Exact next approval phrase

`Approve CORE-PLAN-005 — Define the bounded no-op portable feature-capsule reference implementation plan and evidence contract without creating source modules, modifying app lanes, triggering builds, or merging`
