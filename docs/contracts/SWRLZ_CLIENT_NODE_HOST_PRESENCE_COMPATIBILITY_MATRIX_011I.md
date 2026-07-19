# SWRLZ CLIENT–NODE_HOST Presence Compatibility Matrix — INTEGRATION-FIX-011I

- **Status:** Draft analysis; no implementation authorized
- **Checkpoint:** `INTEGRATION-FIX-011I`
- **Generated:** 2026-07-19
- **Authority base:** `main` commit `f35a9053cbd15c1d5f77b7dd6b5b07e0b778e181`
- **Draft contract:** `docs/contracts/SWRLZ_PERSISTENT_PRESENCE_REGISTRY_WRITE_CONTRACT_V1_DRAFT.md`
- **CLIENT evidence:**
  - canonical v1.0.1 promotion commit `43d696b02413ad52b30c860ab3d1fcca9dc21e86`;
  - checksum-bounded v1.0.2 candidate commit `667641948264cc0d5d9b066ad9d42b4102a165b2`;
  - v1.0.2 source SHA-256 `e618938d662c9b39dc33a786eca40eeecd4b9675f6558a7bd4a328b5fa5b92c1`;
  - historical CF7/CF10 reports are used only as lineage evidence where marked.
- **NODE_HOST evidence:**
  - installed v1.0.3 read-only compatibility behavior;
  - `docs/contracts/SERVER_NODE_HOST_COMPATIBILITY_SURFACE_V1.md`;
  - `BUILD_REQUESTS/SERVER_CONTRACT_CATCHUP_010D/files/app/src/main/java/sh/swrlz/nodehost/service/NodeCompatibilityProtocol.kt`.
- **Protocol/schema target:** `1 / 1`, additive and capability-negotiated.

## Classification values

- `MATCH`
- `CLIENT_MISSING`
- `NODE_HOST_MISSING`
- `SCHEMA_MISMATCH`
- `STATE_MISMATCH`
- `SECURITY_MISMATCH`
- `VERSION_MISMATCH`
- `DOCUMENTATION_DRIFT`
- `UNVERIFIED`

## Matrix

| Integration item | Shared contract requirement | CLIENT evidence | NODE_HOST evidence | Classification | Consequence | Owner | Smallest correction |
|---|---|---|---|---|---|---|---|
| Discovery transport | Local-link HTTP on port 8787 | Successfully discovers and reconnects to `127.0.0.1:8787` | Listener serves discovery/read routes | `MATCH` | Read transport is proven | Shared | Preserve |
| Discovery identity | Valid SWRLZ signature before use | CLIENT validates `/discovery/signature` | v1.0.3 returns stable node/installation identity | `MATCH` | Correct node selection | Shared | Preserve |
| Protocol/schema read version | Version `1 / 1` | CLIENT decodes v1 read responses | NODE_HOST returns `protocolVersion:1`, `schemaVersion:1` | `MATCH` | Additive v1 extension is possible | Shared | Preserve |
| Read status | `GET /status` | CLIENT displays live status | Implemented | `MATCH` | Health remains independently visible | NODE_HOST | Preserve |
| Presence summary read | `GET /presence/summary` from registry | CLIENT calls route | Implemented as authoritative-empty constant | `STATE_MISMATCH` | Route works but cannot reflect writes | NODE_HOST | Replace constant with persistent projection after registry acceptance |
| Group read | `GET /presence/groups` from registry | CLIENT calls route | Implemented as zero-count constant | `STATE_MISMATCH` | Group UI remains empty | NODE_HOST | Registry-backed scoped read |
| Device read | `GET /presence/devices` from registry | CLIENT calls route | Implemented as zero-count constant | `STATE_MISMATCH` | Roster remains empty | NODE_HOST | Registry-backed scoped read |
| Online derivation | Online from accepted server lease | CLIENT displays server online counts | NODE_HOST has no CLIENT-device lease model | `NODE_HOST_MISSING` | Heartbeat cannot produce online state | NODE_HOST | Add 90-second server-clock lease projection |
| Granular capability advertisement | Exact `presence.*.v1` capability identifiers | CLIENT does not gate all controls on exact write capabilities | NODE_HOST advertises only legacy `discovery` capability | `SCHEMA_MISMATCH` | Unsupported controls remain actionable | Shared | Add signature capabilities; add CLIENT capability adapter |
| Unsupported-control truth | Missing capability means `unsupported`, no request | CLIENT currently exposes register/join/check-in controls | NODE_HOST has no write routes | `CLIENT_MISSING` | User action appears valid but cannot succeed | CLIENT | Disable/label controls before network call |
| Group creation route | `POST /groups/create` | Implemented in CLIENT; CF7 lineage documents it | Missing | `NODE_HOST_MISSING` | Create action cannot persist | NODE_HOST | Implement contract handler and durable group record |
| Group creation authority | Pairing gate + creator lineage; group key not admin | CLIENT may send saved pairing/admin context; creator inference is incomplete | No handler or ownership model | `SECURITY_MISMATCH` | Ownership cannot be proven | Shared | Resolve creator from authenticated device header/body; store lineage |
| Device registration route | `POST /devices/register` | Historical CF10 report proposed it; current canonical v1.0.2 inclusion is not proven and should be treated absent | Missing | `CLIENT_MISSING` + `NODE_HOST_MISSING` | Canonical register-first flow unavailable | Shared | Add NODE_HOST route and minimal CLIENT adapter in separate implementation checkpoint |
| Device identity proposal | Stable `device_id` plus optional redacted identity metadata | Current CLIENT has saved app-scope device ID/key; richer CF10 identity authority is historical lineage | NODE_HOST has node identity only, no CLIENT-device registry | `STATE_MISMATCH` | CLIENT and host identities remain correctly distinct but device authority is absent | Shared | Persist CLIENT-device record; keep NODE_HOST installation identity separate |
| Group join route | `POST /groups/join` | Implemented and used by auto-presence | Missing | `NODE_HOST_MISSING` | Join fails before check-in | NODE_HOST | Implement idempotent membership handler |
| Legacy atomic join | Absent device may be atomically registered during valid join | CLIENT auto-presence can join without proven `/devices/register` | No route | `NODE_HOST_MISSING` | Current CLIENT needs compatibility bootstrap | NODE_HOST | Add explicit `legacy_atomic_join` mode |
| Device check-in route | `POST /devices/checkin` | Implemented and used by auto-presence | Missing | `NODE_HOST_MISSING` | No `last_seen_at` or lease update | NODE_HOST | Implement credential-checked atomic lease update |
| Auto-presence sequencing | Capability check → register → join → check-in → refresh | Current CLIENT joins then checks in; register step not proven | No write support | `STATE_MISMATCH` | Flow cannot converge on authoritative online state | CLIENT | Implement negotiated multi-step state machine after SERVER contract acceptance |
| Auto-presence error composition | Preserve per-step result; do not wrap failure as generic success | Join failure can be swallowed before check-in and successful reads can dominate UI | NODE_HOST returns route-level failures for unsupported paths | `CLIENT_MISSING` | Root error is obscured | CLIENT | Return explicit register/join/check-in result object |
| Route-specific errors | Actual route and stable error code | CLIENT v1.0.2 fixes actual-route compiler path and HTTP mapping | Write error envelope absent because routes absent | `NODE_HOST_MISSING` | No stable write diagnostics | NODE_HOST | Implement v1 error envelope and codes |
| Pairing token gate | Required for every write | CLIENT can send saved pairing token header | NODE_HOST write enforcement absent | `NODE_HOST_MISSING` | Group/device secrets would otherwise become sole authority | NODE_HOST | Validate pairing before body credentials |
| Group-key handling | Hash at rest; never echo/log | CLIENT stores local group secret and currently may place it in read query | No registry storage | `SECURITY_MISMATCH` | Secret can enter URLs/logs; no server verifier | Shared | Hash in NODE_HOST; add transitional redacted read adapter; move new CLIENT auth out of URL |
| Device-key handling | Hash at rest; device-scoped only | CLIENT stores device secret locally and sends on join/check-in | No verifier/store | `NODE_HOST_MISSING` | Check-in cannot authenticate device | NODE_HOST | Add salted verifier and conflict behavior |
| Secret response behavior | Never return plaintext secret | CLIENT can parse historical returned `device_key` but does not require it when locally generated | No handler | `DOCUMENTATION_DRIFT` | Old server assumptions risk secret echo | Shared | Contract forbids echo; CLIENT ignores/removes dependency |
| Durable group persistence | Success only after durable atomic commit | CLIENT assumes server ownership | No group store | `NODE_HOST_MISSING` | Restart cannot preserve group | NODE_HOST | Add restart-persistent store |
| Durable device persistence | Success only after durable atomic commit | CLIENT assumes server ownership | No device store | `NODE_HOST_MISSING` | Roster cannot exist | NODE_HOST | Add restart-persistent store |
| Durable membership persistence | Natural key `(group_id, device_id)` | CLIENT sends both IDs | No membership store | `NODE_HOST_MISSING` | Join has no authoritative effect | NODE_HOST | Add atomic membership record |
| Audit lineage | Bounded write audit without secrets | CLIENT has no authority to create server audit truth | NODE_HOST has runtime evidence mechanisms but no presence-write audit | `NODE_HOST_MISSING` | Identity repair lacks evidence | NODE_HOST | Append request/result audit transactionally |
| Group creator ownership | Preserve creator lineage; no implied admin | CF10 report requests creator law but current canonical enforcement is unproven | Missing | `NODE_HOST_MISSING` | Future destructive actions lack owner basis | NODE_HOST | Store creator identity at create time |
| Device lifecycle | `active`, `retired`, `blocked` separate from online | CLIENT UI doctrine references ghost/legacy devices | No CLIENT-device records | `NODE_HOST_MISSING` | Duplicates cannot become lineage | NODE_HOST | Add lifecycle fields and ordinary-view filtering |
| Trust state | Registration/join/check-in never auto-grant mission trust | CLIENT/NODE_HOST discovery reports `pairing_required` and `trusted_only` | No presence write path to enforce separation | `UNVERIFIED` | Future implementation could accidentally conflate states | Shared | Contract tests must prove trust unchanged after writes |
| Ghost Device merge | Similar identity evidence warns; never auto-merges | Historical CF10 doctrine supports lineage | No registry/merge state | `NODE_HOST_MISSING` | Old profiles cannot be retired safely | NODE_HOST | Add conflict/warning and successor link; no hard delete |
| CLIENT/NODE_HOST identity separation | CLIENT device ID must not replace NODE_HOST node/installation ID | CLIENT has its own saved device ID | NODE_HOST has durable node/installation identity | `MATCH` | Correct authority separation exists | Shared | Preserve during registry implementation |
| Online lease TTL | Default 90 seconds; heartbeat every 30 seconds | CLIENT has heartbeat toggle but no accepted common TTL contract | Missing | `SCHEMA_MISMATCH` | UI cannot know when online expires | Shared | Adopt explicit lease fields and interval |
| Background retry | Bounded, no paid/remote fallback | CLIENT has auto behavior but exact bounded policy is incomplete | No write route | `UNVERIFIED` | Risk of rapid retries or silent failure | CLIENT | Add bounded retry policy after capability support |
| LAN write exposure | Loopback default; explicit paired LAN enablement | CLIENT can target loopback or LAN-discovered URL | NODE_HOST read listener can expose LAN discovery; write policy absent | `SECURITY_MISMATCH` | Future write surface could be overexposed | NODE_HOST | Separate LAN-write configuration and `LAN_WRITE_DISABLED` error |
| Public internet exposure | Prohibited by v1 | CLIENT discovery is local-network oriented | NODE_HOST local-link design | `MATCH` | Offline-first boundary retained | Shared | Preserve |
| Admin separation | Presence writes do not create admin authority | CLIENT verified-admin policy separates UI mode/token verification | NODE_HOST presence writes absent | `MATCH` at policy, `UNVERIFIED` in implementation | Prevents privilege inflation | Shared | Add explicit tests in implementation checkpoint |
| Mission authority separation | Presence state does not authorize missions | CLIENT/NODE_HOST signature shows `trusted_only` | No write implementation | `UNVERIFIED` | Must be proven when writes exist | Shared | Test trust state unchanged and Truth Firewall preserved |
| Empty-state truth | No records → authoritative empty | CLIENT displays zero state | NODE_HOST implements truthful empty state | `MATCH` | Current screenshots are correct | NODE_HOST | Preserve as registry base case |
| Restart behavior | Records persist; devices restart offline until fresh check-in | CLIENT expects saved server-owned state | No presence persistence | `NODE_HOST_MISSING` | Reinstall/restart loses authoritative state by design today | NODE_HOST | Durable store plus lease reset/re-evaluation |
| Compatibility adapter | Legacy route names accepted; query-secret reads temporary | CLIENT uses legacy write paths and query read credentials | NODE_HOST only has canonical read paths | `NODE_HOST_MISSING` | Current CLIENT cannot interoperate without adapter | NODE_HOST | Add bounded legacy adapter under same use cases |
| Protocol incompatibility | Explicit `422` version/schema error | CLIENT can display HTTP errors but no negotiated write schema | No write envelope | `VERSION_MISMATCH` | Future drift would be opaque | Shared | Add request version fields and explicit errors |
| No synthetic success | Write failure must remain failure | CLIENT UI can show healthy reads after failed action | NODE_HOST correctly does not invent records | `CLIENT_MISSING` | User may interpret connection health as registration success | CLIENT | Separate read and write status cards |

## Compatibility conclusion

The discovery and read-only compatibility boundary is functioning correctly. The first unsupported functional boundary is the persistent presence-write layer. CLIENT already carries legacy route expectations for group creation, group join, and check-in, but Android NODE_HOST intentionally implements no matching handlers or persistent registry.

The smallest safe integration direction is:

1. accept the shared draft contract;
2. implement NODE_HOST registry ownership and the four write handlers, including the legacy atomic-join adapter;
3. implement CLIENT capability gating and exact multi-step error reporting;
4. verify persistence, leases, secrets, trust separation, and Ghost Device lineage before promoting either lane.

## First actionable root cause

```text
NODE_HOST_MISSING:
Android NODE_HOST has no persistent presence registry and no presence-write routes.
```

The CLIENT presentation mismatch is secondary: it exposes controls without negotiated write capability and does not preserve a clear per-step registration failure.

## Recommended implementation split

### SERVER-first bounded slice

- persistent group/device/membership/audit store;
- capability advertisement;
- `/groups/create`;
- `/devices/register`;
- `/groups/join` with explicit legacy atomic registration;
- `/devices/checkin` with 90-second lease;
- registry-backed existing presence reads;
- focused contract tests only.

### CLIENT follow-up slice

- parse granular capabilities;
- disable unsupported controls truthfully;
- register → join → check-in state machine;
- preserve route-specific write status independently of read health;
- remove dependence on returned plaintext device keys;
- migrate group authorization out of query strings while retaining a bounded compatibility period.

## Checkpoint boundary

This matrix does not authorize implementation, source modification, builds, workflows, promotion, release, deployment, or installation. It records the contract gap and the smallest correction ownership only.