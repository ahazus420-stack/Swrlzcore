# CFv2.1.0 Chat Entry Gate

**Target:** coordinated CLIENT and SERVER minor-version advancement  
**Current stabilization line:** CLIENT and SERVER `CFv2.0.x`

## Purpose

`CFv2.1.0` marks the transition from platform foundation to conversational control plane.

```text
2.0.x = identity, authority, nodes, Forge, artifacts, notifications, bubble foundation
2.1.0 = persistent Chat orchestrating real capabilities and creating approval-aware Missions
```

The minor-version change must not be used for a placeholder chat screen. CLIENT and SERVER advance together only when the shared contracts are ready.

## 2.0.x exit criteria

### Android identity

- CLIENT launcher resolves to the cyan wordless crystal dragon.
- SERVER launcher resolves to the purple wordless crystal guardian.
- SWURVER fused art appears only in authenticated admin/fusion context.
- Launcher state survives application update, launcher restart, reboot, and theme changes.
- Adaptive safe zones avoid clipping the primary silhouette.
- CLIENT and SERVER notification artwork is never reversed.
- Notification large icons contain real alpha transparency.
- Notification small icons are dedicated monochrome resources.

### Forge

- GitHub authentication and disconnect function reliably.
- Repository target and branch are explicit.
- CLIENT and SERVER packages auto-route correctly.
- Large ZIP uploads use bounded-memory streaming.
- Live byte progress advances through the actual transfer.
- Multi-file upload produces one atomic commit.
- Failure states distinguish permission, network, timeout, memory, repository, branch, and workflow errors.
- Workflow observer auto-refreshes while open.
- Queued, active, success, failure, cancellation, and skipped states are visually distinct.
- Workflow elapsed time and timestamps are correct.
- Artifact discovery and download function from the workflow card.
- Artifact transfer exposes byte progress, percentage, speed, and ETA where transport metadata permits.
- Saved APK/ZIP artifacts preserve meaningful names.
- Build-complete notification and install handoff are defined.

### Bubble foundation

- One Android-managed CLIENT conversation bubble is stable.
- CLIENT avatar is clean, circular, cyan, and padding-free.
- Authenticated admin mode may morph to the SWURVER fused avatar.
- Bubble opens a compact surface rather than another full-screen duplicate.
- Circular actions are started for Chat, Missions, Nodes, Groups, Forge, and Settings.
- Bubble state and last page can be restored.
- Old three-overlay CLIENT/SERVER/fusion experiment is removed or permanently disabled.
- Bubble visuals do not imply trust or authority.

### Shared platform

- CLIENT/SERVER protocol version boundaries are explicit.
- Node registration, presence, trust, authorization, and retirement remain separate.
- Forge and bubble actions route through capability interfaces suitable for Chat invocation.
- Audit/event envelopes can carry correlation IDs and user-approval state.

## CFv2.1.0 minimum deliverable

### CLIENT

- persistent local-first conversation storage;
- conversation/session identity;
- message delivery and rendering;
- system, user, assistant, capability, approval, result, error, and artifact message types;
- Forge result and artifact cards in Chat;
- basic Mission creation from Chat;
- capability approval prompts;
- compact bubble Chat entry;
- resumable conversation state.

### SERVER

- matching conversation and mission envelopes;
- authenticated remote/admin scopes;
- mission submission, acceptance, rejection, progress, and completion events;
- audit records and correlation IDs;
- node/group addressing primitives required by Chat commands;
- explicit protocol compatibility behavior.

## Initial Chat-to-Forge scenario

The first acceptance scenario should be concrete:

```text
User: Build the latest SERVER source package.

Chat
-> resolves Forge capability
-> presents target repository, branch, package, and requested action
-> obtains approval
-> stages and streams source
-> creates commit
-> dispatches or observes build
-> returns live phase events
-> returns artifact card
-> downloads with transfer progress on request
-> offers Install, Save, Share, Logs, Commit, and Workflow actions
```

No browser automation is required when the authenticated API capability is available.

## Accepted visual backlog for early 2.1.x

- animated dragon state driven by Forge phase;
- spinning crystal for active packaging, hashing, commit, workflow, or verification work;
- live GitHub event/timeline feed;
- artifact download animation tied to real transferred bytes;
- node activity indicators for local CLIENT, remote SERVER, GitHub-hosted Actions, and future build workers;
- build-complete notification actions;
- launcher safe-zone refinement;
- Forge diagnostics and health card.

## Artifact progress policy

Artifact and workflow progress are not the same:

- workflow progress is phase-based and may be indeterminate;
- artifact download progress may be exact from HTTP bytes and content length;
- artifact upload progress inside GitHub Actions may only be shown as a named phase unless GitHub provides step-level data;
- no percentage may be fabricated from elapsed time.

## Release gate

`CFv2.1.0` is ready only when:

1. 2.0.x exit criteria have device evidence;
2. CLIENT and SERVER shared contracts are versioned;
3. the initial Chat-to-Forge scenario completes end-to-end;
4. approval and audit boundaries remain intact;
5. artifacts can return to Chat and the bubble without bypassing security policy.
