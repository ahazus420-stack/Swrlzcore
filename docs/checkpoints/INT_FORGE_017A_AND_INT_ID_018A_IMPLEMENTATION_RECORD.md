# INT-FORGE-017A + INT-ID-018A Implementation Record

**Recorded:** 2026-07-23  
**Status:** Device-observed foundation implemented; final runtime acceptance remains gated by repeat CLIENT/SERVER builds, launcher persistence, notification identity checks, and bubble stabilization.

## Scope

This record captures the July 23, 2026 implementation sequence that established:

- `INT-FORGE-017A` — authenticated CLIENT-owned GitHub Forge upload, workflow observation, artifact retrieval, and large-file streaming.
- `INT-ID-018A` — canonical CLIENT, SERVER, and SWURVER visual identities across launcher, notification, bubble, and admin surfaces.
- preliminary Android conversation-bubble foundations required before the `CFv2.1.0` Chat control plane.

## Source lineage

Observed source progression during this work:

- CLIENT advanced through `CFv2.0.36` to `CFv2.0.46`.
- SERVER advanced through `CFv2.0.31` to `CFv2.0.32`.
- Forge successfully uploaded SERVER source packages, created commits, observed GitHub Actions runs, and downloaded SERVER APK artifacts back to Android.

Patch numbering remains app-specific through the `2.0.x` stabilization line. CLIENT and SERVER are intended to advance together to `CFv2.1.0` when the shared Chat, mission-envelope, authorization, and event contracts are ready.

## INT-FORGE-017A requirements

### CLIENT-owned GitHub Forge

Forge is a first-class CLIENT capability and is available in the new User Mode navigation as its own `Forge` destination. Developer Mode and Settings shortcuts may remain as compatibility entry points.

Forge responsibilities:

```text
Authenticate GitHub account
-> stage CLIENT and SERVER packages
-> route packages to authoritative repository locations
-> upload files
-> create one atomic tree/commit
-> optionally dispatch GitHub Actions
-> observe workflow state
-> discover and download artifacts
-> expose results to future Chat and Mission layers
```

### Canonical routing

Default source-package routing:

```text
CLIENT_* -> SOURCES/CLIENT/
SERVER_* -> SOURCES/SERVER/
unknown  -> configured fallback destination
```

The staged-file preview must show filename, size, and final repository path before commit.

### Authentication and permission boundary

Forge supports a fine-grained GitHub personal access token for the current personal-development workflow.

Minimum intended repository permissions:

```text
Contents  read/write
Actions   read/write
Metadata  read-only
```

Workflow-file mutation and pull-request permissions are optional and must be requested only when corresponding features are enabled.

Tokens are stored using Android Keystore-backed encrypted storage, never logged, and removable through an explicit disconnect action.

### Atomic commit pipeline

Forge creates one commit for the staged package set rather than exposing partially uploaded source state.

```text
resolve branch head
-> create file blobs
-> create Git tree
-> create commit
-> update branch ref
```

A completed commit displays the resulting commit identifier.

### Streaming upload and memory safety

The original whole-file path multiplied memory usage through raw bytes, Base64 conversion, and JSON serialization. Large CLIENT/SERVER packages could therefore cause Android heap exhaustion.

The accepted upload path is streaming:

```text
ContentResolver/InputStream
-> bounded buffer
-> streamed Base64 encoding
-> GitHub request body
```

Requirements:

- no whole-ZIP `ByteArray` allocation;
- bounded working memory;
- live transferred-byte reporting;
- upload percentage based on actual source bytes read;
- explicit size-limit validation before transfer;
- network timeout and retry behavior appropriate for mobile connections.

Device evidence showed live progression through transferred MiB and successful completion rather than appearing permanently frozen near 30 percent.

### Workflow observer

The observer displays:

- workflow name;
- branch and event;
- queued, in-progress, completed, success, failure, cancelled, or skipped state;
- start timestamp;
- elapsed or completed duration;
- theme-derived state treatment;
- workflow link;
- artifact action.

Auto-refresh defaults to a bounded interval while Forge is open. A future smart-refresh policy may use shorter intervals while active and longer intervals after completion.

GitHub does not expose a trustworthy exact compile percentage. Forge must not invent one. It may show:

- an indeterminate running animation;
- known job/phase completion when job data is available;
- elapsed time;
- artifact-ready completion.

### Artifact progress

Two progress domains must remain distinct:

1. **Workflow/build progress** — phase-based and indeterminate unless GitHub exposes concrete job-step state.
2. **Artifact download progress** — exact transferred bytes, percentage, speed, and ETA when the HTTP response length is known.

Future artifact cards should expose:

```text
Artifact discovered
-> download queued
-> downloading bytes / total bytes
-> integrity verification
-> saved
-> install/share/open actions
```

The workflow-card `ARTIFACTS` action should directly download when exactly one valid artifact exists; otherwise it opens the run artifact list.

## INT-ID-018A requirements

### Canonical identities

```text
SWRLZ CLIENT     cyan/blue crystal signal dragon
SWURLZER SERVER  purple crystal guardian dragon
SWURVER ADMIN    cyan-purple fusion dragon
```

CLIENT and SERVER notification artwork must never be reversed.

### Android launcher identity

The launcher must resolve consistently across:

- application `android:icon`;
- `android:roundIcon`;
- launcher activity or alias;
- adaptive foreground/background resources;
- legacy density resources;
- theme-switch reconciliation;
- update and reboot behavior.

The canonical launcher artwork should match the clean circular notification identity while respecting Android adaptive-icon safe zones. Theme switching must return to the canonical identity rather than an obsolete alias or placeholder.

### Notification identity

Large notification artwork uses clean, alpha-transparent full-color dragon artwork. Notification small icons remain separate Android-compliant monochrome silhouettes.

Requirements:

- no white or checkerboard background;
- no CLIENT/SERVER inversion;
- no text-heavy artwork in constrained icon positions;
- status notification and expanded notification use the correct app identity;
- SWURVER artwork appears only for authenticated fused/admin context.

Device evidence confirmed a substantially cleaner CLIENT notification identity and correct cyan artwork after the update sequence.

## Bubble foundation

The abandoned three-overlay experiment rendered CLIENT, remote SERVER, and fusion bubbles from one CLIENT process, causing every tap to open the CLIENT surface. The accepted direction is one Android-managed SWRLZ conversation bubble.

Foundation requirements before `CFv2.1.0`:

- one clean circular CLIENT avatar;
- authenticated SWURVER avatar morph for admin mode;
- compact bubble UI rather than a duplicate full-screen application window;
- initial circular actions for Chat, Missions, Nodes, Groups, Forge, and Settings;
- last-page restoration;
- capability routing into real subsystems;
- no cross-package overlay coordination;
- no revival of the three-floating-overlay model.

## Future Forge visual and observability work

Accepted design backlog:

- animated dragon status by Forge phase;
- spinning crystal while packaging, committing, or building;
- live GitHub event/timeline feed;
- artifact download animations;
- exact artifact byte progress where available;
- node activity indicators;
- build-complete notifications with Install, Artifact, Logs, and Open Forge actions;
- launch-safe icon refinement with slightly increased adaptive safe-zone margin;
- Forge health diagnostics for authentication, repository access, branch, Actions, storage, and network state.

Visual effects are projections of authoritative state. Animation must never imply a phase or success that the underlying Forge state has not confirmed.

## Runtime acceptance gate

This checkpoint is accepted only after repeated evidence confirms:

- CLIENT and SERVER large-package streaming without OOM;
- complete atomic commit creation;
- workflow observation from queued through completion;
- artifact download with correct naming and content;
- launcher identity surviving update, reboot, and theme changes;
- correct CLIENT/SERVER notification mapping;
- clean alpha transparency;
- stable Android-managed bubble entry and compact navigation.

## Excluded authority

This record does not authorize:

- fabricated build percentages;
- plaintext token storage;
- unrestricted GitHub permissions by default;
- automatic direct-to-main mutation without confirmation or policy;
- release signing keys stored in ordinary CLIENT storage;
- trust elevation through visual fusion alone;
- claims of complete Chat or Mission functionality before `CFv2.1.0` evidence.