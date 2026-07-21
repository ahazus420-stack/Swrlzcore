# CORE Integrator Host-Capability Contract v1

- **Status:** Accepted
- **Version:** 1
- **Checkpoint:** CORE-ARCH-003 / CORE-ARCH-003A / CORE-ARCH-003B-LANGUAGE / CORE-GATE-004B-CONSTITUTION-SKILL
- **Related ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Extraction guide:** `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
- **Constitutional authority:** `docs/governance/SWRLZ_CONSTITUTION.md`
- **Acceptance approval:** `Approve CORE-GATE-004B-CONSTITUTION-SKILL — Add the SWRLZ Constitution Guardian skill to the repository and accept the constitutionally aligned portable feature-capsule architecture without creating source modules, changing app code, triggering builds, or merging`

## 1. Purpose

This contract defines how reusable SWRLZ capability modules attach to compatible projects without copying whole applications, merging identities, inheriting unrestricted authority, or requiring every present or future host project to be predefined.

## 2. Definitions

- **Portable feature capsule:** a versioned reusable capability package with portable logic, declared service requirements, lineage, documentation, and optional runtime adapters.
- **Host:** any compatible application or service that exposes required services and hosts capsule lifecycle.
- **Host adapter:** the narrow bridge that translates existing host services into capsule contracts.
- **Integration manifest:** the receiving project's authoritative declaration of the capsule, adapter, mapped services, permissions, storage, routing, and lifecycle.
- **Extraction manifest:** the authoritative record used to separate an existing project-local feature into portable and host-owned parts.
- **Origin project:** the project from which an existing feature is extracted.
- **ATTACH:** add a canonical capsule to an established compatible project.
- **EXTRACT:** separate portable feature behavior from an established project.
- **REINTEGRATE:** make the origin project reference and compose the accepted canonical capsule.

## 3. Required capsule descriptor

Every capsule MUST declare:

- stable capsule or integrator identity;
- semantic version;
- contract version;
- source lineage and checksum identity;
- supported runtime classes rather than a closed list of project names;
- required and optional host services;
- required platform permissions and components;
- storage namespace and migration version;
- local, LAN, and remote behavior;
- lifecycle requirements;
- failure policy;
- protocol and schema compatibility range;
- Truth Firewall and audit impact;
- optional configuration profiles.

Named SWRLZ projects MAY appear as examples or tested integrations, but MUST NOT define the complete compatibility boundary.

## 4. Host-service declaration

A host MUST expose an explicit service set. Services MAY include secure storage, audit output, clocks, schedulers, background execution, notifications, diagnostics, IPC, local networking, LAN networking, remote networking, policy evaluation, identity references, route inspection, Android role services, or server runtime services.

A capsule MUST validate required services before initialization. It MUST NOT infer authority or availability from package name, project name, signature matching, shared device identity, reflection, or directory placement.

## 5. Composition and attachment rules

1. A project MUST attach or compose a capsule through an explicit integration manifest.
2. Build-time composition and runtime authorization remain separate decisions.
3. A host MUST reject initialization when contract, protocol, runtime, service, permission, storage, or lifecycle requirements are incompatible.
4. Rejection MUST preserve a specific reason code.
5. Capsules MUST NOT add permissions, components, routes, or background behavior silently.
6. A project MUST compose or reference the canonical shared module rather than copy and independently mutate its implementation.
7. Same-repository module references and portable ZIP/SHA imports are both valid attachment mechanisms when documented.

## 6. Constitutional relationship semantics

Architecture, contracts, code comments, manifests, reports, and handoffs MUST use relationship verbs that represent the actual behavior.

Preferred terms include:

- **composes** for assembling reusable behavior;
- **attaches** or **imports** for adding a capsule to a project;
- **hosts** for providing lifecycle or execution environment;
- **exposes** for making host services available;
- **requires** for declaring prerequisites;
- **references** or **links** for lineage and artifact relationships;
- **registers** for recording availability;
- **invokes** for initiating executable behavior;
- **extracts** for separating portable behavior;
- **reintegrates** for making the origin project use the canonical capsule;
- **preserves** for maintaining lineage, identity, evidence, or invariants.

The term `consume` MUST be reserved for operations that genuinely advance, exhaust, deplete, spend, remove, or irreversibly transform a resource. Reusable module composition is not depletion.

## 7. Lifecycle

The initial lifecycle is:

```text
inspect descriptor
→ validate compatibility
→ initialize
→ start or invoke
→ pause/resume as applicable
→ stop
→ migrate, detach, or retire explicitly
```

Initialization MUST be idempotent or return an explicit conflict. Stop MUST release owned resources. Process death and restart behavior MUST be documented.

## 8. Storage and migration

- Storage MUST be namespaced by capsule identity and host installation identity where applicable.
- Cross-project storage access is prohibited unless an accepted IPC, provider, or server contract authorizes it.
- Schema changes require versioned migrations.
- Failed migration MUST fail closed for protected data and preserve recovery evidence.
- Removal or retirement MUST preserve lineage and rollback instructions.

## 9. Trust and authority

- Packaging, attachment, or registration does not grant trust, enrollment, entitlement, or execution authority.
- Shared identity, signature, device, repository, or lineage does not grant unrelated authority.
- Host adapters MUST expose least authority.
- Entitlement cannot override safety, privacy, identity, trust, protocol, or Truth Firewall requirements.

## 10. Routing and offline behavior

- Offline-first operation is mandatory where the feature can operate locally.
- Route classes MUST remain explicit: local, LAN, or remote.
- No silent local-to-remote fallback is allowed.
- Remote dependencies, cost implications, trust requirements, and failure behavior MUST be declared.
- Network failure MUST NOT create an obedience-only or policy-bypass mode.

## 11. Failure isolation

- Optional capsule failure MUST NOT crash unrelated host startup.
- Mandatory capsules require an accepted fail-closed declaration.
- Repeated failure SHOULD enter a quarantined or unavailable state with reason code and audit evidence.
- One capsule MUST NOT mutate another capsule's storage, lifecycle, or policy state directly.

## 12. UI contributions

Capsules MAY expose typed UI contributions, but MUST NOT own host navigation or assume one shell layout. The host decides placement, visibility, accessibility, and role-appropriate presentation. UI hiding is not an authorization boundary.

## 13. Existing-app ATTACH requirements

An established application MAY attach a capsule without restructuring its entire source tree. The integration checkpoint MUST preserve and document:

- package or service identity;
- signer lineage where applicable;
- existing lifecycle and workflows;
- accepted protocols and contracts;
- storage ownership and migrations;
- adapter and integration-manifest paths;
- permission and component differences;
- build, runtime, rollback, and device evidence.

## 14. Existing-feature EXTRACT requirements

An extraction checkpoint MUST:

- identify origin paths, commit, checkpoint, contracts, and accepted behavior;
- audit identity, trust, authority, storage, routing, lifecycle, UI, and platform dependencies;
- separate portable behavior from host-owned adapters;
- create source lineage and checksum evidence;
- avoid creating a second undocumented canonical implementation;
- preserve rollback and supersession records.

## 15. Origin-project REINTEGRATE requirement

Extraction is incomplete until the origin project either:

1. references and composes the accepted canonical capsule through its origin adapter; or
2. follows a documented, explicitly temporary delegation path with a bounded completion checkpoint.

The origin project MUST verify behavioral equivalence or document and approve each intentional difference.

## 16. Migration strategies

- **Clean extraction:** direct separation where boundaries are already modular.
- **Strangler extraction:** gradual delegation from mature CLIENT or SERVER code into the capsule.
- **Wrapper-first transition:** temporary stable interface around existing behavior before later movement into the capsule.

Wrapper-first and strangler states MUST remain visibly transitional and lineage-bound.

## 17. Evidence requirements

Implementation checkpoints MUST produce, as applicable:

- capsule descriptor and integration manifest;
- extraction manifest for project-local origins;
- module and dependency graph;
- source ZIP, checksum, and lineage;
- adapter mapping evidence;
- permission and component diff;
- compatibility and failure-isolation tests;
- storage migration tests;
- offline and routing tests;
- Truth Firewall preservation evidence;
- behavioral-equivalence evidence;
- build and on-device evidence for each host;
- rollback and retirement records;
- terminology review against the SWRLZ Constitution.

## 18. Non-authorization

This accepted contract does not authorize implementation, source extraction, shared-feature directories, Gradle changes, permissions, app-lane changes, builds, workflow runs, releases, deployments, installations, dynamic executable loading, or merge.
