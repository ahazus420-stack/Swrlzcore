# CORE-ARCH-003A Current Handoff

- **Status:** Portable feature-capsule architecture documented; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative branch:** `main`
- **Checkpoint branch:** `checkpoint/core-arch-003`

## Read first

1. `docs/architecture/SWRLZ_PLATFORM_MAP_V1.md`
2. `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`
3. `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`
4. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
5. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
6. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
7. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
8. `docs/handoffs/CORE_BUILD_002A_CURRENT_HANDOFF.md`

## Corrected architecture direction

- Reusable capabilities are project-agnostic portable feature capsules.
- Capsules target runtime classes and required host services, not a closed list of named apps.
- A feature may originate in any project and later become a canonical shared package.
- The proposed neutral lane is `SOURCES/SHARED_FEATURES/<FEATURE>/`; it does not yet exist by authorization.
- Portable ZIP plus sibling SHA-256 supports transfer between separate repositories or canonical source ZIPs.
- Same-repository projects may use direct module dependencies.
- Each receiving project owns a small adapter and integration manifest.
- Portable core logic, runtime adapters, and optional presentation remain separate.
- The originating project should reintegrate the canonical shared package after extraction to prevent drift.
- Packaging never grants trust, entitlement, enrollment, or execution authority.
- Runtime-downloaded arbitrary executable plugins remain outside the initial architecture.

## Core compatibility rule

```text
feature requirements
    + host-provided services
    + receiving-project manifest
    + compatibility and policy gates
    = attached capability
```

Named projects such as CORE_BASE, CLIENT, SERVER, Launcher, Keyboard, and NODE_HOST are examples only.

## Phoenix Firewall example

Phoenix Firewall should eventually separate into:

- portable policy engine;
- Android adapter;
- JVM/server adapter;
- optional UI surfaces;
- descriptor, migrations, tests, documentation, ZIP, and SHA-256.

A receiving project maps services such as secure storage, audit sink, policy clock, scheduling, network inspection, and notifications. It should not require Phoenix Firewall source changes merely because a new project wants to attach it.

## Documentation created or changed

- Revised ADR: `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- Revised contract: `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- Revised architecture guide: `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
- ADR index: `docs/architecture/adr/README.md`
- Updated operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- Updated handoff: `docs/handoffs/CORE_ARCH_003_CURRENT_HANDOFF.md`

## Explicit non-implementation state

No `SOURCES/SHARED_FEATURES/` lane, source module, feature extraction, Gradle configuration, integration manifest, adapter, permission, app-lane source, build request, workflow, APK, release, deployment, or installation was created or changed.

## Recommended next checkpoint

The next bounded checkpoint should accept the revised ADR and contract, then define only:

- capsule descriptor schema;
- host-service identifier set;
- receiving-project integration-manifest schema;
- canonical ZIP/SHA naming and lineage rules;
- a tiny no-op portable reference capsule;
- two independent test-host criteria;
- exact implementation, testing, build, evidence, and documentation boundaries.

## Approval state

CORE-ARCH-003A authorized documentation-only correction. It did not authorize implementation, shared-feature directories, source extraction, app changes, builds, workflows, merge, or release.

## Exact next approval phrase

`Approve CORE-GATE-004A — Accept the project-agnostic portable feature-capsule ADR and contract, then define the bounded no-op reference implementation plan without creating source modules, shared-feature lanes, app changes, workflows, or builds`