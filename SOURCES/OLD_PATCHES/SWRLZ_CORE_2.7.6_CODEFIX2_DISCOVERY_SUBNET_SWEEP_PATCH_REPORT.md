# SWRLZ-Core 2.7.6 CODEFIX2 Discovery Subnet Sweep Patch Report

## Purpose

Fix SCAN LOCAL so it does real local discovery across the current device subnet instead of only probing a tiny static candidate list.

## Identity

- versionName: `0.2.7.6-codefix2-subnet-sweep`
- versionCode: `24`
- Patch: `2.7.6-CODEFIX2-DISCOVERY-SUBNET-SWEEP`
- Source: `SWRLZ_CORE_2.7.6_CODEFIX2_DISCOVERY_SUBNET_SWEEP_SOURCE.zip`

## Behavior

SCAN LOCAL now:

1. Tries the typed Node URL first.
2. Tries the saved Core Node URL second.
3. Uses the typed/saved URL to infer a same-subnet x.x.x.1-254 sweep.
4. Reads this Android device's active non-loopback IPv4 address(es).
5. Derives the first three octets and sweeps x.x.x.1 through x.x.x.254 on port 8787.
6. Probes `/discovery/signature` on candidates in concurrent batches.
7. Accepts only a valid `swrlz-local-node` discovery signature.

## Why

Hotspot networks may use `10.x.x.x`; shelter/router Wi-Fi may use `192.168.1.x`; other routers may use another private subnet. The app should inspect its own network context instead of hardcoding only default router IPs.

## Manual / online server path

Manual URL input remains supported for online/proxy-hosted nodes. TEST SIGNATURE still probes the exact typed URL, and SCAN LOCAL still tries the typed URL first before subnet sweeping.
