# SWRLZ Architecture Decision Records

Status: active documentation system

This directory preserves durable architectural decisions for SWRLZ-Core and its descendant applications.

An Architecture Decision Record (ADR) captures:

- the decision and its status;
- the problem and constraints that led to it;
- accepted requirements and invariants;
- alternatives considered;
- consequences and tradeoffs;
- implementation boundaries;
- superseding or related decisions.

ADRs integrate with accepted contracts and checkpoint evidence. They do not replace contracts, implementation reports, checksums, canonical source archives, or build evidence.

## Rules

1. Accepted contracts remain authoritative for normative protocol, trust, identity, privacy, and security behavior.
2. An ADR MUST NOT silently weaken the Truth Firewall, offline-first behavior, lineage preservation, explicit local-versus-remote distinctions, or protocol-version discipline.
3. An ADR records a decision; it does not itself authorize implementation unless the related checkpoint explicitly grants that authority.
4. Superseded ADRs remain in the repository and are marked `Superseded`; they are not deleted.
5. New applications derived from SWRLZ Core MUST record their app identity, included capabilities, excluded capabilities, signing lineage, and source lineage.
6. Shared capabilities should be integrated as modules rather than copied into independent code forks unless an accepted decision explicitly justifies a fork.

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

## ADR template

```markdown
# ADR-NNNN: Title

- Status:
- Date:
- Checkpoint:
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
