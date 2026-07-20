# INTEGRATION-FIX-011P — CLIENT Implementation Impact Review

- **Checkpoint:** `INTEGRATION-FIX-011P`
- **Status:** Review complete; implementation remains separately approval-gated
- **Branch:** `checkpoint/server-presence-registry-011k`
- **Reviewed source lane:** canonical `CLIENT_CFv1.0.0_SWRLZ.zip`
- **Contract basis:** `docs/contracts/CLIENT_AUTOMATIC_BOOTSTRAP_GROUP_RESTORATION_RADAR_DEV_MODE_V1.md`
- **Addendum basis:** approved 011O-A SERVER-authoritative persistent admin-device/session decisions
- **Protocol/schema assumption:** no silent change from `1 / 1`

## 1. Scope and guardrails

This checkpoint reviewed the existing Android CLIENT implementation and mapped it to the approved automatic bootstrap, persistent group restoration, Radar privacy, persistent Dev Mode, and SERVER-authoritative admin-session requirements.

This checkpoint did **not** modify application source, build an APK, run a workflow, install software, merge/rebase branches, promote to `main`, release, or deploy.

## 2. Current implementation facts

### 2.1 Existing reusable foundations

The current CLIENT already contains several useful foundations:

1. `Prefs.kt`
   - persists Core Node URL and check history;
   - persists relay group ID, device ID, device label, and role;
   - stores relay group/device secrets through `SecretStore`;
   - provides `ensureRelayIdentity()` for stable local device identity material;
   - persists admin username, admin mode, session ID, last verification time, and broad-device visibility preference;
   - stores the admin session token through encrypted preferences.

2. `SecretStore.kt`
   - uses Android Keystore-backed `EncryptedSharedPreferences`;
   - already separates ordinary preferences from pairing, relay, and admin-session secrets.

3. `CoreNodeAutoDiscovery.kt`
   - attempts saved/last-good candidates and verified discovery;
   - verifies `/discovery/signature` before persisting a discovered endpoint;
   - saves the verified endpoint and last-good timestamp;
   - already supports the saved-first/discover-second direction.

4. `Api.kt` / `CoreNodeApi`
   - contains Core Node status calls;
   - contains group create/join calls;
   - contains device check-in and auto-presence logic;
   - contains presence, group-scoped device, admin-device, and admin-ledger calls;
   - contains admin session login, status verification, and revocation calls;
   - binds admin login to the local device ID in the login request.

5. `CommandsScreen.kt`
   - already renders Radar groups/devices and online state;
   - already calls auto-presence on entry and periodically while active;
   - already exposes manual relay fields, group create/join, admin login/verify/revoke, and debug output.

### 2.2 Current mismatches against 011O / 011O-A

1. **No application-level bootstrap coordinator.**
   - Startup navigation loads preferences and UI, but there is no single state machine coordinating saved-server reconnect, discovery fallback, identity recognition, automatic registration, membership restoration, admin verification, and Radar activation.

2. **Auto-presence is screen-scoped rather than app-scoped.**
   - Heartbeat/check-in behavior is triggered from Radar/Commands UI lifecycle, so normal launch does not guarantee bootstrap before the user enters Radar.

3. **Current auto-presence still depends on locally saved group ID/key.**
   - When group material is absent, auto-presence skips.
   - It does not ask SERVER for authoritative membership by stable device identity.

4. **Join is used as a recurring presence repair.**
   - `autoPresence()` attempts `joinGroup()` before check-in.
   - This is not the approved known-device reconnect contract and risks conflating membership restoration with repeated join behavior.

5. **Unknown-device recognition and idempotent registration are not explicit.**
   - The CLIENT currently lacks a dedicated identify-device / register-if-unknown sequence and explicit duplicate-prevention state.

6. **Admin authority is partially controlled by local toggles.**
   - `AdminSessionState.active` currently derives from local `modeEnabled && sessionTokenSaved`.
   - Presence scope may choose admin authentication based on local mode/token presence before fresh SERVER verification.
   - A local toggle therefore influences admin-facing behavior even though SERVER must be authoritative.

7. **Admin session status is not verified during normal bootstrap.**
   - Verification is currently manual from Radar admin controls.
   - Revocation is therefore not guaranteed to collapse the CLIENT admin UI immediately on reconnect/startup.

8. **Admin role/device approval is not represented as authoritative SERVER state in the CLIENT model.**
   - The CLIENT has session fields but no explicit SERVER-returned role, device-admin approval, grant metadata, revocation metadata, or capability set.

9. **Manual infrastructure controls are normal Radar controls.**
   - Raw group/device IDs, keys, create/join controls, admin mode toggles, and debug output are directly available from Radar rather than being gated by a persistent Dev Mode visibility setting.

10. **No persistent Dev Mode preference exists.**
    - Existing local admin mode is not equivalent to diagnostic Dev Mode and must not be reused as authority.

11. **Radar privacy is UI-selection based rather than authority-state based.**
    - `allDevicesVisible` is a local preference.
    - The final visible scope must come from SERVER authorization and response scope, not a CLIENT toggle.

12. **Error mapping is too generic for the new bootstrap state machine.**
    - Current 401/403 messaging generally reports pairing rejection, which can mislabel expired/revoked admin sessions, device-key rejection, group-key rejection, or authorization denial.

## 3. Required architectural additions

### 3.1 CLIENT bootstrap coordinator

Add one lifecycle-aware coordinator responsible for:

```text
LOCAL_BOOT
→ TRY_SAVED_SERVER
→ DISCOVERING (only when needed)
→ SERVER_VERIFIED
→ IDENTIFYING_DEVICE
→ AUTO_REGISTERING (unknown only)
→ RESTORING_MEMBERSHIP
→ VERIFYING_ADMIN_SESSION
→ ACTIVE
```

The coordinator should expose one immutable observable state to UI and should not be embedded inside a Composable screen.

### 3.2 SERVER-authoritative identity/membership response

The CLIENT needs a typed response representing at minimum:

- recognized/unknown/conflict device state;
- canonical device ID and lineage status;
- current authoritative group membership;
- permitted Radar scope;
- current lease/last-seen state;
- registry revision;
- protocol/schema compatibility;
- admin authorization summary where permitted.

Exact routes and wire fields must be validated against SERVER implementation before source work. No protocol field should be invented silently.

### 3.3 SERVER-authoritative admin state

Replace local `admin active` derivation with a SERVER-verified state such as:

```text
UNKNOWN
VERIFYING
AUTHORIZED(role/capabilities)
REVOKED
EXPIRED
DENIED
OFFLINE_CACHED_STALE
```

The encrypted session token may persist locally, but it is only a credential for verification. It is not proof of current authority.

### 3.4 Persistent Dev Mode

Add a separate local preference:

```text
dev_mode_enabled = true | false
```

Dev Mode controls visibility of manual endpoint, raw IDs/keys, discovery diagnostics, manual check-in, raw responses, and protocol details. It must not grant admin, pairing, trust, or mission authority.

## 4. File-by-file implementation impact

### Existing files requiring modification in a later implementation checkpoint

1. `android/app/src/main/java/sh/swurlz/core/data/Prefs.kt`
   - add persistent Dev Mode preference/flow;
   - add cached bootstrap/membership display state only where needed;
   - remove local admin toggle as an authority decision;
   - retain encrypted session persistence and stable relay identity;
   - avoid storing plaintext admin password.

2. `android/app/src/main/java/sh/swurlz/core/data/SecretStore.kt`
   - likely retain current encrypted session token storage;
   - add only narrowly required secret material after SERVER contract confirmation;
   - no plaintext credentials.

3. `android/app/src/main/java/sh/swurlz/core/model/Models.kt`
   - add typed bootstrap state;
   - add recognized-device and membership models;
   - add SERVER-authoritative admin authorization/role/capability model;
   - remove or deprecate `AdminSessionState.active` as a locally inferred authority claim.

4. `android/app/src/main/java/sh/swurlz/core/net/Api.kt`
   - add/align identity lookup and register-if-unknown calls;
   - add membership restoration call or parse it from authoritative device response;
   - stop using recurring `joinGroup()` as normal reconnect repair;
   - verify saved admin session during bootstrap;
   - classify pairing, device, membership, admin, protocol, and transport errors separately;
   - enforce SERVER-provided scope for admin/global reads.

5. `android/app/src/main/java/sh/swurlz/core/net/CoreNodeAutoDiscovery.kt`
   - preserve saved-first verified behavior;
   - expose discovery result to the coordinator rather than owning the full startup flow;
   - retain signature validation before endpoint persistence;
   - preserve the previous valid endpoint when a manual candidate fails.

6. `android/app/src/main/java/sh/swurlz/core/MainActivity.kt`
   - instantiate/observe application bootstrap state after preflight;
   - start bootstrap independent of Radar navigation;
   - present reconnecting/degraded/action-required state without blocking cached UI unnecessarily.

7. `android/app/src/main/java/sh/swurlz/core/ui/screens/CommandsScreen.kt`
   - consume coordinator state instead of independently bootstrapping;
   - remove recurring join semantics from heartbeat;
   - render SERVER-authoritative current group and permitted device roster;
   - hide raw/manual infrastructure controls unless Dev Mode is enabled;
   - remove local admin-authority toggle;
   - render admin controls only after SERVER authorization;
   - add confirmation surfaces only for approved administrative writes in later slices.

8. `android/app/src/main/java/sh/swurlz/core/ui/screens/CoreNodeScreen.kt`
   - move manual server/admin diagnostics behind Dev Mode;
   - separate status inspection from admin authority;
   - consume verified admin state for admin-only routes.

9. `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`
   - remain a Dev Mode/manual recovery screen;
   - normal startup should not require navigation here;
   - explicit user discovery must not overwrite a still-valid endpoint until verification succeeds.

10. `android/app/src/main/java/sh/swurlz/core/ui/screens/MoreScreen.kt`
    - show advanced discovery/Core Admin entries conditionally under persistent Dev Mode or verified admin role as appropriate;
    - keep Dev Mode and Admin Mode visibly distinct.

11. `android/app/src/main/java/sh/swurlz/core/ui/screens/StyleScreen.kt`
    - relocate or rename Auto Presence control if automatic presence is mandatory normal behavior;
    - prevent a cosmetic/settings toggle from disabling required registration/membership restoration;
    - expose Dev Mode persistence in an appropriate advanced settings surface, not as admin authority.

### Recommended new files for a later implementation checkpoint

1. `android/app/src/main/java/sh/swurlz/core/bootstrap/ClientBootstrapCoordinator.kt`
   - owns the startup state machine, bounded retries, reconciliation, and heartbeat lifecycle.

2. `android/app/src/main/java/sh/swurlz/core/bootstrap/BootstrapModels.kt`
   - typed state and action-required reason models if not placed in `Models.kt`.

3. `android/app/src/main/java/sh/swurlz/core/data/BootstrapRepository.kt`
   - optional separation layer wrapping discovery, API, preference cache, identity, and session verification.

4. `android/app/src/main/java/sh/swurlz/core/ui/screens/DevModeScreen.kt`
   - central home for manual server address, discovery, raw IDs, protocol/schema, heartbeat tests, and redacted diagnostics.

5. `android/app/src/main/java/sh/swurlz/core/ui/screens/AdminConsoleScreen.kt`
   - later administrative control surface driven only by verified role/capabilities.

## 5. Smallest safe implementation slice

The smallest coherent source slice should **not** attempt the entire admin console or new server-side mutation surface.

### Proposed checkpoint 011Q scope

Implement only:

1. persistent Dev Mode preference;
2. application-level bootstrap coordinator skeleton;
3. saved SERVER verification first, discovery fallback second;
4. stable local identity load;
5. automatic admin-session status verification when a saved token exists;
6. authoritative CLIENT admin state (`AUTHORIZED`, `REVOKED`, `EXPIRED`, etc.) based on SERVER response;
7. removal of local admin toggle as proof of authority;
8. movement/hiding of raw manual controls behind Dev Mode;
9. no new SERVER routes and no database/schema migration.

### Why this is the smallest safe slice

- It fixes the most dangerous authority mismatch first: local state must not claim admin power.
- It creates the app-level lifecycle seam needed for all later registration/group restoration work.
- It preserves existing group/create/join/check-in functionality while avoiding an oversized first patch.
- It can be tested against current SERVER session routes before designing new identity/membership routes.
- It does not require silently inventing protocol fields.

## 6. Subsequent implementation slices

### 011R — Identity recognition and idempotent auto-registration

After SERVER route confirmation:

- identify stable device on reconnect;
- register only when SERVER reports unknown;
- distinguish conflict, revoked, retired, and Ghost lineage states;
- prevent duplicate registration loops.

### 011S — SERVER-authoritative membership restoration

- retrieve authoritative current membership;
- stop recurring join behavior for known members;
- cache membership for stale/offline display;
- reconcile CLIENT cache to SERVER truth.

### 011T — Radar scope and normal Create/Join UX

- render only permitted group scope;
- hide raw IDs/keys;
- provide short-name Create/Join UX;
- enforce atomic SERVER results.

### 011U — Admin console and server-side device promotion

Only after SERVER contracts exist:

- list authorized devices/groups;
- grant/revoke device roles;
- persist and verify device-bound admin authority;
- apply risk-based confirmations;
- record audit/ledger evidence;
- keep Truth Firewall and mission authority separate.

## 7. Tests required for later source work

### Unit/state tests

- saved endpoint succeeds without discovery;
- saved endpoint fails and verified discovery succeeds;
- no endpoint produces truthful action-required state;
- saved admin token verifies and authorizes correct role;
- revoked/expired token removes admin capabilities;
- offline cached admin state is visibly stale and cannot perform writes;
- Dev Mode persists across process restart;
- Dev Mode does not alter authority state;
- repeated bootstrap does not rotate identity;
- join is not repeatedly used as known-device membership restoration.

### Integration/on-device acceptance tests

- normal launch reaches Radar without manual network steps;
- CLIENT reconnects to the same verified SERVER after restart;
- raw networking controls are absent outside Dev Mode;
- admin controls appear only after SERVER verification;
- SERVER revocation collapses CLIENT admin controls on next request/reconnect;
- non-admin CLIENT cannot access global devices/groups;
- temporary SERVER outage preserves stale local display without inventing online/admin truth.

## 8. Requirements, assumptions, and unresolved dependencies

### Requirements confirmed by approved contracts

- SERVER is authoritative for registration, membership, leases, global scope, and admin status.
- CLIENT may persist stable identity, endpoint cache, display cache, Dev Mode visibility, and encrypted session token.
- Discovery does not grant trust or admin authority.
- Dev Mode is not Admin Mode.
- Mission authority and Truth Firewall remain separate.

### Assumptions requiring SERVER validation before implementation

- current `/admin/session/status` response is sufficient to return or derive device-bound role/capabilities;
- current SERVER route set may not yet expose recognized-device membership restoration;
- exact unknown-device and lineage response semantics remain to be confirmed;
- current protocol/schema remain `1 / 1` unless an explicit versioned contract is approved.

### Blocking dependency for 011R and later

Before identity/membership source changes, inspect the current SERVER implementation and record the exact available routes, request fields, response fields, status codes, persistence behavior, and idempotency guarantees. Do not design CLIENT wire behavior from UI assumptions.

## 9. Recommendation

Proceed with `INTEGRATION-FIX-011Q` as a bounded CLIENT source patch focused on the app-level bootstrap seam, persistent Dev Mode, and SERVER-verified saved admin sessions. Defer new registration/membership routes and administrative mutation endpoints until a SERVER route-impact review confirms the exact contract.
