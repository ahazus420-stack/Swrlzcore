# CORE_BASE Canonical Build and Handoff Process

Status: active
Checkpoint origin: CORE-BUILD-002A

## Purpose

This document is the durable process for introducing or updating the canonical CORE_BASE Android application when a chat or engineer cannot build locally. GitHub is the source of truth and GitHub Actions is the verification environment.

## Canonical layout

- Active immutable source archive: `SOURCES/CORE_BASE/<checkpoint-source>.zip`
- Required sibling checksum: same basename with `.sha256`
- Editable canonical tree: `SOURCES/CORE_BASE/source/`
- Superseded material: `SOURCES/CORE_BASE/OLD_PATCHES/`
- Build request: `BUILD_REQUESTS/000_CURRENT.request`
- Workflow: `.github/workflows/build-swrlz-core-android-foundation.yml`

## Required sequence

1. Inspect repository documentation, current request, workflow, and canonical lineage.
2. Work one bounded checkpoint at a time.
3. Produce a source ZIP from the approved source tree without build output, `.gradle`, or `local.properties`.
4. Generate a sibling SHA-256 file.
5. Place both directly under `SOURCES/CORE_BASE/`.
6. Update the `[core_base]` request block to name the exact ZIP, checksum, task, application ID, and expected evidence.
7. Verify archive integrity and SHA-256 before extraction.
8. Extract only into an isolated build workspace.
9. Verify checkpoint invariants before building.
10. Build `:app:assembleDebug` under Java 17 and Android SDK.
11. Upload deterministic APK, APK checksum, source checksum, build log, tool versions, and provenance.
12. Do not publish, deploy, install, merge, or commit release artifacts without separate explicit approval.

## Existing-workflow rule

When a target already has a repository workflow, analyze and integrate with it. Do not create a parallel workflow merely because a new chat cannot immediately understand the existing one. Improve the accepted workflow in place on a bounded branch when necessary.

## New-project rule

For a new canonical Android project, establish the same source-tree, source-ZIP, sibling-checksum, build-request, workflow, and evidence pattern before claiming reproducible build status.

## OLD_PATCHES rule

`OLD_PATCHES` preserves superseded packages and patches with lineage. It is never an automatic fallback search path. Migration requires a bounded checkpoint and records original checksum, source reference, superseded-by reference, and date.

## Handoff minimum

Every handoff states repository and branch; active source ZIP and SHA-256; request ID and workflow path; package/version identity; completed checkpoint; build run/artifact or exact unresolved gate; and the complete approval boundary.
