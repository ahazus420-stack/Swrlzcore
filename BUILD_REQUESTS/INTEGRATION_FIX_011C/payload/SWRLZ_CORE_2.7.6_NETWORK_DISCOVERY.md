# SWRLZ-Core 2.7.6 Network Discovery

## Patch

- Client patch: `2.7.6-NETWORK-DISCOVERY`
- Client versionName target: `v0.2.7.6-network-discovery`
- Client versionCode target: `19`
- Server lane: `0.7.3-DISCOVERY-SIGNATURE`
- Current Android NODE_HOST contract-sync candidate: `SERVER_CFv1.0.3_SWRLZ.zip`

## Purpose

Make SWRLZ-Core stop depending on manual IP hunting. The client should be able to find a local SWRLZ node, verify that it is actually a SWRLZ node, save the working URL, and continue into Admin Registry / Radar flows.

## Core law

Integrate, do not overwrite.

This patch must preserve:

- no invisible patches
- no mystery APKs
- explicit approval gates
- stable device identity direction
- local-first behavior
- no paid runtime dependency
- truthful build identity

## Client target

Add a Network Discovery lane to the client codebase. The first forge slice adds shared discovery logic and build identity truth for 2.7.6:

- candidate local URLs
- `/discovery/signature` probe helper
- signature parsing/validation helper
- network permissions check
- updated Home Build Identity metadata

## Device Ledger / Ghost Device architecture addendum

Network Discovery must not treat every duplicate or outdated device profile as bad data.

If a device profile was historically true but has been replaced by a newer profile after registration/build changes, it should become a Ghost Device / Legacy Anchor instead of being hard-deleted.

The authoritative architecture rule lives at:

`docs/architecture/DEVICE_LEDGER_GHOST_DEVICE_VAULT.md`

Design effect for 2.7.6 and later:

- active device dashboards stay clean
- ghost/retired/legacy profiles are hidden from normal controls
- debug/history views can inspect ghost profiles
- ghost profiles can link to active devices
- deletion requires strong confirmation
- preserved logs support identity repair, duplicate detection, and build-history debugging

## Server target

Expose:

```http
GET /discovery/signature
```

Expected response:

```json
{
  "ok": true,
  "server": "swrlz-local-node",
  "version": "0.7.3-discovery-signature",
  "patch": "LNS-0.7.3-DISCOVERY-SIGNATURE",
  "port": 8787
}
```

## SERVER v1.0.3 contract-sync addendum

The Android NODE_HOST successor `SERVER_CFv1.0.3_SWRLZ.zip` preserves
`GET /discovery/signature` and adds a bounded read-only compatibility surface
on the same local-link listener:

```http
GET /status
GET /presence/summary
GET /presence/groups
GET /presence/devices
```

These routes report only locally provable NODE_HOST state. Until a separately
accepted persistent presence registry exists, presence is represented as an
authoritative empty state with zero counts and empty collections. No demo node,
synthetic device, group, online state, pairing, trust, or mission authority is
invented.

The v1.0.3 candidate advances the Android source to `versionCode 2` and
`versionName 1.0.3`, retains protocol/schema version `1`, and adds deterministic
Glitch Dragon Core launcher resources.

### Canonical integration correction

The checksum-matching v1.0.2 source uses the accepted `NodeHealth` / `_health`
runtime model. The earlier staged one-shot applicator references stale runtime
symbols and cannot be treated as proof that it can promote the canonical base.
The prepared v1.0.3 source integrates directly against the actual canonical
source. The stale manual workflow remains unmodified and must not be dispatched
as part of the source-ZIP auto-build path.

## Acceptance test

1. Build `SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY` through GitHub Actions forge.
2. Install APK.
3. Confirm app identity shows 2.7.6 Network Discovery.
4. Start Local Node Server 0.7.3.
5. Confirm `/discovery/signature` returns the expected JSON.
6. Use the app discovery lane / test helper to validate a SWRLZ node URL.
7. Save the server URL.
8. Open Admin Registry and Radar against the saved node.
9. Confirm ghost/legacy device profiles are treated as archived lineage objects, not hard-deleted by default.
10. Commit the approved `SERVER_CFv1.0.3_SWRLZ.zip` to `SOURCES/SERVER/` and allow the existing SERVER auto-build to run.
11. Install the resulting NODE_HOST APK.
12. Confirm `/status`, `/presence/summary`, `/presence/groups`, and `/presence/devices` return truthful `200` JSON responses.
13. Confirm empty presence remains zero-count/empty-array state with no fabricated rows.
14. Confirm the Glitch Dragon Core launcher icon renders correctly.

## Roadmap position

Previous:

- 2.7.5 CODEFIX1 Build Identity Sync
- 0.7.3 Discovery Signature foundation

Current:

- 2.7.6 Network Discovery
- SERVER v1.0.3 CLIENT compatibility contract sync
- Ghost Device Vault / Legacy Device Archive architecture rule

Next:

- On-device SERVER v1.0.3 route and launcher verification
- Persistent presence registry only through a separately accepted contract
- 2.8.x Forge Runner / Mission Board / Action Ledger / QA Report

## INTEGRATION-FIX-011A on-device evidence and correction

Fresh installation of repository-built SERVER v1.0.3 proved:

- `/discovery/signature`, `/status`, `/presence/summary`, `/presence/groups`, and `/presence/devices` return HTTP 200.
- Empty groups/devices are authoritative, zero-count, local NODE_HOST state.
- The CLIENT Network pulse selected `/admin/devices` and `/admin/queues` when the Admin Mode preference was ON even though no saved/verified token existed.
- The CLIENT Forget Admin action attempted `/admin/session/revoke` even with no local token, and generic error mapping mislabeled the resulting route-specific 404 as `/status` unavailable.
- The SERVER launcher image was functionally installed but visibly distorted because the foreground originated from a 72×72 indexed transport derivative.

Prepared successors:

- `CLIENT_CFv1.0.1_SWRLZ.zip`: verified-admin route policy, local-first Forget Admin, actual-route error reporting, restored-toggle normalization, and Radar read-health separation.
- `SERVER_CFv1.0.4_SWRLZ.zip`: icon-only successor using the accepted high-resolution Glitch Dragon Core master; v1.0.3 compatibility/runtime source remains byte-identical.

Repository promotion and APK builds remain separately approval-gated for each lane.
