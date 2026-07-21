# SWRLZ Discovery Contract Equivalence and Rollback Plan v1

- **Status:** Planned evidence contract; implementation not authorized
- **Checkpoint:** CORE-PLAN-008
- **Capsule:** `swrlz.discovery.contract` version `0.1.0`
- **Applies to:** standalone capsule, SERVER REINTEGRATE, CLIENT ATTACH, retirement

## 1. Purpose

Define the evidence required to prove that the discovery contract capsule preserves accepted behavior while keeping networking, identity, trust, authorization, persistence, UI, and lifecycle inside their owning hosts.

Passing one evidence class does not imply another. A successful standalone capsule test does not prove mature-host integration, device behavior, trust safety, or production readiness.

## 2. Evidence classes

Required evidence is divided into:

1. lineage and source identity;
2. capsule package integrity;
3. API and descriptor conformance;
4. wire-vector conformance;
5. SERVER producer equivalence;
6. CLIENT parser and compatibility equivalence;
7. host-boundary preservation;
8. security and authority non-expansion;
9. build and runtime evidence;
10. rollback and retirement evidence.

## 3. Lineage evidence

Before source changes, record:

- repository and branch;
- base commit;
- exact CLIENT ZIP and SHA-256;
- exact SERVER ZIP and SHA-256;
- archive integrity and path-safety results;
- exact origin path hashes;
- accepted contract path, status, commit, and content hash;
- capsule predecessor status;
- all historical same-name archive collisions.

Hard stop conditions:

- missing sibling checksum;
- checksum mismatch;
- duplicate or unsafe archive entries;
- unresolved current SERVER archive identity;
- origin file hash differs without an accepted successor record;
- contract source or status cannot be proven.

## 4. Standalone capsule evidence

`CORE-IMP-009A` must prove:

- one pure Kotlin/JVM implementation;
- no Android imports;
- no network APIs;
- no filesystem, database, preferences, or secure-storage APIs;
- no clock, random, identity-generation, token, proof, trust, or mission dependency;
- descriptor declares no services, permissions, components, routes, or storage;
- canonical success and error bytes match the planned vectors;
- positive vectors pass with exact warning sets;
- negative vectors return exact reason codes;
- producer validation rejects invalid host input;
- repeated runs are deterministic;
- ZIP and sibling SHA-256 verify;
- terminology review passes.

## 5. SERVER equivalence evidence

### 5.1 Scope

The SERVER adapter may change body production only. The following must remain byte-identical or semantically unchanged unless separately approved:

- listener port and interface restrictions;
- route and method handling;
- request body and `Accept` validation;
- status codes and reason phrases;
- `Content-Type`, `Cache-Control`, and `Allow` headers;
- durable identity storage and lifecycle;
- host version source;
- enabled capability truth;
- runtime start/stop behavior;
- logs and diagnostics;
- all `/status`, `/health`, and presence routes.

### 5.2 Shadow comparison

Before switching runtime delegation, tests must execute:

```text
existing SERVER serializer
            vs
capsule codec through SERVER adapter
```

Required comparisons:

- canonical success exact UTF-8 bytes;
- quotes, backslashes, control characters, and Unicode escaping;
- node and installation ID values unchanged;
- field types and nullability unchanged;
- required capability unchanged;
- trust policy values unchanged;
- canonical error-body bytes for each currently emitted discovery error;
- no additional fields or removed fields;
- no host identity mutation;
- no extra runtime response.

Where exact bytes differ only in JSON member order or equivalent escaping, semantic equality may be documented, but the initial migration should preserve canonical bytes. Any accepted byte difference requires explicit classification and approval before runtime delegation.

### 5.3 Negative SERVER vectors

The host must still enforce:

| Condition | Host-owned expected result |
|---|---|
| wrong route | `404` JSON error |
| wrong method | `405` and `Allow: GET` |
| request body present | `400` JSON error |
| JSON not accepted | `406` JSON error |
| identity unavailable or invalid | `503`; never partial success |
| host version unavailable | `503`; never partial success |
| unexpected host failure | bounded `500`; no secrets |

The capsule may encode error bodies; it does not decide these HTTP outcomes.

## 6. CLIENT equivalence evidence

### 6.1 Structured parser behavior

The CLIENT adapter must prove:

- HTTP `200` and compatible JSON content type remain required at the host boundary;
- canonical protocol-v1 response decodes successfully;
- exact sentinels remain required;
- required fields and UUIDv4 prefixes validate;
- protocol `2` rejects;
- higher additive schema with protocol `1` accepts when all required v1 fields validate;
- unknown fields are ignored with warnings;
- unknown capability is ignored for execution;
- unknown trust policy or mission authorization fails closed;
- malformed or partial payloads reject with typed reason codes;
- response body alone cannot create trust.

### 6.2 Identity-aware host policy

Separate CLIENT-host tests must prove:

| Existing record | New response | Host-owned result |
|---|---|---|
| same URL/node/install | same URL/node/install | update observation only |
| same node | different installation | reinstall/reset state; require trust reassessment |
| same URL | different node | different node; no inherited trust |
| invalid identity | any URL | reject discovery |
| legacy response | explicitly identified legacy host | legacy-compatible state only; no structured identity or trust |

The capsule returns validated claims; the CLIENT owns persistence and trust reassessment.

### 6.3 Legacy bridge evidence

The legacy bridge must be:

- named and isolated;
- disabled for protocol-v1-shaped malformed payloads;
- unable to create a structured node identity;
- unable to establish or transfer trust;
- separately observable in diagnostics;
- covered by an explicit retirement condition.

No generic catch-all fallback is permitted.

## 7. Cross-host canonical identity evidence

Both mature hosts must reference:

- the same capsule ID and version;
- the same source commit;
- the same source ZIP filename;
- the same source ZIP SHA-256;
- the same contract version;
- the same canonical vector set.

Distinct host adapters are required. Copying the capsule implementation into either host does not satisfy attachment.

## 8. Host-boundary preservation matrix

| Concern | Capsule | SERVER | CLIENT |
|---|---|---|---|
| wire fields and semantics | canonical | references | references |
| JSON encode/decode | canonical | invokes encode | invokes decode |
| listener and sockets | none | authoritative | none |
| HTTP client | none | none | authoritative |
| identity generation/storage | none | authoritative | observes claims only |
| candidate scanning | none | none | authoritative |
| endpoint persistence | none | none | authoritative |
| capability truth | validates shape | authoritative producer | interprets known values |
| trust establishment | none | separate authority | separate workflow |
| authorization | none | separate authority | no grant from discovery |
| UI | none | host diagnostics | authoritative presentation |
| local/LAN/remote routing | none | listener policy | scan/route policy |

Every implementation diff must be classified against this matrix.

## 9. Security and authority non-expansion

Evidence must prove:

- discovery remains read-only;
- no token or secret is added to a discovery response;
- no proof key enters the capsule;
- no private key or device key enters the capsule;
- no trust state is created by successful parsing;
- no mission route is authorized by advertised capability;
- no identity is generated or mutated by the capsule;
- no network route is selected by the capsule;
- no local-to-remote fallback exists;
- no Android permission or component is added by capsule attachment alone;
- no unrelated host storage is accessed;
- Truth Firewall objection, refusal, pause, and safer-alternative behavior are unaffected.

## 10. Build evidence

Builds remain separately approval-gated.

When authorized, record independently for:

### Capsule

- Kotlin and JDK versions;
- exact build/test command;
- test result;
- artifact size and SHA-256;
- ZIP integrity;
- logs and provenance.

### SERVER

- exact SERVER source ZIP and SHA;
- capsule ZIP and SHA;
- adapter diff;
- Gradle/JDK/Android toolchain;
- build result and APK SHA;
- focused protocol tests;
- device runtime result.

### CLIENT

- exact CLIENT source ZIP and SHA;
- capsule ZIP and SHA;
- adapter and persistence diff;
- Gradle/JDK/Android toolchain;
- build result and APK SHA;
- focused parser tests;
- device discovery result.

One successful host build does not prove the other.

## 11. Device/runtime evidence

When separately authorized, verify:

1. start NODE_HOST;
2. private `8080` status/health behavior remains unchanged;
3. request LAN discovery on `8787`;
4. confirm `200`, JSON content type, no-store, canonical body;
5. confirm node and installation identities remain stable across stop/start and process restart;
6. confirm CLIENT discovers and structurally validates the node;
7. confirm unknown additive fields do not break protocol-v1 discovery;
8. confirm incompatible protocol fails visibly;
9. confirm discovery does not mark the node trusted;
10. confirm identity replacement does not inherit trust;
11. confirm no public/remote route is silently enabled;
12. confirm no observed crash in either host.

## 12. Rollback — standalone capsule

Before mature attachment, rollback is:

- remove the unaccepted shared-feature branch or module reference;
- preserve planning, vectors, reports, ZIP, checksum, and failure evidence;
- do not alter mature hosts.

No host behavior changes in the standalone checkpoint.

## 13. Rollback — SERVER

The SERVER integration checkpoint must preserve:

- exact pre-integration source ZIP and checksum;
- original `DiscoveryProtocol.kt` hash and source;
- host adapter diff;
- capsule ZIP and checksum;
- one-command or deterministic procedure to restore original body serialization;
- proof that `NodeRuntime.kt`, `NodeIdentityStore.kt`, manifest, and unrelated routes are not removed.

Rollback procedure:

1. restore the pre-integration `DiscoveryProtocol.kt` implementation or disable capsule delegation through the bounded adapter change;
2. remove the capsule build reference and adapter;
3. preserve integration manifest and reports;
4. rebuild only under separate approval;
5. verify canonical pre-integration vectors.

Rollback must not rotate identity, clear app data, or modify trust records.

## 14. Rollback — CLIENT

The CLIENT integration checkpoint must preserve:

- exact pre-integration source ZIP and checksum;
- original discovery probe/parser paths and hashes;
- preference schema before migration;
- capsule ZIP and checksum;
- adapter and persistence migration diff;
- a reversible or forward-safe persistence plan.

Rollback procedure:

1. remove the capsule parser adapter and build reference;
2. restore the prior structured/legacy behavior for the approved compatibility window;
3. preserve newly observed node identities as archived lineage rather than deleting them;
4. do not silently reattach trust to URL-only records;
5. preserve reports and failure evidence;
6. rebuild only under separate approval.

If new persistence cannot be safely downgraded, rollback must be forward-compatible: old code ignores new fields while retained records remain nonauthoritative for trust.

## 15. Detach verification

For each host, simulate or prove that detaching removes:

- capsule module/package reference;
- host adapter;
- integration manifest activation;
- capsule-specific tests from the active build graph;

without deleting:

- host identity;
- trust records;
- endpoint history;
- logs and evidence;
- unrelated host data;
- canonical ZIP/checksum lineage.

## 16. Retirement conditions

Superseded source may be retired only when:

- capsule package is accepted;
- SERVER REINTEGRATE passes build and runtime evidence;
- CLIENT ATTACH passes build and runtime evidence;
- both hosts reference one canonical checksum;
- all wire vectors pass;
- identity/trust boundary evidence passes;
- rollback is verified;
- the legacy bridge retirement is separately approved;
- retired source is linked through lineage and not silently erased.

## 17. Prohibited overclaims

Without separate evidence, do not claim:

- cryptographic discovery authenticity;
- pairing readiness;
- trust or authorization readiness;
- Phoenix Firewall readiness;
- production security hardening;
- remote/public-internet suitability;
- universal future-host compatibility;
- release signing continuity;
- successful mature-host extraction before both REINTEGRATE and ATTACH complete.

## 18. Approval boundary

This evidence and rollback plan does not authorize source, shared-feature creation, Gradle changes, builds, workflow runs, packaging, mature host attachment, merge, release, deployment, or installation.