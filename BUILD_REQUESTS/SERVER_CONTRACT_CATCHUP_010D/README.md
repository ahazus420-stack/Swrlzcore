# SERVER-CONTRACT-CATCHUP-010D repository stage

Status: staged for review on `checkpoint/server-contract-catchup-010d-stage`.

## Canonical base

- Source: `SOURCES/SERVER/SERVER_CFv1.0.2_SWRLZ.zip`
- Required SHA-256: `9d278bed4944eee20d6b9dc1ea89ba9363c30a7e7daa5b91b44836c386abd200`
- Successor prepared by the manual workflow: `SERVER_CFv1.0.3_SWRLZ.zip`

## Bounded changes

- Adds truthful read-only compatibility routes on the discovery surface:
  - `GET /status`
  - `GET /presence/summary`
  - `GET /presence/groups`
  - `GET /presence/devices`
- Empty presence state returns successful empty collections and zero counts.
- No synthetic devices, groups, pairing, trust, mission authority, or remote state are introduced.
- Preserves `/discovery/signature`, runtime diagnostics, foreground ownership, durable identity, and protocol/schema version 1 behavior.
- Integrates a launcher derivative of the user-supplied Glitch Dragon Core master.
  - Master SHA-256: `2183730b2823b47709f074a531999f83043f128cc424a5aefd97846f52688256`
  - Transport: deterministic palette/RLE text; the workflow reconstructs Android PNG resources without external network access or image libraries.

## Promotion control

`.github/workflows/server-contract-catchup-010d-promote-v103.yml` is manual-only and requires the exact confirmation phrase:

`PROMOTE SERVER-CONTRACT-CATCHUP-010D`

The workflow verifies the canonical base checksum and every staged file before creating the deterministic source-only v1.0.3 archive. The workflow has not been dispatched as part of this repository-stage checkpoint.

## Not authorized by this stage

- No APK build
- No CLIENT modification
- No workflow dispatch
- No release, installation, or deployment
