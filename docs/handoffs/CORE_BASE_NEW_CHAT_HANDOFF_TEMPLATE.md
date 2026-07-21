# CORE_BASE New-Chat Handoff Template

Use this when starting a new canonical CORE_BASE engineering chat.

## Scope

This chat is dedicated only to canonical CORE_BASE. Do not modify CLIENT, NODE_HOST, Keyboard, or Launcher without separate explicit authorization.

## Read first

1. `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
2. `BUILD_REQUESTS/000_CURRENT.request`
3. `.github/workflows/build-swrlz-core-android-foundation.yml`
4. active ZIP and sibling SHA under `SOURCES/CORE_BASE/`
5. `SOURCES/CORE_BASE/OLD_PATCHES/README.md`
6. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`
7. any checkpoint-specific report, ADR, contract, or evidence document listed below

## Operating rules

- GitHub is authoritative.
- Integrate; never overwrite lineage.
- Verify ZIP integrity and SHA-256 before extraction or build.
- Build from the request-selected immutable archive in an isolated workspace.
- Produce APK checksum and provenance.
- Every repository addition or material change must be documented in the correct durable location.
- A repository upload is incomplete without checksum, lineage, evidence mapping, documentation, and handoff coverage where applicable.
- Update this handoff and the operating skill whenever the process changes.
- Never claim completion without repository and workflow evidence.
- Do not commit, push, merge, publish, release, deploy, or install without active checkpoint authorization.

## Required state fields

- Repository:
- Authoritative branch:
- Checkpoint branch:
- Draft PR:
- Current checkpoint:
- Current commit:
- Canonical source tree:
- Source ZIP:
- Source SHA-256:
- Application ID:
- Version name/code:
- Request ID:
- Workflow:
- Workflow trigger model:
- Expected APK:
- Expected evidence artifact:
- Last completed checkpoint:
- Last workflow run:
- Last verified APK and SHA-256:
- Current gate:

## Documentation inventory

List every document added or changed by the current checkpoint:

- Build-process document:
- Architecture document or ADR:
- Contract:
- Governance document:
- Implementation or verification report:
- `OLD_PATCHES` lineage record:
- Skill:
- Handoff:
- Other cross-links:

For each item, state its repository path and what it documents.

## Lineage inventory

- Predecessor source/package:
- Successor source/package:
- Superseded-by reference:
- `OLD_PATCHES` migration status:
- Recovery or rollback path:

## Evidence inventory

- Source ZIP checksum evidence:
- Build log:
- Java/Gradle versions:
- Invariant report:
- APK checksum:
- Provenance report:
- Artifact download identity:
- Unverified claims that must remain unclaimed:

## Approval state

- Approval already granted:
- What that approval authorized:
- What it did not authorize:
- Approval currently waiting:
- Expected result of that approval:
- Exact next approval phrase:

## Stop requirement

Before ending the chat, verify that implementation, checksum files, workflow/request agreement, evidence, documentation inventory, lineage inventory, handoff state, and skill guidance are current. Do not ask the user to verbally reconstruct missing repository context.
