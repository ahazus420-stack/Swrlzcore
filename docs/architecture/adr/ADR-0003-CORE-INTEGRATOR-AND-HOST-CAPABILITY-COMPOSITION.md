# ADR-0003: CORE Integrator and Host-Capability Composition

- **Status:** Proposed for acceptance
- **Date:** 2026-07-21
- **Checkpoint:** CORE-ARCH-003
- **Constitutional basis:** Integrate; do not overwrite. Preserve offline-first behavior, Truth Firewall dissent, lineage, local-versus-remote distinctions, and protocol-version discipline.
- **Related decisions:** ADR-0001 Shared Core Capabilities and Distinct Android App Shells; ADR-0002 Modular Capability and Entitlement Gates
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`

## Context

SWRLZ needs to implement shared capabilities such as Phoenix Firewall once and compose them safely into Core, Keyboard, Launcher, CLIENT, NODE_HOST, and future app shells. Copying an entire application source tree into each descendant would create drift, identity confusion, and repeated security fixes. Unrestricted runtime plugin loading would introduce code-signing, class-loading, permission, lifecycle, and trust risks.

## Decision

SWRLZ will use **compile-time integrators**: versioned reusable capability modules that implement a stable integrator contract and are assembled into distinct app shells through explicit host composition manifests.

An integrator is not an APK, Android application identity, or authority grant. It is a reusable implementation module whose execution remains subject to host capability declarations, build-time packaging, runtime policy, entitlement, configuration, availability, trust, protocol compatibility, and Truth Firewall evaluation.

Conceptual structure:

```text
integrator-api
    ↑
integrator-runtime
    ↑
integrator-phoenix-firewall
    ↑
explicit host adapters and composition manifests
    ├── Core
    ├── Keyboard
    ├── Launcher
    ├── CLIENT
    └── NODE_HOST
```

## Requirements

1. Integrators MUST depend on stable platform interfaces, not app-shell UI or role-specific lifecycle classes.
2. Each app shell MUST declare packaged integrators explicitly.
3. Each integrator MUST declare identity, semantic version, contract version, supported host types, required capabilities, permissions, storage scope, network behavior, lifecycle needs, failure mode, lineage, and migration requirements.
4. Package inclusion MUST NOT imply authorization.
5. Host adapters MUST expose only the minimum capabilities needed by the integrator.
6. Keyboard and Launcher MUST use restricted host profiles and MUST NOT inherit unrestricted Core authority.
7. Integrator failure MUST be isolated so one capability cannot prevent unrelated host startup unless an accepted contract marks it as mandatory and fail-closed.
8. Integrators MUST preserve explicit local, LAN, and remote route distinctions and MUST NOT add silent remote fallback.
9. Truth Firewall behavior MUST remain available across all executable host surfaces and MUST NOT be weakened by entitlement or host composition.
10. Integrator storage MUST be namespaced and migration-controlled.
11. Integrator protocol incompatibility MUST produce an explicit unavailable or incompatible state, not undefined behavior.
12. Dynamic third-party code loading is outside the initial architecture.

## Phoenix Firewall first-profile model

Phoenix Firewall is the first proposed integrator and SHOULD define host profiles rather than one unrestricted mode:

- `CORE_FULL`
- `KEYBOARD_RESTRICTED`
- `LAUNCHER_RESTRICTED`
- `CLIENT_SCOPED`
- `NODE_HOST_SCOPED`

Profiles select approved surfaces and policy behavior; they do not fork the firewall engine.

## Consequences

### Positive

- one implementation can serve multiple shells;
- fixes and trust requirements propagate through versioned modules;
- app identities and Android roles remain distinct;
- host-specific least-authority behavior is explicit;
- composition can be evidenced and tested per shell.

### Costs and risks

- stable APIs and compatibility discipline are required;
- Gradle dependency boundaries become more deliberate;
- composition manifests and host adapters require maintenance;
- profile design must avoid accidental capability escalation;
- migration and storage ownership must be defined before implementation.

## Rejected alternatives

### Full source-tree cloning

Rejected because fixes, contracts, and trust behavior would drift.

### Runtime-downloaded executable plugins

Rejected for the initial architecture because they add signing, code-loading, permission, supply-chain, lifecycle, and policy complexity.

### One unrestricted integrator configuration for every host

Rejected because Keyboard, Launcher, CLIENT, NODE_HOST, and Core have different Android roles and trust boundaries.

## Implementation boundary

This ADR documents architecture only. It does not authorize source modules, Gradle changes, app-lane changes, permissions, builds, workflow runs, release, deployment, installation, or merge.

## Verification expectations

Future implementation evidence should demonstrate explicit composition manifests, dependency direction, host-profile restrictions, protocol compatibility, storage isolation, failure isolation, Truth Firewall preservation, per-shell build evidence, and no silent authority expansion.