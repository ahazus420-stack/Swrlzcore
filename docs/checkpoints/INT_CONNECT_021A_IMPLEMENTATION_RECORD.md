# INT-CONNECT-021A Implementation Record

Date: 2026-07-22

## Approval

`APPROVE INT-CONNECT-021A — Repair modern CLIENT auto-connect and disconnect behavior, responsive connection controls, launcher resources, final single-APK routing, SERVER registry persistence diagnostics, and privacy-preserving device identity deduplication with installation lineage`

## Objective

Repair the remaining CLIENT/SERVER connection UX, identity, packaging, launcher, and registry-persistence defects without weakening identity, proof, trust, Truth Firewall, or offline-first boundaries.

## Source lineage

- CLIENT: CFv2.0.9 → CFv2.0.10
- SERVER: CFv2.0.10 → CFv2.0.11
- Router helper: `scripts/ci/build_swrlz_component.sh`

## Implemented

### CLIENT

- Execute default-enabled auto-connect from the modern User Mode Core shell.
- Try saved/last-good SERVER first, then approved discovery including same-device loopback.
- Add explicit disconnect while preserving registration and saved SERVER state.
- Stop heartbeat on intentional disconnect.
- Use privacy-preserving stable device-binding fingerprint rather than installation UUID as the node identity anchor.
- Keep installation UUID as lineage.
- Present full-width single-line CONNECTING state and DISCONNECT when connected.
- Remove the broken adaptive monochrome declaration so launchers use the full-color dragon icon path.

### SERVER

- Add `POST /nodes/disconnect`.
- Preserve durable registration while setting live presence OFFLINE.
- Add Room schema v3 fields for device binding and installation lineage.
- Match registration by binding, then stable device ID, then node ID.
- Archive duplicate records for the same device as superseded instead of deleting lineage.
- Add registry startup diagnostics including database name, schema version, and loaded registered count.

### Repository router

- Build-output APKs are authoritative; `APK_DOWNLOAD` is fallback only.
- Require exactly one canonical APK before final staging.
- Fail on ambiguous multi-APK output.
- Remove the nested APK bundle that caused a second installable copy inside the GitHub artifact.
- Keep provenance and checksum documents alongside one final APK.

## Evidence

- SOURCE IMPLEMENTED
- STATIC VERIFICATION PASS
- ZIP INTEGRITY PASS
- REPOSITORY ROUTER PUBLISHED
- BUILD NOT RUN
- MIGRATION NOT EXECUTED
- RUNTIME NOT TESTED

## Exclusions

- No automatic trust elevation.
- No proof bypass.
- No raw IMEI, serial, phone number, advertising ID, or invasive hardware collection.
- No destructive database reset.
- No APK build, workflow run, release, or deployment.
