# SWRLZ Architecture Decision Records

Status: active documentation system

This directory preserves durable architectural decisions for SWRLZ-Core and its descendant applications.

The ADR system operates beneath the accepted [SWRLZ Constitution Foundation](../../governance/SWRLZ_CONSTITUTION.md). ADRs interpret constitutional principles for bounded architectural decisions; they do not redefine those principles silently.

An Architecture Decision Record (ADR) captures:

- the decision and its status;
- the problem and constraints that led to it;
- accepted requirements and invariants;
- alternatives considered;
- consequences and tradeoffs;
- implementation boundaries;
- superseding or related decisions.

ADRs integrate with accepted contracts and checkpoint evidence. They do not replace the Constitution, contracts, implementation reports, checksums, canonical source archives, or build evidence.

## Governing hierarchy

```text
SWRLZ Constitution
    ↓
ADRs, contracts, platform maps, and protocol specifications
    ↓
Implementation
    ↓
Evidence
```

## Rules

1. The SWRLZ Constitution is the highest-level governing engineering document.
2. Accepted contracts remain authoritative for normative protocol, trust, identity, privacy, and security behavior within their accepted scope.
3. An ADR MUST NOT silently weaken the Truth Firewall, offline-first behavior, lineage preservation, explicit local-versus-remote distinctions, protocol-version discipline, bounded human authority, or constitutional relationship semantics.
4. An ADR records a decision; it does not itself authorize implementation unless the related checkpoint explicitly grants that authority.
5. Superseded ADRs remain in the repository and are marked `Superseded`; they are not deleted.
6. New applications derived from SWRLZ Core MUST record their app identity, included capabilities, excluded capabilities, signing lineage, and source lineage.
7. Shared capabilities should be composed as modules rather than copied into independent code forks unless an accepted decision explicitly justifies a fork.
8. Architectural language should use the most accurate relationship verb available. The term `consume` should be reserved for operations that genuinely advance, exhaust, deplete, spend, remove, or irreversibly transform a resource.

## Status values

- `Proposed`
- `Accepted`
- `Implemented`
- `Superseded`
- `Rejected`

## Index

| ADR | Title | Status |
|---|---|---|
| [ADR-0001](ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md) | Shared Core capabilities and distinct Android app shells | Accepted |
| [ADR-0002](ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md) | Modular capability and entitlement gates | Accepted |
| [ADR-0003](ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md) | CORE integrator and host-capability composition | Proposed |

## ADR template

```markdown
# ADR-NNNN: Title

- Status:
- Date:
- Checkpoint:
- Constitutional basis:
- Related contracts:

## Context

## Decision

## Requirements

## Alternatives considered

## Consequences

## Implementation boundary

## Verification

## Related decisions
```