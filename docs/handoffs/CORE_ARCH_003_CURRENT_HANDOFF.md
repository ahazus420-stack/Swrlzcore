# CORE-ARCH-003 Current Handoff

- **Status:** Project-agnostic portable feature-capsule architecture documented; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative branch:** `main`
- **Checkpoint branch:** `checkpoint/core-arch-003`

## Read first

1. `docs/governance/SWRLZ_CONSTITUTION.md`
2. `docs/architecture/SWRLZ_PLATFORM_MAP_V1.md`
3. `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`
4. `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`
5. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
6. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
7. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
8. `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
9. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
10. `docs/handoffs/CORE_BUILD_002A_CURRENT_HANDOFF.md`

## Confirmed architecture direction

- Portable feature capsules target runtime classes and required host services, not a closed list of named projects.
- Features may originate in any mature SWRLZ project.
- Existing applications attach capsules through thin adapters and local integration manifests without wholesale restructuring.
- A neutral `SOURCES/SHARED_FEATURES/` lane is proposed but not created.
- Canonical capsules may attach through same-repository module references or checksum-verified ZIP/SHA packages.
- Packaging, attachment, registration, or shared identity does not grant authority.
- Runtime-downloaded arbitrary executable code remains outside the initial architecture.

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

## Documentation created or changed

- ADR: `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- Contract: `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- Architecture guide: `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
- Existing-app extraction guide: `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
- ADR index: `docs/architecture/adr/README.md`
- CORE_BASE operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- This handoff.

## Explicit non-implementation state

No source module, shared-feature directory, extraction, adapter, Gradle configuration, application source, permission, workflow, build request, APK, release, deployment, or installation was created or changed by this checkpoint.

## Recommended next checkpoint

The next bounded checkpoint should accept the corrected ADR and contract and define a no-op reference capsule plan, including descriptor schema, service identifiers, integration manifest, ZIP/SHA naming, terminology validation, two independent test hosts, evidence requirements, and rollback boundaries.

## Approval state

Approval granted for CORE-ARCH-003B-LANGUAGE authorized documentation, extraction workflow design, terminology correction, and operating-skill updates only. It did not authorize implementation, app-lane changes, builds, workflows, merge, or release.

## Exact next approval phrase

`Approve CORE-GATE-004B — Accept the constitutionally aligned portable feature-capsule ADR, contract, ATTACH/EXTRACT/REINTEGRATE guide, and terminology gate, then define the bounded no-op reference implementation plan without creating source modules, changing apps, triggering builds, or merging`
