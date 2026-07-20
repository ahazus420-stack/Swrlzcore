# SWRLZ Governance Documentation

Status: active governing documentation system

This directory contains the highest-level principles that govern SWRLZ architecture, implementation, evidence, authority, and organizational continuity.

## Governing hierarchy

```text
Constitution
    ↓
Architecture and contracts
    ↓
Implementation
    ↓
Evidence
```

The Constitution defines enduring principles. Architecture Decision Records, contracts, platform maps, and protocol specifications interpret those principles for bounded technical decisions. Implementation realizes accepted architecture. Evidence demonstrates what was actually built, signed, executed, or verified.

## Index

| Document | Status | Purpose |
|---|---|---|
| [SWRLZ Constitution Foundation](SWRLZ_CONSTITUTION.md) | Accepted foundation | Governing principles for identity, truth, lineage, relationship semantics, authority, capabilities, protocol discipline, offline-first behavior, and organizational continuity |

## Rules

1. Governance documents do not silently replace accepted contracts, ADRs, canonical source archives, checksums, or implementation evidence.
2. Lower-level documents must not silently weaken higher-level constitutional invariants.
3. Constitutional changes require an explicit bounded checkpoint and approval record.
4. Superseded governance documents remain available with their status and lineage recorded.
5. Repository wording should use semantically accurate relationship verbs, especially where identity, authority, trust, lifecycle, or dependency direction are affected.
6. Documentation approval does not authorize source changes, builds, workflows, releases, deployment, installation, enrollment, or remote execution unless explicitly stated.