# CORE-IMP-009A Current Handoff

- **Status:** Standalone discovery contract capsule implemented on checkpoint branch; not merged and not attached to mature hosts
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/core-imp-009a`
- **Base commit:** `00bb23d7f4fed389a7b24e7ec2127399da0d924f`
- **Required ancestor:** `ef62e870e30143912be992972aed89849f186448`
- **Capsule:** `swrlz.discovery.contract` version `0.1.0`

## Implemented lane

`SOURCES/SHARED_FEATURES/DISCOVERY_CONTRACT/`

The lane contains pure Kotlin/JVM source, public domain API, internal JSON backend abstraction, a `kotlinx.serialization` JSON-tree backend, a strict standalone verification backend, canonical vectors, deterministic tests, descriptor, canonical ZIP, sibling SHA-256, capsule documentation, and rollback placeholder.

## Canonical package

`SWRLZ_DISCOVERY_CONTRACT_CAPSULE_v0.1.0.zip`

SHA-256:

`e0b139a84aaf5a5ea470fbea03c6f42dca987620c8459a3577d27c91058e484e`

Package verification:

- deterministic rebuild: PASS;
- archive integrity: PASS;
- unsafe paths: none;
- duplicate entries: none.

## Test result

`43 passed / 0 failed`

Verified behavior includes canonical success/error bytes, positive warning vectors, negative reason codes, producer validation, repeated determinism, and prohibited-boundary scans.

## Serializer decision

- Kotlin JVM plugin: `1.9.22`;
- `kotlinx-serialization-json:1.6.3`;
- JVM toolchain: `17`;
- no serializer-library type is exposed publicly;
- no serialization compiler plugin is required.

The local environment executed the portable codec and strict backend tests and performed a complete production-source API-shape compile. The official Maven artifact could not be downloaded in the execution environment, so an official dependency-backed Gradle build is not claimed.

## Mature source lineage

Current repository declarations remain:

- CLIENT v1.0.1: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`;
- SERVER v1.0.3: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`.

The unpaired SERVER v1.0.4 checksum anomaly remains preserved and unresolved. No mature source was copied, modified, attached, built, or invoked.

## Explicitly not performed

- no CLIENT changes;
- no SERVER/NODE_HOST changes;
- no Keyboard, Launcher, or CORE_BASE changes;
- no mature Gradle changes;
- no APK build;
- no workflow edits or triggers;
- no merge to `main`;
- no release, deployment, installation, or branch deletion.

## Discovery sequence status

The planned next discovery checkpoint remains `SERVER-REINT-009B`, but it is not authorized. It must repeat exact SERVER byte-level source verification before any mature-source change.

## User-proposed next direction

The proposed removal of nested ZIPs from Keyboard and Launcher, packaging of CORE source, and creation of distinct Keyboard and Launcher applications is not authorized by CORE-IMP-009A. Direct copying would risk collapsing package identity, signer lineage, manifests, app roles, storage ownership, and version boundaries.

The safe next bounded checkpoint is a read-only topology and lineage audit followed by a separation plan.

## Approval waiting

`APP-SHELL-GATE-010 — CORE_BASE, Keyboard, and Launcher Source Topology Audit and Separation Plan`

Approval would authorize:

- read-only inspection of current CORE_BASE, Keyboard, and Launcher repository lanes and canonical packages;
- verification of available ZIP/SHA lineage and nested archive layout;
- inspection of package names, Gradle projects/modules, manifests, app entry points, signer/version evidence, shared assets, and existing capsule references;
- classification of reusable shared behavior versus shell-owned behavior;
- a documentation-only separate-app plan with exact migration checkpoints, rollback, and package-preservation rules;
- bounded planning documentation commits on a new checkpoint branch.

Approval would not authorize:

- removing, moving, or replacing ZIPs;
- copying CORE source into Keyboard or Launcher;
- changing package IDs, application IDs, manifests, Gradle files, signing, versions, permissions, components, storage, or UI;
- creating implementation source;
- building APKs;
- triggering workflows;
- merging, releasing, deploying, installing, or deleting branches.

Expected result:

One evidence-grounded plan that determines whether Keyboard and Launcher should attach shared Core capsules, reference shared modules, or receive bounded extracted shell source while preserving distinct Android identities and rollback lineage.

Exact approval phrase:

`Approve APP-SHELL-GATE-010 — Audit the current CORE_BASE, Keyboard, and Launcher source/package topology, verify canonical ZIP/SHA and nested-archive lineage, classify shared versus shell-owned code, and produce a documentation-only separate-app extraction and attachment plan with bounded checkpoint commits without removing archives, copying source, modifying app code or build graphs, building APKs, triggering workflows, merging, releasing, deploying, installing, or deleting branches`
