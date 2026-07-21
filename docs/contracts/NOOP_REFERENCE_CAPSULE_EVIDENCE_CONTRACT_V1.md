# No-Op Reference Capsule Evidence Contract v1

- **Status:** Planned evidence contract; implementation not authorized
- **Version:** 1
- **Checkpoint:** CORE-PLAN-005
- **Related plan:** `docs/implementation/CORE_PLAN_005_NOOP_REFERENCE_CAPSULE_IMPLEMENTATION_PLAN.md`
- **Constitutional authority:** `docs/governance/SWRLZ_CONSTITUTION.md`
- **Architecture authority:** ADR-0003 and `CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1`

## 1. Purpose

Define the minimum evidence required to claim that one canonical no-op portable feature capsule attaches to two independent hosts through distinct adapters without source duplication, authority expansion, silent fallback, or constitutional language drift.

## 2. Claim classes

Evidence must distinguish:

- source and lineage claims;
- package and checksum claims;
- compatibility claims;
- attachment claims;
- lifecycle claims;
- invocation claims;
- failure-isolation claims;
- permission and authority claims;
- rollback claims;
- build and runtime claims.

Passing one class does not imply another.

## 3. Canonical source evidence

Required:

- capsule source path;
- source checkpoint and commit;
- source ZIP filename;
- sibling SHA-256 filename;
- verified SHA-256 value;
- archive-integrity result;
- descriptor version;
- contract version;
- predecessor, successor, and retirement status where applicable.

Two hosts must reference the same canonical capsule source identity and package checksum.

## 4. Descriptor evidence

Required:

- exact descriptor file;
- parsed capsule ID and version;
- runtime targets;
- required and optional services;
- permissions and components;
- storage namespace and schema version;
- network route declaration;
- lifecycle declaration;
- failure policy;
- Truth Firewall impact;
- descriptor validation result.

## 5. Host attachment evidence

For each host, record:

- host identity and source checkpoint;
- adapter path;
- integration-manifest path;
- capsule ID/version/checksum reference;
- service mappings;
- optional-service mappings;
- permissions added;
- components added;
- storage and route effects;
- build-graph relationship;
- rollback/detach procedure.

The two adapters must be independently implemented against the same stable service contracts.

## 6. Compatibility evidence

For each host, prove:

- runtime target supported;
- contract version supported;
- required services present and correctly versioned;
- optional service absence handled correctly;
- descriptor and integration manifest valid;
- explicit compatibility state and reason code recorded.

Negative tests must cover missing required service, incompatible service version, invalid descriptor, invalid integration manifest, and checksum mismatch.

## 7. Lifecycle evidence

For each host, record results for:

- descriptor inspection;
- compatibility validation;
- initialization;
- repeated initialization;
- registration of availability;
- start where applicable;
- invocation;
- stop;
- restart or clean reinitialization;
- detach or retirement simulation.

Undefined or generic failure states are insufficient.

## 8. Invocation evidence

A successful invocation must return a deterministic typed result containing only approved metadata such as:

- capsule ID;
- capsule version;
- host-provided timestamp;
- execution state;
- reason code where applicable.

It must not include user content, secret material, device identifiers, or unrelated host state.

## 9. Failure-isolation evidence

Required tests:

- missing required service rejects capsule only;
- malformed descriptor rejects capsule only;
- adapter exception does not crash unrelated host startup;
- invocation before initialization is rejected explicitly;
- optional service absence does not block execution;
- repeated failure enters explicit unavailable or quarantined state where designed;
- no other capsule or host storage is mutated;
- no remote or alternate route is attempted.

## 10. Permission, component, and authority evidence

For both hosts, prove:

- no new Android permission unless explicitly approved;
- no new Android component unless explicitly approved;
- no network route;
- no trust, enrollment, entitlement, identity, or authorization expansion;
- no access to unrelated storage;
- no package-name or project-name compatibility shortcut;
- no silent fallback.

A zero-change manifest diff is preferred for the Android reference host.

## 11. Constitutional terminology evidence

Review all changed documentation, manifests, comments, reports, and user-facing labels.

Required relationship terms:

- host **exposes** services;
- adapter **translates** services;
- project **attaches** or **composes** capsule;
- registry **registers** availability;
- runtime **invokes** behavior;
- package **preserves** lineage.

Any use of `consume` must be justified as genuinely depleting or irreversible. Reusable module relationships must not be described as consumption.

## 12. Build evidence

For each host, record:

- exact source commit;
- build command or Gradle task;
- toolchain versions;
- build conclusion;
- deterministic artifact name where applicable;
- artifact size;
- artifact SHA-256;
- logs and provenance location.

One host's successful build does not prove the other host.

## 13. Runtime evidence

For each host, record as applicable:

- initialization result;
- invocation result;
- shutdown result;
- restart behavior;
- observed crash status;
- log or test report location.

Android installation or UI evidence is required only if the approved implementation checkpoint includes an installable Android test host.

## 14. Rollback evidence

Each host must document and verify that detaching the capsule removes:

- module or package reference;
- adapter;
- integration manifest;
- capsule-owned transient state where appropriate;

without deleting lineage, reports, checksums, or unrelated host data.

## 15. Evidence bundle structure

Recommended bundle:

```text
NOOP_REFERENCE_CAPSULE_EVIDENCE/
├── provenance/
├── descriptor/
├── package/
├── host-a/
│   ├── integration-manifest/
│   ├── compatibility/
│   ├── lifecycle/
│   ├── build/
│   └── rollback/
├── host-b/
│   ├── integration-manifest/
│   ├── compatibility/
│   ├── lifecycle/
│   ├── build/
│   └── rollback/
├── terminology-review/
└── SUMMARY.md
```

## 16. Acceptance conditions

The reference implementation may be called verified only when:

1. both hosts reference the same canonical capsule ZIP/SHA identity;
2. both expose required services through distinct adapters;
3. compatibility and lifecycle tests pass;
4. deterministic invocation succeeds on both hosts;
5. negative tests preserve explicit reason codes;
6. optional failure remains isolated;
7. no permissions, components, routes, or authority expand silently;
8. rollback is documented and verified;
9. constitutional terminology review passes;
10. every claim is linked to evidence.

## 17. Prohibited overclaims

Without separate evidence, do not claim:

- Phoenix Firewall readiness;
- mature CLIENT or SERVER feature extraction readiness;
- production security hardening;
- release signing continuity;
- production deployment suitability;
- dynamic plugin support;
- compatibility with every future host.

## 18. Non-authorization

This evidence contract does not authorize source modules, app changes, shared-feature directories, Gradle edits, builds, workflow runs, merge, release, deployment, or installation.