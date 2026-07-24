# CLIENT CFv2.0.62 + SERVER CFv2.0.44 — Manual Token Validation State Repair

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub APK builds and device acceptance pending.

## Symptom

Entering a fine-grained GitHub token could make the Forge account surface disappear, begin connecting before the full token was entered, and repeatedly return the user to token entry without establishing a durable connection.

## Root cause

Both CLIENT and SERVER Forge reused one Compose state variable for two different security states:

```text
manual text being typed
active validated runtime credential
```

The account UI treated any non-blank value as an active token. Therefore the first typed character could:

1. make `token.isNotBlank()` true;
2. switch the UI out of the manual-entry branch;
3. trigger saved-token `LaunchedEffect` validation against an incomplete value;
4. fail validation before the user could submit the complete token.

This was a state-model defect, not evidence that the GitHub token itself was necessarily invalid.

## Corrected state model

```text
manualTokenInput   unverified candidate text only
token              active validated runtime credential only
```

The repaired behavior is:

- typing never changes the active token;
- typing never starts auto-connect or hides the manual token form;
- `VALIDATE TOKEN` snapshots and trims the complete candidate;
- GitHub `/user` validation runs once against that immutable candidate;
- persistence to the Android Keystore-backed secret store occurs only after validation succeeds;
- connected account state is published only after persistence succeeds;
- invalid input remains editable and is not saved;
- restored-token auto-validation observes only the active validated token;
- device-code authorization also validates and persists a local token snapshot before publishing connected state;
- approved disconnect clears both active and unverified token state.

## CLIENT package

```text
Package: CLIENT_CFv2.0.62_SWRLZ.zip
versionCode: 89
versionName: 2.0.62-manual-token-validation-state-v1
SHA-256: 7fe9633f014b4d37ade13219d28527c4e2c971214c96333c617e4dcd700cd743
```

## SERVER package

```text
Package: SERVER_CFv2.0.44_SWRLZ.zip
versionCode: 45
versionName: 2.0.44-manual-token-validation-state-v1
SHA-256: 7c602bc49843d4f9f04e8e4dd8ec1d209caf3fc1a9dd253cf3bd1b88bfa645d0
```

## Static and package evidence

- Kotlin PSI syntax parsing passed for every Kotlin and Gradle Kotlin source file across both packages.
- All XML resources parse.
- All PNG/JPEG/WebP resources open and verify.
- Neither Forge binds the fine-grained token text field to the active runtime token.
- Both Forges validate `candidateToken` and assign it to the active token only after success.
- Both ZIP archives pass compressed-data integrity testing.
- Both SHA-256 receipts match the final archive bytes.

## Evidence classification

- reported token-entry loop: device-reported;
- root cause: source-verified;
- CLIENT and SERVER repairs: source/static verified;
- final ZIP/SHA pairs: locally package verified;
- clean GitHub Android builds: pending;
- successful manual token connection and persistence: pending device acceptance.

## Acceptance gate

1. Build and install CLIENT CFv2.0.62.
2. Open the fine-grained token fallback and type the complete token.
3. Confirm the field remains visible and no connection attempt begins while typing.
4. Press `VALIDATE TOKEN` once.
5. Confirm invalid input remains editable and is not stored.
6. Confirm valid input becomes Connected only after GitHub accepts it.
7. Restart CLIENT, switch Theme Armor, and confirm no token re-entry.
8. Repeat the same validation sequence in SERVER CFv2.0.44.
