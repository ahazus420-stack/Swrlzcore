# Forge Event Routing and Embedded Bubble Architecture v1

**Status:** Accepted CFv2.0.x architecture; GitHub Android builds and device acceptance remain evidence-gated.  
**Recorded:** 2026-07-25

## 1. Purpose

This document defines the final CFv2.0.x cohesion model for source selection, GitHub Actions routing, generic Android project builds, CLIENT/SERVER bubble presentation, and CLIENT first-launch permission onboarding.

## 2. Source lane is authoritative

The repository lane determines the build contract. A source ZIP's display name is transport metadata and does not decide whether the archive is eligible.

```text
SOURCES/CLIENT/      -> CLIENT build contract
SOURCES/SERVER/      -> SERVER build contract
SOURCES/CORE_BASE/   -> generic Android Gradle project contract
SOURCES/KEYBOARD/    -> keyboard Android project contract
SOURCES/LAUNCHER/    -> launcher Android project contract
```

Forge exposes these lanes directly. Known CLIENT and SERVER names may still assist default routing, but arbitrary names remain valid when the user selects the intended lane and the archive passes local and repository integrity checks.

## 3. Exact event source selection

For a push event, the APK Router selects the exact ZIP changed by that commit inside each affected lane. Semantic version ordering does not override the event source.

Therefore, re-uploading an older source version intentionally builds that newly changed archive rather than a numerically newer ZIP already present in the lane.

Selection order:

1. explicit `workflow_dispatch` source path;
2. exact ZIP changed by the current push;
3. ZIP resolved from a checksum-only correction in the current push;
4. most recently committed repository ZIP when no event-specific source exists.

If more than one distinct ZIP changes in the same lane in one commit, resolution fails closed and requires separate uploads or explicit dispatch. Different lanes may build in the same matrix run.

## 4. Filename policy

Any non-empty `.zip` basename is accepted by the shared resolver. Android download-copy suffixes immediately before the extension remain transport aliases:

```text
My Keyboard Experiment (3).zip
My Keyboard Experiment.sha256
```

The suffix is ignored for logical pairing. Artifact filenames use a sanitized stem, but original source names remain in provenance.

Checksums remain mandatory. Manifests remain optional and are validated when present.

## 5. Generic Android project lane

`SOURCES/CORE_BASE` is the generic Android Gradle-project lane. Its archive must contain:

- a Gradle wrapper, or a supported wrapper-generation structure;
- a root `settings.gradle` or `settings.gradle.kts` adjacent to the wrapper;
- an `app` module capable of `:app:assembleDebug` or `:app:assembleRelease`;
- a discoverable APK output.

This lane is intentionally filename-independent. A completely separate Android project may be uploaded and built without pretending to be CLIENT, SERVER, KEYBOARD, or LAUNCHER.

## 6. Integrity alignment

The Source Package Integrity verifier uses the same transport-name logic as the APK Router:

- copy-suffixed ZIP and checksum aliases may pair;
- checksum files may declare filenames containing spaces;
- filename declarations are compared by logical transport stem;
- actual ZIP bytes and SHA-256 remain authoritative;
- aliases containing different bytes or different hashes fail closed.

## 7. Embedded bubble shell

CLIENT and SERVER bubbles no longer require a separate initial destination grid. Each bubble embeds the same User shell used by its full application:

- version strip directly above navigation;
- horizontally scrollable bottom navigation;
- identical Core, Chat/Activity, Groups/Nodes, Missions, Forge, Settings, and related destinations;
- the same app-level credential, Forge, mission, and runtime state.

The bubble is a presentation host, not an independent authority or state store.

## 8. CLIENT first-launch permission experience

User Mode presents a modern SWRLZ permission overlay rather than the legacy Developer permission screen. It covers:

- Accessibility Service;
- Draw over other apps;
- notifications;
- restricted-settings recovery through Android App Info;
- live re-check after returning from system settings;
- guarded entry until Accessibility and overlay access are ready.

Developer Mode retains the legacy engineering permission center for diagnostics and testing.

## 9. SERVER launcher safe zone

SERVER adaptive foreground and monochrome assets receive a final scale and upward safe-zone adjustment. Theme aliases remain versioned to prevent stale launcher component caching. Normal and Android themed-icon modes must preserve recognizability within the launcher mask.

## 10. Invariants

- Filename text never substitutes for archive-content validation.
- Lane choice never bypasses ZIP/SHA integrity.
- Push builds use the exact changed source rather than inferred highest version.
- Ambiguous same-lane multi-ZIP pushes fail closed.
- Bubble and full-app surfaces share one authority per application.
- CLIENT and SERVER credentials remain separate Android sandbox authorities.
- User permission onboarding does not silently grant Android permissions.
- GitHub compilation and device tests remain the authoritative acceptance gates.
