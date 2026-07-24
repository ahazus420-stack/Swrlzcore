# CLIENT Forge, Identity, and Bubble Foundation Architecture v1

**Status:** Accepted foundation architecture; final runtime acceptance pending repeated build, launcher, notification, and bubble evidence.  
**Related checkpoints:** `INT-FORGE-017A`, `INT-ID-018A`

## 1. Purpose

This document defines the CLIENT-owned control architecture that connects Android package selection, GitHub repository mutation, GitHub Actions observation, artifact delivery, Android visual identity, and the preliminary conversation-bubble surface.

The design establishes SWRLZ CLIENT as the control plane:

```text
User / future Chat request
        |
        v
SWRLZ CLIENT capability layer
        |
        +--> Forge package and repository operations
        +--> GitHub Actions observation
        +--> artifact delivery and install handoff
        +--> authenticated SWURLZER administration
        +--> Android bubble projection
```

GitHub is a backend capability, not the product identity of the Forge interface.

## 2. Forge authority boundary

The CLIENT may stage files, request repository operations, observe builds, and retrieve artifacts. It must not treat a visually selected repository or workflow as authorization by itself.

Every mutation requires:

- authenticated GitHub credentials;
- repository scope;
- required permission scope;
- explicit target branch;
- explicit staged-file preview;
- user confirmation or an approved future Mission policy.

The SERVER remains authoritative for SWURLZER runtime state, node registration, trust, and mission admission. GitHub authority does not imply SERVER authority.

## 3. Forge package pipeline

```text
Select CLIENT/SERVER packages
-> classify filenames
-> preview authoritative paths
-> validate size and readability
-> stream each blob with bounded memory
-> construct one Git tree
-> create one commit
-> update target branch
-> report commit identity
```

Canonical package routing:

```text
CLIENT_* -> SOURCES/CLIENT/
SERVER_* -> SOURCES/SERVER/
```

Unknown files must use a visible configured fallback path or require explicit routing.

## 4. Streaming transport

Android source packages may exceed browser upload limits and may eventually grow significantly. Forge therefore streams from Android storage rather than creating full in-memory copies.

Required properties:

- bounded buffer size;
- progress based on bytes consumed from the source stream;
- cancellation support;
- mobile-network-aware timeouts;
- retryable failure classification;
- no duplicate commit when retrying after branch update success;
- integrity metadata preserved when checksum files are staged.

Forge UI progress should distinguish the current file from overall transaction progress.

## 5. Workflow observation

Workflow runs are projections of GitHub's authoritative run data.

State treatment:

```text
queued / pending     warning treatment
in_progress          active treatment
success              success treatment
failure              failure treatment
cancelled / skipped  neutral or restrained treatment
```

Forge may calculate elapsed duration from timestamps. It must not calculate a false compile percentage from elapsed time.

### 5.1 Phase progress

When GitHub job and step data is available, Forge may render a discrete phase timeline:

```text
queued
-> source resolution
-> integrity validation
-> Gradle configuration
-> compile
-> tests / lint
-> package APK
-> artifact upload
-> complete
```

Unknown or parallel phases remain indeterminate.

### 5.2 Artifact transfer progress

Artifact download is a separate HTTP transfer and may expose exact progress:

```text
bytes downloaded / content length
percentage
transfer rate
ETA
verification state
save destination
```

If content length is unavailable, use transferred bytes plus indeterminate progress rather than a fabricated percentage.

## 6. Forge visual state system

Future visuals are permitted only as truthful state projections.

### Dragon status

- dormant: idle;
- awakening: validation or connection;
- carrying crystals: source upload;
- forging: tree and commit creation;
- ignition: workflow dispatch;
- active aura: workflow running;
- completed glow: artifact ready;
- restrained fault state: failure.

### Spinning crystal

A spinning crystal may indicate an active bounded operation such as packaging, hashing, commit construction, build observation, or artifact verification. It must stop or change state when the operation blocks, fails, or completes.

### Live event stream

Forge may maintain a timestamped event timeline:

```text
staging completed
upload started
blob accepted
commit created
workflow queued
workflow started
job/step transition
artifact discovered
artifact download started
integrity verified
install handoff requested
```

Each event should retain a correlation identifier where available.

### Node activity indicators

Forge-related activity may be projected to Nodes when a CLIENT, SERVER, GitHub backend, or future build worker participates. Indicators must identify whether activity is local, remote, GitHub-hosted, or SERVER-hosted.

## 7. Android identity architecture

Canonical visual roles:

```text
CLIENT    cyan crystal signal dragon
SERVER    purple crystal guardian
SWURVER   cyan-purple fused identity
```

### 7.1 Launcher

The launcher icon must use the wordless full-color identity, fit the adaptive safe zone, and remain recognizable at small sizes.

The icon system must reconcile:

- default activity/alias;
- application icon and round icon;
- adaptive foreground/background;
- legacy density icons;
- theme-selection aliases;
- package upgrade;
- launcher cache invalidation where possible.

Recommended final refinement: modestly increase the safe-zone margin so horns and crystal shards are not clipped by aggressive launcher masks while keeping the dragon large enough to match the notification identity.

### 7.2 Notification

Notification large icons use transparent full-color art. Notification small icons use dedicated monochrome Android silhouettes. The two resource classes must not be reused interchangeably.

### 7.3 Bubble

Bubble/person avatars use compact circular artwork with genuine alpha transparency and no text.

## 8. Bubble ownership

The accepted model is one CLIENT-owned Android-managed conversation bubble.

```text
normal local-first state -> cyan CLIENT avatar
authenticated admin state -> SWURVER fused avatar
```

The purple SERVER identity may appear inside authenticated remote-control content, but a second fake SERVER bubble is not required.

The bubble launches a compact CLIENT surface that can route to:

- Chat;
- Missions;
- Nodes;
- Groups;
- Forge;
- Settings.

The bubble surface observes the same repositories and capability layer as the full app. It must not maintain a separate source of truth.

## 9. Chat and Mission integration

`CFv2.1.0` introduces the conversational control plane over existing capabilities.

Example:

```text
"Build the latest SERVER"
-> Chat creates an approval-aware Forge command
-> Forge stages or resolves the package
-> repository operation executes
-> workflow is observed
-> artifact card returns to Chat
-> user may download, install, inspect logs, or reject
```

Chat does not bypass Forge validation, GitHub authorization, SERVER authorization, or approval requirements.

## 10. Diagnostics

Forge health should eventually expose:

- internet reachability;
- GitHub authentication;
- repository access;
- Contents permission;
- Actions permission;
- branch resolution;
- workflow availability;
- Android storage availability;
- upload engine state;
- artifact save/install capability.

Diagnostics must distinguish credential, permission, network, memory, repository, workflow, and Android-storage failures.

## 11. Invariants

- Forge uses bounded-memory streaming for large files.
- Build status comes from GitHub, not animation or elapsed-time guesses.
- Artifact download progress is exact only when transport metadata supports it.
- CLIENT, SERVER, and SWURVER identities remain distinct.
- Notification and launcher resources remain purpose-specific.
- Bubble state is a projection of CLIENT capability state.
- Visual fusion never grants trust or authority.
- Future Chat orchestrates capabilities; it does not simulate them through browser automation by default.
