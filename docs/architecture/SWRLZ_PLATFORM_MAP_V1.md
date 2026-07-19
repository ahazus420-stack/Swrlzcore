# SWRLZ Platform Map v1

- **Status:** Architecture baseline
- **Checkpoint:** SWRLZ-PLATFORM-MAP-001
- **Date:** 2026-07-19
- **Authority:** Derived from accepted contracts and ADRs; does not replace them
- **Primary decisions:** ADR-0001 Shared Core Capabilities and Distinct Android App Shells; ADR-0002 Modular Capability and Entitlement Gates

## 1. Purpose

This document is the architectural north star for locating SWRLZ applications, shared modules, optional capabilities, trust boundaries, communication paths, and contract dependencies.

It answers four recurring questions:

1. Where should a new feature live?
2. Which app shells may package it?
3. Which trust and authority boundaries apply?
4. Which contracts and evidence must govern implementation?

This map separates confirmed repository facts, accepted requirements, target architecture, assumptions, and unresolved decisions. It is not an implementation claim.

## 2. Governing principles

1. Integrate; do not overwrite.
2. Shared behavior belongs in reusable modules rather than drifting source copies.
3. Invisible complexity should converge; visible behavior may diverge.
4. SWRLZ grows by adding capabilities to the ecosystem, not by creating isolated islands.
5. Shared code does not imply shared Android application identity.
6. Shared device identity does not imply shared unrestricted authority.
7. Discovery does not grant trust, enrollment, entitlement, or execution authority.
8. Offline-first behavior, Truth Firewall dissent, lineage, local-versus-remote distinctions, and protocol-version discipline remain platform invariants.

## 3. Platform layers

```text
SWRLZ Ecosystem
|
+-- App shells
|   +-- Core
|   +-- Keyboard
|   +-- Launcher
|   +-- CLIENT
|   +-- NODE_HOST
|   +-- Future shells: Watch, Voice, Studio, Mobility, Robotics
|
+-- Capability modules
|   +-- Missions
|   +-- Client
|   +-- Node host
|   +-- Keyboard
|   +-- Launcher
|   +-- Discovery
|   +-- Automation
|   +-- Voice
|   +-- Remote access
|   +-- Advanced tools
|
+-- Shared platform modules
|   +-- Identity
|   +-- Lineage
|   +-- Trust and authorization
|   +-- Truth Firewall
|   +-- Storage
|   +-- Networking and routing
|   +-- Protocol and schema versioning
|   +-- Capability policy
|   +-- Update metadata
|   +-- Observability and audit
|
+-- Contracts and evidence
    +-- Normative contracts
    +-- Architecture Decision Records
    +-- Architecture and implementation guides
    +-- Workflow standards
    +-- Checkpoint reports
    +-- Build and on-device evidence
```

## 4. Core stability boundary

### 4.1 Stable foundation

Changes here have broad ecosystem impact and require strong justification, contract alignment, migration planning, and regression evidence.

- identity and installation lineage;
- trust and scoped authorization;
- Truth Firewall behavior;
- protocol and schema discipline;
- local-versus-remote route semantics;
- capability decision model;
- shared storage and migration abstractions;
- update identity and signer-lineage metadata;
- audit and evidence primitives.

### 4.2 Shared but evolvable

Reusable modules may evolve behind stable interfaces and accepted contracts.

- mission framework;
- notification and approval framework;
- discovery implementation;
- local AI interfaces;
- shared design system;
- theme and accessibility components;
- common diagnostics;
- synchronization helpers;
- capability explorer presentation.

### 4.3 App-specific

These belong to a particular shell or Android role unless deliberately extracted later.

- Keyboard layouts, IME lifecycle, and editor-context handling;
- Launcher home surface, widgets, and HOME-role behavior;
- CLIENT enrollment and trust-administration UI;
- NODE_HOST runtime controls and diagnostics;
- shell-specific navigation, icons, labels, and presentation.

## 5. App-shell map

| App shell | Primary role | Required identity property | Typical packaged capabilities | Must remain distinct from |
|---|---|---|---|---|
| Core | Canonical reusable foundation and platform inspector | Permanent Core package and signer lineage | shared platform, capability registry, diagnostics | Keyboard, Launcher, CLIENT, NODE_HOST package identities |
| Keyboard | Android IME surface | Permanent Keyboard package, IME role, scoped surface credential | keyboard, selected missions, CLIENT bridge, optional trusted node routing | physical device identity; unrestricted CLIENT or NODE authority |
| Launcher | Android HOME surface | Permanent Launcher package and HOME role | launcher, selected missions, discovery, CLIENT bridge, optional node features | CLIENT trust authority unless explicitly composed and scoped |
| CLIENT | Device identity broker and user-facing trust authority | Preserve accepted CLIENT package and signer lineage where possible | client, enrollment, approvals, routing, selected missions | NODE_HOST execution authority |
| NODE_HOST | Local or hosted processing node | Preserve accepted NODE_HOST package, node, installation, and signer lineage | node host, discovery, execution, diagnostics | CLIENT identity-broker authority |

An app shell may package both CLIENT and NODE_HOST capabilities, but packaging does not merge their identities, lifecycles, trust states, credentials, or authority.

## 6. Initial capability map

| Capability | Platform dependencies | Candidate shells | Default boundary |
|---|---|---|---|
| Identity | lineage, secure storage, protocol versions | all | stable foundation |
| Truth Firewall | identity, policy, audit | all executable surfaces | stable foundation |
| Missions | identity, Truth Firewall, routing, audit | Core, CLIENT, Launcher; optional Keyboard and NODE_HOST | shared but evolvable |
| CLIENT | identity, trust, enrollment, routing | CLIENT; optionally combined shells | capability module with explicit authority |
| NODE_HOST | node identity, trust, discovery, execution | NODE_HOST; optionally combined shells | capability module with explicit authority |
| Keyboard | identity, editor privacy, CLIENT bridge | Keyboard | app-role capability |
| Launcher | identity, Android HOME integration | Launcher | app-role capability |
| Discovery | networking, protocol versions, trust separation | CLIENT, NODE_HOST, Launcher, Core diagnostics | shared but evolvable |
| Voice | consent, capture state, routing, privacy | future selective composition | unresolved contract required |
| Automation | missions, approvals, Truth Firewall, audit | future selective composition | unresolved contract required |
| Remote access | identity, trust, route disclosure, authorization | future selective composition | unresolved contract required |
| Update metadata | package identity, signer lineage, version code, checksum | all app shells | stable metadata; automatic updater out of scope |

## 7. Capability decision path

A feature is executable only when every applicable gate succeeds:

```text
Packaged in this shell
AND entitled
AND enabled by configuration
AND available in the current environment
AND authorized by trust and scope
AND allowed by policy and Truth Firewall
AND compatible with protocol/schema versions
= executable
```

The system must preserve the reason when execution is unavailable. `Unsupported`, `not packaged`, `user disabled`, `offline`, `not enrolled`, `not entitled`, `policy blocked`, and `version incompatible` are not interchangeable states.

## 8. Identity and lineage map

```text
Physical device
`-- deviceId
    +-- CLIENT surface
    |   +-- surfaceInstanceId
    |   `-- installationId
    +-- Keyboard surface
    |   +-- surfaceInstanceId
    |   `-- installationId
    +-- Launcher surface
    |   +-- surfaceInstanceId
    |   `-- installationId
    `-- NODE_HOST surface
        +-- surfaceInstanceId
        +-- installationId
        `-- nodeId
```

Android update continuity is a separate but related package lineage:

```text
same applicationId
+ same signing lineage
+ monotonically increasing versionCode
= eligible same-app update
```

Each app shell updates only its own installed lineage.

## 9. Trust boundaries

### Boundary A: User to app surface

The user must be able to distinguish the current app shell, active capability, route, permission, and approval state.

### Boundary B: App shell to shared module

An app shell may call only packaged capabilities. Shared modules must not infer authority from package inclusion alone.

### Boundary C: Surface to CLIENT

CLIENT may broker device identity and enrollment. A surface requires a scoped credential and deliberate approval; package or signature matching alone is insufficient.

### Boundary D: CLIENT to NODE_HOST

Discovery or reachability does not authorize use. Node trust, requested scope, route class, protocol compatibility, and user policy must be evaluated.

### Boundary E: Local to LAN or remote

Any route change that increases exposure, cost, latency, or authority must remain explicit. No silent local-to-remote fallback is allowed.

### Boundary F: Capability to entitlement

Commercial or administrative entitlement may permit access but cannot override safety, privacy, identity, trust, protocol, or Truth Firewall requirements.

## 10. Communication paths

```text
Keyboard --scoped IPC--> CLIENT --trusted route--> NODE_HOST
Launcher --scoped IPC--> CLIENT --trusted route--> NODE_HOST
Core diagnostics ------> local app metadata and accepted status surfaces
CLIENT <--------------> Server, only through accepted protocol contracts
NODE_HOST <-----------> Server or peer nodes, only through accepted trust and protocol contracts
```

Every implemented path must document:

- caller and receiver identities;
- local, LAN, or remote route class;
- transport and protocol version;
- authentication and authorization evidence;
- consent or approval boundary;
- failure and mismatch behavior;
- audit fields and prohibited telemetry;
- offline behavior.

## 11. Contract dependency map

| Area | Canonical governing source |
|---|---|
| Modular app shells and update lineage | `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md` |
| Capability and entitlement separation | `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md` |
| Device and surface identity | `core-swrlz-board/docs/identity/SWRLZ_IDENTITY_SURFACES_CONTRACT_V1.md` |
| Keyboard trust, privacy, and enrollment | `core-swrlz-board/docs/contracts/SWRLZ_KBD_CONTRACT_V1.md` |
| Keyboard IPC wire behavior | `core-swrlz-board/docs/contracts/SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md` |
| Long-term platform direction | `SWRLZ_VISION_2035.md` |
| Engineering workflow discipline | `SWRLZ_Engineering_Handbook_CF11_UPDATED.md` and accepted workflow standards |

This map must link to contracts rather than restating or weakening their normative requirements.

## 12. Placement rule for future work

Before adding a feature, determine its narrowest correct home:

1. Does it define a cross-platform invariant? Place it behind an accepted contract and stable platform interface.
2. Is it reusable behavior with multiple consumers? Place it in a shared or capability module.
3. Is it an Android role or product-specific presentation? Place it in the app shell.
4. Does it combine roles? Compose existing capabilities without merging identities or authority.
5. Is no suitable location present? Propose a bounded module and documentation checkpoint before implementation.

## 13. Confirmed facts

- The repository already contains accepted identity and Keyboard contracts.
- ADR-0001 accepts shared Core modules, capability modules, and distinct app shells.
- ADR-0002 accepts separate capability, entitlement, configuration, availability, and policy decisions.
- Core, Keyboard, and Launcher build lanes have demonstrated build independence from common source lineage.
- Durable same-app updating requires stable package identity, signing lineage, and version progression.

## 14. Target architecture, not yet implementation evidence

The following remain target-state items until repository and build evidence confirms them:

- final Gradle module boundaries;
- permanent package IDs for Core, Keyboard, and Launcher;
- accepted preservation strategy for existing CLIENT and NODE_HOST IDs;
- stable per-lineage signing infrastructure;
- typed capability registry implementation;
- per-shell composition manifests;
- Capability Explorer UI;
- automatic APK update delivery;
- commercial entitlement mechanism.

## 15. Unresolved decisions

1. One shared SWRLZ development signing key versus one stable key per app lineage.
2. Exact permanent package identifiers for new Core, Keyboard, and Launcher shells.
3. Whether combined CLIENT and NODE_HOST packaging is one dedicated shell or an option in selected shells.
4. The first implementation-grade capability registry schema.
5. The canonical module dependency graph after the repository audit.
6. Future distribution and entitlement verification mechanism.

## 16. Verification expectations

Each implementation checkpoint placed on this map should produce applicable evidence for:

- contract and ADR traceability;
- source lineage and checksums;
- package, surface, installation, and node identity;
- capability composition;
- dependency direction and absence of circular authority;
- local-versus-remote route handling;
- Truth Firewall behavior;
- protocol and schema compatibility;
- same-lineage update behavior;
- side-by-side app installation;
- rollback and retirement lineage.

## 17. Change policy

This map is maintained by integration, not replacement.

- Confirmed implementation evidence may move an item from target state into confirmed facts.
- New capabilities should be added to the capability map and linked to their governing contract or ADR.
- Superseded architecture must remain traceable through ADR status and lineage.
- Normative requirements belong in contracts, not solely in this map.
- A map update does not itself authorize code changes, builds, releases, deployments, billing, or automatic updates.
