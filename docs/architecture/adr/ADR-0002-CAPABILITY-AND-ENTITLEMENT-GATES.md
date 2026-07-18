# ADR-0002: Modular Capability and Entitlement Gates

- **Status:** Accepted
- **Date:** 2026-07-18
- **Checkpoint:** CAPABILITY-GATE-001
- **Approval phrase:** `APPROVE CAPABILITY-GATE-001 MODULAR FEATURE AND ENTITLEMENT ARCHITECTURE SCAFFOLD`
- **Related decision:** ADR-0001 Shared Core Capabilities and Distinct Android App Shells

## Context

SWRLZ applications may eventually expose different feature sets by app shell, installation, user decision, deployment mode, commercial plan, device capability, local-node availability, or accepted policy. The project may be distributed outside the Play Store, so the architecture must not assume Play Billing or continuous online verification.

The immediate requirement is architectural scaffolding only. Billing, payment processing, subscription enforcement, and automatic update delivery are out of scope.

## Decision

SWRLZ will separate four concepts:

1. **Capability** — functionality the software can technically provide.
2. **Entitlement** — functionality an installation, account, or user is authorized to use.
3. **Configuration** — functionality the user or administrator has enabled.
4. **Availability** — functionality that can run in the current environment.

A capability is usable only when all required decisions succeed. UI visibility alone is not an authorization boundary.

Conceptual decision model:

```text
packaged
AND entitled
AND configured
AND available
AND policy-allowed
= executable
```

## Capability states

The scaffold SHOULD support explicit states such as:

- `enabled`
- `disabled`
- `hidden`
- `locked`
- `unavailable`
- `unsupported`
- `policy_blocked`
- `temporarily_unverified`

Implementations MUST preserve the reason for a state rather than collapsing all failures into a generic disabled state.

## Enforcement layers

Capability decisions MUST be enforceable at the layers that apply:

- navigation and UI exposure;
- use-case or business-logic entry points;
- service startup and lifecycle;
- mission planning and execution;
- network/API boundaries;
- local-versus-remote routing;
- storage and data access;
- inter-app or surface authorization.

Hiding a button is insufficient when the underlying feature remains callable.

## Offline-first requirements

1. Ordinary offline operation MUST remain first-class.
2. The capability model MUST NOT require a paid per-call runtime loop.
3. Future entitlement evidence MAY be cached locally when cryptographically verifiable and governed by an accepted contract.
4. Offline, expired, revoked, unsupported, user-disabled, and policy-blocked states MUST remain distinguishable.
5. A failed entitlement check MUST NOT silently route work to a paid or remote service.
6. The Truth Firewall MUST remain able to object, refuse, pause, or propose a safer alternative regardless of commercial state.
7. Feature gating MUST NOT create an obedience-only mode.

## Composition and runtime gating

Build-time composition and runtime gating are separate controls.

- **Build-time composition** decides whether a capability module is packaged in an app shell.
- **Runtime gating** decides whether a packaged capability may be shown, started, or executed.

An app shell MUST declare its packaged capabilities. Runtime state MUST NOT claim that an unbundled capability is merely disabled; it is unsupported or absent.

Example conceptual registry:

```text
CapabilityRegistry
├── missions
├── client
├── node_host
├── keyboard
├── launcher
├── automation
├── voice
├── remote_access
└── advanced_tools
```

## Authority and trust

Entitlement does not grant unrelated trust or authority.

For example:

- entitlement to NODE_HOST functionality does not automatically enroll a node;
- entitlement to remote processing does not authorize a route without trust and user approval;
- entitlement to a Keyboard action does not grant unrestricted access to typed content;
- shared packaging does not merge CLIENT and NODE_HOST identities;
- commercial access does not override safety, privacy, identity, or protocol contracts.

## Future commercial use

The model MAY later support:

- free and paid capability bundles;
- one-time grants;
- subscription grants;
- device-bound grants;
- account-bound grants;
- operator or organization grants;
- development and test grants;
- time-limited signed licenses;
- explicit grace periods.

No specific monetization mechanism is accepted by this ADR.

## Required audit fields

A capability decision record SHOULD include, as applicable:

- capability identifier;
- app shell and package identity;
- installation and surface identity;
- decision state;
- reason code;
- entitlement source and version;
- configuration source;
- availability evidence;
- policy/contract version;
- timestamp;
- local-versus-remote route implication;
- expiry or reevaluation time.

Content-sensitive user data MUST NOT be added merely for entitlement telemetry.

## Alternatives considered

### Separate free and paid source forks

Rejected as the default because fixes and trust requirements would drift, creating lineage and maintenance risk.

### UI-only feature hiding

Rejected because hidden functionality could remain reachable through services, missions, intents, or inter-app calls.

### Always-online authorization

Rejected as the default because it conflicts with offline-first operation and creates a remote dependency for local capability.

### Play-Store-only billing architecture

Rejected as a foundational assumption because distribution and commercial strategy remain undecided.

## Consequences

### Positive

- features can be packaged and exposed selectively;
- future commercial choices remain open;
- offline-first behavior remains possible;
- one shared implementation can support multiple app shells and plans;
- blocked features can report accurate reasons;
- trust and entitlement remain separate.

### Costs and risks

- multiple enforcement points require consistent policy evaluation;
- cached entitlement evidence requires future key-management design;
- capability dependencies must be modeled explicitly;
- poor reason-code discipline could confuse users and diagnostics;
- combined-role apps need careful least-authority defaults.

## Implementation boundary

This ADR authorizes architecture scaffolding and documentation for capability and entitlement decisions. It does not authorize:

- billing integration;
- collecting payment;
- subscriptions;
- remote license servers;
- disabling currently accepted functionality without a separate approved migration;
- automatic APK updating;
- Play Store integration;
- analytics containing user content.

## Verification

Scaffolding should eventually demonstrate:

- a typed capability identifier model;
- explicit capability states and reason codes;
- separation of capability, entitlement, configuration, and availability;
- enforcement below the UI layer;
- offline behavior tests;
- fail-closed handling for privileged features;
- preservation of Truth Firewall and route transparency;
- per-app capability composition evidence.
