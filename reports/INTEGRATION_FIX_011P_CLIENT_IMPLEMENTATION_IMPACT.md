# INTEGRATION-FIX-011P — CLIENT Implementation-Impact Review

- **Checkpoint:** `INTEGRATION-FIX-011P`
- **Status:** Review complete; implementation separately approval-gated
- **Branch reviewed:** `checkpoint/server-presence-registry-011k`
- **Scope:** CLIENT automatic bootstrap, registration, membership restoration, Radar, persistent Dev Mode, and SERVER-authoritative administration

## 1. Executive finding

The CLIENT already contains reusable primitives for stable identity, secure secret storage, discovery, reconnect, group presence, and admin-session handling. The required work is primarily integration and authority correction, not a rewrite.

Recommended architecture:

```text
existing identity + secure storage
→ bootstrap coordinator
→ saved SERVER verification
→ SERVER device lookup / idempotent registration
→ SERVER-authoritative membership restoration
→ heartbeat
→ scoped Radar
→ SERVER-verified Admin Console
```

## 2. Existing reusable components

### 2.1 Stable identity

Relevant files include:

- `android/app/src/main/java/sh/swurlz/core/data/Prefs.kt`
- `android/app/src/main/java/sh/swurlz/core/data/SecretStore.kt`
- `android/app/src/main/java/sh/swurlz/core/data/ContextCollectors.kt`
- `android/app/src/main/java/sh/swurlz/core/model/Models.kt`

Existing identity persistence should be reused. Identity rotation/forget operations must remain explicit recovery or Dev Mode actions and must preserve Ghost/Legacy lineage.

### 2.2 Secure secrets

`SecretStore.kt` already separates sensitive values from normal preferences, including device key, group key, Core Node token, and admin session token.

Admin passwords must not be persisted for convenience. Persistent SERVER-issued sessions are the approved mechanism.

### 2.3 Discovery and saved reconnect

Relevant files include:

- `android/app/src/main/java/sh/swurlz/core/net/CoreNodeAutoDiscovery.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`

Existing saved-node probing, last-good fallback, discovery-signature verification, and subnet discovery should be reused.

Networking orchestration should be extracted from Compose into a lifecycle-aware coordinator or repository.

### 2.4 Existing admin-session groundwork

The CLIENT already contains admin-session state and operations resembling login, status, and revoke behavior. Login is device-aware and session material is stored securely.

Current local admin-mode or token-presence checks must not be treated as authority. The required future rule is:

```text
SERVER verifies session
AND SERVER confirms registered device admin role
AND session/device is not revoked
```

### 2.5 Existing auto-presence

Current auto-presence behavior appears to load local group material, repeat `joinGroup()`, then check in.

This must not be expanded into a larger repeat-join loop.

Required future behavior:

```text
stable device identity
→ ask SERVER for current device record
→ auto-register only if unknown
→ receive SERVER-authoritative membership
→ heartbeat without recurring rejoin
```

## 3. Current structural mismatches

1. Connection and presence orchestration is too tightly coupled to UI screens.
2. Manual SERVER address, pairing, raw endpoints, subnet controls, and raw responses are visible outside a dedicated persistent Dev Mode.
3. Persistent Dev Mode does not yet exist as a distinct authority-neutral state.
4. Admin UI still relies on credential-per-call patterns in places.
5. Local flags and cached session presence are insufficient proof of admin authority.
6. CLIENT cache cannot currently guarantee restoration when local group data is stale or absent.
7. Full implementation depends on confirmed SERVER routes; CLIENT must not invent endpoint names.

## 4. File-by-file impact plan

| File | Planned responsibility |
|---|---|
| `model/Models.kt` | Add typed bootstrap, membership, registration, and SERVER admin-authorization states |
| `data/Prefs.kt` | Persist verified endpoint identity, cached membership, Dev Mode state, and non-authoritative admin metadata |
| `data/SecretStore.kt` | Continue storing device keys, pairing material, and admin session token; never store admin password |
| `net/Api.kt` | Add typed lookup/register/membership/heartbeat/admin-role operations after SERVER routes are confirmed |
| `net/CoreNodeAutoDiscovery.kt` | Reuse verified discovery and make saved-SERVER-first behavior callable outside UI |
| `MainActivity.kt` | Start or observe bootstrap lifecycle and route normal users away from infrastructure screens |
| `ui/screens/NetworkDiscoveryScreen.kt` | Reduce to Dev Mode diagnostics and explicit recovery/search actions |
| `ui/screens/CoreNodeScreen.kt` | Migrate from credential-per-call behavior to SERVER-verified session/Admin Console |
| `ui/screens/SetupScreen.kt` | Remove recurring infrastructure setup from normal post-bootstrap use |
| `ui/screens/HomeScreen.kt` / `CockpitScreen.kt` | Show connection, group, scoped devices, stale state, and admin authorization |

Recommended new components:

```text
data/bootstrap/ClientBootstrapCoordinator.kt
data/bootstrap/BootstrapRepository.kt
model/BootstrapModels.kt
ui/screens/AdminConsoleScreen.kt
ui/screens/DevModeScreen.kt
```

Exact paths remain implementation decisions and must follow existing repository conventions.

## 5. Required bootstrap state model

```text
LOCAL_BOOT
TRY_SAVED_SERVER
DISCOVERING
SERVER_VERIFIED
IDENTIFYING_DEVICE
AUTO_REGISTERING
RESTORING_MEMBERSHIP
ACTIVE
DEGRADED_OFFLINE
ACTION_REQUIRED
```

The state machine should live outside Compose and be exposed through `Flow`/`StateFlow` or equivalent lifecycle-aware state.

## 6. SERVER dependencies

CLIENT implementation requires confirmed SERVER-authoritative operations for:

- lookup of the current registered device by stable identity;
- idempotent unknown-device registration;
- retrieval of current membership for that device;
- heartbeat/check-in without forced group rejoin;
- device-bound admin-role verification;
- admin promotion and revocation;
- session validation/revocation and role-change propagation.

Exact routes and payloads must be derived from accepted SERVER contracts or implementation evidence. No route may be silently invented in CLIENT code.

## 7. Smallest bounded implementation sequence

### Slice 1 — Bootstrap foundation

- dedicated bootstrap state model;
- saved-SERVER-first verification;
- discovery extraction from Compose;
- stable identity loading;
- stale/offline state handling.

No registration, membership mutation, or admin writes.

### Slice 2 — Device resolution and registration

After SERVER routes are confirmed:

- identify current device;
- register only when SERVER reports unknown;
- handle pairing rejection truthfully;
- prevent duplicate registration loops;
- preserve Ghost/Legacy lineage.

### Slice 3 — Membership restoration and heartbeat

- retrieve authoritative membership;
- replace repeated startup join behavior;
- begin bounded heartbeat;
- render same-group Radar scope.

### Slice 4 — Persistent Dev Mode

- create dedicated persisted setting;
- hide manual networking and raw diagnostics outside Dev Mode;
- preserve strict separation from admin status.

### Slice 5 — SERVER-authorized administration

- verify saved sessions on reconnect;
- model SERVER-returned role/capabilities;
- close admin UI immediately after revocation;
- migrate legacy credential-per-call actions;
- apply confirmation tiers from `011O-A`.

## 8. Risks and safeguards

- Do not regenerate identity because SERVER is temporarily unavailable.
- Do not create duplicate registrations after read, heartbeat, or connection failures.
- Do not let cached membership override SERVER truth.
- Do not let Dev Mode grant admin authority.
- Do not persist admin passwords.
- Do not expose unrelated groups/devices to non-admin CLIENTs.
- Do not weaken Truth Firewall, pairing, trust, lineage, or protocol-version discipline.

## 9. Conclusion

The CLIENT is not starting from zero. Existing primitives include:

```text
stable identity
secure secrets
saved endpoint
verified discovery
group join/check-in
admin session token
presence views
```

The missing layer is a coordinator that binds those primitives to SERVER-authoritative identity, membership, and administration.

The highest-priority technical correction is:

```text
Replace repeat-join autoPresence behavior with SERVER-authoritative device resolution and membership restoration.
```

## 10. Non-authorization statement

This report does not authorize:

- source changes;
- endpoint implementation;
- database/schema migration;
- builds or workflows;
- rebase or merge;
- promotion to `main`;
- APK installation;
- release or deployment.
