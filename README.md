# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, and versioned CLIENT/SERVER contracts.

## Current CFv2.0.x baselines

- **Last device-verified CLIENT build:** `CLIENT_CFv2.0.65_SWRLZ.zip`; device screenshots confirm Dragon Kamileon identity, Mission HUD, direct bubble Forge access, embedded bubble navigation, version provenance, repeat uploads, and upload notifications.
- **Prepared CLIENT baseline:** `CLIENT_CFv2.0.66_SWRLZ.zip` with matching SHA-256. It preserves credential lifecycle, generated receipts, repeated uploads, notifications, collapsible Forge panels, and Dragon Kamileon identity while adding source-lane controls, event-routed arbitrary-name builds, a complete embedded CLIENT bubble shell, and modern User-mode permission onboarding.
- **Last device-verified SERVER build:** `SERVER_CFv2.0.47_SWRLZ.zip`; screenshots confirm full SWURLZER Forge, bubble access, Dragon Kamileon identity, version provenance, and operational SERVER surfaces.
- **Prepared SERVER baseline:** `SERVER_CFv2.0.48_SWRLZ.zip` with matching SHA-256. It preserves the full SERVER Forge, credential lifecycle, generated receipts, collapsible panels, and versioned launcher aliases while adding the same source-lane controls, an embedded SERVER bubble shell, and a final adaptive-icon safe-zone adjustment.
- **Status:** CLIENT CFv2.0.66 and SERVER CFv2.0.48 are source/static and package verified. Their GitHub Android builds and new device acceptance remain pending. Stabilization continues on `CFv2.0.x` before coordinated Chat and Missions expansion.

## Product identities

- **SWRLZ:** Android CLIENT and local control surface.
- **SWURLZER:** SERVER/node host.
- **SWURVER:** authenticated fused CLIENT state with approved SERVER administrative capabilities; it is not a third application.

Visual state never grants authority. Trust, session validity, selected SERVER identity, and capability grants remain authoritative.

## Event-routed source builds

The selected repository lane determines the build contract:

```text
SOURCES/CLIENT/      CLIENT
SOURCES/SERVER/      SERVER
SOURCES/CORE_BASE/   generic Android Gradle project
SOURCES/KEYBOARD/    keyboard project
SOURCES/LAUNCHER/    launcher project
```

Any non-empty ZIP filename is accepted by the shared source resolver. Android download-copy counters such as ` (1)` are transport aliases, not software versions.

For push-triggered builds, the APK Router selects the exact ZIP changed by the current commit. Re-uploading an older source therefore builds that exact archive instead of silently selecting the numerically highest version already present. If multiple different ZIPs change in the same lane in one commit, resolution fails closed and requires separate uploads or explicit workflow dispatch.

`SOURCES/CORE_BASE` is the generic lane for unrelated Android Gradle projects with an `app` module and supported wrapper/project structure.

## Source-package integrity

Forge requires one logical source pair:

```text
<base>.zip
<base>.sha256
```

A `<base>.manifest.json` remains optional during the CFv2.0.x stabilization line and is validated when present.

The Source Package Integrity verifier shares the router's transport-name policy. Copy-suffixed ZIP/checksum aliases and filenames containing spaces may pair, but actual ZIP bytes and SHA-256 remain authoritative. Aliases containing different bytes or disagreeing hashes fail closed.

When Android exposes a readable sibling receipt, Forge uses it. When sibling access is withheld, Forge hashes the selected ZIP locally and stages a canonical receipt automatically. `LOCATE SHA-256` is reserved for exceptional failure of both paths.

## Forge lane selection and completion truth

CLIENT and SERVER Forge expose remembered lane controls for CLIENT, SERVER, generic ANDROID, KEYBOARD, and LAUNCHER projects. Filename patterns may suggest a route but do not grant or deny source eligibility.

Each Forge can validate source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission receives a fresh transaction identifier and actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

The separate Android apps do not claim a shared local lock. Concurrent branch movement is handled through latest-head resolution, bounded non-fast-forward retry, final branch confirmation, and uploaded-path read-back. Transfer reaching 100 percent is not sufficient evidence that the repository changed.

## Credential lifecycle and surface cohesion

Forge distinguishes unverified token input, active access token, refresh token, expiration metadata, connected account projection, and rejected credential state.

Before source streaming, each Forge validates one immutable credential snapshot against GitHub `/user` and the configured repository branch. Refreshable GitHub App user tokens may rotate before expiry or after one bounded transfer-time 401 retry. Explicit rejection retires the credential; transient failures remain non-destructive.

Within each Android application, full-app and bubble Forge surfaces observe the same Keystore-backed credential authority. CLIENT and SERVER remain separate application sandboxes and never exchange token material.

## Embedded bubble shells

CLIENT and SERVER bubbles embed the same User shells as their full applications rather than presenting separate initial destination grids.

Each bubble provides:

- a version strip directly above navigation;
- horizontally scrollable bottom navigation;
- direct access to the application's Core, Chat/Activity, Groups/Nodes, Missions, Forge, Settings, and related surfaces;
- the same credential, mission, Forge, and runtime state as the full application.

Bubble presentation remains a capability projection and does not establish independent authority.

## CLIENT permission onboarding

CLIENT User Mode presents a modern first-launch permission overlay for Accessibility Service, Draw over other apps, notifications, restricted-settings recovery, and live permission refresh after Android Settings returns.

Accessibility and overlay access gate entry to the User shell. The legacy engineering permission center remains available in Developer Mode.

## Build and identity truth

- CLIENT uses Kotlin Android, serialization, and Compose Compiler Gradle plugins aligned at `2.0.0`.
- SERVER uses Kotlin/Compose `2.0.20`, Activity Compose `1.9.2`, no legacy compiler-extension override, and the required SERVER bubble `border` import.
- SERVER adaptive foregrounds and monochrome assets receive a final safe-zone scale/position adjustment while preserving versioned launcher aliases and distinct Theme Armor families.
- Theme state remains presentation-only and never grants authority.

## Visible version provenance

```text
CLIENT · CFv2.0.66    VC 93
SERVER · CFv2.0.48    VC 49
```

## Documentation entry points

- [`docs/checkpoints/CLIENT_066_SERVER_048_EVENT_ROUTED_BUBBLE_PERMISSION.md`](docs/checkpoints/CLIENT_066_SERVER_048_EVENT_ROUTED_BUBBLE_PERMISSION.md) — final package identities, event/path routing, arbitrary source lanes, embedded bubble shells, User permission onboarding, launcher alignment, evidence, and acceptance gates.
- [`docs/architecture/FORGE_EVENT_ROUTING_AND_EMBEDDED_BUBBLE_V1.md`](docs/architecture/FORGE_EVENT_ROUTING_AND_EMBEDDED_BUBBLE_V1.md) — authoritative lane/event routing, generic Android builds, integrity alignment, embedded bubble-shell architecture, and permission model.
- [`docs/checkpoints/CLIENT_065_SERVER_047_FINISH_LINE_COHESION.md`](docs/checkpoints/CLIENT_065_SERVER_047_FINISH_LINE_COHESION.md) — copy-suffix handling, direct bubble Forge entry, credential-state cohesion, SERVER collapsible panels, and launcher safe-zone history.
- [`docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md`](docs/architecture/FORGE_CREDENTIAL_AND_CHECKSUM_LIFECYCLE_V1.md) — authoritative credential states, refresh/rejection policy, immutable preflight, and checksum-generation contract.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability foundation.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — coordinated Chat entry gate.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes source implementation, static verification, workflow-log evidence, device/runtime evidence, and accepted architecture. A feature is not runtime-accepted solely because source exists, a transfer reaches 100 percent, or a local repair archive was created.
