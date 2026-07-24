# CLIENT CFv2.0.65 + SERVER CFv2.0.47 — Finish-Line Cohesion

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub APK builds and device acceptance pending.

## Context

This is a finish-line integration pass over already-operational Forge, bubble, notification, workflow-observer, and SERVER runtime foundations. It connects state and presentation consistently across the full applications and their bubble surfaces rather than replacing the established architecture.

## 1. Android download-copy filenames

Android and file providers may rename repeated downloads as:

```text
CLIENT_CFv2.0.65_SWRLZ (1).zip
CLIENT_CFv2.0.65_SWRLZ (2).zip
SERVER_CFv2.0.47_SWRLZ (37).zip
```

CLIENT and SERVER Forge now treat the trailing ` (<integer>)` segment as a transport-local copy counter when it appears immediately before `.zip`, `.sha256`, or `.manifest.json`.

Source validity remains determined by archive contents. The copy counter does not make an otherwise valid Android source tree invalid.

For pairing, routing, and repository mutation, Forge restores the canonical name:

```text
CLIENT_CFv2.0.65_SWRLZ (2).zip
    -> SOURCES/CLIENT/CLIENT_CFv2.0.65_SWRLZ.zip

SERVER_CFv2.0.47_SWRLZ (3).zip
    -> SOURCES/SERVER/SERVER_CFv2.0.47_SWRLZ.zip
```

Checksum matching accepts either the locally copy-suffixed companion or the canonical exact-basename receipt. Generated receipts use the canonical repository filename.

## 2. Full-app and bubble credential cohesion

The full application and bubble Forge already use the same Android Keystore-backed secret store, but each Compose surface could retain an older in-memory credential snapshot after another surface connected or refreshed GitHub.

Both apps now expose a process-level credential revision stream from their authoritative connection store. Any successful connection, validation, refresh, rejection, or approved disconnect republishes the revision. Open full-app and bubble Forge surfaces reload the shared token and account projection immediately.

This does not copy secrets between CLIENT and SERVER. Each Android application remains its own credential authority.

## 3. Bubble Forge entry points

CLIENT CFv2.0.65 adds a first-class `FORGE` destination to the CLIENT bubble and embeds the same `GitHubForgeScreen` used by the full CLIENT.

SERVER CFv2.0.47 preserves its existing `FORGE` bubble destination. Both bubbles therefore expose source staging, upload, workflow observation, logs, and artifacts without leaving the foreground application.

## 4. SERVER Forge panel alignment

SERVER Forge now uses the same remembered collapsible panels as CLIENT for:

- `REPOSITORY TARGET`;
- `SOURCE PACKAGE MATCHING`.

Both default to compact/collapsed presentation and persist their expanded state.

## 5. SERVER launcher safe-zone and themed identity

SERVER launcher identity was rebuilt for Android adaptive and themed-icon behavior:

- adaptive foreground dragon artwork is centered inside the safe zone rather than extending toward the mask edge;
- every theme family has a dedicated detail-based monochrome layer instead of tinting the whole circular artwork as one solid shape;
- launcher alias component names advance from `V2` to `V3` to invalidate stale launcher-component caches;
- Theme Identity Manager and AndroidManifest consistently target the new aliases;
- Glitch Dragon, Dragon Kamileon, Original Core, Glitch Neon, Pharaoh Emerald, and Void Jester remain distinct.

## 6. Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.65_SWRLZ.zip
versionCode: 92
versionName: 2.0.65-finish-line-cohesion-v1
SHA-256: 40a9ea03219620b9bfd22135918b2ef94094472cee22b38835b80599dbfa2c7d
```

### SERVER

```text
Package: SERVER_CFv2.0.47_SWRLZ.zip
versionCode: 48
versionName: 2.0.47-finish-line-cohesion-v1
SHA-256: e27c1954f55e1483f24a3f6111c96e1abe45b6dcf965c84f841032ed3795b258
```

Both archives pass compressed-data integrity testing and match their sibling SHA-256 receipts.

## 7. Static evidence

- Kotlin PSI syntax parsing passed for all 123 Kotlin and Gradle Kotlin files across both packages.
- All 45 XML resources parse.
- All 205 PNG/JPEG/WebP resources open and verify.
- All local drawable, mipmap, color, string, style, and XML references resolve.
- CLIENT bubble `BubbleSection` handling includes the new Forge destination in navigation and exhaustive text mappings.
- SERVER manifest aliases and Theme Identity Manager use the same `V3` component names.
- Every SERVER adaptive icon references a dedicated centered foreground and matching monochrome resource.
- GitHub Android compilation remains the authoritative build gate.

## Evidence classification

- existing Forge, bubble, upload notification, repeated upload, and SERVER runtime behavior: previously device-verified as recorded in earlier checkpoints;
- download-copy canonicalization: source/static verified;
- full-app/bubble credential revision propagation: source/static verified;
- CLIENT bubble Forge destination: source/static verified;
- SERVER collapsible panels: source/static verified;
- SERVER safe-zone/themed launcher repair: source/resource verified;
- final ZIP/SHA pairs: locally package verified;
- CLIENT CFv2.0.65 and SERVER CFv2.0.47 GitHub builds: pending;
- device acceptance of credential propagation and launcher rendering: pending.

## Acceptance gate

1. Build and install CLIENT CFv2.0.65.
2. Connect GitHub in the full CLIENT, open CLIENT bubble Forge, and confirm the same account is immediately available without re-entry.
3. Select a valid source ZIP renamed with ` (1)` or another integer suffix and confirm it is accepted, paired, and routed under the canonical repository name.
4. Complete an upload and workflow-observation cycle from the CLIENT bubble Forge.
5. Build and install SERVER CFv2.0.47.
6. Confirm Repository Target and Source Package Matching collapse, expand, and remember state.
7. Connect GitHub in the full SERVER, open SERVER bubble Forge, and confirm shared in-app credential state.
8. Confirm the current SWURLZER launcher icon is centered and detailed in both normal and Android themed-icon modes.
9. Switch every SERVER Theme Armor family and verify each launcher identity remains inside the adaptive mask.
10. Repeat canonical copy-suffix pairing and upload through SERVER Forge.
