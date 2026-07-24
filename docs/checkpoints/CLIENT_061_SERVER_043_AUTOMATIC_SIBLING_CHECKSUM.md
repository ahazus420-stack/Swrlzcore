# CLIENT CFv2.0.61 + SERVER CFv2.0.43 — Automatic Sibling Checksum Resolution

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub builds and device acceptance pending.

## Runtime evidence received

Device testing confirms that CLIENT repeated uploads now work from the same installed app without reinstalling. Separate uploads create new confirmed commits, and the CLIENT bubble plus Android notification shade report upload-success commit evidence correctly.

## SERVER workflow-log clarification

The newly supplied SERVER log resolved the source package as `SERVER_CFv2.0.41_SWRLZ` and failed at:

```text
ServerBubbleActivity.kt:176:30 Unresolved reference 'border'.
```

That is the already-diagnosed CFv2.0.41 missing-import failure. SERVER CFv2.0.42 added:

```kotlin
import androidx.compose.foundation.border
```

SERVER CFv2.0.43 preserves that exact repair. The supplied log is not evidence that CFv2.0.42 failed.

## Checksum-selection problem

After selecting one source ZIP, Forge could immediately launch a second Android file browser when its first direct sibling-URI guess failed. The expected filename was already derived correctly, but the lookup was too narrow and the fallback was forced rather than user-controlled.

## Automatic resolver

For a selected:

```text
<base>.zip
```

both CLIENT and SERVER now derive:

```text
<base>.sha256
```

and attempt the following provider-safe lookup sequence:

1. ordinary file-URI parent lookup;
2. direct document-ID substitution for path-based providers;
3. parent-directory child enumeration when a parent document ID can be derived;
4. Android 10+ MediaStore Files lookup using source display name, size, and relative directory.

A candidate is accepted only when:

- its display name exactly matches the derived `.sha256` name;
- its content URI can actually be opened;
- later ZIP/SHA validation succeeds before upload.

## Manual fallback policy

Forge no longer launches a second file browser automatically.

When Android's selected document provider does not expose a readable sibling, Forge:

- keeps the ZIP staged;
- reports the unresolved expected checksum;
- shows an explicit `LOCATE SHA-256` action beside that ZIP;
- opens the provider picker only after that direct user action.

No broad storage or all-files permission is added. No Downloads-root tree grant is required.

## Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.61_SWRLZ.zip
versionCode: 88
versionName: 2.0.61-automatic-sibling-checksum-v1
SHA-256: af72627619d9f0255b6c167419c8fc3153ad0ee7982ad07ae1097dc004d34d2a
```

### SERVER

```text
Package: SERVER_CFv2.0.43_SWRLZ.zip
versionCode: 44
versionName: 2.0.43-automatic-sibling-checksum-v1
SHA-256: 9c745737a8df7099570071d9733a5e9390aef43c18bc32fb7324c08aae51ea95
```

Both final archives passed compressed-data integrity testing and match their sibling SHA-256 receipts.

## Static evidence

- Kotlin PSI syntax parsing passed for all 121 Kotlin and Gradle Kotlin files across both packages.
- All XML resources parse.
- All PNG/JPEG/WebP resources open and verify.
- SERVER CFv2.0.43 retains the required `androidx.compose.foundation.border` import.
- The automatic selection path contains no automatic `checksumPicker.launch(...)` call.
- The explicit manual `LOCATE SHA-256` fallback is present in both Forge staging surfaces.
- Full Android compilation remains a GitHub Actions evidence gate.

## Evidence classification

- repeated-upload and notification behavior: device-reported with screenshot evidence;
- SERVER CFv2.0.41 failure: workflow-log verified;
- SERVER CFv2.0.42 border repair preservation: source verified;
- automatic checksum resolver and non-forced fallback: source/static verified;
- final ZIP/SHA pairs: locally package verified;
- clean CLIENT CFv2.0.61 and SERVER CFv2.0.43 builds: pending;
- automatic Downloads sibling matching on the target device: pending.

## Acceptance gate

1. Build and install CLIENT CFv2.0.61.
2. Select a ZIP and confirm its exact sibling `.sha256` stages without another picker.
3. Confirm a provider that denies sibling access leaves the ZIP staged and shows `LOCATE SHA-256` without opening a browser automatically.
4. Confirm repeat uploads and commit notifications remain correct.
5. Build SERVER CFv2.0.43 and confirm Kotlin compilation passes beyond the prior `border` failure.
6. Validate the same checksum behavior in SERVER User Mode, Developer Mode, and the bubble Forge.
