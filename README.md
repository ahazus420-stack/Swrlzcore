# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Repository CLIENT package:** `CLIENT_CFv2.0.53_SWRLZ.zip` with matching SHA-256; APK build failed at a bounded Compose import defect.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.55_SWRLZ.zip` with matching SHA-256. It includes the `CFv2.0.54` Compose repair plus repeated-upload transaction verification, upload/build outcome notifications, bubble event projection, durable artifact-build watches, and selected-ZIP sibling SHA matching. GitHub build and device acceptance remain pending.
- **Repository SERVER package:** `SERVER_CFv2.0.37_SWRLZ.zip` with matching SHA-256; APK build failed at a bounded bubble package-namespace defect.
- **Prepared SERVER repair:** `SERVER_CFv2.0.38_SWRLZ.zip` with matching SHA-256; pending Forge upload and GitHub build.
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

The repository verifier and Source Package Integrity workflow were aligned to this contract on 2026-07-24. A missing optional manifest is not an integrity failure.

## Forge completion truth

Forge treats source transfer, commit creation, branch confirmation, uploaded-path verification, workflow dispatch, workflow completion, and artifact discovery as separate outcomes. A transfer reaching 100 percent is not sufficient evidence that the repository changed.

Every `CFv2.0.55` submission receives a fresh transaction identifier. Upload success requires the target branch to resolve to the new commit and every uploaded repository path to resolve to its expected blob SHA. Upload and artifact-build terminal outcomes are projected to the CLIENT notification channel and CLIENT bubble event banner.

## Documentation entry points

- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability architecture.
- [`docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md`](docs/checkpoints/CLIENT_CFV2_0_55_FORGE_REPEAT_NOTIFY_SIBLING.md) — repeated-upload transaction repair, upload/build notifications, bubble event projection, durable build watching, and selected-ZIP sibling SHA matching.
- [`docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md`](docs/checkpoints/CFV2_0X_CONVERSATION_UPDATE_RECORD_2026_07_24.md) — additive documentation audit and CLIENT/SERVER checkpoint record.
- [`docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md`](docs/checkpoints/INT_PKG_022B_AND_CLIENT_BUILD_054_REPAIR.md) — integrity-workflow correction and CLIENT CFv2.0.54 Kotlin build repair.
- [`docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md`](docs/checkpoints/SERVER_CFV2_0_38_BUBBLE_NAMESPACE_BUILD_REPAIR.md) — SERVER CFv2.0.37 namespace failure and CFv2.0.38 repair.
- [`docs/checkpoints/INT_FORGE_017A_AND_INT_ID_018A_IMPLEMENTATION_RECORD.md`](docs/checkpoints/INT_FORGE_017A_AND_INT_ID_018A_IMPLEMENTATION_RECORD.md) — Forge and Android identity implementation lineage.
- [`docs/checkpoints/INT_PKG_022A_IMPLEMENTATION_RECORD.md`](docs/checkpoints/INT_PKG_022A_IMPLEMENTATION_RECORD.md) — immutable package-integrity history and package-policy amendment.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes among source-reported implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not considered runtime-accepted solely because source exists, a transfer reaches 100 percent, or a repair package has been generated locally.