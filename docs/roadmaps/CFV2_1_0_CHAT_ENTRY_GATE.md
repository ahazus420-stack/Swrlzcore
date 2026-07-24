# CFv2.1.0 Chat Entry Gate

**Target:** coordinated CLIENT and SERVER minor-version advancement  
**Current stabilization line:** CLIENT and SERVER `CFv2.0.x`  
**Current packaged baselines:** CLIENT `CFv2.0.51`; SERVER `CFv2.0.36`  
**Updated:** 2026-07-24

## Purpose

`CFv2.1.0` marks the transition from platform foundation to a persistent conversational control plane.

```text
2.0.x = identity, authority, nodes, Forge, artifacts, logs, notifications, bubble foundation
2.1.0 = persistent Chat orchestrating real capabilities and creating approval-aware Missions
```

The minor-version change must not be used for a placeholder chat screen. CLIENT and SERVER advance together only when shared contracts, authority boundaries, artifacts, and evidence are ready.

## 2.0.x exit criteria

### Android identity and asset hygiene

- CLIENT launcher and recent-apps presentation resolve to cyan SWRLZ artwork.
- SERVER launcher and recent-apps presentation resolve to violet SWURLZER artwork.
- SWURVER fused art appears only inside authenticated CLIENT admin/fusion context.
- Launcher identity survives valid app update, launcher restart, reboot, and theme changes.
- Adaptive safe zones avoid clipping and excessive transparent or white margins.
- CLIENT and SERVER notification artwork is never reversed.
- Large notification icons contain genuine alpha transparency.
- Small notification icons are dedicated monochrome resources.
- Resource cleanup preserves referenced manifest aliases, theme-selectable launcher families, adaptive icons, notification assets, and active bubble art.

### Forge staging and integrity

- Additional selections merge into existing staging.
- Re-selecting a repository destination replaces that staged destination without duplication.
- CLIENT and SERVER source packages auto-route correctly.
- Selecting a ZIP auto-matches its exact sibling SHA by default when the approved source folder is available.
- ZIP and SHA render as one logical source-package card.
- Local SHA validation completes before upload.
- The active package contract is ZIP+SHA required and manifest optional.
- The repository verifier and Source Package Integrity workflow implement the same contract.

### Forge transport and commit truth

- Large ZIP uploads use bounded-memory streaming.
- Live byte progress advances through the actual transfer.
- Transfer completion, commit creation, branch confirmation, workflow discovery, build completion, and artifact return remain separate phases.
- Multi-file upload produces one atomic commit.
- Staging clears only after the target branch confirms the new commit.
- A second CLIENT or SERVER update in the same app session creates a new commit and newly associated workflow run.
- Failure states distinguish credential, permission, network, timeout, memory, repository, branch, integrity, workflow, log, and Android-storage errors.

### GitHub authentication

- Connect and disconnect function reliably.
- Repository target and branch are explicit.
- Credentials are stored in Keystore-backed encrypted preferences.
- Credential persistence survives process restart and a valid same-certificate in-place update.
- Temporary network or verification failures do not erase a valid saved credential.
- Credentials are never included in SWRLZ-exported logs.

### Workflow observation, logs, and artifacts

- Workflow observer refreshes while active.
- Queued, active, success, failure, cancellation, and skipped states are distinct.
- Elapsed time and timestamps are correct.
- Actual GitHub jobs and steps are displayed where available.
- No build percentage is fabricated from elapsed time.
- Every workflow card can download the run-log ZIP.
- Artifact discovery and download work from the workflow card.
- Artifact transfer exposes bytes, percentage, speed, and ETA where transport metadata permits.
- Saved APK/ZIP artifacts preserve meaningful names.
- Build-complete notification and install handoff are defined.

### Bubble authority foundation

The accepted direction is a CLIENT-owned role-specific bubble cluster:

```text
SWRLZ bubble      local CLIENT authority
SWURLZER bubble   selected authenticated SERVER context
SWURVER bubble    fused CLIENT state with approved admin capabilities
```

Exit requirements:

- SWRLZ remains available without a SERVER.
- SWURLZER and SWURVER require verified session and capability state.
- Persisted layout or visual state cannot grant authority.
- Session expiry, revocation, lost SERVER selection, or removed capabilities downgrade the projected surfaces.
- CLIENT, SERVER, and fused artwork are clean, tightly cropped, and padding-free.
- Each role opens a compact role-appropriate surface rather than an unrelated full-screen duplicate.
- Actions route through the same authoritative capability services as the full app.
- Bubble state and last page can be restored without restoring authority.
- TalkBack linear fallback and sensitive-screen suppression are defined.
- Bubble visuals never imply trust or permission by themselves.

This supersedes the earlier permanent single-bubble decision while retaining its valid security boundary against fake remote authority.

### Android installability

- CLIENT updates preserve `applicationId`, increase `versionCode`, and use the same signing certificate.
- SERVER updates preserve `applicationId`, increase `versionCode`, and use the same signing certificate.
- Every update-capable build channel uses the same persistent project key.
- The transition from historical debug signing to the permanent key is documented, including any one-time uninstall/reinstall requirement.

### Shared platform

- CLIENT/SERVER protocol version boundaries are explicit.
- Node registration, presence, trust, authorization, revocation, and retirement remain separate.
- Forge and bubble actions route through capability interfaces suitable for Chat invocation.
- Audit/event envelopes carry correlation IDs and user-approval state.
- Administrative capability discovery does not grant authority.

## CFv2.1.0 minimum deliverable

### CLIENT

- persistent local-first conversation storage;
- conversation and session identity;
- message delivery and rendering;
- system, user, assistant, capability, approval, result, error, log, and artifact message types;
- Forge status, logs, and artifact cards in Chat;
- basic Mission creation from Chat;
- capability approval prompts;
- role-aware compact bubble Chat entry;
- resumable conversation state;
- explicit downgrade when remote/fused authority is lost.

### SERVER

- matching conversation and mission envelopes;
- authenticated remote/admin scopes;
- capability-manifest negotiation without implicit granting;
- mission submission, acceptance, rejection, progress, and completion events;
- audit records and correlation IDs;
- node/group addressing primitives required by Chat commands;
- explicit protocol compatibility behavior;
- revocation and expiry behavior for administrative sessions.

## Initial Chat-to-Forge acceptance scenario

```text
User: Build the latest SERVER source package.

Chat
-> resolves Forge capability
-> presents repository, branch, package pair, and requested action
-> confirms ZIP/SHA integrity
-> obtains approval
-> stages and streams source
-> creates commit
-> confirms branch head
-> discovers and observes workflow
-> returns actual job/step events
-> exposes run-log ZIP
-> returns artifact card
-> downloads and verifies artifact on request
-> offers Install, Save, Share, Logs, Commit, and Workflow actions
```

No browser automation is required when the authenticated API capability is available.

## Initial bubble-authorization acceptance scenario

```text
CLIENT starts without SERVER
-> SWRLZ surface available
-> SWURLZER and SWURVER unavailable

CLIENT authenticates to selected SERVER
-> supported capabilities discovered
-> approved admin grants established
-> SWURLZER surface becomes available
-> SWURVER appears only when fused requirements are satisfied

session expires or grant is revoked
-> SWURLZER/SWURVER actions stop
-> surfaces downgrade or disappear
-> persisted UI state does not restore authority
-> audit event records the transition
```

## Accepted visual backlog for early 2.1.x

- animated dragon state driven by authoritative Forge phase;
- spinning crystal for active packaging, hashing, commit, workflow, or verification work;
- live GitHub event/timeline feed;
- artifact animation tied to real transferred bytes;
- node activity indicators for local CLIENT, remote SERVER, GitHub Actions, and future workers;
- build-complete notification actions;
- launcher safe-zone refinement;
- Forge diagnostics and health card;
- role-specific bubble animations that never imply unverified authority.

## Progress policy

Workflow and transfer progress are not the same:

- source upload progress may be exact from bytes read;
- commit progress is phase-based;
- workflow progress is job/step based and may remain indeterminate;
- run logs are downloadable after GitHub exposes them;
- artifact download progress may be exact from HTTP bytes and content length;
- no percentage may be fabricated from elapsed time.

## Release gate

`CFv2.1.0` is ready only when:

1. the complete 2.0.x exit criteria have current device/workflow evidence;
2. CLIENT and SERVER shared contracts are versioned;
3. the Chat-to-Forge scenario completes end to end;
4. the bubble-authorization scenario passes expiry and revocation tests;
5. approval, trust, and audit boundaries remain intact;
6. logs and artifacts return to Chat and bubble surfaces without bypassing policy;
7. CLIENT and SERVER update APKs install over prior versions using the permanent signing keys;
8. the repository integrity workflow matches the documented ZIP+SHA-required, manifest-optional contract.