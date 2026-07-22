# SWRLZ Engineering Log

This living engineering journal records implementation checkpoints, runtime findings, corrective work, verification evidence, and unresolved items for SWRLZ-Core CLIENT, SWURLZER SERVER, and related integration surfaces.

## Operating rules

- The GitHub repository is the long-term source of truth.
- Integrate; do not overwrite.
- Work one bounded checkpoint at a time.
- Separate facts, requirements, assumptions, recommendations, and runtime evidence.
- Preserve offline-first behavior, identity, trust, Truth Firewall, lineage, local-versus-remote distinctions, and protocol-version discipline.
- Do not claim a build, runtime result, migration result, registration result, workflow result, release, or deployment without direct evidence.
- Every future source-package delivery must update this log and the relevant checkpoint, architecture, contract, release, migration, user, developer, roadmap, or UI documentation affected by the change.

---

## 2026-07-22 — INT-CONV-012A

### Objective
Implement the CLIENT/SERVER communication foundation while preserving the separation between ordinary conversation and approved mission execution.

### Source lineage
- CLIENT advanced from CFv2.0.2 to CFv2.0.3.
- SERVER advanced from CFv2.0.3 to CFv2.0.4.

### Engineering changes
- Added matching communication envelope models.
- Added conversation routing rules for ordinary chat, approval-required promotion, existing missions, and rejected mission requests without mission identity.
- Added compatibility fixtures and matching JVM tests.
- Preserved protocol-version discipline and avoided transport, provider, Room, and endpoint changes.

### Evidence state
- Source-only implementation completed.
- Static verification completed.
- APK builds were performed later by the repository workflow and reported successful by the operator.

### Follow-up discovered
- User Mode still routed operational controls into Developer Mode.

---

## 2026-07-22 — INT-UI-013A

### Objective
Keep normal CLIENT and SERVER operation entirely inside User Mode while retaining Developer Mode for engineering and testing.

### Source lineage
- CLIENT advanced to CFv2.0.4.
- SERVER advanced to CFv2.0.5.

### Engineering changes
- Added CLIENT User Mode routes for permissions, style, sharing, discovery, and node trust.
- Added Activity surfaces.
- Reworked SERVER runtime control toward a single state-aware action.
- Added launcher icon resources from the crystalline Glitch Dragon artwork.

### Runtime findings
- SERVER showed `START SWURLZER` while loopback services were active but health was `DEGRADED`.
- CLIENT launcher icon remained incorrect.
- CLIENT workflow artifact contained duplicate APKs.
- SERVER did not yet show the connected CLIENT as registered.

---

## 2026-07-22 — INT-RUNTIME-014A

### Objective
Repair runtime-state presentation, degraded-state explanation, launcher branding, artifact packaging, and presence truthfulness.

### Source lineage
- CLIENT advanced to CFv2.0.5.
- SERVER advanced to CFv2.0.6.

### Engineering changes
- Treated `STARTING`, `RUNNING`, and `DEGRADED` as runtime-active.
- Added reason-coded degraded presentation, including local-only operation when no approved Wi-Fi/LAN interface is available.
- Clarified that reachability alone is not authoritative registration.
- Added exact-one-APK staging guards in the CLIENT source workflow.

### Runtime findings
- SERVER correctly explained `LOCAL-ONLY` degradation.
- Persistent registration and lifecycle orchestration were still missing.

---

## 2026-07-22 — INT-PRES-015A

### Objective
Implement verified automatic CLIENT registration, persistent SERVER node inventory, heartbeat-derived presence, synchronized counts, and state-driven node visuals.

### Source lineage
- CLIENT advanced to CFv2.0.6.
- SERVER advanced to CFv2.0.7.

### Engineering changes
- CLIENT automatically initiated registration after SERVER discovery and status verification.
- Added durable CLIENT installation identity and stable node identity.
- Added SERVER registration, heartbeat, and node-list routes.
- Added persistent SERVER node records with a non-destructive Room migration.
- Added registered, online, offline, connecting, verifying, registering, connected, busy, and failure-related state concepts.
- Preserved the boundaries: registered is not trusted, reachable is not authorized, and proof presented is not proof accepted.

### Runtime evidence
- Later device testing proved:
  - registered node count remained durable at `1`;
  - online/offline changed independently of durable registration;
  - CLIENT displayed `REGISTERED`, `CONNECTED`, `UNTRUSTED`, and `PENDING` proof state;
  - SERVER displayed `1 REGISTERED`, `1 ONLINE`, `0 OFFLINE` while the CLIENT was connected.

### Follow-up discovered
- CLIENT compilation failed because of a duplicate local `context` declaration.
- SERVER restart entered a stale-listener failure state.

---

## 2026-07-22 — INT-LIFE-016A

### Objective
Implement a professional state-aware SERVER lifecycle and graceful shutdown sequence.

### Source lineage
- Implemented together with SERVER CFv2.0.7.

### Engineering changes
- Added lifecycle states for stopped, starting, active, degraded, maintenance, configuration reload, service restart, shutdown, and failure.
- Added maintenance, reload, restart, and graceful shutdown actions.
- Added a ten-stage shutdown sequence covering admission closure, CLIENT notification, operation draining, write/queue flushing, state persistence, log finalization, encrypted-store closure, worker/service shutdown, network shutdown, and clean power-down.

### Runtime findings
- Initialize and shutdown worked.
- Restart exposed a listener-generation race and could leave the SERVER failed until reinstall.
- Lifecycle work temporarily blocked the UI.

---

## 2026-07-22 — INT-RECOVERY-017A

### Objective
Repair CLIENT compilation, SERVER restart generation safety, failed-state recovery, and active-versus-degraded status presentation.

### Source lineage
- CLIENT advanced to CFv2.0.7.
- SERVER advanced to CFv2.0.8.

### Engineering changes
- Removed duplicate `LocalContext.current` declaration.
- Added runtime generation identity to SERVER listeners.
- Made intentional stop/restart transitions distinct from unexpected listener termination.
- Ordered restart cleanup: retire generation, close sockets, interrupt/join workers, clear stale references, then start a new generation.
- Added failed-state retry without reinstall.
- Added separate `ACTIVE` and `DEGRADED` indicators in the detailed health surface.

### Runtime evidence
- CLIENT CFv2.0.7 built successfully.
- SERVER shutdown and subsequent initialization worked.
- SERVER restart no longer wedged in `FAILED`.
- SERVER showed `ACTIVE` alongside `DEGRADED` in the lower health card.

### Follow-up discovered
- Top SERVER header still displayed `CORE · DEGRADED` instead of lifecycle state.
- Restart and shutdown still appeared to hang because lifecycle work blocked the UI path.
- Artifact ZIP still surfaced two APK install entries.
- Android launcher still displayed an incorrect themed/adaptive icon despite installer surfaces showing the correct dragon.

---

## 2026-07-22 — INT-UXPACK-019A

### Objective
Correct SERVER lifecycle header and responsive progress, enforce a single final CLIENT APK artifact, repair launcher branding, implement state-aware auto-connect, and move CLIENT Nodes into Core.

### Source lineage
- CLIENT advanced to CFv2.0.8.
- SERVER advanced to CFv2.0.9.

### Engineering decisions
- CLIENT node/server connectivity belongs in the Core home surface because it is primary operational state, not a separate application domain.
- Primary CLIENT navigation becomes Core, Chat, Missions, Activity, and Settings.
- The connection action must derive from real state rather than always saying `RECONNECT`.
- Auto-connect on app launch defaults to enabled and is persisted locally.
- SERVER top header represents lifecycle; detailed health cards represent degradation and availability.

### Engineering changes
- Moved saved SERVER, registration, discovery, trust, and connection controls into CLIENT Core.
- Added state-derived labels such as CONNECT, RETRY CONNECTION, RECONNECT, CONNECTING, and CONNECTED.
- Added persistent default-enabled auto-connect and saved-SERVER-first startup behavior.
- Added dedicated adaptive foreground, legacy, round, and monochrome launcher layers.
- Added final artifact staging guards and version-correct artifact names.
- Moved SERVER lifecycle work to background coroutine execution and added visible operation progress.
- Disabled conflicting lifecycle actions during transitions.

### Evidence state
- Source packages prepared and delivered.
- Build and device acceptance still pending at the time of this log creation.

---

## 2026-07-22 — DOC-ENG-001A

### Objective
Create a durable engineering journal and establish documentation as a required deliverable for every future source-package checkpoint.

### Documentation policy
Whenever a new CLIENT, SERVER, NODE_HOST, launcher, keyboard, plugin, or related source download is delivered, the same checkpoint must update:

1. This engineering log.
2. The bounded checkpoint record.
3. Architecture documentation when architecture changed.
4. Protocol/data contracts when behavior or wire contracts changed.
5. Release notes for each advanced source version.
6. Migration documentation when storage or protocol migrations changed.
7. User/developer/UI documentation when operator behavior changed.
8. Blueprint Council log when the change introduces a durable architectural learning delta.
9. Receipts and checksums.

### Evidence discipline
Each entry must explicitly label:
- implemented in source;
- statically verified;
- built or not built;
- runtime tested or not tested;
- repository-published or package-only;
- known failures and unresolved follow-up.

### Status
- Engineering log created and recent checkpoint history backfilled.
- A reusable ChatGPT documentation skill was prepared separately for installation.