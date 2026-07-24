# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Last device-verified CLIENT build:** `CLIENT_CFv2.0.58_SWRLZ.zip`; APK Router succeeded and device screenshots confirm Dragon Kamileon interface/bubble identity, bubble-hosted Forge observation, version provenance, and upload-success notification identity.
- **Additional CLIENT runtime evidence:** repeated uploads work from the same installed CLIENT without reinstalling, producing distinct confirmed commits and correct bubble/notification outcomes.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.65_SWRLZ.zip` with matching SHA-256. It preserves credential lifecycle preflight, generated receipts, repeated uploads, notifications, collapsible Forge panels, and Dragon Kamileon identity while adding canonical Android download-copy filenames, a direct CLIENT bubble Forge destination, and shared full-app/bubble credential state.
- **Last log-verified SERVER build:** `SERVER_CFv2.0.41_SWRLZ.zip`; Kotlin compilation failed because `ServerBubbleActivity.kt` used `Modifier.border(...)` without importing `androidx.compose.foundation.border`.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.47_SWRLZ.zip` with matching SHA-256. It preserves the exact border repair, full SWURLZER Forge, generated receipts, and credential lifecycle while adding canonical download-copy filenames, CLIENT-equivalent collapsible Forge panels, shared full-app/bubble credential state, centered adaptive art, true themed-icon monochrome layers, and `V3` launcher aliases.
- **Status:** existing CLIENT Forge/bubble behavior and repeated uploads are device-verified. CLIENT CFv2.0.65 and SERVER CFv2.0.47 are source/static and package verified; their GitHub builds and new device acceptance remain pending. Stabilization continues on `CFv2.0.x`.

## Product identities

- **SWRLZ:** Android CLIENT and local control surface.
- **SWURLZER:** SERVER/node host.
- **SWURVER:** authenticated fused CLIENT state with approved SERVER administrative capabilities; it is not a third application.

Visual state never grants authority. Trust, session validity, selected SERVER identity, and capability grants remain authoritative.

## Source-package contract

Forge requires an exact-basename logical pair:

```text
<base>.zip
<base>.sha256
```

A `<base>.manifest.json` remains optional during the CFv2.0.x stabilization line and is validated when present.

Android/file-provider copy counters such as ` (1)`, ` (2)`, or ` (37)` immediately before `.zip`, `.sha256`, or `.manifest.json` are transport-local names. Forge restores the canonical basename for pairing, routing, and repository destinations. Source validity remains determined by archive contents.

When Android exposes a readable sibling receipt, Forge uses it. When sibling access is withheld, Forge hashes the selected ZIP locally and stages a canonical receipt automatically. `LOCATE SHA-256` is reserved for exceptional failure of both paths.

## GitHub credential lifecycle and surface cohesion

Forge distinguishes unverified token input, active access token, refresh token, expiration metadata, connected account projection, and rejected credential state.

Before source streaming, each Forge validates one immutable credential snapshot against GitHub `/user` and the configured repository branch. Refreshable GitHub App user tokens may rotate before expiry or after one transfer-time 401 retry. Explicit rejection retires the credential; transient failures remain non-destructive.

Within each Android application, the full-app and bubble Forge surfaces observe one process-level revision stream over the same Keystore-backed credential authority. Connecting, refreshing, rejecting, or disconnecting in one surface updates the other without re-entering credentials. CLIENT and SERVER remain separate application sandboxes and never exchange token material.

## Dual Forge completion and concurrency truth

CLIENT and SERVER can each validate source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission receives a fresh transaction identifier and actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

The separate Android apps do not claim a shared local lock. Concurrent branch movement is handled through latest-head resolution, bounded non-fast-forward retry, final branch confirmation, and uploaded-path read-back. Transfer reaching 100 percent is not sufficient evidence that the repository changed.

## Bubble and interface cohesion

- CLIENT and SERVER bubbles both expose a first-class `FORGE` destination using the same Forge composable as their full applications.
- CLIENT and SERVER Forge both provide remembered collapsible Repository Target and Source Package Matching panels.
- Bubble state remains a capability projection; it does not create independent authority.

## Build and identity truth

- CLIENT uses Kotlin Android, serialization, and Compose Compiler Gradle plugins aligned at `2.0.0`.
- SERVER uses Kotlin/Compose `2.0.20`, Activity Compose `1.9.2`, no legacy compiler-extension override, and the required SERVER bubble `border` import.
- SERVER adaptive foregrounds are centered inside Android safe zones; themed icons use dedicated detail-based monochrome layers.
- SERVER launcher aliases advance to `V3` so stale launcher-component caches do not preserve the previous identity.
- Theme state remains presentation-only and never grants authority.

## Visible version provenance

```text
CLIENT · CFv2.0.65    VC 92
SERVER · CFv2.0.47    VC 48
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_065_SERVER_047_FINISH_LINE_COHESION.md`](docs/checkpoints/CLIENT_065_SERVER_047_FINISH_LINE_COHESION.md) — canonical copy-suffix handling, bubble Forge entry, credential-state cohesion, SERVER collapsible panels, launcher safe-zone repair, final hashes, and acceptance gates.
- [`docs/checkpoints/CLIENT_064_SERVER_046_CREDENTIAL_LIFECYCLE_ICON_CHECKSUM.md`](docs/checkpoints/CLIENT_064_SERVER_046_CREDENTIAL_LIFECYCLE_ICON_CHECKSUM.md) — recurring 401 diagnosis, credential lifecycle, generated checksum receipts, SERVER launcher correction, final hashes, and acceptance gates.
- [`docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md`](docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md) — authoritative credential states, refresh/rejection policy, immutable preflight, and checksum-generation contract.
- [`docs/checkpoints/CLIENT_062_SERVER_044_MANUAL_TOKEN_VALIDATION_STATE.md`](docs/checkpoints/CLIENT_062_SERVER_044_MANUAL_TOKEN_VALIDATION_STATE.md) — manual token candidate/active-state separation.
- [`docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md`](docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md) — provider-safe sibling lookup and explicit fallback history.
- [`docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md`](docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md) — CLIENT/SERVER Forge capability, concurrency truth, and Dragon Kamileon Theme Armor.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability foundation.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes source implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not runtime-accepted solely because source exists, a transfer reaches 100 percent, or a local repair archive was created.
