# SWRLZ Platform Map — Current-State Module, Identity, and Dependency Mapping

- **Status:** Completed audit baseline
- **Checkpoint:** SWRLZ-PLATFORM-MAP-001A
- **Approval phrase:** `APPROVE SWRLZ-PLATFORM-MAP-001A CURRENT-STATE MODULE IDENTITY AND DEPENDENCY MAPPING`
- **Date:** 2026-07-19
- **Scope:** Repository inspection and documentation only
- **Governing map:** `docs/architecture/SWRLZ_PLATFORM_MAP_V1.md`
- **Governing decisions:** ADR-0001 and ADR-0002

## 1. Executive finding

The repository currently contains one directly inspectable unpacked Android Gradle project under `SOURCES/CORE_BASE/source/`. That project is already split into four Gradle modules:

```text
:app
:core
:designsystem
:featurehome
```

The present dependency direction is:

```text
:app
 +-- :core
 +-- :designsystem
 `-- :featurehome
```

This is a useful modular seed, but it is not yet the accepted multi-shell platform architecture. `:app` is the only installable module in the inspected project, and no checked-in `:keyboard`, `:launcher`, `:client`, or `:nodehost` Gradle app modules were confirmed in this source tree.

CLIENT and SERVER/NODE-related lanes exist in the repository primarily as canonical source ZIPs, checksum files, workflows, update metadata, release evidence, reports, and contracts. They are therefore separate source and evidence lanes, not yet demonstrated as composed modules inside the unpacked `CORE_BASE` Gradle graph.

## 2. Confirmed current Gradle graph

The root project is named `Swrlzing-core` and includes exactly four modules:

| Module | Current role | Android plugin class | Direct consumers | Classification against Platform Map |
|---|---|---|---|---|
| `:app` | Installable Core application shell | Android application | none | current Core shell seed |
| `:core` | Domain/data foundation | Android library | `:app` | shared-platform candidate |
| `:designsystem` | Compose theme and reusable presentation | Android library | `:app` | shared-but-evolvable candidate |
| `:featurehome` | Current home screen feature | Android library | `:app` | shell presentation or future capability candidate |

No circular Gradle dependency is visible in the confirmed graph.

## 3. Confirmed Core Android identity

The inspected `:app` module declares:

| Field | Current value |
|---|---|
| Namespace | `com.swrlz.core.app` |
| Application ID | `com.swrlz.core.app` |
| Version code | `1` |
| Version name | `1.0.0` |
| Compile SDK | `34` |
| Target SDK | `34` |
| Minimum SDK | `24` |
| JVM target | `17` |

This is a concrete current identity for the unpacked `CORE_BASE` project. It must not be assumed to be the final accepted identity for all historical Core APK lanes without signer and artifact reconciliation.

## 4. Current source placement

### 4.1 `:app`

Confirmed responsibilities:

- Android application plugin and package identity;
- application lifecycle entry point;
- main activity and launcher presentation;
- Hilt application wiring;
- composition of `:core`, `:designsystem`, and `:featurehome`.

Recommended target classification:

- keep shell-specific lifecycle, navigation, icon, label, manifest, and Android role here;
- do not place shared identity, trust, lineage, protocol, mission, or route policy directly in this shell when reusable module boundaries exist.

### 4.2 `:core`

Confirmed contents are small domain/data primitives, including `DomainModels.kt` and `FeatureRepository.kt`.

Recommended target classification:

- preserve as the seed for shared platform code;
- split only through bounded migrations when concrete identity, lineage, trust, storage, protocol, mission, or capability-policy responsibilities become substantial;
- avoid turning it into an undifferentiated catch-all module.

### 4.3 `:designsystem`

Confirmed responsibility:

- reusable Compose theme/presentation primitives.

Recommended target classification:

- shared but evolvable;
- no trust, authority, entitlement, route, or identity decisions should be enforced solely in this module.

### 4.4 `:featurehome`

Confirmed responsibility:

- current Core home screen presentation.

Placement decision still required:

- retain as Core-specific `featurehome` if it remains a Core dashboard;
- rename or split later only if it becomes a reusable capability explorer or is consumed by additional app shells.

## 5. App-shell current-state mapping

| Intended shell | Current repository evidence | Confirmed Gradle app module in `CORE_BASE` | Current-state conclusion |
|---|---|---:|---|
| Core | unpacked source, manifests, Gradle modules, source manifest, build lanes | yes: `:app` | concrete shell seed exists |
| Keyboard | contracts, documentation, earlier build-lane evidence | no | not yet represented as a distinct checked-in module in this graph |
| Launcher | earlier build-lane evidence and architecture decisions | no | not yet represented as a distinct checked-in module in this graph |
| CLIENT | canonical ZIP/checksum lane, update metadata, build provenance and APK evidence | no | separate canonical product lane; package/module reconciliation still required |
| NODE_HOST / SERVER | SERVER source/checksum lane, contracts/reports/workflows | no | separate canonical product lane; Android shell identity still requires direct source audit |

## 6. CLIENT lineage observation

The inspected CLIENT build provenance demonstrates that a canonical CLIENT source ZIP was unpacked and built as a separate single-app Gradle project under an `android/app` path. The recorded build identity was:

- Android version name: `0.2.7.6-cf7-netpulse`;
- Android version code: `29`;
- source ZIP identified by the build report as `SRC_CF7_276_NETPULSE.zip`;
- output APK identified as `APK_CF7_276_NETPULSE_DEBUG.apk`.

The build log also shows source packages under `sh/swurlz/core/...`, which differs from the unpacked `CORE_BASE` namespace `com.swrlz.core.app`. This is a confirmed identity/source-lineage mismatch requiring deliberate reconciliation; it must not be normalized by assumption.

The CLIENT provenance report does not, by itself, establish the final APK `applicationId` or durable release signer lineage. Those remain evidence gaps until the canonical CLIENT Gradle file and signed artifact metadata are directly inspected together.

## 7. Dependency and composition gap

Current implementation state:

```text
CORE_BASE app
  -> core
  -> designsystem
  -> featurehome
```

Accepted target state:

```text
Distinct app shells
  -> shared platform modules
  -> selected capability modules
```

Missing bridge between those states:

1. no checked-in multi-shell Gradle composition;
2. no typed capability registry implementation;
3. no per-shell composition manifest;
4. no confirmed shared identity/lineage/trust module boundaries;
5. no directly confirmed package/signing/version matrix covering Core, Keyboard, Launcher, CLIENT, and NODE_HOST;
6. no build-time enforcement preventing shell modules from acquiring authority merely through dependency inclusion.

## 8. Candidate module boundaries

These are recommendations, not implementation claims.

### Stable shared-platform candidates

```text
:platform:identity
:platform:lineage
:platform:trust
:platform:truthfirewall
:platform:protocol
:platform:storage
:platform:routing
:platform:audit
:platform:update-metadata
:platform:capability-policy
```

### Shared-but-evolvable candidates

```text
:capability:missions
:capability:discovery
:capability:notifications
:capability:local-ai-api
:capability:sync
:ui:designsystem
:ui:capability-explorer
```

### Distinct shell candidates

```text
:apps:core
:apps:keyboard
:apps:launcher
:apps:client
:apps:nodehost
```

### Role-specific capability candidates

```text
:capability:keyboard
:capability:launcher
:capability:client
:capability:nodehost
```

This naming is illustrative. No directory rename or module creation is authorized by this report.

## 9. Dependency rules recommended for acceptance before migration

1. App shells may depend on shared platform and capability modules.
2. Shared platform modules must not depend on app shells.
3. Capability modules must not infer authorization from being packaged.
4. UI modules must not become the sole enforcement point for trust or entitlement.
5. CLIENT and NODE_HOST capability modules may share primitives but must not merge authority.
6. Identity, lineage, trust, protocol, and Truth Firewall modules require acyclic dependency direction.
7. Local-versus-remote routing decisions must remain below shell presentation and preserve explicit route semantics.
8. `:core` should be decomposed only when a tested boundary is clearer than the present cohesive seed.

## 10. Identity and signing evidence gaps

The audit confirms the following unresolved evidence requirements:

- final permanent package IDs for Keyboard and Launcher;
- direct canonical CLIENT `applicationId` and namespace inspection;
- direct canonical NODE_HOST/SERVER Android `applicationId` and namespace inspection;
- durable signer fingerprints per app lineage;
- release versus debug signing separation;
- monotonic version-code history for each lineage;
- side-by-side installation evidence using final identities;
- same-lineage update evidence using stable signers.

Earlier APK forensics found that Core, Keyboard, and Launcher test artifacts contained identical functional payload entries while using different signing certificates. That is consistent with independent ephemeral debug signing and is not a durable update architecture.

## 11. Migration roadmap

### Stage 0 — Evidence completion

- inspect canonical CLIENT and NODE_HOST Gradle identity files;
- record artifact package IDs, version codes, version names, and signer fingerprints;
- publish one authoritative app identity matrix.

### Stage 1 — Shell identity acceptance

- accept permanent package identifiers;
- accept signer-key strategy;
- accept version-code ownership and progression rules;
- define retirement/rollback lineage.

### Stage 2 — Minimal multi-shell scaffold

- introduce distinct app-shell modules without moving shared behavior unnecessarily;
- preserve current Core behavior;
- prove side-by-side installation before capability extraction.

### Stage 3 — Shared-platform extraction

- extract identity, lineage, trust, protocol, Truth Firewall, and audit boundaries one bounded module at a time;
- add contract and regression evidence for each extraction.

### Stage 4 — Capability composition

- implement typed capability identifiers and composition declarations;
- separate packaged, entitled, configured, available, trusted, policy-allowed, and protocol-compatible states;
- add per-shell dependency evidence.

### Stage 5 — Optional combined-role shells

- consider CLIENT plus NODE_HOST composition only after independent roles are stable;
- preserve separate identities, credentials, lifecycle, scope, and authority internally.

## 12. Risks

- premature directory restructuring could sever canonical ZIP lineage;
- reusing one package ID across shells would repeat the installation collision;
- changing a package ID after durable distribution would create a new app lineage;
- changing signers without an accepted rotation mechanism would break updates;
- moving behavior into a shared module without authority boundaries could widen privilege;
- extracting tiny modules too early could create dependency complexity without architectural value;
- treating SERVER and NODE_HOST as interchangeable terms could blur hosted-server, Android-node, and local-node distinctions.

## 13. Rollback considerations

Documentation and future migration checkpoints must preserve:

- the original canonical source ZIP and SHA-256;
- pre-migration module graph;
- prior package, signer, version, installation, and surface identity evidence;
- explicit retired/replaced lineage links;
- the ability to restore the last accepted Core source lane without claiming that descendants overwrite it.

## 14. Checkpoint conclusion

SWRLZ-PLATFORM-MAP-001A establishes a repository-grounded current-state baseline:

- one confirmed unpacked four-module Core Gradle project;
- one current Core application identity in that project;
- separate CLIENT and SERVER/NODE source/evidence lanes;
- no confirmed multi-shell Gradle composition yet;
- a recommended migration sequence that begins with identity and signer evidence rather than source movement.

This checkpoint does not authorize source movement, package changes, signing changes, builds, workflows, releases, or deployment.
