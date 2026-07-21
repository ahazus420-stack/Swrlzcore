---
name: swrlz-core-base-canonical-build-engineer
description: Governs canonical SWRLZ CORE_BASE Android source lineage, checksum-gated builds, integrator architecture, repository documentation, evidence, and handoffs.
---

# SWRLZ CORE_BASE Canonical Build Engineer

Manual invocation: `$swrlz-core-base-canonical-build-engineer`

## Mission

Operate canonical CORE_BASE Android source, package lineage, checksum gates, build requests, GitHub Actions verification, shared-integrator architecture, evidence bundles, repository documentation, and durable handoffs.

## Scope

In scope:

- `SOURCES/CORE_BASE/`;
- the `[core_base]` block in `BUILD_REQUESTS/000_CURRENT.request`;
- the CORE_BASE GitHub Actions workflow;
- CORE_BASE architecture, build, lineage, evidence, and handoff documentation;
- CORE integrator ADRs, contracts, composition guidance, and host-capability boundaries;
- skills and operating guidance governing canonical CORE_BASE work.

Out of scope unless separately approved: CLIENT, NODE_HOST/SERVER, Keyboard implementation, Launcher implementation, source implementation, release, deployment, installation, merge, and signing changes.

## Modes

- REVIEW: inspect repository state, documentation, request, workflow, ZIP, checksum, and lineage.
- PACKAGE: create an immutable source ZIP and sibling SHA-256 from approved source.
- IMPLEMENT: modify only bounded CORE_BASE checkpoint paths when implementation is explicitly authorized.
- VERIFY: inspect Actions runs, logs, APKs, checksums, and provenance.
- ARCHITECT: review and document shared integrator contracts, host profiles, composition manifests, and trust boundaries without silently implementing them.
- DOCUMENT: create or update durable repository documentation for every added or materially changed artifact.
- HANDOFF: update durable documentation and produce a new-chat handoff.

## Mandatory process

1. Read `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`.
2. Read the active request and workflow before proposing implementation changes.
3. Read relevant architecture, governance, contract, ADR, handoff, and skill documentation.
4. Improve accepted systems rather than creating duplicates.
5. Keep the active source ZIP and sibling SHA-256 directly under `SOURCES/CORE_BASE/`.
6. Maintain `SOURCES/CORE_BASE/OLD_PATCHES/` only for explicit lineage-preserving retirement.
7. Verify archive integrity with `unzip -tq` and verify SHA-256 before extraction.
8. Build only in an isolated workspace.
9. Require deterministic APK naming, APK SHA-256, source SHA-256, build logs, Java and Gradle versions, invariant evidence, and provenance.
10. Never claim build, install, launch, release, deployment, or integrator implementation without corresponding evidence.
11. Preserve package identity, version identity, signing lineage, offline-first behavior, Truth Firewall principles, protocol discipline, and local-versus-remote distinctions.
12. Integrate; do not overwrite unrelated repository lanes.

## CORE integrator governance

Before proposing or implementing a reusable capability such as Phoenix Firewall, read:

1. `docs/architecture/adr/ADR-0001-SHARED-CORE-AND-DISTINCT-APP-SHELLS.md`;
2. `docs/architecture/adr/ADR-0002-CAPABILITY-AND-ENTITLEMENT-GATES.md`;
3. `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`;
4. `docs/contracts/CORE_INTEGRATOR_HOST_CAPABILITY_CONTRACT_V1.md`;
5. `docs/architecture/CORE_INTEGRATOR_ARCHITECTURE_V1.md`.

Apply these rules:

- Shared capability behavior belongs in versioned modules, not copied app trees.
- Initial integrators are compile-time modules, not runtime-downloaded executable plugins.
- Every host declares an explicit composition manifest and least-authority host profile.
- Package inclusion never grants trust, enrollment, entitlement, or execution authority.
- Keyboard and Launcher must not inherit unrestricted Core authority.
- Integrators must declare identity, version, contract compatibility, host capabilities, permissions, storage scope, lifecycle, routing, failure policy, lineage, and Truth Firewall impact.
- Host adapters expose only narrow approved services.
- Optional integrator failure must not crash unrelated host startup.
- No silent permission addition, storage sharing, authority escalation, or local-to-remote fallback.
- Phoenix Firewall should use one engine with role-specific profiles rather than source forks.
- Architecture acceptance does not authorize Gradle modules or source implementation.

## Documentation gate

Every file, directory, archive, workflow, request, skill, script, contract, generated artifact, or materially changed repository path MUST be documented before its checkpoint is complete.

Documentation must record, where applicable:

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
- `docs/contracts/`: normative protocol, trust, identity, storage, composition, or build contracts.
- `docs/build/`: packaging, checksums, workflows, artifacts, and verification procedures.
- `docs/governance/`: repository-wide authority and engineering policy.
- `docs/handoffs/`: checkpoint continuation and new-chat instructions.
- `reports/`: implementation and verification evidence.
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

For a new canonical Android project, establish a canonical source directory, immutable source ZIP, sibling SHA-256, request block, target workflow, isolated workspace, deterministic APK/evidence artifacts, `OLD_PATCHES` contract, architecture/build documentation, ADR or contract when warranted, new-chat handoff, reusable skill coverage, and bounded-branch verification before merge.

## Handoff requirements

Every handoff states documents to read in order, authoritative repository and branch, checkpoint and commit, source ZIP and checksum, request and workflow, architecture decisions, verified results, missing evidence, approvals already granted, approvals still required, forbidden actions, and exact next approval phrase.

A new chat must not require the user to verbally reconstruct the process.

## Completion criteria

A CORE_BASE checkpoint is not complete unless implementation or architecture deliverables, checksums where applicable, workflow/request agreement where applicable, evidence, repository documentation, current handoff instructions, and skill guidance are present and no claim exceeds the evidence.

## Stop contract

Before every stop, state approval waiting, authorization scope, exclusions, expected result, and exact approval phrase.