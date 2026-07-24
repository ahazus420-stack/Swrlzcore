# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Repository CLIENT package:** `CLIENT_CFv2.0.55_SWRLZ.zip`; APK Router reached Kotlin compilation and failed at a bounded `ForgeBuildWatchStore` collection-operation defect.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.57_SWRLZ.zip` with matching SHA-256. It includes the `CFv2.0.56` compiler, workflow-observer, current Theme Armor, theme-reactive bubble, and main-footer work, plus live exportable Forge upload diagnostics, backup-aware GitHub connector restoration, validated auto-connect, disconnect approval, and corrected CLIENT bubble-footer spacing.
- **Repository SERVER package:** `SERVER_CFv2.0.38_SWRLZ.zip`; its package-namespace repair is preserved.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.39_SWRLZ.zip` with matching SHA-256. It replaces fallback/default/theme launcher resources with the current violet SWURLZER artwork, makes SERVER/fusion bubble icons theme-reactive, reasserts the selected launcher alias after update/start, and adds main-page version footers.
- **Status:** stabilization continues on `CFv2.0.x`; the coordinated `CFv2.1.0` Chat control plane remains gated by documented exit criteria.

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

## Forge completion, observation, and diagnostics

Forge treats source selection, checksum matching, validation, transfer, Git object creation, branch confirmation, uploaded-path verification, workflow dispatch, workflow completion, and artifact discovery as separate outcomes. A transfer reaching 100 percent is not sufficient evidence that the repository changed.

Every submission receives a fresh transaction identifier. Upload success requires the target branch to resolve to the new commit and every uploaded repository path to resolve to its expected blob SHA. Upload and artifact-build terminal outcomes project to the CLIENT notification channel and bubble event banner.

CLIENT `CFv2.0.57` keeps all active workflow runs visible, shows four completed runs by default, permits a 2–20 completed-run history, refreshes every two seconds while active and five seconds while idle, and begins polling immediately after a verified upload.

The CLIENT also maintains a bounded, redacted Forge upload-session log that can be exported during selection, staging, validation, upload, workflow discovery, failure, or completion. GitHub tokens, authorization headers, OAuth device codes, and token-like values are sanitized from exported diagnostics.

## GitHub connector continuity

Restart and same-signed in-place updates retain the connector profile and Keystore-backed authorization. A temporary verification failure does not automatically erase the saved credential.

Uninstall/reinstall restoration is conditional on Android restoring eligible app data through encrypted cloud backup or direct device transfer. The CLIENT backs up the non-secret profile and uses a transport-gated restore envelope to import authorization into the fresh install's new Keystore store, then validates it before showing connected. When only profile data is restored, one authorization is required. Disconnecting requires approval and clears restore/auto-connect intent.

## Visible version provenance

The prepared applications expose Android build identity at the bottom of their main User and Developer surfaces:

```text
CLIENT · CFv2.0.57    VC 84
SERVER · CFv2.0.39    VC 40
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md`](docs/checkpoints/CLIENT_CFV2_0_57_FORGE_LIVE_LOG_CONNECTOR_RESTORE.md) — live Forge upload logs, credential redaction, connector persistence, eligible reinstall restoration, validated auto-connect, disconnect approval, and bubble-footer correction.
- [`docs/checkpoints/CLIENT_056_SERVER_039_IDENTITY_FORGE_OBSERVER_REPAIR.md`](docs/checkpoints/CLIENT_056_SERVER_039_IDENTITY_FORGE_OBSERVER_REPAIR.md) — CLIENT build repair, duplicate integrity-workflow retirement, current launcher/theme identity, theme-reactive bubbles, workflow observer cleanup, and main-page version footers.
- [`docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md`](docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md) — repeated-upload transaction repair, upload/build notifications, bubble event projection, durable build watching, and selected-ZIP sibling SHA matching.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability architecture.
- [`docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md`](docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md) — additive documentation audit and CLIENT/SERVER checkpoint record.
- [`docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md`](docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md) — integrity-policy correction and CLIENT CFv2.0.54 Compose build repair.
- [`docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md`](docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md) — SERVER CFv2.0.37 namespace failure and CFv2.0.38 repair.
- [`docs/checkpoints/INT_FORGE_017A_AND_INT_ID_018A_IMPLEMENTATION_RECORD.md`](docs/checkpoints/INT_FORGE_017A_AND_INT_ID_018A_IMPLEMENTATION_RECORD.md) — Forge and Android identity implementation lineage.
- [`docs/checkpoints/INT_PKG_022A_IMPLEMENTATION_RECORD.md`](docs/checkpoints/INT_PKG_022A_IMPLEMENTATION_RECORD.md) — immutable package-integrity history and package-policy amendment.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes among source-reported implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not considered runtime-accepted solely because source exists, a transfer reaches 100 percent, or a repair package has been generated locally.
