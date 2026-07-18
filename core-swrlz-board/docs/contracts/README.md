# Contract Registry

Store accepted and proposed normative contracts here. Each contract must declare status, checkpoint, version, scope, compatibility, and implementation authorization state.

## Accepted contract lineage

- `SWRLZ_IDENTITY_SURFACES_CONTRACT_V1.md` — SWRLZ-IDENTITY-SURFACES-001; accepted design contract for physical-device, surface, installation, node, and lineage semantics.
- `SWRLZ_KBD_CONTRACT_V1.md` — SWRLZ-KBD-CON-001; accepted design contract for Android IME trust, privacy, CLIENT enrollment, routing, telemetry, failure, and verification requirements.
- `SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md` — SWRLZ-KBD-IPC-001; accepted design contract for explicit Binder IPC, enrollment wire schemas, caller verification, scoped credentials, replay protection, route reporting, errors, limits, cancellation, and version negotiation.

## Implementation authorization state

These accepted design contracts do not authorize production implementation, Android manifest changes, server schema changes, APK builds, workflow execution, release, or deployment. Those actions require later bounded checkpoints.