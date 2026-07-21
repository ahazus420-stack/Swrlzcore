# APP-SHELL-GATE-010 — CORE_BASE, Keyboard, and Launcher Separation and Attachment Plan

- **Status:** Complete plan; source implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/app-shell-gate-010`
- **Base commit:** `961e92907acb6a3158f6da982902f07acbfba019`
- **Primary first implementation target:** SWRLZ Keyboard

## 1. Decision

Create Keyboard and Launcher as distinct Android app shells. Do not make either a renamed or independently mutated copy of the Core app.

Use the active reduced CORE_BASE project as toolchain and lineage evidence. Use shared modules and portable capsules through explicit references or attachments. Preserve each shell’s role, identity, signing, version, storage, lifecycle, permissions, and UI.

## 2. Archive policy

### Current inheritance seeds

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip
SOURCES/LAUNCHER/SWRLZ_LAUNCHER_BASE_CFv1.0.1.zip
```

Both remain lineage evidence until verified successor source packages exist.

### Required successor sequence

For each shell:

1. create a role-specific `source/` project;
2. create a new role-specific source ZIP and sibling SHA-256;
3. verify archive integrity, path safety, source identity, and role invariants;
4. accept the new source package as canonical;
5. only then move the byte-identical inheritance seed and checksum into `OLD_PATCHES/` through a separate explicit retirement checkpoint;
6. never delete the seed by default.

No source checkpoint may overwrite the historical seed under the same filename.

## 3. Keyboard implementation profile

### 3.1 Proposed source lane

```text
SOURCES/KEYBOARD/
├── README.md
├── source/
│   ├── settings.gradle.kts
│   ├── build.gradle.kts
│   ├── gradle.properties
│   ├── gradle/
│   ├── app/
│   ├── keyboard-domain/
│   └── docs/
├── packages/
├── OLD_PATCHES/
├── SWRLZ_KEYBOARD_SOURCE_v0.1.0.zip
└── SWRLZ_KEYBOARD_SOURCE_v0.1.0.sha256
```

The exact filename may be refined at implementation start, but it must not reuse the inheritance-seed filename.

### 3.2 Identity proposal requiring explicit acceptance

Recommended permanent candidate:

```text
namespace:     com.swrlz.keyboard.app
applicationId: com.swrlz.keyboard.app
surfaceType:   keyboard
label:         SWRLZ Keyboard
versionCode:   1
versionName:   0.1.0
```

No repository evidence shows this package identity is already occupied. Final acceptance belongs to the implementation approval.

### 3.3 Minimal first source scope

The first implementation should contain only:

- a standalone Android Gradle project;
- unique Keyboard package identity;
- one `InputMethodService`;
- required `BIND_INPUT_METHOD` service permission;
- IME intent filter and metadata XML;
- minimal keyboard view or Compose-hosted view;
- deterministic ordinary local key dispatch;
- backspace, space, enter, and basic character input;
- protected-editor classification seam;
- offline-first behavior;
- Keyboard-specific README, manifest evidence, source ZIP/SHA, and rollback record.

The first implementation must not contain:

- CLIENT enrollment implementation;
- NODE_HOST discovery or routing;
- remote AI calls;
- clipboard history;
- voice input;
- telemetry containing text;
- mission execution;
- automatic trust;
- Launcher behavior;
- copied Core `MainActivity` or Core manifest identity.

### 3.4 Relationship to Core

The Keyboard source may derive build-tool versions and repository conventions from active CORE_BASE.

It must not copy the Core app shell as its application layer.

The current `CoreKernel` is effectively empty and need not be duplicated merely to claim Core inheritance. Initial Keyboard source can remain standalone while declaring its Core lineage. Later shared behavior should attach through:

- a canonical shared module;
- an accepted portable capsule;
- or a thin adapter plus integration manifest.

### 3.5 Keyboard verification sequence

Source checkpoint:

```text
KEYBOARD-IMP-011A
```

- create source only;
- run local/static source tests where available;
- create canonical ZIP/SHA and evidence;
- no APK build.

Build checkpoint:

```text
KEYBOARD-VER-011B
```

- update or replace the old seed-build workflow through separate authorization;
- checksum-verify the new source package;
- build Keyboard independently;
- record APK package ID, version, checksum, manifest/service evidence;
- no installation unless separately authorized.

Device checkpoint:

```text
KEYBOARD-DEVICE-011C
```

- install only under explicit approval;
- enable/select the IME;
- verify ordinary typing offline;
- verify protected editor behavior;
- verify side-by-side coexistence with Core;
- verify no CLIENT/NODE_HOST dependency for basic typing.

Retirement checkpoint:

```text
KEYBOARD-RETIRE-011D
```

- archive the old inheritance seed and checksum under `OLD_PATCHES/`;
- preserve original names, checksum, Git history, and superseded-by reference;
- do not delete lineage.

## 4. Launcher planning profile

Launcher should follow only after a dedicated Launcher contract is accepted.

Recommended future checkpoints:

```text
LAUNCHER-CON-012A
LAUNCHER-IMP-012B
LAUNCHER-VER-012C
LAUNCHER-DEVICE-012D
LAUNCHER-RETIRE-012E
```

`LAUNCHER-CON-012A` must define:

- permanent package identity candidate;
- Android HOME-role behavior;
- package/app indexing and visibility;
- widget and wallpaper boundaries;
- CLIENT relationship and scoped credential model;
- offline behavior;
- search and remote-routing disclosure;
- Truth Firewall behavior;
- storage and telemetry policy;
- recovery when not default launcher.

Provisional package candidate for later review:

```text
com.swrlz.launcher.app
```

This plan does not accept that identifier.

## 5. Shared-module strategy

### Immediate

Keep the first Keyboard shell small and role-specific. Reuse toolchain configuration by reference in documentation rather than copying Core app behavior.

### Later

Extract or implement shared components only when at least two shells need them and the boundary is testable. Candidate relationships:

```text
Keyboard app
  → keyboard-domain
  → identity/protocol contract capsules
  → design-system module when accepted
  → CLIENT bridge through scoped adapter

Launcher app
  → launcher-domain
  → identity/protocol contract capsules
  → design-system module when accepted
  → CLIENT bridge through scoped adapter

Core app
  → CoreKernel
  → selected shared modules/capsules
```

Shared modules must not depend on app shells. Packaging a module must not grant authority.

## 6. Build-workflow migration

The current Keyboard and Launcher workflows build the historical byte-identical Core seed. They must remain untouched until a successor source checkpoint exists.

After source acceptance, separate workflow checkpoints should:

- change the selected source archive and checksum;
- verify expected unique `applicationId`;
- verify required role declarations;
- reject Core package identity;
- reject historical demo modules unless explicitly accepted;
- produce role-named artifacts and evidence;
- avoid push recursion and unauthorized promotion.

## 7. Rollback

### Keyboard source rollback

Before build attachment:

- remove or abandon the unaccepted checkpoint branch/source lane;
- retain audit, plan, failed package, checksum, and test evidence;
- leave Core, Launcher, CLIENT, SERVER, and seed ZIPs unchanged.

### Keyboard build rollback

- restore the prior workflow selector;
- preserve the new source package and failure evidence as unaccepted lineage;
- do not reactivate the old seed as though it were a real Keyboard product;
- do not alter Core installation or identity.

### Launcher rollback

No Launcher source change is planned by this checkpoint.

## 8. Nonclaims

This plan does not claim:

- a Keyboard APK exists;
- a Launcher APK exists as a distinct role-valid application;
- permanent package IDs are accepted;
- stable release signing exists;
- Core shared modules are mature;
- CLIENT enrollment is implemented;
- Keyboard remote processing is safe or available;
- Launcher requirements are complete.

## 9. Recommended next checkpoint

```text
KEYBOARD-IMP-011A — Minimal Standalone SWRLZ Keyboard IME Source
```

This is the shortest safe route toward the user’s requested Keyboard build while preserving the accepted architecture and source lineage.
