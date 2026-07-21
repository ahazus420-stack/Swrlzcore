# APP-SHELL-GATE-010 Current Handoff

- **Status:** Audit and separation plan complete; source implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/app-shell-gate-010`
- **Base commit:** `961e92907acb6a3158f6da982902f07acbfba019`

## Completed

Created documentation-only records:

```text
docs/audits/APP_SHELL_GATE_010_CORE_KEYBOARD_LAUNCHER_TOPOLOGY_AUDIT.md
docs/implementation/APP_SHELL_GATE_010_SEPARATION_AND_ATTACHMENT_PLAN.md
docs/handoffs/APP_SHELL_GATE_010_CURRENT_HANDOFF.md
```

## Primary findings

1. Active CORE_BASE is the reduced two-module `:app + :core` source, not the historical four-module package.
2. Core’s active app identity is `com.swrlz.core.app`.
3. Keyboard and Launcher seed ZIPs are byte-identical to the historical Core v1.0.1 package.
4. All three historical ZIP paths share SHA-256 `8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40` and Git blob `a8cec2ab4889d53055b73aa18cf60423ec315f6a`.
5. Keyboard and Launcher do not currently have checked-in `source/` projects.
6. The lane-root ZIPs are the source seeds themselves; no inner nested-source ZIP model was found.
7. Current Keyboard/Launcher workflows build the unchanged Core-shaped seed and do not establish distinct application identities or Android roles.
8. Keyboard has an accepted IME trust/privacy/enrollment contract and is ready for a bounded source implementation decision.
9. Launcher lacks a dedicated accepted role/behavior contract and should remain planning-only.

## Decision

Do not delete the seed ZIPs and do not copy the full Core app into Keyboard or Launcher.

Create a new role-specific Keyboard source project using the Core toolchain profile and accepted Keyboard contract. Preserve the historical seed until the new package is independently verified and accepted, then archive it under `OLD_PATCHES/` through a separate checkpoint.

## Proposed Keyboard identity

Recommended candidate requiring approval:

```text
namespace:     com.swrlz.keyboard.app
applicationId: com.swrlz.keyboard.app
label:         SWRLZ Keyboard
surfaceType:   keyboard
versionCode:   1
versionName:   0.1.0
```

## Explicitly not performed

- no ZIP removed, moved, renamed, replaced, or generated;
- no source directory created;
- no Core, Keyboard, or Launcher source modified;
- no application identity accepted or changed;
- no Gradle, manifest, build request, or workflow change;
- no APK build;
- no workflow trigger;
- no merge, release, deployment, installation, or branch deletion.

## Approval waiting

```text
KEYBOARD-IMP-011A — Minimal Standalone SWRLZ Keyboard IME Source
```

Approval would authorize:

- accepting `com.swrlz.keyboard.app` as the initial permanent Keyboard Android application identity;
- creating `SOURCES/KEYBOARD/source/` as a standalone Android IME project;
- deriving compatible toolchain settings from active CORE_BASE without copying the Core app shell;
- implementing a minimal offline ordinary-typing IME with basic characters, space, backspace, and enter;
- adding the InputMethodService, required manifest/service metadata, protected-editor classification seam, and Keyboard-specific documentation;
- creating a new canonical Keyboard source ZIP and sibling SHA-256 under a new filename;
- adding static/deterministic source verification, lineage, rollback, implementation evidence, and handoff;
- bounded commits on a new checkpoint branch.

Approval would not authorize:

- removing, moving, renaming, replacing, or archiving the existing Keyboard seed ZIP or checksum;
- modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, or shared mature source;
- CLIENT enrollment, NODE_HOST attachment, discovery, remote AI, clipboard history, voice input, or mission execution;
- modifying workflows or build requests;
- building an APK;
- triggering workflows;
- merging, releasing, deploying, installing, or deleting branches.

Expected result:

One independently packaged, role-valid SWRLZ Keyboard IME source project with a unique Android identity, offline basic typing behavior, canonical ZIP/SHA evidence, and no mature-host attachment.

Exact approval phrase:

`Approve KEYBOARD-IMP-011A — Accept com.swrlz.keyboard.app as the initial permanent SWRLZ Keyboard application identity and implement a standalone minimal offline-first Android IME source project under SOURCES/KEYBOARD/source with basic character, space, backspace, and enter input, protected-editor classification seam, manifest and IME metadata, canonical source ZIP/SHA, verification evidence, rollback, and handoff without removing or archiving the existing seed ZIP, copying the Core app shell, modifying CORE_BASE, Launcher, CLIENT, SERVER/NODE_HOST, workflows, or build requests, building an APK, triggering workflows, merging, releasing, deploying, installing, or deleting branches`
