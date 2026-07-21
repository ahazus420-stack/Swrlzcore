# ADR-0003: Project-Agnostic Portable Feature Capsules and Host-Service Composition

- **Status:** Accepted
- **Date:** 2026-07-21
- **Checkpoint:** CORE-ARCH-003 / CORE-ARCH-003A / CORE-ARCH-003B-LANGUAGE / CORE-GATE-004B-CONSTITUTION-SKILL
- **Constitutional basis:** Integrate; do not overwrite. Preserve offline-first behavior, Truth Firewall dissent, lineage, local-versus-remote distinctions, protocol-version discipline, and accurate relationship semantics.
- **Related decisions:** ADR-0001 Shared Core Capabilities and Distinct Android App Shells; ADR-0002 Modular Capability and Entitlement Gates
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`
- **Extraction guide:** `docs/architecture/PORTABLE_FEATURE_EXTRACTION_AND_EXISTING_APP_INTEGRATION_V1.md`
- **Acceptance approval:** `Approve CORE-GATE-004B-CONSTITUTION-SKILL — Add the SWRLZ Constitution Guardian skill to the repository and accept the constitutionally aligned portable feature-capsule architecture without creating source modules, changing app code, triggering builds, or merging`

## Context

SWRLZ needs to move useful capabilities between mature projects such as CLIENT, SERVER, NODE_HOST, Core, Keyboard, Launcher, and future applications without requiring every host to be predefined and without cloning entire application source trees. Existing features also need a controlled path to become reusable modules while preserving the work, contracts, storage, trust boundaries, and behavior already established in their origin projects.

Unrestricted runtime code loading remains outside the initial architecture because it introduces signing, class-loading, permission, lifecycle, supply-chain, and trust risks.

## Decision

SWRLZ will use **project-agnostic portable feature capsules**. A capsule is a versioned reusable package that declares runtime targets, required and optional host services, lineage, lifecycle, storage, routing, permission, protocol, failure, and Truth Firewall requirements.

Compatibility is determined by requirements and exposed services, not by a closed list of named applications.

```text
capsule declares requirements
        +
host exposes services
        +
receiving-project adapter
        +
integration manifest
        =
portable feature attached to a compatible project
```

A capsule is not an APK, app identity, trust grant, enrollment, entitlement, or unrestricted authority grant.

## Existing-project workflows

The architecture defines three first-class workflows:

- **ATTACH** — add a canonical capsule to an established compatible project through a thin adapter and local integration manifest.
- **EXTRACT** — separate portable behavior from an established project-local feature after dependency, authority, storage, lifecycle, route, and protocol audit.
- **REINTEGRATE** — make the origin project reference and compose the accepted canonical capsule so extraction does not leave two drifting implementations.

Mature applications do not need wholesale restructuring. They retain their package or service identity, signer lineage where applicable, workflows, protocols, storage ownership, lifecycle, and accepted contracts.

## Requirements

1. Capsules MUST depend on stable service contracts rather than project-specific UI or lifecycle classes.
2. Capsules MUST declare supported runtime classes and required services rather than a complete list of project names.
3. Named apps MAY appear as examples, tested adapters, or evidence targets only.
4. Every attachment MUST use an explicit receiving-project adapter and integration manifest.
5. Packaging or attachment MUST NOT imply authorization.
6. Host adapters MUST expose least authority.
7. Optional capsule failure MUST be isolated from unrelated host startup.
8. Capsules MUST preserve explicit local, LAN, and remote route distinctions and MUST NOT add silent remote fallback.
9. Truth Firewall behavior MUST remain available and MUST NOT be weakened by composition, entitlement, or host integration.
10. Capsule storage MUST be namespaced and migration-controlled.
11. Protocol incompatibility MUST produce an explicit incompatible state.
12. Extraction MUST preserve origin lineage, accepted behavior, rollback, and supersession evidence.
13. Extraction MUST NOT create a second undocumented canonical implementation.
14. The origin project MUST REINTEGRATE the accepted capsule or follow an explicitly temporary, bounded delegation path.
15. Runtime-downloaded arbitrary executable code remains outside the initial architecture.

## Relationship semantics

Architecture must use the most accurate relationship verb:

- projects **attach**, **import**, **reference**, or **compose** capsules;
- hosts **expose** services and **host** lifecycle;
- adapters **translate** services;
- registries **register** availability;
- runtimes **invoke** behavior;
- origin projects **reintegrate** accepted capsules;
- packages **preserve** lineage.

`Consume` is reserved for genuinely depleting or irreversible operations. Reusable software composition does not deplete the referenced module.

## Migration strategies

- **Clean extraction** for already modular features.
- **Strangler extraction** for mature CLIENT and SERVER features that must move gradually.
- **Wrapper-first transition** when immediate movement is too risky and a stable interface must precede extraction.

Strangler and wrapper-first states are transitional and must remain checkpoint-bound and documented.

## Consequences

### Positive

- useful features can originate in any project;
- established applications retain prior work and accepted boundaries;
- one canonical implementation can attach to multiple compatible projects;
- fixes and trust requirements propagate through versioned packages;
- app and service identities remain distinct;
- extraction and reintegration prevent implementation drift;
- portable ZIP/SHA packages support separate repositories and offline workflows.

### Costs and risks

- stable service contracts and compatibility discipline are required;
- mature-feature extraction requires dependency and authority audits;
- adapters and integration manifests require maintenance;
- storage migration and behavioral-equivalence evidence can be substantial;
- transitional wrapper states must not become permanent duplicate implementations.

## Rejected alternatives

### Full source-tree cloning

Rejected because fixes, contracts, lineage, and trust behavior drift.

### Closed host-name registry

Rejected because future compatible projects should not require capsule modification merely to add their names.

### Runtime-downloaded executable plugins

Rejected for the initial architecture due to signing, code-loading, permission, supply-chain, lifecycle, and policy complexity.

### Extraction without origin reintegration

Rejected because it leaves two candidate canonical implementations.

## Implementation boundary

This ADR documents accepted architecture only. It does not authorize source extraction, module creation, shared-feature directories, Gradle changes, app changes, permissions, builds, workflow runs, merge, release, deployment, or installation.

## Verification expectations

Future evidence should demonstrate descriptors, integration and extraction manifests, service mappings, dependency direction, protocol compatibility, storage isolation, migration recovery, failure isolation, Truth Firewall preservation, behavioral equivalence, terminology compliance, source ZIP/checksum lineage, and independent build/on-device evidence for each attached host.
