# CORE-GATE-007 Current Handoff

- **Status:** Mature feature audit complete; extraction planning not yet authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative branch:** `main`
- **Checkpoint branch:** `checkpoint/core-gate-007`
- **Audit:** `docs/audits/CORE_GATE_007_FIRST_MATURE_FEATURE_EXTRACTION_CANDIDATE_AUDIT.md`

## Decision recommendation

Recommend the shared discovery contract and compatibility codec as the first mature portable feature extraction candidate.

Provisional capsule identity:

```text
swrlz.discovery.contract
```

The capsule boundary should contain only pure protocol models, canonical field and sentinel definitions, serialization/parsing, structural validation, compatibility reason codes, version-range evaluation, and deterministic wire vectors.

It must not contain networking, sockets, Android lifecycle, identity persistence, pairing, trust, authorization, proof keys, UI, preferences, retries, or route fallback.

## Origin evidence

SERVER origin evidence:

- `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt`
- `SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/NodeCompatibilityProtocol.kt`

SERVER host-owned boundaries:

- `NodeRuntime.kt`
- `NodeIdentityStore.kt`
- Android service/lifecycle and diagnostics

CLIENT attachment evidence:

- `android/app/src/main/java/sh/swurlz/core/net/Api.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`
- related CLIENT-local models and preferences

## Migration recommendation

Use strangler extraction with a clean portable core:

1. freeze canonical protocol-v1/schema-v1 wire vectors;
2. define the portable API from contracts and both implementations;
3. add delegation adapters without removing old behavior;
4. REINTEGRATE SERVER producer/validator behavior;
5. ATTACH CLIENT parser/compatibility behavior;
6. prove behavioral equivalence;
7. retire duplicated origin logic through explicit lineage.

## Important unresolved evidence

Historical artifacts contain more than one documented SHA-256 identity for a file named `SERVER_CFv1.0.4_SWRLZ.zip`. The extraction checkpoint must identify the current canonical SERVER archive and matching checksum from repository truth before inspecting or moving source.

## Alternatives deferred

- CLIENT verified-admin route policy: lower risk but not naturally cross-project.
- SERVER device-proof subsystem: too security-sensitive for the first mature extraction.
- Phoenix Firewall: authority and Truth Firewall impact require later dedicated planning.
- NODE_HOST runtime/lifecycle and presence registry: host-owned infrastructure and persistence boundaries.

## Explicit non-implementation state

No mature source, feature capsule, shared-feature lane, Gradle file, workflow, build, package, release, deployment, installation, or `main` state was changed.

## Approval state

CORE-GATE-007 authorized inspection and a recommendation only. This branch contains documentation only and remains unmerged.

## Exact next approval phrase

`Approve CORE-PLAN-008 — Define the bounded swrlz.discovery.contract extraction manifest, portable API, canonical wire vectors, CLIENT and SERVER adapter plan, strangler sequence, equivalence evidence, lineage, and rollback without modifying mature source, creating capsule code, changing builds, triggering workflows, merging, releasing, deploying, or installing`
