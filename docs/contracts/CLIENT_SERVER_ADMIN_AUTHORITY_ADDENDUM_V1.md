# CLIENT ↔ SERVER Admin Authority Addendum v1

- **Checkpoint:** `INTEGRATION-FIX-011O-A`
- **Status:** Approved behavioral contract; implementation separately approval-gated
- **Protocol version:** `1`
- **Schema version:** `1`
- **Applies to:** SWRLZ CLIENT ↔ Android NODE_HOST administrative-control lane

## 1. Purpose

Define persistent, revocable, SERVER-authoritative administrator recognition for registered CLIENT devices.

The intended flow is:

```text
stable CLIENT identity
→ SERVER verifies credentials or SERVER app directly promotes device
→ SERVER records device admin status
→ SERVER issues or validates a revocable admin session
→ CLIENT exposes Admin Console only while SERVER confirms authority
```

CLIENT must never establish administrator authority from a local toggle, cached boolean, Dev Mode, or possession of stale credentials alone.

## 2. Authority ownership

SERVER is authoritative for:

- whether a registered device is an administrator;
- the device's current role or capability set;
- admin grant, revocation, and expiration state;
- session validity;
- audit records for authority-changing operations;
- whether an administrative request is permitted.

CLIENT is authoritative only for:

- its stable local identity material;
- securely persisted session material issued by SERVER;
- local UI preferences;
- persistent Dev Mode visibility state.

A locally persisted `admin=true` value is never proof of authority.

## 3. Device-bound admin status

SERVER may maintain fields equivalent to:

```text
device_id
admin_status
admin_role
admin_granted_at
admin_granted_by
admin_revoked_at
active_session_ids
```

Exact storage representation remains implementation-specific and must not silently change protocol or schema authority.

Admin status attaches to the registered device identity. A person may own multiple devices while only selected devices are approved as administrators.

## 4. Grant paths

### 4.1 Credential-based CLIENT authorization

1. CLIENT presents stable device identity and administrator credentials to SERVER.
2. SERVER validates credentials.
3. SERVER confirms the device is registered or resolves registration according to the approved bootstrap contract.
4. SERVER may mark the device as admin-approved.
5. SERVER issues a revocable session token or equivalent authenticated session.
6. CLIENT stores the session securely; CLIENT must not persist the password as a convenience credential.

### 4.2 Direct SERVER-side promotion

The SERVER administration app may promote a registered device without requiring the CLIENT to re-enter credentials.

The SERVER app must still require a deliberate confirmation dialog before granting administrator authority.

Direct promotion does not bypass audit, lineage, Truth Firewall, protocol, or revocation requirements.

## 5. Persistent session behavior

On reconnect, CLIENT may present its saved admin session.

SERVER must verify:

- the session is valid;
- the session belongs to the connecting registered device;
- the device remains admin-approved;
- the role/capability set is current;
- the device or session has not been revoked.

Only after positive SERVER verification may CLIENT expose administrative controls.

If verification fails, CLIENT falls back to normal group-scoped behavior and must not continue using cached admin privileges.

## 6. Revocation

SERVER-side revocation takes effect immediately when practical and no later than the next authenticated request or heartbeat validation.

Revoking admin authority:

- invalidates active admin sessions for that device;
- causes CLIENT to close or disable administrative controls;
- does not automatically unregister the device;
- does not automatically erase group membership;
- does not erase lineage or audit history.

## 7. CLIENT Admin Console scope

A SERVER-authorized CLIENT administrator may be allowed to access capabilities such as:

- SERVER overview and health;
- all groups and registered devices;
- group membership management;
- heartbeat and lease state;
- pairing approvals;
- Ghost/Legacy lineage views;
- administrator device/session management;
- SERVER settings permitted by role;
- diagnostics and logs.

Every administrative write remains SERVER-validated. CLIENT is a control surface, not the source of authority.

## 8. Confirmation tiers

### 8.1 Normal admin actions

Read-only inspection and low-risk operational actions may proceed without repeated credential entry.

### 8.2 Explicit confirmation required

Actions including administrator grant/revoke, device removal, membership changes, session revocation, pairing changes, or SERVER network/discovery changes require an explicit confirmation dialog.

### 8.3 Strong confirmation required

Destructive or trust-root operations require stronger confirmation, such as re-authentication or typed confirmation. Examples include registry reset, SERVER identity reset, deletion of lineage/history, trust-root changes, or factory reset.

Truth Firewall protections must not be disabled through an ordinary admin toggle.

## 9. Separation of concerns

```text
Dev Mode = local diagnostic visibility
Administrator = SERVER-authorized operational authority
Mission Authority = separately granted execution authority
```

These are independent states. Enabling Dev Mode must not grant administrator or mission authority.

## 10. Audit requirements

SERVER should record, where available:

- affected device;
- action performed;
- actor device or SERVER-local authority;
- timestamp;
- prior and resulting role/status;
- optional reason.

Audit history must preserve truthful lineage and must not silently disappear when devices are retired or demoted.

## 11. Acceptance scenarios

1. CLIENT logs in once; SERVER approves the device and issues a session.
2. CLIENT reconnects later and regains admin UI only after SERVER session/role verification.
3. SERVER app directly promotes a registered device after explicit confirmation.
4. SERVER revokes a device; CLIENT loses admin UI on the next verified interaction.
5. Dev Mode remains enabled while admin authority is absent.
6. Admin revocation does not erase registration, membership, or lineage.
7. Administrative writes are rejected by SERVER when the session, device role, or capability is invalid.

## 12. Non-goals

This addendum does not authorize or define:

- source implementation;
- new endpoint names;
- database migrations;
- builds or workflows;
- installation;
- branch merge/rebase;
- promotion to `main`;
- release or deployment;
- granting administrator status to any real device;
- weakening identity, pairing, trust, Truth Firewall, lineage, or protocol-version discipline.
