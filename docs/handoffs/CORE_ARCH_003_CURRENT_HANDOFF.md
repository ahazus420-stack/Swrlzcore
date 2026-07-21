# CORE-ARCH-003 Current Handoff

- **Status:** Architecture documented; implementation not authorized
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

## Confirmed architecture direction

- Shared capabilities are versioned compile-time integrator modules.
- Distinct app shells keep distinct package, signer, lifecycle, role, and authority boundaries.
- Each host packages integrators through an explicit composition manifest.
- Each integrator declares identity, version, contract compatibility, host requirements, permissions, storage, lifecycle, routing, failure behavior, lineage, and Truth Firewall impact.
- Each app shell supplies a least-authority host adapter and selects a role-specific profile.
- Package inclusion does not grant trust, enrollment, entitlement, or execution authority.
- Runtime-downloaded arbitrary executable plugins are outside the initial architecture.

## Phoenix Firewall first-integrator proposal

One shared Phoenix Firewall engine with profiles:

- `CORE_FULL`
- `KEYBOARD_RESTRICTED`
- `LAUNCHER_RESTRICTED`
- `CLIENT_SCOPED`
- `NODE_HOST_SCOPED`

Profiles are allowlisted authority surfaces, not divergent source forks.

## Documentation created or changed

- New ADR: `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- New normative contract: `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- New architecture guide: `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`
- Updated ADR index: `docs/architecture/adr/README.md`
- Updated CORE_BASE operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- New handoff: `docs/handoffs/CORE_ARCH_003_CURRENT_HANDOFF.md`

## Explicit non-implementation state

No source module, Gradle configuration, composition manifest, Phoenix Firewall implementation, app permission, app-lane source, build request, workflow, APK, release, deployment, or installation was created or changed by CORE-ARCH-003.

## Recommended next checkpoint

The next bounded checkpoint should be contract acceptance and implementation planning only. It should decide:

- whether ADR-0003 and Contract v1 are accepted;
- the first implementation-grade `IntegratorDescriptor` schema;
- the first host-capability identifier set;
- the composition-manifest format;
- whether a no-op reference integrator precedes Phoenix Firewall;
- module naming after a repository dependency audit;
- exact implementation, test, build, and documentation boundaries.

## Approval state

Approval already granted authorized architecture review and documentation only. It did not authorize implementation, app-lane changes, builds, workflows, merge, or release.

## Exact next approval phrase

`Approve CORE-GATE-004 — Accept ADR-0003 and the CORE Integrator Host-Capability Contract v1, then define the bounded implementation plan without modifying source code, app lanes, workflows, or builds`