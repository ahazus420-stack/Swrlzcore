# Swrlzcore

SWRLZ is an offline-first Android automation and AI control ecosystem built around explicit authority, local ownership, observable workflows, versioned CLIENT/SERVER contracts, and an expanding local-first cognitive architecture.

## Current transition state

- **CLIENT:** the project has entered the `CFv2.1.x` transition, where Chat becomes the persistent control plane over real capabilities rather than a thin provider front end.
- **SERVER:** remains separately versioned and should advance only from an authoritative SERVER source package with matching integrity evidence.
- **Historical 2.0.x baselines:** remain useful evidence, but newer verified source packages/repository HEAD supersede them for current development.
- **CFv2.1.0 direction:** Chat orchestration, Mission execution/reporting, Forge lifecycle narration, autonomous update/repair foundation, and eventual Client -> SWURVER -> external intelligence escalation.

## Product identities

- **SWRLZ:** Android CLIENT and local control surface.
- **SWURLZER:** SERVER/node host.
- **SWURVER:** authenticated fused/distributed reasoning state that extends CLIENT cognition through approved SERVER capabilities; it is not a third Android application.

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

A `<base>.manifest.json` remains optional unless a later versioned contract explicitly makes it mandatory for a defined purpose.

The Source Package Integrity verifier shares the router's transport-name policy. Copy-suffixed ZIP/checksum aliases and filenames containing spaces may pair, but actual ZIP bytes and SHA-256 remain authoritative. Aliases containing different bytes or disagreeing hashes fail closed.

When Android exposes a readable sibling receipt, Forge uses it. When sibling access is withheld, Forge may hash the selected ZIP locally and stage a canonical receipt automatically. Manual checksum location remains an exceptional fallback.

## Forge completion truth

CLIENT and SERVER Forge can validate source pairs, stream GitHub blobs, create atomic commits, confirm the branch, verify uploaded paths, observe Actions, and retrieve artifacts and logs.

Every submission should carry a fresh transaction identifier and actor marker so Chat, Forge, workflows, logs, artifacts, and repair attempts remain correlated.

Transfer reaching 100 percent is not sufficient evidence that the repository changed or that a build succeeded.

```text
transfer complete
-> Git objects created
-> commit created
-> branch updated
-> branch confirmed
-> workflow discovered
-> workflow completed
-> artifact discovered
-> artifact downloaded
-> artifact verified
```

## CFv2.1.x autonomous update and repair direction

The accepted closed-loop architecture is:

```text
User requests update
-> CLIENT resolves exact downloaded ZIP + SHA
-> local integrity + lineage verification
-> Forge upload
-> commit-bound workflow observation
-> success? retrieve/verify artifact
-> failure? retrieve jobs/steps/logs
-> deterministic local diagnosis/repair when known
-> SWURVER reasoning when local knowledge is insufficient
-> configured external provider(s) only when needed
-> bounded retry
-> verified artifact install/update handoff
-> reconnect/resume and report result in Chat
```

SERVER updates are easier to orchestrate because CLIENT can remain alive while SERVER is replaced and reconnected. CLIENT self-updates require a durable `UpdateHandoff` so update state survives process/package replacement.

## Credential lifecycle and authority

Forge credentials remain Keystore-backed and scoped to the Android application that owns them. Temporary verification/network failures must not silently erase valid saved credentials. Secrets must never be exported into normal SWRLZ logs or provider repair payloads.

SERVER authority remains independent from GitHub authority. A successful GitHub operation does not grant node/session privileges.

## Chat, Missions, and bubbles

CFv2.1.x treats Chat as a conversational projection over actual local/server capabilities:

- casual conversation is not automatically a Mission;
- system-state questions are grounded in real runtime state;
- guided configuration flows do not require Mission semantics;
- substantial work can become approval-aware Missions;
- meaningful Forge/Mission transitions become Chat milestones;
- high-frequency progress mutates live operational cards rather than spamming transcript messages;
- local stop/pause/cancel commands bypass external LLM reasoning;
- bubble/full-app surfaces share authoritative application state rather than creating parallel authorities.

## Brain direction

SWRLZ cognition is intended to expand outward only when required:

```text
CLIENT local capabilities / memory / procedures
-> SWURVER local or remote knowledge/reasoning
-> external single or multi-LLM consultation
```

The lowest sufficient reasoning tier wins. Verified successful solutions can become procedural knowledge so repeated tasks migrate inward toward local execution.

Future Knowledge/Brain Packs should remain versioned, provenance-aware, evidence-tagged, and independently updateable from APK software releases.

## Android installability

In-place updates require package/signing continuity. Uninstall/reinstall is a fallback because uninstalling can destroy application data and trust continuity.

## Documentation entry points

- [`docs/architecture/CFV2_1_0_AUTONOMOUS_UPDATE_AND_REPAIR_V1.md`](docs/architecture/CFV2_1_0_AUTONOMOUS_UPDATE_AND_REPAIR_V1.md) — closed-loop update, failure analysis, retry, artifact, and install-handoff architecture.
- [`docs/reference/DOCUMENTATION_EVIDENCE_MODEL_V1.md`](docs/reference/DOCUMENTATION_EVIDENCE_MODEL_V1.md) — evidence levels, provenance rules, package accounting, and future Brain-ingestion discipline.
- [`docs/architecture/FORGE_EVENT_ROUTING_AND_EMBEDDED_BUBBLE_V1.md`](docs/architecture/FORGE_EVENT_ROUTING_AND_EMBEDDED_BUBBLE_V1.md) — authoritative lane/event routing, generic Android builds, integrity alignment, embedded bubble-shell architecture, and permission model.
- [`docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md`](docs/architecture/CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md) — Forge, identity, bubble authority, workflow observation, packaging, and installability foundation.
- [`docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md`](docs/roadmaps/CFV2_1_0_CHAT_ENTRY_GATE.md) — original coordinated Chat entry gate; historical baseline values inside this roadmap should be read as the state at the time of that document.
- [`reports/Blueprint_Council_Log.md`](reports/Blueprint_Council_Log.md) — architecture decisions, learning deltas, and evidence gates.

## Evidence discipline

Documentation distinguishes source implementation, static verification, compilation, workflow/build evidence, artifact generation, device/runtime evidence, integration evidence, accepted architecture, and inference. A feature is not runtime-accepted solely because source exists, a transfer reaches 100 percent, or a repair archive was created.
