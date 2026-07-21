# CORE Integrator Architecture v1

- **Status:** Proposed architecture guide
- **Checkpoint:** CORE-ARCH-003
- **Governing ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`
- **Normative contract:** `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`

## Purpose

This guide explains how SWRLZ can implement Phoenix Firewall and future shared capabilities once, then compose approved subsets into Core, Keyboard, Launcher, CLIENT, NODE_HOST, and future app shells.

## Core rule

Build shared capability modules, not copies of complete applications.

```text
shared platform APIs
        +
versioned integrator implementation
        +
host-specific adapter
        +
explicit composition manifest
        =
role-correct app shell
```

## Proposed module boundaries

```text
:core-api
:core-runtime
:integrator-api
:integrator-runtime
:integrator-phoenix-firewall
:app-core
:app-keyboard
:app-launcher
```

These names are conceptual until an implementation checkpoint audits the actual Gradle graph.

### `integrator-api`

Defines stable types only:

- integrator descriptor;
- host type and host-capability identifiers;
- lifecycle contract;
- compatibility result and reason codes;
- host profile identity;
- typed UI contribution descriptors;
- audit and failure-state interfaces.

### `integrator-runtime`

Provides host-neutral orchestration:

- descriptor validation;
- compatibility checks;
- lifecycle sequencing;
- capability and policy gating;
- failure isolation;
- migration coordination;
- audit events without user-content telemetry.

### Integrator implementation

Contains reusable behavior such as Phoenix Firewall. It depends on the stable API and narrowly defined platform services, never directly on a host Activity, IME service, launcher surface, or host navigation framework.

### Host adapter

Maps one app shell's approved services into the integrator API. It is the least-authority boundary and must not expose unrelated host powers.

### Composition manifest

Declares which integrators and profiles are packaged in one shell. It should eventually record:

- host identity and role;
- integrator ID and version;
- selected profile;
- required contract version;
- included and excluded capabilities;
- required manifest components and permissions;
- storage namespace;
- expected route classes;
- build-time lineage and checksums.

## Phoenix Firewall composition

One engine, multiple profiles:

```text
Phoenix Firewall engine
├── CORE_FULL
├── KEYBOARD_RESTRICTED
├── LAUNCHER_RESTRICTED
├── CLIENT_SCOPED
└── NODE_HOST_SCOPED
```

The restricted profiles must be additive allowlists, not full authority followed by ad hoc disabling.

### Core

May expose full local policy administration, diagnostics, profile inspection, and evidence views.

### Keyboard

May expose only input-path protections required for the IME role. It must not gain unrelated content capture, unrestricted networking, overlay, CLIENT administration, or NODE_HOST authority.

### Launcher

May expose app-launch, intent, and surface-policy protections appropriate to the HOME role.

### CLIENT

May apply firewall policy to enrollment, user approvals, trust routes, and CLIENT-owned communication boundaries.

### NODE_HOST

May apply firewall policy to node execution, request authorization, and NODE_HOST-owned route boundaries.

## Execution decision

An integrator may execute only when all applicable gates pass:

```text
packaged
AND descriptor compatible
AND host type supported
AND selected profile valid
AND required host capabilities present
AND entitled
AND configured
AND available
AND trust-authorized
AND policy and Truth Firewall allowed
AND protocol compatible
= executable
```

Every failure must preserve its reason.

## Security model

- No initial runtime download or arbitrary executable plugin loading.
- No authority inferred from package inclusion, common signature, or shared device identity.
- No direct integrator-to-integrator storage mutation.
- No silent permission or Android-component additions.
- No silent local-to-remote fallback.
- No weakening of Truth Firewall objection, refusal, pause, or safer-alternative behavior.
- Optional integrator failure must not crash unrelated host startup.

## Implementation sequence recommendation

1. Accept ADR and contract.
2. Audit existing Gradle and package dependencies.
3. Define `integrator-api` types only.
4. Add composition-manifest schema and validation tests.
5. Implement a no-op reference integrator to verify lifecycle and failure isolation.
6. Define Phoenix Firewall engine boundaries and profile matrix.
7. Integrate Phoenix into Core first.
8. Verify Core build and on-device behavior.
9. Add Keyboard and Launcher adapters in separate checkpoints with role-specific evidence.
10. Preserve per-shell identity, signing lineage, version progression, and rollback evidence.

## Documentation and evidence gate

Every implementation checkpoint must update the ADR/contract traceability, dependency graph, composition manifest, permissions, checksums, build evidence, on-device evidence, handoff, and operating skill. Implementation is not complete merely because one host compiles.

## Non-authorization

This guide does not authorize source modules, Gradle edits, app-lane changes, permissions, builds, workflows, releases, deployment, installation, dynamic plugins, or merge.