# CORE-PLAN-008 Current Handoff

- **Status:** Discovery contract extraction plan complete; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Authoritative main commit at checkpoint start:** `ef62e870e30143912be992972aed89849f186448`
- **Planning branch:** `checkpoint/core-plan-008`
- **Parent branch:** `checkpoint/core-gate-007`
- **Parent audit head:** `13e6cf47c7ba17d36ecf1c2c69cb7cda9cb9fa93`

## Completed repository lineage

### CORE-ARCH-003

Merged through PR #16.

```text
merge commit: 2d17fd4d577e10d805adaac0de1bbda745c8c5ba
```

Established and accepted:

- project-agnostic portable feature capsules;
- host-service composition;
- ATTACH / EXTRACT / REINTEGRATE workflows;
- constitutional relationship terminology;
- Constitution Guardian skill;
- no-op capsule planning and evidence contract.

### CORE-IMP-006

Merged through PR #17.

```text
merge commit: ef62e870e30143912be992972aed89849f186448
```

Added:

- `swrlz.reference.noop` version `0.1.0`;
- two distinct reference adapters;
- deterministic tests;
- canonical ZIP/SHA;
- evidence and rollback records.

No mature app lane was modified.

### CORE-GATE-007

Documentation-only audit remains unmerged on:

```text
checkpoint/core-gate-007
```

Files:

```text
docs/audits/CORE_GATE_007_FIRST_MATURE_FEATURE_EXTRACTION_CANDIDATE_AUDIT.md
docs/handoffs/CORE_GATE_007_CURRENT_HANDOFF.md
```

Recommendation: use a bounded shared discovery contract and codec as the first mature two-host extraction.

### CORE-PLAN-008

Documentation-only plan is recorded on:

```text
checkpoint/core-plan-008
```

This branch was created from the CORE-GATE-007 head, so both the audit and plan remain together in ancestry while `main` remains unchanged.

New planning files:

```text
docs/implementation/CORE_PLAN_008_DISCOVERY_CONTRACT_EXTRACTION_PLAN.md
docs/manifests/SWRLZ_DISCOVERY_CONTRACT_EXTRACTION_MANIFEST_V1.md
docs/contracts/SWRLZ_DISCOVERY_CONTRACT_PORTABLE_API_AND_WIRE_VECTORS_V1.md
docs/evidence/SWRLZ_DISCOVERY_CONTRACT_EQUIVALENCE_AND_ROLLBACK_PLAN_V1.md
docs/handoffs/CORE_PLAN_008_CURRENT_HANDOFF.md
```

## Planned capsule

```text
capsule_id: swrlz.discovery.contract
capsule_version: 0.1.0
contract_version: 1
runtime_target: kotlin-jvm
required host services: none
permissions: none
components: none
network routes: none
storage: none
```

## Exact portable boundary

The capsule may contain:

- protocol/schema constants;
- exact `swrlz-local-node` and `discovery-signature` sentinels;
- success and error JSON models;
- canonical serialization and structured parsing;
- structural validation;
- UUIDv4-prefixed node/installation ID validation;
- compatibility evaluation;
- typed reason codes and nonfatal warnings;
- canonical positive and negative wire vectors.

The capsule must not contain:

- HTTP clients or servers;
- sockets, addresses, interface selection, timeouts, retries, or route fallback;
- Android lifecycle or `Context`;
- identity generation or persistence;
- tokens, proof keys, pairing, trust mutation, or authorization;
- mission behavior;
- CLIENT preferences, UI, navigation, or notifications;
- SERVER runtime health or capability authority.

## Wire contract planned

Protocol-v1/schema-v1 success requires:

```text
ok
service
endpoint
protocolVersion
schemaVersion
nodeId
installationId
displayName
hostVersion
port
capabilities
trust.policy
trust.missionAuthorization
```

Canonical values:

```text
service = swrlz-local-node
endpoint = discovery-signature
protocolVersion = 1
schemaVersion = 1
trust.policy = pairing_required
trust.missionAuthorization = trusted_only
capabilities includes discovery
```

Unknown additive fields are tolerated for supported protocol `1`. Higher additive schema versions are accepted only when every required v1 field validates. Unknown capabilities are ignored for execution. Unknown trust-policy or mission-authorization values fail closed.

## Serializer decision

Use a capsule-internal JSON backend abstraction with an initial `kotlinx.serialization` implementation. Do not expose serializer-library types or exceptions through the public API.

The exact dependency version remains an implementation checkpoint decision after toolchain verification. CORE-PLAN-008 made no Gradle change.

## Mature adapters planned

### SERVER

`DiscoveryProtocol.kt` remains the host HTTP boundary and later delegates only body model validation/serialization through a thin adapter. The SERVER retains:

- listener, method, route, body, and `Accept` enforcement;
- HTTP statuses and headers;
- `NodeRuntime.kt`;
- `NodeIdentityStore.kt`;
- identity and capability truth;
- Android lifecycle and diagnostics.

`NodeCompatibilityProtocol.kt` remains outside the discovery capsule.

### CLIENT

`Api.kt` remains the HTTP boundary. A thin adapter later passes only the JSON body to the capsule and maps validated results to CLIENT-local models. CLIENT retains:

- candidate scanning;
- HTTP execution and cancellation;
- endpoint and identity-aware persistence;
- identity replacement and trust-reassessment workflow;
- UI, navigation, notifications, and recovery.

A temporary named legacy bridge may exist only for explicitly identified pre-contract hosts. It may not create structured identity or trust and may not silently accept malformed protocol-v1 payloads.

## Extraction sequence

1. resolve canonical CLIENT and SERVER ZIP/checksum lineage;
2. freeze wire vectors and origin hashes;
3. implement standalone capsule only;
4. perform SERVER shadow equivalence tests;
5. REINTEGRATE SERVER through a thin adapter;
6. ATTACH CLIENT structured parsing;
7. add CLIENT identity-aware persistence in a separate checkpoint;
8. independently build and verify both hosts;
9. retire duplicate logic only after accepted equivalence and rollback evidence.

Recommended checkpoint decomposition:

```text
CORE-IMP-009A
SERVER-REINT-009B
CLIENT-ATTACH-009C
CLIENT-MIG-009D
DISCOVERY-VER-009E
DISCOVERY-RETIRE-009F
```

## Critical unresolved lineage issue

Historical evidence contains two distinct SHA-256 identities for a file named:

```text
SERVER_CFv1.0.4_SWRLZ.zip
```

Values:

```text
795fe420c43e0d1ad32502869499fede042609bd29bbb2bdeb09cedfcdabee70
32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6
```

The later implementation checkpoint must inspect repository truth and select the exact current ZIP plus sibling checksum. Filename equality is not sufficient. Stop if the authoritative pair cannot be proven.

Known CLIENT candidate:

```text
CLIENT_CFv1.0.1_SWRLZ.zip
9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7
```

Preserved SERVER origin hashes recorded in evidence:

```text
DiscoveryProtocol.kt
36c248b3d2ad5cee53d8a048607b41aa3fea32ae9578afbe5773ae06cee21a97

NodeCompatibilityProtocol.kt
43325599ffa1fb97cf7c508b5eef600aad5e4247cdcf384924e655867754c319
```

## Current non-implementation state

CORE-PLAN-008 did not:

- modify CLIENT or SERVER source;
- create `SOURCES/SHARED_FEATURES/DISCOVERY_CONTRACT/`;
- create capsule Kotlin code;
- change Gradle or workflows;
- build or test either mature app;
- create a capsule ZIP/SHA;
- trigger workflows;
- merge either CORE-GATE-007 or CORE-PLAN-008;
- release, deploy, or install anything.

## Next recommended checkpoint

```text
CORE-IMP-009A — Standalone Discovery Contract Capsule Implementation
```

This checkpoint should implement only:

- pure Kotlin capsule source;
- public API and internal serializer backend;
- descriptor;
- canonical positive/negative vectors;
- deterministic tests;
- package ZIP and sibling SHA-256;
- standalone evidence and rollback records.

It should not modify or attach to CLIENT or SERVER.

## Approval waiting

Approval is waiting to implement the standalone capsule only.

Approval would authorize:

- resolving repository lineage without modifying mature source;
- creating the neutral discovery shared-feature lane;
- implementing pure Kotlin models, codec, reason codes, and warnings;
- selecting a serializer version compatible with verified toolchains;
- running standalone deterministic tests;
- generating canonical ZIP/SHA and evidence;
- committing those bounded files to a checkpoint branch.

Approval would not authorize:

- CLIENT or SERVER source changes;
- SERVER REINTEGRATE;
- CLIENT ATTACH or persistence migration;
- Android APK builds;
- workflow changes or workflow triggers;
- merge into `main`;
- release, deployment, installation, or branch deletion.

Expected result:

One independently verified, project-agnostic `swrlz.discovery.contract` capsule package ready for later mature-host attachment.

Exact approval phrase:

`Approve CORE-IMP-009A — Implement the standalone swrlz.discovery.contract capsule, serializer abstraction, canonical protocol-v1/schema-v1 wire vectors, typed compatibility tests, descriptor, ZIP/SHA package, evidence, and handoff without modifying or attaching to CLIENT or SERVER, changing mature app builds, triggering workflows, merging, releasing, deploying, installing, or deleting branches`