# CORE-IMP-009A — Standalone Discovery Contract Capsule Implementation

- **Status:** Implemented on checkpoint branch; not merged and not attached to mature hosts
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/core-imp-009a`
- **Implementation base:** `00bb23d7f4fed389a7b24e7ec2127399da0d924f`
- **Current main at close:** `961e92907acb6a3158f6da982902f07acbfba019`
- **Capsule:** `swrlz.discovery.contract`
- **Version:** `0.1.0`
- **Contract version:** `1`
- **Runtime:** Kotlin/JVM

## Implemented

- neutral `SOURCES/SHARED_FEATURES/DISCOVERY_CONTRACT/` lane;
- immutable public models and typed result surface;
- protocol-v1/schema-v1 constants and exact sentinels;
- internal JSON-backend abstraction;
- canonical `kotlinx.serialization` JSON-tree backend;
- strict portable verification backend;
- structural and semantic validation;
- UUIDv4-prefixed node and installation validation;
- higher additive schema handling;
- unknown-field and unknown-capability warnings;
- fail-closed trust-policy and mission-authorization handling;
- canonical success/error vectors;
- positive, negative, producer-validation, and determinism tests;
- descriptor, package, checksum, evidence, rollback, and handoff.

## Verification result

```text
43 passed
0 failed
```

The complete production source surface passed an API-shape compile check. Official Maven dependency resolution was unavailable in the execution environment and is explicitly not overclaimed.

## Boundary preservation

No CLIENT, SERVER/NODE_HOST, Keyboard, Launcher, or CORE_BASE source was modified. No Android API, networking, route selection, storage, identity generation, trust mutation, authorization, mission execution, UI, workflow, mature-app build, release, deployment, or installation behavior was added.

## Package

- Filename: `SWRLZ_DISCOVERY_CONTRACT_CAPSULE_v0.1.0.zip`
- SHA-256: `e0b139a84aaf5a5ea470fbea03c6f42dca987620c8459a3577d27c91058e484e`
- Archive integrity: PASS
- Unsafe paths: none
- Duplicate entries: none
- Deterministic rebuild: PASS

## Concurrent repository truth

While this checkpoint was in progress, separately authorized work advanced `main` and canonicalized SERVER v1.0.4 as the active source pair with SHA-256 `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`; SERVER v1.0.3 was preserved under `OLD_PATCHES/`.

The capsule branch was not rebased or merged. Only this checkpoint's evidence and handoff were reconciled to current repository truth.
