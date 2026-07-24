# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Last device-verified CLIENT build:** `CLIENT_CFv2.0.58_SWRLZ.zip`; APK Router succeeded and device screenshots confirm Dragon Kamileon interface/bubble identity, bubble-hosted Forge observation, version provenance, and upload-success notification identity.
- **Additional CLIENT runtime evidence:** repeated uploads now work from the same installed CLIENT without reinstalling, producing new confirmed commits and correct bubble/notification commit outcomes.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.61_SWRLZ.zip` with matching SHA-256. It includes the CFv2.0.60 GitHub theme-refresh continuity and collapsible panels, then replaces forced checksum re-browsing with automatic exact sibling lookup plus an explicit fallback only when Android blocks sibling access.
- **Last log-verified SERVER build:** `SERVER_CFv2.0.41_SWRLZ.zip`; Kotlin compilation failed because `ServerBubbleActivity.kt` used `Modifier.border(...)` without importing `androidx.compose.foundation.border`.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.43_SWRLZ.zip` with matching SHA-256. It preserves the CFv2.0.42 exact border-import repair and adds the same automatic checksum resolver and non-forced fallback to SWURLZER Forge.
- **Status:** CLIENT CFv2.0.58 and repeated CLIENT Forge uploads are device-verified. CLIENT CFv2.0.61 and SERVER CFv2.0.43 are source/static and package verified; their GitHub builds and device acceptance remain pending. Stabilization continues on `CFv2.0.x`.

## Product identities

- **SWRLZ:** Android CLIENT and local control surface.
- **SWURLZER:** SERVER/node host.
- **SWURVER:** authenticated fused CLIENT state with approved SERVER administrative capabilities; it is not a third application.

Visual state never grants authority. Trust, session validity, selected SERVER identity, and capability grants remain authoritative.

## Source-package contract

The current Forge contract requires an exact-basename pair:

1. `<base>.zip`
2. `<base>.sha256`

The SHA file may contain either the 64-character digest alone or the standard digest-plus-filename form. A `<base>.manifest.json` file is optional during the CFv2.0.x stabilization line and is validated when present.

The repository verifier and authoritative Source Package Integrity workflow implement this contract. The obsolete duplicate integrity workflow was removed on 2026-07-24, so one source transaction should start one integrity run.

## Automatic ZIP/SHA resolution

CLIENT and SERVER derive the exact `.sha256` name from the selected ZIP and attempt provider-safe sibling resolution through path-based document IDs, parent child enumeration, and Android 10+ MediaStore relative-directory matching.

Forge never launches a second file browser automatically. When Android denies sibling access, the ZIP remains staged and an explicit `LOCATE SHA-256` fallback appears. Upload remains blocked until the exact checksum is readable and validates.

## Dual Forge completion and concurrency truth

CLIENT and SERVER can each select and validate CLIENT/SERVER source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission receives a fresh transaction identifier and an actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

The separate Android apps do not claim a shared local lock. Concurrent branch movement is handled by resolving the latest GitHub head after transfer, rebuilding the candidate tree, retrying bounded non-fast-forward races, confirming the final branch head, and reading every uploaded path back before success. A transfer reaching 100 percent is not sufficient evidence that the repository changed.

## GitHub connector continuity

CLIENT stores the GitHub token in Android Keystore-backed encrypted preferences and the non-secret connection profile separately. A Theme Armor or launcher identity refresh must not clear authorization or expose a new OAuth device-code flow while the saved token is validating. Temporary verification failure remains non-destructive; deliberate disconnect still requires approval.

## Build configuration truth

CLIENT uses the Kotlin 2.0 Compose Compiler Gradle plugin with Kotlin Android and serialization plugins aligned to `2.0.0`.

SERVER uses Kotlin/Compose `2.0.20`, no legacy `kotlinCompilerExtensionVersion` override, Activity Compose `1.9.2`, and the required Compose foundation `border` import in the SERVER bubble.

## Dragon Kamileon Theme Armor

Both applications include a selectable iridescent cyan, violet, magenta, and gold Dragon Kamileon family with dedicated launcher and bubble identities. CLIENT includes CLIENT, SERVER-context, and fusion variants; SERVER includes SERVER and fusion variants. Theme state remains non-authoritative presentation.

## Visible version provenance

The prepared applications expose Android build identity at the bottom of their main User and Developer surfaces:

```text
CLIENT · CFv2.0.61    VC 88
SERVER · CFv2.0.43    VC 44
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md`](docs/checkpoints/CLIENT_061_SERVER_043_AUTOMATIC_SIBLING_CHECKSUM.md) — repeated-upload evidence, SERVER log clarification, automatic sibling checksum resolution, explicit fallback policy, final hashes, and acceptance gates.
- [`docs/checkpoints/CLIENT_060_SERVER_042_THEME_CONNECTOR_AND_BORDER_REPAIR.md`](docs/checkpoints/CLIENT_060_SERVER_042_THEME_CONNECTOR_AND_BORDER_REPAIR.md) — CLIENT CFv2.0.58 runtime evidence, theme-refresh GitHub continuity repair, collapsible Forge panels, and SERVER missing-border diagnosis.
- [`docs/checkpoints/CLIENT_059_SERVER_041_BUILD_PREFLIGHT_ALIGNMENT.md`](docs/checkpoints/CLIENT_059_SERVER_041_BUILD_PREFLIGHT_ALIGNMENT.md) — Kotlin/Compose alignment, SERVER Activity Compose API repair, static checks, and package evidence.
- [`docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md`](docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md) — CLIENT/SERVER Forge capability, actor/concurrency truth, and Dragon Kamileon Theme Armor.
- [`docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md`](docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md) — live Forge upload logs, credential redaction, connector persistence, eligible reinstall restoration, validated auto-connect, disconnect approval, and bubble-footer correction.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability architecture.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes among source-reported implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not considered runtime-accepted solely because source exists, a transfer reaches 100 percent, or a repair package has been generated locally.
