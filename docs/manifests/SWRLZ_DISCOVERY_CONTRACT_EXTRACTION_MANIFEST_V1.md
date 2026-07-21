# SWRLZ Discovery Contract Extraction Manifest v1

- **Status:** Planned; extraction not authorized
- **Checkpoint:** CORE-PLAN-008
- **Feature identity:** `swrlz.discovery.contract`
- **Proposed capsule version:** `0.1.0`
- **Capsule contract version:** `1`
- **Protocol range:** `1`
- **Schema range:** `1..MAX_ADDITIVE_SCHEMA` when all protocol-v1 required fields validate
- **Runtime target:** Kotlin/JVM
- **Network behavior:** none
- **Permissions:** none
- **Components:** none
- **Storage:** none
- **Host services required:** none

## 1. Origin projects

### SERVER/NODE_HOST origin evidence

```text
SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt
SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/NodeCompatibilityProtocol.kt
```

Relevant preserved hashes:

```text
DiscoveryProtocol.kt
36c248b3d2ad5cee53d8a048607b41aa3fea32ae9578afbe5773ae06cee21a97

NodeCompatibilityProtocol.kt
43325599ffa1fb97cf7c508b5eef600aad5e4247cdcf384924e655867754c319
```

`DiscoveryProtocol.kt` is origin evidence for success/error body production and discovery contract constants. It must not be copied wholesale because its HTTP request validation and host identity dependencies remain host-owned.

`NodeCompatibilityProtocol.kt` is comparison evidence only. Its `/status` and presence surfaces are not part of this capsule.

### CLIENT attachment evidence

```text
android/app/src/main/java/sh/swurlz/core/net/Api.kt
android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt
related CLIENT-local model and preference paths
```

The current CLIENT behavior includes candidate probing and legacy sentinel recognition. The capsule will provide structured decoding and compatibility evaluation, while probing, caching, presentation, and trust reassessment remain CLIENT-owned.

## 2. Required lineage gate

At implementation start, record and verify:

| Lane | Required evidence |
|---|---|
| CLIENT | exact repository path, ZIP filename, sibling checksum filename, verified SHA-256, archive integrity, relevant file hashes |
| SERVER | exact repository path, ZIP filename, sibling checksum filename, verified SHA-256, archive integrity, relevant file hashes |
| Contract | exact repository contract path, status, commit, content hash |
| Capsule | branch base commit and predecessor status |

Known CLIENT candidate:

```text
CLIENT_CFv1.0.1_SWRLZ.zip
9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7
```

Known SERVER ambiguity:

```text
SERVER_CFv1.0.4_SWRLZ.zip
795fe420c43e0d1ad32502869499fede042609bd29bbb2bdeb09cedfcdabee70

SERVER_CFv1.0.4_SWRLZ.zip
32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6
```

Implementation must stop until repository truth selects one exact current archive/checksum pair. No source movement may rely on filename alone.

## 3. Portable candidates

The capsule may contain only:

- protocol and schema constants;
- service and endpoint sentinels;
- success and error wire models;
- raw trust-policy wire strings;
- validated semantic models;
- JSON encoding and decoding through an internal backend;
- structural validation;
- ID, display-name, host-version, port, capability, trust-policy, protocol, and schema checks;
- compatibility decisions;
- explicit reason codes and nonfatal warnings;
- canonical wire vectors and deterministic tests;
- descriptor, lineage, ZIP/SHA, and evidence records.

## 4. Host-owned dependencies

### SERVER-owned

- listener creation and shutdown;
- local-link interface selection;
- socket limits and request parsing;
- HTTP method, route, body, and `Accept` enforcement;
- status code, reason phrase, content type, cache header, and `Allow` header;
- durable `nodeId` and `installationId` creation and persistence;
- display name, host version, active port, and enabled capability truth;
- runtime health and lifecycle;
- pairing, trust, proof, authorization, and mission enforcement;
- logs and Android diagnostics.

### CLIENT-owned

- candidate URL generation;
- HTTP client execution, timeout, retry, cancellation, and concurrency;
- status and content-type enforcement;
- source URL and observation time;
- saved endpoint and identity-aware persistence;
- user-visible compatibility states;
- trust reassessment and identity replacement workflow;
- legacy-node policy;
- navigation, notifications, and recovery.

## 5. Sensitive boundaries

The capsule must not receive or return:

- tokens;
- authorization headers;
- proof keys;
- private keys;
- device keys;
- encrypted sidecar material;
- user content;
- mission payloads;
- trust grants;
- local preference handles;
- Android `Context`;
- database or file handles;
- network addresses beyond the advertised numeric discovery port in the response model.

## 6. Planned canonical lane

```text
SOURCES/SHARED_FEATURES/DISCOVERY_CONTRACT/
├── source/
├── packages/
├── integrations/
│   ├── server-node-host/
│   └── android-client/
├── docs/
├── OLD_PATCHES/
├── SWRLZ_DISCOVERY_CONTRACT_CAPSULE_v0.1.0.zip
└── SWRLZ_DISCOVERY_CONTRACT_CAPSULE_v0.1.0.sha256
```

This lane is not created by CORE-PLAN-008.

## 7. Planned descriptor

```yaml
capsule_id: swrlz.discovery.contract
capsule_version: 0.1.0
contract_version: 1
runtime_targets:
  - kotlin-jvm
required_services: []
optional_services: []
permissions: []
components: []
network_routes: []
storage:
  namespace: null
  schema_version: null
lifecycle:
  initialize: stateless
  invoke: supported
  stop: stateless
failure_policy: isolated
truth_firewall_impact: none
identity_authority: none
trust_authority: none
authorization_authority: none
```

## 8. Proposed adapter paths

### SERVER

Provisional host-local path:

```text
SWRLZ_NODE_HOST/app/src/main/java/sh/swrlz/nodehost/service/DiscoveryContractAdapter.kt
```

Existing `DiscoveryProtocol.kt` remains the HTTP boundary and delegates body production after later authorization.

### CLIENT

Provisional host-local path:

```text
android/app/src/main/java/sh/swurlz/core/net/DiscoveryContractAdapter.kt
```

`Api.kt` remains the transport boundary. `NetworkDiscoveryScreen.kt` remains presentation. Persistence remains CLIENT-owned.

## 9. Integration-manifest requirements

Each host integration must record:

- exact capsule ZIP and SHA-256;
- capsule source commit;
- host source ZIP and SHA-256;
- adapter path;
- build-graph attachment;
- mapped API calls;
- host-owned responsibilities;
- permissions/components/routes added;
- storage changes;
- compatibility result and reason codes;
- test vector results;
- rollback steps;
- superseded local implementation paths;
- legacy bridge status.

Expected zero-impact declarations for capsule attachment itself:

```yaml
permissions_added: []
components_added: []
network_routes_added: []
capsule_storage_added: []
identity_authority_added: false
trust_authority_added: false
authorization_authority_added: false
```

## 10. Migration classification

```text
strategy: strangler extraction with clean portable core
origin_first_host: SERVER/NODE_HOST
second_host: Android CLIENT
```

SERVER becomes the first mature host to REINTEGRATE the capsule because its existing serializer is the producer origin. CLIENT then ATTACHES the same canonical capsule for parsing and compatibility evaluation.

## 11. Retirement state

At version `0.1.0`:

```text
predecessor capsule: none
successor capsule: none
retirement status: active only after acceptance
origin logic status: retained until REINTEGRATE and equivalence acceptance
legacy CLIENT bridge: temporary and separately gated
```

No origin implementation is deleted during initial attachment.

## 12. Approval boundary

This manifest defines a future extraction. It does not authorize source code, shared-feature directories, Gradle edits, builds, workflows, ZIP generation, mature host attachment, merge, release, deployment, or installation.