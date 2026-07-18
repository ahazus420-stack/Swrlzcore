# CLIENT Verified Admin Route Policy v1

Status: **Prepared with INTEGRATION-FIX-011A; APK proof pending**

## Rule

An Admin Mode UI preference is not authentication authority. The CLIENT may
select `/admin/*` only when all three conditions are true:

1. Admin Mode is enabled.
2. A non-empty admin session token exists in the secure store.
3. The session has a recorded successful verification timestamp.

Otherwise:

- device reads use `/presence/devices`;
- group reads use `/presence/groups`;
- `/admin/queues`, `/admin/ledger`, and admin actions are skipped/hidden;
- restored `Admin ON + token absent` state is normalized off.

## Forget Admin

Forgetting is local-first:

- no saved token: clear local admin state without contacting the NODE_HOST;
- saved token: attempt best-effort remote revoke, then clear local state whether
  remote revoke succeeds, returns 404, times out, or is unreachable;
- clear mode, all-devices visibility, session ID, verification timestamp, and
  secure token material.

The CLIENT must report remote revoke availability separately from the successful
local credential removal.

## Health and errors

- HTTP errors name the actual requested route.
- Optional admin/write failures do not overwrite successful read-only Radar
  connection health.
- This policy does not create SERVER admin endpoints, pairing authority, trust,
  mission authority, or synthetic presence state.
