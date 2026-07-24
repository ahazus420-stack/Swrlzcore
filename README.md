# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Last log-verified CLIENT build input:** `CLIENT_CFv2.0.57_SWRLZ.zip`; APK Router reached Kotlin compilation and failed at a bounded `ForgeUploadLogStore` SharedPreferences receiver defect.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.59_SWRLZ.zip` with matching SHA-256. It preserves the `CFv2.0.58` live redacted Forge logs, connector continuity, CLIENT actor evidence, and Dragon Kamileon Theme Armor while aligning the Kotlin Android, serialization, and Compose Compiler Gradle plugins at `2.0.0`.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.41_SWRLZ.zip` with matching SHA-256. It preserves full SWURLZER Forge capability and Dragon Kamileon Theme Armor while upgrading Activity Compose to `1.9.2` for `rememberLauncherForActivityResult` and removing the obsolete pre-Kotlin-2 Compose compiler extension setting.
- **Status:** static preflight and package verification are complete; GitHub Android compilation and device acceptance remain pending. Stabilization continues on `CFv2.0.x`; the coordinated `CFv2.1.0` Chat control plane remains gated by documented exit criteria.

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

## Dual Forge completion and concurrency truth

CLIENT and SERVER can each select and validate CLIENT/SERVER source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission receives a fresh transaction identifier and an actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

The separate Android apps do not claim a shared local lock. Concurrent branch movement is handled by resolving the latest GitHub head after transfer, rebuilding the candidate tree, retrying bounded non-fast-forward races, confirming the final branch head, and reading every uploaded path back before success. A transfer reaching 100 percent is not sufficient evidence that the repository changed.

CLIENT keeps its backup-aware connector continuity and exportable redacted upload diagnostics. SERVER stores its Forge profile locally with a Keystore-backed token, reconnects across restart/same-signed update when validation succeeds, and requires approval before disconnecting.

## Build configuration truth

CLIENT uses the Kotlin 2.0 Compose Compiler Gradle plugin with Kotlin Android and serialization plugins aligned to the same `2.0.0` version.

SERVER uses Kotlin/Compose `2.0.20`, no legacy `kotlinCompilerExtensionVersion` override, and Activity Compose `1.9.2` for the Forge Activity Result APIs.

## Dragon Kamileon Theme Armor

Both applications include a selectable iridescent cyan, violet, magenta, and gold Dragon Kamileon family with dedicated launcher and bubble identities. CLIENT includes CLIENT, SERVER-context, and fusion variants; SERVER includes SERVER and fusion variants. Theme state remains non-authoritative presentation.

## Visible version provenance

The prepared applications expose Android build identity at the bottom of their main User and Developer surfaces:

```text
CLIENT · CFv2.0.59    VC 86
SERVER · CFv2.0.41    VC 42
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_059_SERVER_041_BUILD_PREFLIGHT_ALIGNMENT.md`](docs/checkpoints/CLIENT_059_SERVER_041_BUILD_PREFLIGHT_ALIGNMENT.md) — build preflight findings, Kotlin/Compose alignment, SERVER Activity Compose API repair, static checks, final hashes, and acceptance gates.
- [`docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md`](docs/checkpoints/CLIENT_058_SERVER_040_DUAL_FORGE_DRAGON_KAMILEON.md) — CLIENT compiler repair, CLIENT/SERVER Forge capability, actor/concurrency truth, Dragon Kamileon Theme Armor, final hashes, and acceptance gates.
- [`docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md`](docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md) — live Forge upload logs, credential redaction, connector persistence, eligible reinstall restoration, validated auto-connect, disconnect approval, and bubble-footer correction.
- [`docs/checkpoints/CLIENT_056_SERVER_039_IDENTITY_FORGE_OBSERVER_REPAIR.md`](docs/checkpoints/CLIENT_056_SERVER_039_IDENTITY_FORGE_OBSERVER_REPAIR.md) — CLIENT build repair, duplicate integrity-workflow retirement, current launcher/theme identity, theme-reactive bubbles, workflow observer cleanup, and main-page version footers.
- [`docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md`](docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md) — repeated-upload transaction repair, upload/build notifications, bubble event projection, durable build watching, and selected-ZIP sibling SHA matching.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability architecture.
- [`docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md`](docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md) — additive documentation audit and CLIENT/SERVER checkpoint record.
- [`docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md`](docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md) — integrity-policy correction and CLIENT CFv2.0.54 Compose build repair.
- [`docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md`](docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md) — SERVER CFv2.0.37 namespace failure and CFv2.0.38 repair.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes among source-reported implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not considered runtime-accepted solely because source exists, a transfer reaches 100 percent, or a repair package has been generated locally.
