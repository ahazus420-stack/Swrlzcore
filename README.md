# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Last device-verified CLIENT build:** `CLIENT_CFv2.0.58_SWRLZ.zip`; APK Router succeeded and device screenshots confirm Dragon Kamileon interface/bubble identity, bubble-hosted Forge observation, version provenance, and upload-success notification identity.
- **Additional CLIENT runtime evidence:** repeated uploads work from the same installed CLIENT without reinstalling, producing new confirmed commits and correct bubble/notification commit outcomes.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.64_SWRLZ.zip` with matching SHA-256. It preserves the manual-token state repair, generated exact-basename checksum receipts, collapsible Forge panels, and Dragon Kamileon identity while adding token normalization, device-flow refresh lifecycle storage, immutable credential preflight, one bounded refresh/retry, and rejection-state retirement.
- **Last log-verified SERVER build:** `SERVER_CFv2.0.41_SWRLZ.zip`; Kotlin compilation failed because `ServerBubbleActivity.kt` used `Modifier.border(...)` without importing `androidx.compose.foundation.border`.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.46_SWRLZ.zip` with matching SHA-256. It preserves the exact border repair, full SWURLZER Forge, generated checksum receipts, and versioned launcher aliases with matching dragon monochrome layers while adding the same credential lifecycle and preflight behavior.
- **Status:** CLIENT CFv2.0.58 and repeated CLIENT Forge uploads are device-verified. CLIENT CFv2.0.64 and SERVER CFv2.0.46 are source/static and package verified; GitHub builds and device acceptance remain pending. Stabilization continues on `CFv2.0.x`.

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

The SHA file may contain either the 64-character digest alone or the standard digest-plus-filename form. A `<base>.manifest.json` file remains optional during the CFv2.0.x stabilization line and is validated when present.

The repository verifier and authoritative Source Package Integrity workflow implement this contract. The obsolete duplicate integrity workflow was removed on 2026-07-24, so one source transaction should start one integrity run.

## Automatic ZIP/SHA resolution

CLIENT and SERVER derive the exact `.sha256` name from the selected ZIP and attempt readable sibling resolution through document IDs, parent child enumeration, and MediaStore relative-directory matching.

When Android still withholds sibling access, Forge hashes the selected ZIP locally and stages a canonical exact-basename SHA-256 receipt automatically. `LOCATE SHA-256` is reserved for exceptional failure of both sibling resolution and local receipt generation.

## GitHub credential lifecycle

Forge distinguishes unverified token input, active access token, refresh token, expiration metadata, connected account projection, and rejected credential state.

Before source streaming, each Forge validates one immutable credential snapshot against GitHub `/user` and the configured repository branch. Device-flow refresh metadata is securely persisted when GitHub supplies it. Refreshable GitHub App user tokens rotate before expiry or after a transfer-time 401, with at most one upload retry.

A credential explicitly rejected by GitHub is retired instead of silently reused. Network and other transient failures remain non-destructive. Token material never enters logs, notifications, commits, or cross-app intents.

## Dual Forge completion and concurrency truth

CLIENT and SERVER can each validate source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission receives a fresh transaction identifier and actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

The separate Android apps do not claim a shared local lock. Concurrent branch movement is handled through latest-head resolution, bounded non-fast-forward retry, final branch confirmation, and uploaded-path read-back. Transfer reaching 100 percent is not sufficient evidence that the repository changed.

## Build and identity truth

- CLIENT uses Kotlin Android, serialization, and Compose Compiler Gradle plugins aligned at `2.0.0`.
- SERVER uses Kotlin/Compose `2.0.20`, Activity Compose `1.9.2`, no legacy compiler-extension override, and the required SERVER bubble `border` import.
- SERVER launcher aliases are versioned and every adaptive monochrome layer follows its selected SWURLZER dragon foreground.
- Theme state remains presentation-only and never grants authority.

## Visible version provenance

```text
CLIENT · CFv2.0.64    VC 91
SERVER · CFv2.0.46    VC 47
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_064_SERVER_046_CREDENTIAL_LIFECYCLE_ICON_CHECKSUM.md`](docs/checkpoints/CLIENT_064_SERVER_046_CREDENTIAL_LIFECYCLE_ICON_CHECKSUM.md) — recurring 401 diagnosis, credential lifecycle, generated checksum receipts, SERVER launcher correction, final hashes, and acceptance gates.
- [`docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md`](docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md) — authoritative credential states, refresh/rejection policy, immutable preflight, and checksum-generation contract.
- [`docs/checkpoints/CLIENT_062_SERVER_044_MANUAL_TOKEN_VALIDATION_STATE.md`](docs/checkpoints/CLIENT_062_SERVER_044_MANUAL_TOKEN_VALIDATION_STATE.md) — manual token candidate/active-state separation.
- [`docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md`](docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md) — provider-safe sibling lookup and explicit fallback history.
- [`docs/checkpoints/CLIENT_060_SERVER_042_THEME_CONNECTOR_AND_BORDER_REPAIR.md`](docs/checkpoints/CLIENT_060_SERVER_042_THEME_CONNECTOR_AND_BORDER_REPAIR.md) — theme-refresh connector continuity and SERVER missing-border diagnosis.
- [`docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md`](docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md) — CLIENT/SERVER Forge capability, concurrency truth, and Dragon Kamileon Theme Armor.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability foundation.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes source implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not runtime-accepted solely because source exists, a transfer reaches 100 percent, or a local repair archive was created.
