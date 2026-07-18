# SERVER NODE_HOST Compatibility Surface v1

Status: **Prepared source candidate; repository auto-build and device proof pending**

## Purpose

Define the bounded read-only JSON surface used by the SWRLZ CLIENT network
dashboard when communicating with the Android NODE_HOST on the local-link
listener at TCP `8787`.

This contract supplements, and does not replace, `GET /discovery/signature`.

## Version discipline

- Protocol version: `1`
- Schema version: `1`
- Candidate SERVER source: `SERVER_CFv1.0.3_SWRLZ.zip`
- Installed Android version target: `versionName 1.0.3`, `versionCode 2`

Protocol or schema changes require a separately accepted checkpoint. App-version
changes do not silently change this contract.

## Routes

| Route | Method | Authority |
|---|---|---|
| `/status` | `GET` | Current durable identity, installed version, and observed local runtime/listener state |
| `/presence/summary` | `GET` | Current persistent presence registry, or authoritative-empty when no accepted registry exists |
| `/presence/groups` | `GET` | Current persistent group registry, or authoritative-empty |
| `/presence/devices` | `GET` | Current persistent device registry, or authoritative-empty |

All successful responses return:

```http
Content-Type: application/json; charset=utf-8
Cache-Control: no-store
```

## `/status` truth model

The response includes aliases required by the current CLIENT decoder while
retaining explicit protocol and identity fields:

```json
{
  "ok": true,
  "node": "SWRLZ Node Host",
  "node_name": "SWRLZ Node Host",
  "node_type": "android-node-host",
  "server_version": "1.0.3",
  "version": "1.0.3",
  "protocolVersion": 1,
  "schemaVersion": 1,
  "identity": {
    "nodeId": "swrlz-node-<uuid-v4>",
    "installationId": "swrlz-install-<uuid-v4>"
  },
  "runtime": {
    "phase": "RUNNING",
    "identityReady": true,
    "discoveryHealthy": true,
    "privateHealthy": true
  },
  "connection": {
    "local_url": "http://127.0.0.1:8787",
    "lan_urls": ["http://<approved-ipv4-address>:8787"]
  },
  "paid_ai_required": false
}
```

The exact phase and URLs are observed values, not promises. LAN URLs include
only currently bound, non-loopback discovery sockets.

## Authoritative-empty presence

Until a separately accepted persistent presence registry exists, the NODE_HOST
must return successful empty state rather than `404` or fabricated rows.

Summary shape:

```json
{
  "ok": true,
  "protocolVersion": 1,
  "schemaVersion": 1,
  "nodeId": "swrlz-node-<uuid-v4>",
  "authoritative": true,
  "data_source": "android-node-host-local",
  "state": "empty",
  "group_count": 0,
  "device_count": 0,
  "online_count": 0,
  "groups": [],
  "devices": []
}
```

`/presence/groups` and `/presence/devices` use corresponding zero counts and
empty arrays.

## Fail-closed behavior

| Condition | Status |
|---|---:|
| Unsupported path | `404` |
| Method other than `GET` | `405` with `Allow: GET` |
| Request body present | `400` |
| `Accept` explicitly excludes JSON | `406` |
| Durable identity missing or invalid | `503` |
| Installed version unavailable | `503` |
| Unexpected bounded internal failure | `500` from the discovery request boundary |

All errors remain JSON and include protocol/schema version `1`.

## Truth Firewall boundaries

This surface must not:

- synthesize devices, groups, online state, or remote state;
- reinterpret demo Room rows as presence records;
- pair devices or establish trust;
- authorize, queue, or execute missions;
- disclose credentials or secret material;
- imply hosted-node or public-internet availability;
- require paid AI or a cloud runtime.

Discovery identifies a candidate node. Compatibility reports local state.
Neither operation grants mission authority.

## Runtime integration

The canonical v1.0.2 Android source exposes truth through `NodeHealth` and
`_health`. v1.0.3 maps:

- phase from `runtimeStatus`;
- private health from `privateApi.status`;
- discovery health from `discoveryLoopback.status`;
- identity from `activeIdentity`;
- LAN URLs from currently bound non-loopback discovery sockets.

All other discovery paths continue to `DiscoveryProtocol`, preserving
`/discovery/signature`.

## Acceptance evidence required

Source acceptance requires checksum, deterministic archive, path-safety,
contract, icon-inventory, secret-scan, and Kotlin fixture evidence. Final
acceptance additionally requires the repository-generated APK to pass on-device
route and launcher verification.
