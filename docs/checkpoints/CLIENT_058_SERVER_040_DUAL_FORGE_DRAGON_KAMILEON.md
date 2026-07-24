# CLIENT CFv2.0.58 + SERVER CFv2.0.40 — Dual Forge and Dragon Kamileon

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub APK compilation and device acceptance pending.

## Scope

This checkpoint records:

1. the bounded CLIENT `CFv2.0.57` upload-log compiler failure and `CFv2.0.58` repair;
2. promotion of Forge into a CLIENT-and-SERVER ecosystem capability;
3. explicit CLIENT/SERVER Forge actor evidence and truthful concurrent-branch behavior;
4. the Dragon Kamileon Theme Armor family across both applications.

## CLIENT build failure and repair

The uploaded APK Router log reached `:app:compileDebugKotlin` and failed with:

```text
ForgeUploadLogStore.kt:47:9 Function invocation 'prefs(...)' expected.
```

`startSession()` attempted to call `prefs.edit()` without resolving the local SharedPreferences value. CLIENT `CFv2.0.58` adds:

```kotlin
val prefs = prefs(context)
```

before the current/latest session write. The live-log, credential-redaction, upload, and connector semantics remain unchanged.

## Dual Forge

CLIENT and SERVER can each select and validate CLIENT/SERVER ZIP+SHA pairs, stream blobs, create an atomic Git tree and commit, confirm the branch, verify uploaded paths, observe GitHub Actions, and retrieve artifacts and logs.

SERVER Forge is exposed through:

- SWURLZER User Mode;
- SERVER Developer Mode;
- the SERVER Android bubble as a direct Forge surface.

The SERVER foreground service monitors durable verified-commit watches and publishes terminal artifact-build results through SERVER notifications and the bubble.

## Actor identity and concurrency truth

Each transaction contains a fresh UUID and actor marker:

```text
SWRLZ-Forge-Transaction: <uuid>
SWRLZ-Forge-Actor: CLIENT | SERVER
```

No unsupported shared local lock is claimed between separate CLIENT and SERVER Android processes. Repository safety is provided by:

- streamed blobs before commit construction;
- resolving the latest branch head after transfer;
- rebuilding the candidate tree after branch movement;
- bounded retry of non-fast-forward races;
- confirmation that the branch resolves to the created commit;
- read-back verification of every uploaded repository path and expected blob SHA.

A transfer bar reaching 100 percent is not repository success.

## Dragon Kamileon Theme Armor

Both apps add a selectable Dragon Kamileon family inspired by iridescent metallic cyan, violet, magenta, and gold refraction.

### CLIENT

- interface token family;
- dedicated launcher alias and adaptive icon;
- CLIENT, selected SERVER-context, and fusion bubble identities.

### SERVER

- interface token family;
- dedicated SWURLZER launcher alias and adaptive icon;
- SERVER and fusion bubble identities;
- synchronized notification accent.

Theme state remains presentation and never grants trust, SERVER authority, or fusion capability.

## Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.58_SWRLZ.zip
versionCode: 85
versionName: 2.0.58-kamileon-theme-forge-log-buildfix-v1
SHA-256: edf9dad48363438d13186636eb6c44855540058254c2841f87a164dfa6308967
```

### SERVER

```text
Package: SERVER_CFv2.0.40_SWRLZ.zip
versionCode: 41
versionName: 2.0.40-server-forge-kamileon-v1
SHA-256: 88c8088ee3d1e5a4ded9184caa8774fb60273bcfde55502b4e1904d16e405c60
```

Both final archives passed compressed-data integrity testing and match their sibling SHA-256 receipts.

## Static evidence

- all XML resources parse;
- all PNG resources open and verify;
- new resource references resolve;
- modified Kotlin files pass structural delimiter checks;
- no active Kotlin source imports the incompatible explicit Compose `foundation.layout.weight` extension;
- SERVER Forge uses API 24-compatible Android Base64 streaming;
- the same-folder picker initial-location hint is gated to API 26+;
- local Gradle compilation could not run because the isolated preparation environment could not reach the Gradle distribution host.

## Evidence classification

- CLIENT `CFv2.0.57` failure: workflow-log verified.
- CLIENT repair: source/static verified.
- SERVER Forge: source/static verified.
- Dragon Kamileon resources and mappings: source/resource verified.
- final ZIP/SHA pairs: package verified.
- clean GitHub builds: pending.
- device Forge, bubble, notification, theme, launcher, and dual-actor behavior: pending.

## Acceptance gate

1. Upload CLIENT `CFv2.0.58` and confirm one integrity run and a successful APK Router artifact.
2. Install it with the same signing certificate and validate Dragon Kamileon across interface, launcher, and all bubble roles.
3. Upload SERVER `CFv2.0.40` and confirm one integrity run and a successful APK Router artifact.
4. Install it and validate SERVER Forge from User Mode, Developer Mode, and the bubble.
5. Perform one CLIENT-actor upload and one SERVER-actor upload; confirm different transaction UUIDs and actor markers.
6. Confirm each final commit becomes the branch head in sequence and each uploaded path resolves to the expected blob.
7. Confirm upload/build notifications and downloadable logs/artifacts from both Forge surfaces.
