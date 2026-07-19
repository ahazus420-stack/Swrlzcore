# CLIENT Automatic Bootstrap, Group Restoration, Radar, and Dev Mode Contract v1

- **Checkpoint:** `INTEGRATION-FIX-011O`
- **Status:** Prepared behavioral contract; implementation separately approval-gated
- **Protocol version:** `1`
- **Schema version:** `1`
- **Applies to:** SWRLZ CLIENT ↔ Android NODE_HOST persistent-presence lane

## 1. Purpose

Define the normal CLIENT experience as:

```text
launch
→ show last-known local state
→ reconnect to last verified SERVER
→ discover only when needed
→ identify or automatically register this device
→ receive SERVER-authoritative group membership
→ start automatic heartbeat/check-in
→ populate Radar with this CLIENT's permitted scope
```

The normal user must not repeatedly perform infrastructure actions such as manual registration, manual check-in, manual registry refresh, or manual SERVER address entry.

## 2. Evidence and current facts

1. Existing CLIENT discovery direction already requires finding a local SWRLZ node, verifying `/discovery/signature`, saving the working URL, and continuing into Radar flows.
2. Stable device identity, local-first behavior, no paid runtime dependency, truthful identity, explicit approval gates, and Ghost Device lineage are existing architecture requirements.
3. SERVER v1.1.0 candidate implements durable presence state, pairing-gated writes, registry-backed reads, legacy atomic join, 90-second server-derived online leases, and a 30-second heartbeat recommendation.
4. Discovery and compatibility do not grant pairing, trust, or mission authority.
5. Protocol/schema remain `1 / 1`; this contract does not silently revise wire format authority.

## 3. Normative ownership rules

### 3.1 SERVER authority

SERVER is authoritative for:

- whether a device is registered;
- stable SERVER-side device record and lineage;
- current group membership;
- group existence and membership persistence;
- current online/offline lease state;
- last accepted heartbeat/check-in;
- registry revision and SERVER-observed timestamps;
- administrative global group/device totals.

### 3.2 CLIENT authority

CLIENT is authoritative for:

- its locally persisted stable CLIENT/device identity material;
- its last verified SERVER endpoint cache;
- last-known display cache used before reconciliation;
- user-visible device label/preferences that the protocol permits;
- persistent SWRLZ Dev Mode visibility state.

CLIENT must not overwrite SERVER-authoritative membership merely because its cache differs.

### 3.3 Cache rule

CLIENT may render cached state immediately with a visible `reconnecting`, `last known`, or equivalent stale-state indicator. After SERVER response, CLIENT must reconcile to SERVER truth.

## 4. Startup state machine

### 4.1 Required states

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

### 4.2 Required transitions

1. `LOCAL_BOOT`
   - Load stable local device identity.
   - Load last verified SERVER endpoint.
   - Load cached group/radar state.
   - Load Dev Mode enabled/disabled state.
   - Render immediately without claiming fresh SERVER truth.

2. `TRY_SAVED_SERVER`
   - Attempt the last verified endpoint first.
   - Do not force discovery while the saved endpoint is healthy and identity-valid.

3. `DISCOVERING`
   - Run verified SWRLZ discovery only when no saved endpoint exists, the saved endpoint fails within bounded retry policy, or the user explicitly requests SERVER search.
   - Reject candidates that do not pass SWRLZ discovery-signature validation.

4. `SERVER_VERIFIED`
   - Confirm protocol compatibility and SERVER identity.
   - Save the verified endpoint only after successful validation.

5. `IDENTIFYING_DEVICE`
   - Present stable CLIENT/device identity to SERVER.
   - Ask SERVER for the current device record and membership state.

6. `AUTO_REGISTERING`
   - If SERVER reports this identity is unknown and pairing/write authorization is valid, register automatically.
   - Registration is idempotent: repeating the same valid request must resolve to the same active device identity or a truthful lineage repair result, not create uncontrolled duplicates.
   - If authorization is absent, rejected, or ambiguous, fail closed into `ACTION_REQUIRED`; do not invent registration success.

7. `RESTORING_MEMBERSHIP`
   - SERVER returns current group membership for the recognized device.
   - CLIENT updates its local cache and UI from SERVER truth.
   - No recurring Join action is required for an already-member device.

8. `ACTIVE`
   - Start automatic heartbeat/check-in.
   - Recommended interval is 30 seconds while connected and foreground/background policy permits.
   - SERVER determines online state from accepted leases; CLIENT must not declare itself globally online without SERVER confirmation.

9. `DEGRADED_OFFLINE`
   - Preserve last-known group/device display with stale/offline labeling.
   - Retry saved SERVER using bounded backoff.
   - Do not erase membership or register a replacement identity solely because the SERVER is temporarily unavailable.

10. `ACTION_REQUIRED`
    - Used for multiple verified SERVER candidates, pairing required, identity conflict, incompatible protocol, explicit trust rejection, or unrecoverable membership conflict.
    - Present one clear user action and an accurate reason.

## 5. Device registration contract

- Normal CLIENT UI must not expose a recurring `Register Device` step.
- Unknown-device registration occurs automatically after verified SERVER connection and valid pairing/write authorization.
- Registration must use stable identity, not a newly generated identity per launch.
- Existing historical-but-replaced records become Ghost/Legacy lineage objects rather than being silently deleted.
- CLIENT must not keep registering merely because a heartbeat, read, or temporary connection failed.
- Manual registration/check-in controls may exist only in persistent Dev Mode diagnostics.

## 6. Group creation and joining

### 6.1 Normal UI

```text
Group name: [ short name ]
[ Create Group ]
[ Join Group ]
```

Internal group IDs, registry revisions, route names, and database keys are not normal-user inputs.

### 6.2 Create Group

A valid Create Group action must:

1. normalize and validate the short group name;
2. reject invalid or conflicting names truthfully;
3. create the group atomically on SERVER;
4. add the creating device as a member atomically;
5. persist membership across CLIENT and SERVER restarts;
6. return the authoritative group record and membership state.

### 6.3 Join Group

A valid Join Group action must:

1. normalize the short group name;
2. resolve only an existing group;
3. join the current recognized device atomically;
4. be idempotent when the device is already a member;
5. persist membership across sessions;
6. return the authoritative group and current permitted member/device view.

### 6.4 Membership restoration

On every verified reconnect, SERVER must tell CLIENT its current membership. CLIENT must not require a user to re-enter the group name when membership already exists.

### 6.5 Membership conflict

If CLIENT cache and SERVER differ:

- SERVER membership wins;
- CLIENT updates its cache;
- destructive leave/switch behavior requires explicit user action;
- no automatic group switching is allowed because a different nearby SERVER or stale cache is observed.

## 7. Radar scope

### 7.1 Normal non-admin CLIENT

Radar may display:

- SERVER connected/reconnecting/offline state;
- current group name;
- this device;
- devices visible within this device's current group;
- online/offline/last-seen state where authorized;
- truthful stale-state indicators;
- relevant compatibility or action-required status.

Radar must not normally display:

- unrelated groups;
- devices outside the current group;
- global SERVER device totals;
- global SERVER group totals;
- raw registry/database records;
- secret material, pairing tokens, trust keys, or mission authority data.

### 7.2 Admin CLIENT

Global group/device totals and broader registry views require separately verified admin authorization. An enabled local toggle alone does not establish admin authority.

### 7.3 SERVER app

The SERVER-side administrative UI is the primary home for:

- all groups;
- all registered devices;
- lease/heartbeat status;
- Ghost/Legacy lineage;
- registry revision;
- pairing and protocol diagnostics;
- NODE_HOST operational health.

## 8. Persistent SWRLZ Dev Mode

### 8.1 Persistence

Dev Mode enabled/disabled state is stored locally and survives CLIENT restarts.

### 8.2 Visibility-only law

Enabling Dev Mode changes diagnostic visibility and manual test affordances only. It must not automatically:

- elevate admin authority;
- establish pairing or trust;
- alter Truth Firewall behavior;
- enable missions;
- weaken fail-closed networking;
- expose secrets;
- change identity;
- switch SERVER endpoints.

### 8.3 Dev Mode contents

Dev Mode may expose:

- manual SERVER address;
- saved endpoint and discovery candidates;
- protocol/schema/build identity;
- raw connection and retry state;
- registration state;
- heartbeat/check-in timestamps;
- registry revision;
- route-specific errors;
- manual re-discover/reconnect/check-in test buttons;
- diagnostic logs with secret redaction.

### 8.4 Manual SERVER address

- Hidden from normal CLIENT settings.
- Entering an address does not make it trusted or verified.
- The address must pass normal SWRLZ signature and protocol validation before becoming the saved endpoint.
- Failed manual endpoints must not erase a still-valid saved endpoint without explicit user choice.

## 9. Error and recovery behavior

- Errors must identify the actual failed operation or route category.
- A read failure must not be mislabeled as registration failure.
- A registration failure must not trigger repeated duplicate registration loops.
- Temporary SERVER absence must not erase device identity or group membership.
- Multiple SERVER candidates require explicit selection unless one uniquely matches the saved SERVER identity.
- Protocol incompatibility fails closed and preserves local cached state as stale only.
- Pairing/trust rejection must remain visible and must not be bypassed by automatic bootstrap.

## 10. Privacy and public-hosted readiness

For a public or hosted SERVER:

- a CLIENT receives only its authorized device/group scope by default;
- global counts and unrelated membership are administrative data;
- SERVER must not leak unrelated device labels, IDs, online state, or group names;
- local-versus-remote SERVER identity must remain explicit;
- discovery on a local network must not imply public-internet trust or availability.

## 11. Acceptance scenarios for later implementation

1. **Known device, saved SERVER, existing group**
   - Launch CLIENT.
   - CLIENT reconnects automatically.
   - SERVER recognizes device.
   - Existing group and permitted devices appear without registration or join taps.

2. **Unknown device, valid pairing**
   - Launch CLIENT.
   - CLIENT verifies SERVER.
   - Device auto-registers once.
   - Subsequent launches reuse the same record.

3. **Create group**
   - Enter short name and tap Create Group.
   - Group is created and creator joined atomically.
   - Restart both apps.
   - Membership restores automatically.

4. **Join group**
   - Enter existing short name and tap Join Group.
   - Restart CLIENT.
   - Membership restores automatically.

5. **SERVER temporarily offline**
   - CLIENT shows cached state as stale.
   - No identity regeneration, group erasure, or duplicate registration occurs.
   - Reconnection restores fresh SERVER state.

6. **Manual endpoint in Dev Mode**
   - Address is hidden while Dev Mode is disabled.
   - Dev Mode persists after restart.
   - Manual address must verify before it becomes saved.

7. **Non-admin privacy**
   - CLIENT sees only its own group scope.
   - Global groups/devices are not disclosed.

8. **Ghost lineage**
   - Replaced historical identity is archived/linked.
   - Active Radar remains clean.
   - No silent hard deletion occurs.

## 12. Non-goals

This contract does not authorize or define:

- source implementation;
- a new APK build;
- workflow execution;
- installation or migration;
- signer remediation;
- hosted-node transport;
- new protocol/schema versions;
- mission execution authority;
- weakening pairing, trust, identity, or Truth Firewall rules.

## 13. Implementation order recommendation

1. CLIENT startup state machine and saved-first reconnection.
2. Stable identity lookup and idempotent automatic registration.
3. SERVER-authoritative membership restoration.
4. simple Create/Join Group UI and atomic results.
5. Radar scope filtering.
6. persistent Dev Mode and manual diagnostics relocation.
7. bounded on-device acceptance testing through normal app behavior.
