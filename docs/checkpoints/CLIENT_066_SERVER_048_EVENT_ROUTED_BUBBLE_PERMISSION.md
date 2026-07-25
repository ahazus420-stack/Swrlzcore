# CLIENT CFv2.0.66 + SERVER CFv2.0.48 — Event-Routed Forge, Embedded Bubbles, and User Permissions

**Recorded:** 2026-07-25  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub Android builds and device acceptance pending.

## Context

This checkpoint completes the requested CFv2.0.x cohesion work before the next Chat and Missions phase. Existing Forge, bubble, notification, mission HUD, credential, and SERVER runtime foundations are preserved.

## 1. Repository event routing

The shared APK source resolver no longer requires CLIENT/SERVER-style version names. The repository lane identifies the build contract, while the exact ZIP changed by the current push identifies the source to build.

Supported lanes:

```text
SOURCES/CLIENT
SOURCES/SERVER
SOURCES/CORE_BASE
SOURCES/KEYBOARD
SOURCES/LAUNCHER
```

`SOURCES/CORE_BASE` is the generic Android Gradle-project lane.

A re-uploaded older version is selected when it is the ZIP changed by the current commit. The resolver does not replace it with the numerically highest version already present.

When more than one distinct ZIP changes in the same lane in one commit, resolution fails closed rather than guessing. Different lanes may still build through the workflow matrix.

Repository implementation commits:

```text
b13c803595c0cd6c7259119c5e11d5eeab21fea8  event/path-driven source resolver
190c8e0fc344c19393c3cb5e28b25d98788e0245  arbitrary-name and exact-push tests
e8000b412fec0d0c09649a76a5eb3a3485dded40  transport-tolerant integrity verifier
```

## 2. Forge lane selection

CLIENT and SERVER Forge now expose remembered lane controls for:

- CLIENT;
- SERVER;
- generic ANDROID;
- KEYBOARD;
- LAUNCHER.

Arbitrary ZIP names use the selected lane. Known CLIENT and SERVER prefixes may still assist default routing, but no filename pattern grants or denies source eligibility by itself. Archive contents, checksum integrity, repository access, and the selected lane remain authoritative.

## 3. CLIENT embedded bubble shell

CLIENT CFv2.0.66 removes the separate bubble-only destination grid and embeds the same `ClientShellScreen` used by the full CLIENT.

The bubble now has:

- horizontally scrollable bottom navigation;
- the CLIENT version strip directly above navigation;
- direct access to Core, Chat, Groups, Missions, Activity, Forge, and Settings;
- shared full-app credential, mission, Forge, and application state.

## 4. SERVER embedded bubble shell

SERVER CFv2.0.48 similarly embeds the complete `SwurlzerUserScreen` in the SERVER bubble. It preserves the same HostViewModel and application-level state used by the full SERVER and exposes the scrollable User navigation and SERVER version strip.

## 5. Modern CLIENT permission onboarding

CLIENT User Mode now uses a dedicated first-launch SWRLZ permission overlay for:

- Accessibility Service;
- Draw over other apps;
- notification access;
- restricted-settings recovery through App Info;
- live status refresh after returning from Android settings.

Accessibility and overlay access gate entry into the full User shell. The legacy permissions interface remains available in Developer Mode for engineering diagnostics.

## 6. SERVER launcher alignment

All SERVER Theme Armor foreground and monochrome assets receive a final scale and upward safe-zone adjustment. Existing versioned launcher aliases are preserved. This is intended to correct the remaining low/right launcher placement while retaining detailed normal and themed-icon identities.

## 7. Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.66_SWRLZ.zip
versionCode: 93
versionName: 2.0.66-event-routed-bubble-permissions-v1
SHA-256: 44a6c6f95137fb213dba0a0feaca6586ce250fa0eb6a788d33e42577836fd550
```

### SERVER

```text
Package: SERVER_CFv2.0.48_SWRLZ.zip
versionCode: 49
versionName: 2.0.48-event-routed-bubble-icon-v1
SHA-256: baa660b056575066e73d64a0c54e515557fdabaf8fe224d433ad4ef1615afb83
```

Both archives pass compressed-data integrity testing and match their sibling SHA-256 receipts.

## 8. Static evidence

- Kotlin PSI syntax parsing passed for all 124 Kotlin and Gradle Kotlin files across both packages.
- All 45 XML resources parse.
- All 205 PNG/JPEG/WebP resources open and verify.
- Local Android resource-reference review found no unresolved application resources; two reported menu icons were confirmed Android framework drawables.
- CLIENT and SERVER version codes and names match this checkpoint.
- SERVER bubble uses `@AndroidEntryPoint` and the existing Hilt `HostViewModel`.
- CLIENT User permission onboarding resolves existing PermissionHelper APIs.
- Both embedded shells suppress duplicate status/navigation insets inside bubbles.
- Local Gradle execution could not proceed because the isolated environment could not download Gradle 8.7/8.9 distributions; GitHub Actions remains the authoritative compile gate.

## 9. Evidence classification

- event/path router, tests, and verifier alignment: repository-committed;
- CLIENT/SERVER Android changes: source/static verified;
- final ZIP/SHA pairs: locally package verified;
- GitHub Android compilation: pending;
- exact older-source build, generic Android lane build, bubble-shell behavior, permission onboarding, and final SERVER launcher placement: pending device/workflow acceptance.

## 10. Acceptance gate

1. Upload and build CLIENT CFv2.0.66.
2. Verify the CLIENT bubble opens directly into the embedded app shell with version strip and scrollable bottom navigation.
3. Verify first-launch User Mode presents the modern Accessibility/overlay permission overlay.
4. Upload an arbitrary-name valid Android source to the intended Forge lane and confirm it builds.
5. Re-upload an older source version in its lane and confirm the exact changed ZIP is selected.
6. Upload and build SERVER CFv2.0.48.
7. Verify the SERVER bubble opens directly into its embedded User shell.
8. Verify the selected SERVER launcher icon is centered in normal and Android themed-icon modes.
9. Verify multiple different ZIPs in one lane/commit fail with an explicit ambiguity message rather than selecting silently.
