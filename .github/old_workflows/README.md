# Archived GitHub Workflows

This directory preserves retired SWRLZ workflow definitions outside `.github/workflows/` so GitHub no longer treats them as active automation.

## Archived by WORKFLOW-SURFACE-CLEANUP-001B

- `build-swrlz-apk_multi_target_ready.yml` — generic repository-root Gradle workflow superseded by lane-specific archive-aware builders.
- `build-swrlz-android-project-e0af72.yml` — overlapping generic active-request Android workflow superseded by the canonical CLIENT workflow.
- `locate-stale-ui-source.yml` — historical stale-UI diagnostic and payload-inspection workflow.
- `server-contract-catchup-010d-promote-v103.yml` — completed one-shot SERVER v1.0.3 source-promotion workflow.

## Preservation contract

- Files are moved byte-for-byte; contents are not edited.
- Git history remains authoritative for original paths and prior runs.
- Archived workflows are documentation and lineage only.
- Re-activation requires a separate explicit approval and review.

## Active canonical artifact workflows

The intended active build surface remains:

- SERVER — `.github/workflows/build-swrlz-server-apk.yml`
- CLIENT — `.github/workflows/build-swrlz-apk_target_client_latest.yml`
- CORE — `.github/workflows/build-swrlz-core-android-foundation.yml`
- KEYBOARD — `.github/workflows/build-swrlz-keyboard-base.yml`
- LAUNCHER — `.github/workflows/build-swrlz-launcher-base.yml`
