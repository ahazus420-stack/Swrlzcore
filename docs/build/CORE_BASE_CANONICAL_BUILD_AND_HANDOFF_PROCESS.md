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
- Operating skill: `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
- New-chat handoff: `docs/handoffs/CORE_BASE_NEW_CHAT_HANDOFF_TEMPLATE.md`

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
12. Update every affected durable documentation location, including handoff and skill guidance when the process changes.
13. Do not publish, deploy, install, merge, or commit release artifacts without separate explicit approval.

## Documentation completion gate

Every repository addition or material change must be documented before its checkpoint is complete. This applies to source files, directories, ZIPs, checksum files, workflows, requests, skills, scripts, contracts, evidence, and retired lineage material.

Documentation must record, where applicable:

- checkpoint identifier, purpose, scope, and exclusions;
- exact repository paths added, changed, generated, moved, or retired;
- canonical source, source ZIP, and SHA-256 identity;
- workflow path, trigger model, request identity, Gradle task, expected APK, and evidence artifact;
- package/application identity and version identity;
- predecessor, successor, superseded-by, and `OLD_PATCHES` lineage;
- approval boundary;
- verified facts and unresolved evidence;
- rollback or recovery path;
- handoff impact and cross-links.

A source archive, APK, workflow, or other upload without its checksum, lineage, documentation, evidence mapping, and handoff coverage is incomplete.

## Documentation placement

Use existing accepted locations whenever possible:

- `docs/architecture/` for structural relationships;
- `docs/architecture/adr/` for durable decisions;
- `docs/contracts/` for normative contracts;
- `docs/build/` for packaging, checksum, build, workflow, and artifact procedures;
- `docs/governance/` for repository-wide policy;
- `docs/handoffs/` for continuation state;
- `reports/` or accepted evidence directories for implementation and verification reports;
- `SOURCES/CORE_BASE/OLD_PATCHES/README.md` for retirement rules;
- `skills/` for reusable operating procedures.

Do not create duplicate documents when an existing accepted document can be updated cleanly. Cross-link documents so a future chat can discover the complete process from the handoff template.

## Existing-workflow rule

When a target already has a repository workflow, analyze and integrate with it. Do not create a parallel workflow merely because a new chat cannot immediately understand the existing one. Improve the accepted workflow in place on a bounded branch when necessary.

## New-project rule

For a new canonical Android project, establish the same source-tree, source-ZIP, sibling-checksum, build-request, workflow, evidence, documentation, `OLD_PATCHES`, skill, and handoff pattern before claiming reproducible build status.

## OLD_PATCHES rule

`OLD_PATCHES` preserves superseded packages and patches with lineage. It is never an automatic fallback search path. Migration requires a bounded checkpoint and records original checksum, source reference, superseded-by reference, date, reason, and recovery instructions.

## Handoff minimum

Every handoff states repository and branch; active source ZIP and SHA-256; request ID and workflow path; package/version identity; completed checkpoint and commit; documentation added or changed; build run/artifact or exact unresolved gate; approval already granted; approval still required; forbidden actions; and the exact next approval phrase.

A future chat must not depend on the user verbally reconstructing the repository process.

## Completion rule

A CORE_BASE checkpoint is complete only when implementation, checksums, workflow/request agreement, evidence, durable documentation, handoff instructions, and skill guidance are all current and no claim exceeds the available evidence.
