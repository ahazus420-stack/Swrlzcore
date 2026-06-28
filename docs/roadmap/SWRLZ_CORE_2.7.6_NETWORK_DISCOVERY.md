# SWRLZ-Core 2.7.6 Network Discovery

## Patch

- Client patch: `2.7.6-NETWORK-DISCOVERY`
- Client versionName target: `v0.2.7.6-network-discovery`
- Client versionCode target: `19`
- Server lane: `0.7.3-DISCOVERY-SIGNATURE`

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

## Acceptance test

1. Build `SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY` through GitHub Actions forge.
2. Install APK.
3. Confirm app identity shows 2.7.6 Network Discovery.
4. Start Local Node Server 0.7.3.
5. Confirm `/discovery/signature` returns the expected JSON.
6. Use the app discovery lane / test helper to validate a SWRLZ node URL.
7. Save the server URL.
8. Open Admin Registry and Radar against the saved node.

## Roadmap position

Previous:

- 2.7.5 CODEFIX1 Build Identity Sync

Current:

- 2.7.6 Network Discovery
- 0.7.3 Discovery Signature

Next:

- 0.8.0 Node Host APK
- 2.8.x Forge Runner / Mission Board / Action Ledger / QA Report
