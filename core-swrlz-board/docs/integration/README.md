# CLIENT Integration Documentation

Covers Device Identity Broker enrollment, scoped credentials, local IPC, revocation, offline synchronization, NODE_HOST routing, Truth Firewall preservation, and failure recovery.

The canonical CLIENT remains the source of truth; this lane documents integration rather than duplication.

## Accepted design evidence

- `SWRLZ_KBD_IPC_001_ACCEPTANCE_MATRIX.md` — verification obligations for SWRLZ-KBD-IPC-001.
- Normative wire contract: `../contracts/SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md`.

No document in this directory independently authorizes production source changes, manifest edits, builds, workflows, release, or deployment.