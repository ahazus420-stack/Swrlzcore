# KEYBOARD-IMP-011A Current Handoff

- **Status:** Minimal standalone Keyboard IME source implemented; APK build not authorized or performed
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/keyboard-imp-011a`
- **Parent planning branch:** `checkpoint/app-shell-gate-010`
- **Accepted identity:** `com.swrlz.keyboard.app`
- **Version:** `0.1.0` / `versionCode=1`

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
```

Package integrity, required entries, deterministic rebuild, duplicate-entry scan, and unsafe-path scan pass.

## Preserved predecessor

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip
SHA-256: 8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40
```

The seed remains unchanged and unarchived.

## Explicitly absent

- no CLIENT enrollment or IPC;
- no NODE_HOST discovery or attachment;
- no AI or mission behavior;
- no clipboard history or voice capture;
- no telemetry or keystroke logging;
- no workflow or build-request changes;
- no APK build or workflow trigger;
- no merge, release, deployment, installation, or branch deletion.

## Approval waiting

`KEYBOARD-VER-011B — Checksum-Gated Debug APK Verification Build`

Approval would authorize:

- updating only the Keyboard build workflow and Keyboard section of the current build request to select the new `SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip` package;
- verifying source checksum and archive integrity;
- building `:app:assembleDebug` on a Keyboard verification checkpoint branch;
- collecting the debug APK, SHA-256, package/application ID, version, manifest/service evidence, debug signer fingerprint, build logs, and source-to-artifact provenance;
- confirming that the built package identity is `com.swrlz.keyboard.app` and differs from Core;
- bounded workflow/evidence commits and a verification handoff.

Approval would not authorize:

- archiving, removing, renaming, or replacing the predecessor seed;
- modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, or shared mature source;
- CLIENT/NODE_HOST attachment, AI, missions, clipboard history, voice, or telemetry;
- installation on a device;
- merge to `main`;
- release, publication, deployment, production signing, or branch deletion.

Expected result:

One checksum-bound debug APK verification artifact proving that the new source compiles and packages as a distinct SWRLZ Keyboard IME, with complete build and signer evidence but no installation or release.

Exact approval phrase:

`Approve KEYBOARD-VER-011B — Update only the Keyboard verification workflow and Keyboard build-request selection to checksum-verify SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip, build the com.swrlz.keyboard.app debug APK on a checkpoint branch, and collect APK SHA-256, package, version, manifest, IME service, debug signer, build-log, and source-provenance evidence without archiving or removing the predecessor seed, modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, or shared mature source, attaching services, installing, merging, releasing, publishing, deploying, production-signing, or deleting branches`
