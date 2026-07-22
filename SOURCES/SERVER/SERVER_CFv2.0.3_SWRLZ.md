# SERVER CFv2.0.3 SWURLZER Delivery Receipt

**Patch:** `SWURLZER-LIVING-CITADEL-EFFECTS`  
**Visible identity:** SWURLZER  
**Transport identity:** SERVER  
**Integration law:** Integrate, do not overwrite.

## Final transport pair

- Source: `SERVER_CFv2.0.3_SWRLZ.zip`
- SHA-256: `c1176f1e7dd1fd52bb20bd85b90e22ddb36c4b151f0edc79c2af82f9c1ba3966`
- Checksum receipt: `SERVER_CFv2.0.3_SWRLZ.sha256`
- Archive size: `18812524` bytes
- Packaged files: `86`
- ZIP central-directory/integrity test: **PASS**
- Clean extraction: **PASS**
- Extracted-package verifier: **PASS**
- Final checksum recalculation/comparison: **PASS**
- APK/Gradle build: **NOT RUN**
- CFv2.0.3 compilation: **pending GitHub verification**

The ZIP was not modified or repackaged after this checksum was generated.

## Baseline and GitHub evidence

- Canonical baseline: `SERVER_CFv2.0.2_SWRLZ.zip`
- Baseline SHA-256: `6978332cfe46a3a1d7154c25fe8f677e48bed2fee24cdf506307cfb830af057a`
- Baseline Android identity: `versionCode 5` / `versionName 2.0.2`
- CFv2.0.2 GitHub workflow: `SWRLZ APK Router — Manual / Auto`
- Commit: `cf68aaeb216ff4bb52e7f67a277537c3f5cb800e`
- Result: `SUCCESS`
- Duration: `6m 32s`
- Artifact count: `1`
- New Android identity: `versionCode 6` / `versionName 2.0.3`
- Read-only visual reference: `CLIENT_CFv2.0.2_SWRLZ.zip`
- CLIENT reference SHA-256: `95e84eeb74776a1d4fe719678fc0743f11088d3460d9b721abcc20d71b314d0c`

CLIENT source was not modified.

## Exact modified-path list

Modified:

- `README.md`
- `ReleaseNotes.md`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/sh/swrlz/nodehost/ui/HostScreen.kt`
- `app/src/main/java/sh/swrlz/nodehost/ui/SwurlzerUserScreen.kt`
- `app/src/main/java/sh/swrlz/nodehost/ui/SwurlzerVisuals.kt`
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml`

Added:

- `app/src/main/res/values-v31/themes.xml`
- `reports/SERVER_CFv2.0.2_GITHUB_BUILD_SUCCESS.md`
- `scripts/verify_server_cfv203.py`
- `SOURCES/SERVER/SERVER_CFv2.0.3_SWRLZ.md`

## Effects and runtime-state drivers

| Presentation | Sanitized authoritative input |
|---|---|
| OFFLINE, STARTING, FAILED | `HostUiState.health.runtimeStatus` |
| READY | Runtime RUNNING plus private API and loopback discovery both RUNNING |
| DEGRADED | Existing DEGRADED, or RUNNING without both required local services confirmed |
| LAN/listener detail | Existing `health.discoveryLan.status` |
| Mission atmosphere | Existing persisted `MissionEntity.state` |
| Connection stages | Exact normalized tokens in persisted `NodeEntity.status` |
| Identity and proof gates | Exact normalized persisted status/trust tokens |
| TRUSTED, PENDING, LEGACY, FAILED | Existing persisted `NodeEntity.trust` |
| LOCAL/REMOTE route | Exact normalized route tokens only |
| Stable node animation identity | `NodeEntity.nodeId` |

Added effects: low-frequency core breathing, very gentle dragon drift, sparse remembered shard shimmer, sequential STARTING segments, one finite READY cyan pulse, one finite proof emerald pulse, restrained DEGRADED amber irregularity, static FAILED red boundary, finite navigation/control ripple, mission/pause/caution atmosphere, and a conservative perimeter-to-core handshake.

The core path illuminates only with explicit CONNECTED + IDENTITY CONFIRMED + PROOF VERIFIED + TRUSTED evidence. Reachability stops at the perimeter. LEGACY remains distinct and never implies modern proof verification.

## Static verification summary

Passed:

- Kotlin source delimiter and focused source inspection.
- Gradle structure and Android identity inspection.
- Manifest XML and resource-reference inspection.
- Duplicate value-resource-name detection.
- Adaptive-icon and splash-theme reference validation.
- Protected data/security/service byte-hash comparison with CFv2.0.2.
- Existing `INT-IMP-006` contract guard.
- Existing SERVER contract/icon guard.
- Packaged-output/configuration exclusion guard.
- Clean extracted-source reproduction.

No module, namespace, package, protocol identifier, or SERVER transport prefix changed. No sensitive value or logging call was added to the modified UI source.

## nodeId regression summary

- `NodeEntity::id`: absent.
- `NodeEntity::nodeId`: retained in both repaired UI files.
- `@PrimaryKey val nodeId: String`: unchanged.
- No duplicate `id` property.
- No Room migration.
- No index/display/host/status/trust/last-seen list key.
- Node-linked effects carry the authoritative `nodeId`.

## Compose-import regression summary

Absent:

```kotlin
import androidx.compose.foundation.layout.calculateTopPadding
import androidx.compose.foundation.layout.weight
```

`calculateTopPadding()` remains receiver-resolved from `asPaddingValues()`. `Modifier.weight(...)` remains used only within `Row` or `Column` scope. The extracted-package verifier confirms both regression guards.

## Icon and splash inventory

Unchanged launcher/art resources:

- 4096×4096 `branding/SWURLZER_GLITCH_DRAGON_ICON_MASTER_4096.png`
- 432×432 adaptive foreground PNG
- vector launcher foreground
- adaptive regular/round launcher XML
- mdpi, hdpi, xhdpi, xxhdpi, and xxxhdpi regular/round PNGs
- `glitch_dragon_swurlzer.jpg`

Added splash integration:

- base `Theme.SwrlzNodeHost.Starting`
- Android 12+ `values-v31/themes.xml`
- `swurlzer_splash_background`
- existing adaptive launcher icon reference

All launcher and dragon artwork hashes match CFv2.0.2.

## Accessibility summary

Critical status is expressed with text and iconography, not color alone. Core and node cards expose spoken state descriptions. Connection, identity, proof, trust, legacy, route, and failure remain separate. Decorative dragon/eye/icons are excluded from accessibility traversal. Controls retain established touch heights; summary controls use an 88 dp minimum. Status labels can wrap to two lines, lists remain scrollable, and effects do not obscure diagnostics.

## Reduced-motion summary

Motion-disabled mode renders a static state-correct Citadel and short opacity-only navigation transitions. Ambient drift, shimmer, eye/core event pulses, and shard movement are not composed. Decorative work also suspends below lifecycle STARTED, and FAILED disables nonessential animation.

## Performance-risk summary

Risk is bounded by one low-frequency ambient transition, three remembered shard specs, and four finite event animatables. There are no per-node infinite transitions, particle spawners, per-frame bitmap decodes, per-frame Path allocations, or full-screen blur recomputations. Existing cached artwork is reused. Remaining performance risk is ordinary Compose transparent-layer overdraw on very low-end GPUs; static reduced-motion fallback remains available.

## Known limitations

- CFv2.0.3 has not been compiled; compilation is pending GitHub verification.
- No APK, device-install, runtime-handshake, screenshot, instrumentation, or accessibility-service execution evidence is claimed.
- The current model supplies sanitized persisted node status/trust strings rather than a dedicated live proof-stage event stream. Missing evidence is shown as NOT VERIFIED / NOT REPORTED and never promoted.
- The background handshake uses the node with the highest explicit evidence; every node remains independently visible in the list.
- The eye is decorative and never authoritative.
- Android 12+ receives the platform splash refinement; older versions use the base launch theme.
- The in-archive lineage receipt refers to this sibling receipt for the final ZIP digest because embedding a ZIP's own digest would mutate the bytes being hashed.

**Protocols and Room schema were unchanged.**  
**CFv2.0.3 compilation is pending GitHub verification.**
