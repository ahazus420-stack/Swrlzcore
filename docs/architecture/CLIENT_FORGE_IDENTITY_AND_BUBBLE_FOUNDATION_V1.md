# CLIENT Forge, Identity, and Bubble Foundation Architecture v1.1

**Status:** Accepted CFv2.0.x foundation architecture; final runtime acceptance remains evidence-gated.  
**Updated:** 2026-07-24  
**Related checkpoints:** `INT-FORGE-017A`, `INT-ID-018A`, CLIENT `CFv2.0.47` through `CFv2.0.51`, SERVER `CFv2.0.33` through `CFv2.0.36`

## 1. Purpose

This document defines the CLIENT-owned control architecture connecting Android package selection, GitHub repository mutation, GitHub Actions observation, workflow-log retrieval, artifact delivery, Android identity, role-specific bubble surfaces, and installable update continuity.

```text
User / future Chat request
        |
        v
SWRLZ CLIENT capability layer
        |
        +--> Forge staging and repository operations
        +--> GitHub Actions jobs, steps, logs, and artifacts
        +--> local CLIENT actions
        +--> authenticated SWURLZER administration
        +--> derived SWURVER fused administration
        +--> role-specific bubble projections
```

GitHub is a backend capability, not the product identity of the Forge interface.

## 2. Product identities and authority

```text
SWRLZ      Android CLIENT and local control surface
SWURLZER   SERVER / node host
SWURVER    authenticated fused CLIENT state with approved SERVER capabilities
```

SWURVER is not a third application. Visual state never grants authority.

Every privileged surface and action derives from authoritative state:

- authenticated user/session;
- selected SERVER identity;
- client-bound and server-bound trust;
- unexpired capability grants;
- route-level authorization;
- revocation state;
- audit policy.

Persisted layout, icon, bubble, or navigation state may restore presentation only. It must never restore authority. SWURLZER and SWURVER projections downgrade or disappear when trust is revoked, the session expires, the selected SERVER is lost, or required capabilities are removed.

## 3. Forge authority boundary

The CLIENT may stage files, request repository operations, observe builds, download logs and artifacts, and hand artifacts to Android installation flows. A visually selected repository, branch, workflow, or package is not authorization by itself.

Every mutation requires:

- authenticated GitHub credentials;
- repository scope;
- required permission scope;
- explicit branch;
- explicit staged-file preview;
- user confirmation or an approved Mission policy.

The SERVER remains authoritative for SWURLZER runtime state, node registration, trust, and mission admission. GitHub authority does not imply SERVER authority.

## 4. Forge staging and package routing

Canonical source routing:

```text
CLIENT_* -> SOURCES/CLIENT/
SERVER_* -> SOURCES/SERVER/
unknown  -> configured visible fallback or explicit routing
```

Staging invariants:

- new selections merge into the current staging set;
- duplicate URI selections do not erase prior files;
- a new source targeting an existing repository path replaces that staged destination;
- the preview shows filename, size, component, logical package grouping, and final repository path;
- staging clears only after the target branch is confirmed to point to the new commit.

### 4.1 ZIP and SHA pairing

Forge treats an exact-basename source ZIP and checksum as one logical package:

```text
<base>.zip
<base>.sha256
```

Default behavior:

- automatic ZIP/SHA sibling matching is enabled;
- the user grants a source folder through Android's Storage Access Framework;
- the persisted folder grant permits exact sibling lookup;
- matching never guesses similar names;
- manual multi-file selection remains available;
- commit is blocked when a ZIP lacks a readable and matching checksum.

### 4.2 Manifest policy

The active CFv2.0.x operational contract requires ZIP plus SHA. A sibling `<base>.manifest.json` is optional and is validated when present.

A manifest may become mandatory only after a defined downstream purpose is accepted, such as:

- component routing;
- version enforcement;
- provenance;
- release metadata;
- policy attestation.

The repository verifier and workflow must match this contract. Missing optional metadata must not be reported as a checksum failure.

## 5. Streaming transport and repeated updates

Android source packages may exceed browser upload limits and mobile heap capacity. Forge streams from Android storage rather than constructing whole-file in-memory copies.

Required properties:

- bounded buffers;
- no whole-ZIP `ByteArray` allocation;
- progress based on source bytes consumed;
- current-file and overall transaction progress;
- cancellation support;
- mobile-network-aware timeouts;
- retryable failure classification;
- no duplicate commit after a branch update already succeeded;
- repeat CLIENT or SERVER updates in the same app session create a new commit and associated workflow run.

Transfer completion is not commit success. Forge separates:

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

## 6. Authentication persistence

GitHub credentials are stored in Android Keystore-backed encrypted preferences, never logged, and removable through explicit disconnect.

Required behavior:

- persist synchronously before reporting connection success;
- survive process restart;
- survive a valid same-certificate in-place app update;
- temporary network or authentication-verification failures must not silently erase a saved credential;
- credential removal requires an explicit user action or confirmed invalidation policy.

## 7. Workflow observation and logs

Workflow runs are projections of GitHub's authoritative run data.

```text
queued / pending     warning treatment
in_progress          active treatment
success              success treatment
failure              failure treatment
cancelled / skipped  restrained treatment
```

Forge may render actual jobs and steps when available:

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

Unknown or parallel phases remain indeterminate. Elapsed time must not be converted into a fabricated compile percentage.

Each workflow card exposes:

- workflow name, event, branch, run identity, timestamps, and duration;
- actual jobs and steps;
- authoritative conclusion;
- workflow link;
- artifact action;
- **Download Logs** action for the GitHub Actions run-log ZIP.

### 7.1 Artifact transfer

Artifact download is a separate HTTP transfer and may expose exact progress when transport metadata supports it:

```text
bytes transferred / content length
percentage
transfer rate
ETA
verification state
save destination
```

When content length is absent, show transferred bytes plus indeterminate progress.

## 8. Forge visual state system

Visual effects are truthful projections only.

### Dragon status

- dormant: idle;
- awakening: validation or connection;
- carrying crystals: source transfer;
- forging: tree and commit creation;
- ignition: workflow dispatch/discovery;
- active aura: workflow running;
- completed glow: artifact ready;
- restrained fault: failure.

### Event timeline

Forge may emit correlated events such as:

```text
staging completed
checksum matched
local integrity verified
upload started
blob accepted
commit created
branch confirmed
workflow discovered
job/step transition
logs requested
artifact discovered
artifact download started
artifact verified
install handoff requested
```

## 9. Android identity and asset ownership

Canonical visual roles:

```text
CLIENT    cyan SWRLZ crystal dragon
SERVER    violet SWURLZER crystal guardian
SWURVER   cyan-violet fused identity inside authenticated CLIENT context
```

### 9.1 Launcher and recent-apps identity

Launcher identity must reconcile:

- application icon and round icon;
- launcher activity/alias;
- adaptive foreground/background;
- legacy density resources;
- theme-selection aliases;
- package update and reboot;
- launcher and recent-apps presentation.

Adaptive artwork must remain inside safe zones without excessive transparent or white margins.

### 9.2 Notification identity

- full-color large icons use genuine transparency;
- small notification icons use dedicated monochrome Android silhouettes;
- CLIENT and SERVER artwork must never be reversed;
- SWURVER art appears only for authenticated fused/admin state.

### 9.3 Asset hygiene

Resources may be removed only after checking references in:

- manifests and activity aliases;
- adaptive-icon XML;
- Kotlin/Java/Compose;
- XML resources;
- theme identity switching;
- build scripts.

Theme-selectable launcher families are not dead assets merely because they are not the current default.

## 10. Role-specific bubble control plane

The latest accepted model is a **CLIENT-owned role-specific bubble cluster**.

```text
SWRLZ bubble      local CLIENT authority
SWURLZER bubble   selected authenticated SERVER context
SWURVER bubble    fused CLIENT state with approved admin capabilities
```

These are three capability projections owned by the CLIENT. They are not three independently authoritative applications.

### 10.1 SWRLZ surface

May expose local chat, status, missions, pause/resume, permission recovery, local logs, and emergency stop.

### 10.2 SWURLZER surface

May expose selected SERVER chat, health, connected clients, queue, logs, maintenance, and disconnect only while the SERVER context is authenticated and authorized.

### 10.3 SWURVER surface

May expose cross-boundary approvals, deployment, unified health, trust, audit, artifacts, and coordinated stop only while the required fused capability set is valid.

### 10.4 Bubble invariants

- SWRLZ remains available without a SERVER.
- SWURLZER and SWURVER are gated by verified session state.
- persisted UI state cannot grant or preserve authority;
- role visuals downgrade immediately when authoritative state is lost;
- sensitive actions route through the same capability services as the full app;
- no bubble maintains an independent source of truth;
- bubble artwork remains tightly cropped and genuinely transparent;
- TalkBack linear fallback and sensitive-screen suppression remain required.

This section supersedes the earlier permanent single-bubble decision while preserving the valid security lesson: no fake remote authority and no visual-state trust elevation.

## 11. Chat and Mission integration

`CFv2.1.0` introduces the persistent conversational control plane over these real capabilities.

```text
"Build the latest SERVER"
-> Chat resolves Forge capability
-> target repository, branch, package, and action are shown
-> approval is obtained
-> Forge stages and validates ZIP/SHA
-> source streams to GitHub
-> commit and branch are confirmed
-> workflow jobs and steps are observed
-> logs and artifact actions are returned
-> user may download, verify, install, save, share, or reject
```

Chat does not bypass Forge validation, GitHub authorization, SERVER authorization, package integrity, or approval requirements.

## 12. Android installability

In-place updates require:

- the same `applicationId`;
- a higher `versionCode`;
- the exact same signing certificate.

One persistent key must sign every build channel intended to update an existing installation. Source changes cannot bypass Android certificate continuity. A one-time uninstall/reinstall may be required when migrating from an old debug key to the permanent project key.

## 13. Diagnostics

Forge health should expose:

- internet reachability;
- GitHub authentication and encrypted persistence;
- repository and branch access;
- Contents and Actions permissions;
- source-folder grant health;
- checksum matching and local validation;
- upload engine and branch-confirmation state;
- workflow/job/step availability;
- log-download capability;
- Android storage and artifact save/install capability.

Diagnostics must distinguish credential, permission, network, timeout, memory, repository, branch, integrity, workflow, log, and Android-storage failures.

## 14. Invariants

- Forge uses bounded-memory streaming.
- ZIP and SHA are required; manifest is optional until a defined policy makes it mandatory.
- Transfer completion never substitutes for branch confirmation.
- Build state comes from GitHub, not animation or elapsed-time guesses.
- Workflow logs remain user-initiated downloadable artifacts.
- CLIENT, SERVER, and SWURVER identities remain distinct.
- Bubble role state is a projection of CLIENT capability state.
- Visual fusion never grants trust or authority.
- Resource cleanup preserves referenced aliases and theme identities.
- Update-capable builds preserve application identity and signing continuity.
- Future Chat orchestrates capabilities; it does not simulate them through browser automation by default.