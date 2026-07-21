# KEYBOARD-IMP-011A Current Handoff

- **Status:** Minimal standalone Keyboard IME source implemented; APK build not authorized or performed
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/keyboard-imp-011a`
- **Parent planning branch:** `checkpoint/app-shell-gate-010`
- **Planning commit:** `2fdc76cd8f8e4a6619a5a61eb5c2dcca2a99a0d8`
- **Source implementation commit:** `db97ac1e4af254f7754cff5538f0eecbe7ac849b`
- **Current main at closing reconciliation:** `a03a572a25695c78c5fa9c91970183fb664d7d1b`
- **Accepted identity:** `com.swrlz.keyboard.app`
- **Version:** `0.1.0` / `versionCode=1`

## Repository relationship

Current `main` advanced through separately authorized build-workflow consolidation after the source implementation commit. This branch remains intentionally unrebased and unmerged. No workflow, resolver, helper, or request changes from current `main` were imported into this checkpoint branch.

## Implemented

- standalone `SOURCES/KEYBOARD/source/` Android project;
- unique Keyboard namespace and application ID;
- launcher-visible setup activity;
- Android `InputMethodService` and IME metadata;
- lowercase character, space, backspace, and enter input;
- protected-editor classification seam;
- canonical source ZIP and sibling checksum;
- deterministic/static verification, lineage, rollback, implementation report, and handoff.

## Verification

```text
26 static checks passed
16 policy vectors passed
0 failures
```

No Android APK build is claimed.

## Source package

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip
SHA-256: 7281f083e4776004bd59f4973cc33a25e788b36c8175f42a15a4fc90ccd50442
Git blob: dd05d88b8bb9a60f3153db0f25f6fe88a771b73d
```

Package integrity, required entries, deterministic rebuild, duplicate-entry scan, and unsafe-path scan pass.

## Preserved predecessor

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip
SHA-256: 8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40
Git blob: a8cec2ab4889d53055b73aa18cf60423ec315f6a
```

The seed remains unchanged and unarchived.

## Current build architecture

Current `main` uses:

```text
.github/workflows/swrlz-apk-router.yml
scripts/ci/resolve_swrlz_source.py
scripts/ci/build_swrlz_component.sh
```

The former dedicated Keyboard workflow is retired, and `BUILD_REQUESTS/000_CURRENT.request` remains `enabled=false`.

The unified build helper can generate a Gradle 8.6 wrapper for a Keyboard archive containing settings files but no wrapper. However, the current `KEYBOARD_BASE` resolver accepts only `SWRLZ_KEYBOARD_BASE_CFvX.Y.Z` names and rejects `SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip`, including explicit-source selection. Because the old seed is version `1.0.1`, lane-latest selection would also be conceptually wrong for the new role-specific lineage.

The first build must therefore extend the resolver naming contract and manually select the exact new source while keeping automatic requests disabled.

## Explicitly absent

- no CLIENT enrollment or IPC;
- no NODE_HOST discovery or attachment;
- no AI or mission behavior;
- no clipboard history or voice capture;
- no telemetry or keystroke logging;
- no workflow, resolver, helper, or build-request changes;
- no APK build or workflow trigger;
- no merge, release, deployment, installation, or branch deletion.

## Approval waiting

`KEYBOARD-VER-011B — Unified Router Checksum-Gated Debug APK Verification`

Approval would authorize:

- creating a bounded `checkpoint/keyboard-ver-011b` branch from current repository truth while preserving the accepted Keyboard source commit and lineage;
- extending only the unified resolver's `KEYBOARD_BASE` naming contract and resolver tests to recognize `SWRLZ_KEYBOARD_IME_CFvX.Y.Z_SOURCE` packages;
- proving exact explicit-source selection, checksum agreement, lane-root enforcement, duplicate-suffix discipline, and ambiguity rejection;
- manually triggering the unified APK router once for `KEYBOARD_BASE`, `debug`, and the exact path `SOURCES/KEYBOARD/SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip`;
- keeping `BUILD_REQUESTS/000_CURRENT.request` disabled and `commit_release_artifacts=false`;
- collecting APK SHA-256, package/application ID, version, manifest and IME-service evidence, debug signer fingerprint, build log, source-resolution record, and source-to-artifact provenance;
- bounded resolver/test/evidence/handoff commits.

Approval would not authorize:

- changing Keyboard application source or accepted identity;
- archiving, removing, renaming, replacing, or selecting the predecessor seed as the active implementation;
- modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, or shared mature source;
- enabling automatic build requests;
- committing release artifacts;
- CLIENT/NODE_HOST attachment, AI, missions, clipboard history, voice, or telemetry;
- device installation;
- merge to `main`;
- release, publication, deployment, production signing, or branch deletion.

Expected result:

One checksum-bound debug APK verification artifact proving that the exact new source compiles and packages as the distinct `com.swrlz.keyboard.app` Android IME, with complete resolver, build, package, signer, and provenance evidence but no installation or release.

Exact approval phrase:

`Approve KEYBOARD-VER-011B — Extend only the unified SWRLZ APK router KEYBOARD_BASE source resolver and its tests to recognize the explicit SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip identity, then manually trigger one checksum-gated debug build of that exact source and collect APK SHA-256, package and application ID, version, manifest and IME service, debug signer, build-log, source-resolution, and source-provenance evidence while keeping automatic requests disabled and commit_release_artifacts false, without changing Keyboard source or identity, archiving or removing the predecessor seed, modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, or shared mature source, attaching services, installing, merging, releasing, publishing, deploying, production-signing, or deleting branches`
