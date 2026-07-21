# CORE-ARCH-003 Current Handoff

- **Status:** Project-agnostic portable feature-capsule architecture and Contract v1 accepted; no-op reference implementation planned; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative branch:** `main`
- **Checkpoint branch:** `checkpoint/core-arch-003`
- **Latest planning checkpoint:** `CORE-PLAN-005`

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
10. `docs/implementation/CORE_PLAN_005_NOOP_REFERENCE_CAPSULE_IMPLEMENTATION_PLAN.md`
11. `docs/contracts/NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1.md`
12. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
13. `docs/handoffs/CORE_BUILD_002A_CURRENT_HANDOFF.md`

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

## CORE-PLAN-005 reference capsule plan

The planned reference capsule is:

```text
capsule ID: swrlz.reference.noop
version: 0.1.0
contract version: 1
network behavior: none
permissions: none
privileged operations: none
```

Planned required services:

- `swrlz.audit_sink.v1`
- `swrlz.clock.v1`

Planned optional service:

- `swrlz.ephemeral_state.v1`

The reference implementation must attach one canonical capsule to two independent hosts:

1. an Android/JVM reference host;
2. a plain JVM reference host.

Both must use distinct adapters and integration manifests while referencing the same canonical ZIP/SHA lineage.

## Planned evidence boundary

`NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1` requires evidence for:

- canonical source, ZIP, and SHA-256;
- descriptor and integration manifests;
- independent service mappings;
- compatibility and typed reason codes;
- lifecycle and deterministic invocation;
- failure isolation;
- no permissions, components, routes, or authority expansion;
- constitutional terminology compliance;
- independent build/runtime evidence for both hosts;
- rollback and detach behavior.

No-op evidence must not be represented as Phoenix Firewall, mature-feature extraction, production security, release, deployment, or universal-host compatibility evidence.

## Documentation created or changed

- Accepted ADR: `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- Accepted contract: `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- Architecture guide: `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
- Existing-app extraction guide: `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
- No-op implementation plan: `docs/implementation/CORE_PLAN_005_NOOP_REFERENCE_CAPSULE_IMPLEMENTATION_PLAN.md`
- No-op evidence contract: `docs/contracts/NOOP_REFERENCE_CAPSULE_EVIDENCE_CONTRACT_V1.md`
- ADR index: `docs/architecture/adr/README.md`
- CORE_BASE operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- Constitution Guardian skill and agent descriptor.
- This handoff.

## Explicit non-implementation state

No source module, shared-feature directory, capsule implementation, reference host, adapter, integration manifest, Gradle configuration, application source, permission, workflow, build request, APK, release, deployment, installation, or merge was created or performed by CORE-PLAN-005.

## Approval state

Approval granted for `CORE-PLAN-005` authorized the bounded no-op reference implementation plan and evidence contract only. It did not authorize implementation, source creation, app-lane changes, builds, workflows, merge, release, deployment, or installation.

## Current gate

The next bounded checkpoint may authorize implementation of only the no-op capsule, stable descriptor/service contracts, two independent reference hosts and adapters, deterministic tests, canonical ZIP/SHA package, and required evidence. Mature CLIENT/SERVER attachment or extraction remains excluded.

## Exact next approval phrase

`Approve CORE-IMP-006 — Implement the bounded SWRLZ no-op reference capsule, two independent reference hosts and adapters, deterministic tests, and canonical ZIP/SHA evidence without modifying mature app lanes, implementing Phoenix Firewall, merging, releasing, deploying, or installing`
