# CORE-IMP-006 Evidence Summary

## Verified

- One canonical capsule source lineage and ZIP/SHA identity.
- ZIP SHA-256: `0152ba8a32424d7161ade33359de9d633cb0ae3b8a3b402973f71c585029ddef`.
- Two independent adapters and integration manifests reference the same checksum.
- Ten deterministic compatibility, lifecycle, invocation, and failure-isolation tests pass.
- Android/JVM reference host invocation passes without Android framework dependency or manifest change.
- Plain JVM host invocation passes without optional state service.
- Missing services and incompatible versions return explicit reason codes.
- Adapter failure quarantines the capsule without crashing the test host.
- No permissions, components, network routes, remote fallback, identity mutation, trust decision, entitlement decision, or authorization expansion.
- Detach and rollback procedures documented for both hosts.
- Constitutional terminology review passes.

## Not verified or claimed

Phoenix Firewall readiness, mature CLIENT/SERVER extraction, production hardening, release signing, deployment suitability, dynamic plugins, Android installation, or universal future-host compatibility.
