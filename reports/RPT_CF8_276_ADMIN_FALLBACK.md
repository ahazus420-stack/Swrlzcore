# SWRLZ-Core 2.7.6 CF8 — Verified Admin Fallback

## Device evidence

Fresh SERVER v1.0.3 testing proved the read-only NODE_HOST contract:

- `/status` returned HTTP 200.
- `/discovery/signature` returned HTTP 200 with hostVersion `1.0.3`.
- `/presence/summary`, `/presence/groups`, and `/presence/devices` returned truthful authoritative-empty state.
- The Network pulse incorrectly selected `/admin/devices` and `/admin/queues` when Admin Mode was ON but no token was saved.
- Forget Admin attempted `/admin/session/revoke` even when no local token existed, then mislabeled the resulting 404 as a `/status` failure.
- Heartbeat/write failures could overwrite the Radar read-health badge after a successful presence refresh.

## Bounded correction

- Added one verified-admin route policy shared by the Network dashboard and endpoint dialogs.
- Admin routes now require mode enabled, a saved token, and a recorded successful verification.
- Restored `Admin ON + token absent` state is normalized to ordinary presence mode.
- Public device reads use `/presence/devices` unless the verified-admin policy passes.
- `/admin/queues` is skipped rather than requested without verified authentication.
- Forget Admin is local-first: no token means no network request; a failed or unsupported remote revoke never prevents local credential removal.
- Forget Admin also disables the all-devices preference.
- HTTP failures identify the actual requested route rather than claiming every 404 came from `/status`.
- Radar read health is tracked separately from heartbeat, registration, queue, and admin operations.

## Preserved boundaries

- No SERVER admin aliases were invented.
- No pairing, trust, identity, mission, or Truth Firewall rule changed.
- Public presence remains read-only and authoritative-empty until a separately accepted persistent registry exists.
