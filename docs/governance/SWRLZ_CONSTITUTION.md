# SWRLZ Constitution Foundation

- **Status:** Accepted foundation
- **Checkpoint:** INT-CONST-001 / INT-CONST-001A
- **Approval phrases:**
  - `APPROVE INT-CONST-001 — SWRLZ Constitution Foundation`
  - `APPROVE INT-CONST-001A — CONSTITUTION DRAFT ACCEPTANCE AND REPOSITORY STAGING`
- **Scope:** Constitutional documentation and repository placement only
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Related accepted decisions:**
  - [`ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`](../architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md)
  - [`ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`](../architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md)
  - [`SWRLZ_PLATFORM_MAP_V1.md`](../architecture/SWRLZ_PLATFORM_MAP_V1.md)
  - [`SWRLZ_PLATFORM_MAP_CURRENT_STATE_001A.md`](../architecture/SWRLZ_PLATFORM_MAP_CURRENT_STATE_001A.md)

## Preamble

SWRLZ is an evolving software ecosystem built to preserve identity, trust, truth, lineage, human authority, offline capability, and explicit distinctions between local and remote operation.

This Constitution defines the highest-level engineering principles that future architecture, contracts, protocols, capabilities, implementations, organizations, developers, and AI collaborators must preserve.

The Constitution is not a substitute for technical specifications. It is the governing source from which those specifications derive their legitimacy and direction.

SWRLZ evolves through integration, not erasure.

> Every creation remembers where it came from without being forced to become its origin.

## Constitutional hierarchy

SWRLZ distinguishes three levels of technical truth:

1. **Constitutional truth** — foundational principles that change rarely and only through explicit constitutional review.
2. **Architectural truth** — accepted structures, contracts, protocols, boundaries, and decisions that evolve through bounded checkpoints.
3. **Implementation truth** — current code, modules, APIs, builds, UI, storage, and deployment mechanisms that may change frequently while remaining traceable upward.

```text
Constitution
    ↓
Architecture
    ↓
Implementation
    ↓
Evidence
```

Every implementation should identify the architectural rule it satisfies. Every architectural rule should identify the constitutional principle it protects. Every material claim should be supported by evidence.

---

# Article I — Identity

## I.1 Identity persists

Identity is not a consumable resource.

An identity may be referenced, represented, authenticated, inherited through lineage, delegated within bounded authority, retired, superseded, or transformed through an explicit transition. It is not exhausted merely because another component recognizes or cooperates with it.

## I.2 Identity and instance are distinct

The following must not be silently collapsed into one concept:

- human identity;
- device identity;
- installation identity;
- application identity;
- software-surface identity;
- node identity;
- account identity;
- credential identity;
- process or session identity.

Shared association does not imply identical identity.

## I.3 Identity does not automatically grant authority

Recognition of identity is not equivalent to authorization.

Shared device identity, package inclusion, account association, enrollment, entitlement, or lineage must not silently grant unrelated authority.

## I.4 Identity transitions preserve lineage

When an identity is replaced, renamed, retired, migrated, rotated, or superseded, the prior identity and transition evidence must be preserved when technically and legally appropriate.

A descendant does not overwrite its origin.

---

# Article II — Truth, Evidence, and Provenance

## II.1 Truth must remain contestable

The Truth Firewall must remain capable of objection, refusal, qualification, dissent, pause, or safer-alternative proposal.

No commercial state, entitlement, remote command, user-interface state, model update, or deployment mode may silently create an obedience-only system.

## II.2 Claims require classification

SWRLZ distinguishes fact, requirement, assumption, inference, recommendation, and unresolved question. These categories must not be blended without disclosure.

## II.3 Provenance is part of truth

Canonical source archives, checksums, accepted contracts, implementation files, build evidence, signer evidence, version evidence, and checkpoint records are part of the truth model.

A result without provenance is weaker than a result with traceable evidence.

## II.4 History must not be rewritten by convenience

Corrections may supersede prior conclusions, but accepted evidence must not be silently erased or represented as though it never existed.

---

# Article III — Programmatic Philosophy and Relationship Semantics

## III.1 Language must reflect the actual relationship

SWRLZ uses the most semantically accurate verb available rather than defaulting to generic software jargon.

Preferred relationship verbs include:

- **inherits** — receives lineage, rules, or a defined base model;
- **composes** — assembles behavior or capability without depletion;
- **hosts** — provides an execution environment or lifecycle;
- **references** — points to or retrieves an identity, record, contract, or resource;
- **links** — establishes a defined technical relationship;
- **cooperates with** — coordinates across a boundary without merging identity;
- **delegates to** — transfers bounded authority while retaining accountable origin;
- **authenticates** — verifies an asserted identity;
- **authorizes** — permits a specific operation under policy;
- **exposes** — makes a contract or interface available;
- **registers** — records participation or availability;
- **invokes** — initiates executable behavior;
- **requires** — declares a prerequisite;
- **extends** — adds behavior while preserving a prior contract;
- **preserves** — maintains identity, lineage, evidence, or invariant.

## III.2 “Consume” has a narrow valid meaning

The term **consume** should be used only when an operation genuinely advances, exhausts, depletes, spends, removes, or irreversibly transforms the referenced resource.

Appropriate examples may include consuming a queue message, CPU time, energy, storage capacity, bandwidth, a one-way iterator, or a single-use token.

Inappropriate examples include:

- “CLIENT consumes Identity”;
- “Launcher consumes Discovery”;
- “NODE_HOST consumes Trust”;
- “Keyboard consumes Core.”

More precise alternatives are:

- CLIENT **authenticates through**, **references**, or **inherits rules from** the identity model;
- Launcher **composes** the Discovery capability;
- NODE_HOST **establishes** and **preserves** trust;
- Keyboard **composes** selected Core capabilities.

## III.3 Reuse is not depletion

Software capability normally remains available after composition or invocation. Architectural descriptions should not imply depletion where none occurs.

## III.4 Relationship semantics are architectural data

Important architecture diagrams and contracts should label relationships explicitly where relationship type affects authority, trust, lifecycle, identity, or dependency direction.

---

# Article IV — Evolution and Lineage

## IV.1 Integrate; do not overwrite

New work must preserve accepted source lineage and integrate with existing architecture wherever practical.

## IV.2 Evolution must be bounded

Work proceeds one bounded checkpoint at a time. Each checkpoint states scope, evidence, authorized actions, prohibited actions, expected result, verification criteria, and rollback considerations.

## IV.3 Descendants remain traceable

A derived application, module, protocol, contract, or capability must identify the source or accepted checkpoint from which it descends.

## IV.4 Invisible complexity should converge; visible behavior may diverge

Shared infrastructure, trust logic, identity semantics, protocol primitives, and evidence handling should converge where doing so strengthens consistency. App shells, interfaces, workflows, and user experiences may diverge where their roles require distinct behavior.

## IV.5 Composition is preferred over duplication

Shared behavior should normally be composed through accepted modules or contracts rather than copied into isolated forks.

---

# Article V — Human Authority and Approval

## V.1 Human authority remains explicit

AI systems may analyze, recommend, draft, compare, simulate, and identify risks. They must not silently redefine constitutional principles, grant themselves authority, or treat inferred intent as authorization for material actions.

## V.2 Approval is scoped

Approval for documentation does not authorize code changes. Approval for code changes does not automatically authorize builds. Approval for builds does not automatically authorize commits, pushes, releases, deployment, installation, enrollment, or remote execution.

## V.3 Silence is not approval

Missing objection, delayed response, prior enthusiasm, or general project support must not be interpreted as authorization for a bounded action.

## V.4 Authority must remain auditable

Material actions should record who or what requested the action, what approval authorized it, what evidence was used, what changed, what did not change, and the resulting state.

---

# Article VI — Capabilities and Entitlements

## VI.1 Capability is distinct from entitlement

SWRLZ preserves the distinction between capability, entitlement, configuration, availability, trust, policy permission, and protocol compatibility.

## VI.2 Packaging does not grant authority

A capability being present in an app shell does not mean it is authorized, configured, trusted, available, policy-allowed, or executable.

## VI.3 UI is not the sole enforcement boundary

Hiding or showing a user-interface element is not sufficient enforcement for privileged behavior.

## VI.4 Failure reasons remain explicit

Unsupported, absent, disabled, unavailable, untrusted, incompatible, revoked, expired, policy-blocked, and temporarily unverifiable states must remain distinguishable where relevant.

## VI.5 Commercial state does not override trust

Payment, subscription, license, entitlement, or organizational role must not override privacy, Truth Firewall behavior, identity, trust, protocol, or human-approval requirements.

---

# Article VII — Protocol and Cooperation

## VII.1 Protocol versions are explicit

Components must not silently assume compatibility. Protocol identity, version, supported capability, trust state, and route semantics must remain inspectable.

## VII.2 Local and remote are distinct

Local execution, local-node execution, trusted remote-node execution, hosted execution, and third-party remote execution are materially different routes. The selected route must not be obscured.

## VII.3 Cooperation does not merge identities

CLIENT and NODE_HOST may cooperate, and may eventually coexist in one shell, but their roles, credentials, lifecycle, authority, trust, and audit records must remain explicit.

## VII.4 Offline-first is a governing requirement

Ordinary useful operation should remain possible without continuous paid or remote dependency wherever the capability permits. Offline failure must not silently redirect work to a paid or remote service.

## VII.5 Protocol changes preserve compatibility evidence

Breaking changes require explicit versioning, migration rules, compatibility boundaries, and rollback considerations.

---

# Article VIII — Architecture and Implementation

## VIII.1 Implementations are replaceable; principles endure

Kotlin classes, Android modules, storage engines, service frameworks, UI toolkits, transport libraries, and deployment mechanisms may change. Their replacement must preserve accepted identity, trust, truth, lineage, authority, offline-first, and protocol invariants.

## VIII.2 App shells remain distinct

Each installable application preserves its own package identity, signing lineage, version progression, role, presentation, and declared capability composition.

## VIII.3 Shared platform code does not own app identity

Shared modules may define identity models and contracts, but installable app shells retain their own durable Android identities and update lineages.

## VIII.4 Dependency direction must remain understandable

Shared platform modules must not depend on app shells. Authority must not arise merely because one module is included by another.

## VIII.5 Evidence completes implementation

A change is not complete merely because it compiles. Completion may require tests, checksums, signer verification, version verification, side-by-side installation evidence, same-lineage update evidence, offline behavior evidence, trust-boundary evidence, protocol compatibility evidence, and rollback evidence.

---

# Article IX — Organizational Continuity

## IX.1 The Constitution applies beyond the current codebase

These principles are intended to guide future contributors, maintainers, teams, departments, companies, institutes, AI collaborators, partner systems, and hosted-node operators.

## IX.2 Institutional scale must not erase origin

Growth into a larger team, corporation, institute, or public ecosystem must preserve the founding principles and documented lineage of SWRLZ.

## IX.3 Standards may evolve beneath constitutional invariants

Technology standards, implementation conventions, organizational procedures, and toolchains may evolve without constitutional amendment when they preserve the governing principles.

## IX.4 Constitutional amendments require explicit review

A future amendment should state the exact article affected, reason, evidence, compatibility and migration impact, risks, and approval record.

---

# Constitutional axioms

1. **Integrate; do not overwrite.**
2. **Identity persists and is not consumed.**
3. **Recognition is not authorization.**
4. **Truth remains contestable.**
5. **Evidence and provenance strengthen truth.**
6. **Relationships use verbs that describe what actually occurs.**
7. **Capabilities compose; they do not grant themselves authority.**
8. **Offline-first behavior remains first-class.**
9. **Local and remote routes remain explicit.**
10. **Human approval is bounded and auditable.**
11. **Invisible complexity should converge; visible behavior may diverge.**
12. **Every creation remembers where it came from without being forced to become its origin.**

---

# Relationship to accepted decisions

This Constitution incorporates and elevates principles already present in accepted repository documentation:

- ADR-0001 establishes shared Core capabilities, distinct app shells, durable app identity, stable signing lineage, independent version progression, explicit capability composition, and preservation of offline-first, Truth Firewall, lineage, route, and protocol invariants.
- ADR-0002 separates capability, entitlement, configuration, availability, trust, and policy; rejects UI-only enforcement and always-online entitlement assumptions; and preserves Truth Firewall operation regardless of commercial state.
- The current-state platform map records that the accepted multi-shell architecture is not yet fully implemented and that identity, signer, version, and composition evidence remain prerequisite work.

This document does not replace those decisions. It provides the constitutional layer above them.

---

# Open constitutional questions

1. Should amendments remain checkpoint-based for now, or eventually require a defined council process?
2. Should Relationship Semantics become a separate normative standard referenced by Article III?
3. Should Truth Firewall receive a dedicated constitutional article in a later checkpoint?

---

# Implementation boundary

INT-CONST-001A authorizes the accepted constitutional document and its documentation cross-references only.

It does not authorize code changes, module restructuring, package/signer/version changes, protocol changes, builds, workflow execution, releases, deployment, installation, enrollment, or remote execution.