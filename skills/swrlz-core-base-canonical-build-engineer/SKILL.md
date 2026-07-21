---
name: swrlz-core-base-canonical-build-engineer
description: Governs canonical SWRLZ CORE_BASE Android source lineage, checksum-gated builds, repository documentation, evidence, and handoffs.
---

# SWRLZ CORE_BASE Canonical Build Engineer

Manual invocation: `$swrlz-core-base-canonical-build-engineer`

## Mission

Operate canonical CORE_BASE Android source, package lineage, checksum gates, build requests, GitHub Actions verification, evidence bundles, repository documentation, and durable handoffs.

## Scope

In scope:

- `SOURCES/CORE_BASE/`
- the `[core_base]` block in `BUILD_REQUESTS/000_CURRENT.request`
- the CORE_BASE GitHub Actions workflow
- CORE_BASE architecture, build, lineage, evidence, and handoff documentation
- skills and operating guidance governing canonical CORE_BASE builds

Out of scope unless separately approved: CLIENT, NODE_HOST/SERVER, Keyboard implementation, Launcher implementation, release, deployment, installation, merge, and signing changes.

## Modes

- REVIEW: inspect repository state, documentation, request, workflow, ZIP, checksum, and lineage.
- PACKAGE: create an immutable source ZIP and sibling SHA-256 from approved source.
- IMPLEMENT: modify only bounded CORE_BASE checkpoint paths.
- VERIFY: inspect Actions runs, logs, APKs, checksums, and provenance.
- DOCUMENT: create or update durable repository documentation for every added or materially changed artifact.
- HANDOFF: update durable documentation and produce a new-chat handoff.

## Mandatory process

1. Read `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`.
2. Read the active request and workflow before proposing changes.
3. Read relevant architecture, governance, contract, ADR, handoff, and skill documentation before implementation.
4. Improve an accepted workflow rather than creating a duplicate.
5. Keep the active source ZIP and sibling SHA-256 directly under `SOURCES/CORE_BASE/`.
6. Maintain `SOURCES/CORE_BASE/OLD_PATCHES/` only for explicit lineage-preserving retirement.
7. Verify archive integrity with `unzip -tq`.
8. Verify SHA-256 before extraction.
9. Build in an isolated workspace.
10. Require deterministic APK naming, APK SHA-256, source SHA-256, build logs, Java and Gradle versions, invariant evidence, and provenance.
11. Never claim build, install, launch, release, or deployment success without corresponding evidence.
12. Preserve package identity, version identity, signing lineage, offline-first behavior, Truth Firewall principles, protocol discipline, and local-versus-remote distinctions.
13. Integrate; do not overwrite unrelated repository lanes.

## Documentation gate

Every file, directory, archive, workflow, request, skill, script, contract, generated artifact, or materially changed repository path MUST be documented before its checkpoint is considered complete.

For each repository change, update the appropriate durable location. Prefer existing documentation systems; create a new document only when no accepted location adequately represents the change.

Documentation MUST record, where applicable:

- checkpoint identifier, purpose, scope, and exclusions;
- exact paths added, modified, moved, generated, or retired;
- canonical source identity, ZIP identity, and checksum path;
- workflow, trigger model, request identity, expected APK, and evidence path;
- application ID, package name, version name, and version code;
- predecessor, successor, superseded-by, and `OLD_PATCHES` lineage;
- approval boundary;
- verified and unverified claims;
- evidence and recovery locations;
- handoff impact and cross-links.

A repository upload is incomplete when its documentation, checksum, lineage, evidence mapping, or handoff references are absent.

## Documentation placement

- `docs/architecture/`: structural and component relationships.
- `docs/architecture/adr/`: durable architectural decisions.
- `docs/contracts/`: normative protocol, trust, identity, storage, or build contracts.
- `docs/build/`: packaging, checksums, workflows, artifacts, and verification procedures.
- `docs/governance/`: repository-wide authority and engineering policy.
- `docs/handoffs/`: checkpoint continuation and new-chat instructions.
- `reports/` or accepted evidence directories: implementation and verification evidence.
- `SOURCES/CORE_BASE/OLD_PATCHES/README.md`: retirement and migration rules.
- `skills/`: reusable operating procedures.

Do not create duplicate documents when an accepted document can be updated cleanly. Cross-link related documents so a future chat can discover the complete process from one handoff entry point.

## Repository upload checklist

Before committing or pushing an upload:

1. Classify it as source, package, checksum, evidence, workflow, request, documentation, skill, or retired lineage material.
2. Verify its path and filename.
3. Verify its checksum when binary or archival.
4. Record predecessor, successor, or source lineage.
5. Update the relevant build or architecture document.
6. Update the active handoff when future chats need the change.
7. Update this skill when the operating process changes.
8. Verify cross-links.
9. Confirm the upload does not silently replace an authoritative artifact.
10. Confirm approval covers the repository write.

## New-project bootstrap

For a new canonical Android project lacking this machinery, establish a canonical source directory, immutable source ZIP, sibling SHA-256, request block, target workflow, isolated build workspace, deterministic APK/evidence artifacts, `OLD_PATCHES` contract, architecture/build documentation, an ADR or contract when warranted, new-chat handoff, reusable skill coverage, and bounded-branch verification before merge.

## Handoff requirements

Every handoff states the documents to read in order, authoritative repository and branch, checkpoint and commit, source ZIP and checksum, request and workflow, verified results, missing evidence, approvals already granted, approvals still required, forbidden actions, and exact next approval phrase.

A new chat must not require the user to verbally reconstruct the build process.

## Completion criteria

A CORE_BASE checkpoint is not complete unless implementation, checksums, workflow/request agreement, evidence, repository documentation, current handoff instructions, and skill guidance are all present and no claim exceeds the evidence.

## Stop contract

Before every stop, state approval waiting, authorization scope, exclusions, expected result, and exact approval phrase.
