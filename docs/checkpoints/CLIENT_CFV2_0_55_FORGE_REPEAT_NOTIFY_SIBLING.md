# CLIENT CFv2.0.55 Forge Repeat Upload, Outcome Notification, and Sibling SHA Repair

**Recorded:** 2026-07-24  
**Status:** Source implementation and package verification complete; clean GitHub build, repeated same-install upload evidence, Android notification evidence, and device bubble evidence pending.

## Scope

This checkpoint records the bounded CLIENT `CFv2.0.55` reliability pass addressing three user-observed Forge problems:

1. the first Forge upload after a fresh CLIENT installation could succeed, while a later upload in the same installation appeared to transfer but did not visibly mutate the repository;
2. upload and artifact-build outcomes were not surfaced consistently through the CLIENT notification and bubble surfaces;
3. checksum auto-matching depended on a separate Storage Access Framework folder grant that could not reliably select the Android Downloads root.

The patch preserves the CLIENT bubble interface introduced in `CFv2.0.53` and the Compose build repair from `CFv2.0.54`.

## Evidence boundary

The exact historical one-upload-only state variable was not conclusively isolated from a device log. No unsupported claim is made that one specific cached flag caused every failure.

The repair instead removes or detects the transaction hazards that could produce the observed symptom and requires authoritative postconditions before reporting success:

- every upload receives a fresh transaction identifier;
- files are snapshotted at submission time;
- the latest branch head is resolved after blob transfer;
- branch-update races are retried with a fresh head;
- no-op trees are rejected;
- the new branch head must equal the created commit;
- every uploaded repository path is read back and must resolve to the expected blob SHA;
- staging clears only after those checks pass;
- workflow dispatch failure is reported separately from a repository upload that already committed successfully.

## 1. Repeat-upload transaction repair

### Transaction identity

Each Forge submission now creates a fresh UUID and embeds it in the commit message:

```text
SWRLZ-Forge-Transaction: <uuid>
```

The transaction identifier is retained in the upload result, event record, bubble banner, notification, and build-watch record so a second submission cannot be mistaken for the first transaction.

### Commit truth

Forge now distinguishes:

```text
source transfer complete
-> resolve latest branch head
-> create candidate tree
-> reject no-op tree
-> create commit
-> update branch ref
-> confirm branch head
-> verify uploaded path blob SHAs
-> publish upload success
-> clear staging
```

A progress bar reaching 100 percent is not upload success. A commit object existing without the target branch pointing to it is not upload success.

### Branch race recovery

When GitHub rejects a branch update because the branch moved during the transaction, Forge re-resolves the latest head, rebuilds the tree, and retries a bounded number of times. A transaction that cannot establish and confirm the new head fails visibly instead of replaying a successful-looking transfer state.

### Workflow dispatch truth

Repository mutation and workflow dispatch are separate outcomes:

- confirmed commit + successful dispatch: upload success and build watch started;
- confirmed commit + failed dispatch: upload remains successful, with a separate dispatch warning;
- failed branch confirmation or failed path verification: upload failure and staging retained.

## 2. Upload and build outcome surfaces

A high-importance CLIENT Forge event channel now reports:

- upload succeeded;
- upload failed;
- workflow dispatch failed after a confirmed commit;
- artifact build succeeded;
- artifact build failed.

The same event is persisted into the CLIENT bubble state and displayed through a compact latest-event banner. Bubble and notification surfaces are projections of the same event record rather than independent success calculations.

### Durable build watch

After a confirmed upload, CLIENT stores a bounded build watch containing:

- repository owner and name;
- target branch;
- confirmed commit SHA;
- Forge transaction identifier;
- creation time.

The existing CLIENT foreground status service polls pending watches while active. The monitor matches workflow runs to the exact uploaded commit SHA, reports one terminal result, counts available non-expired artifacts on success, then retires the watch.

Android 13 and newer still require notification permission. Background outcome monitoring also depends on the existing CLIENT status foreground service being enabled and allowed to run.

## 3. Selected-ZIP sibling checksum matching

The separate `OpenDocumentTree` Downloads-folder grant was removed from the normal Forge flow.

New behavior:

1. the user selects a source ZIP;
2. Forge attempts to resolve the exact sibling `<base>.sha256` from the selected document location;
3. when the document provider permits sibling access, the SHA is added automatically;
4. when Android or the provider does not expose sibling enumeration from the selected document URI, Forge opens an exact companion document picker initialized at that ZIP location;
5. the selected file must still have the exact required basename and pass checksum validation.

This is intentionally provider-aware. Android's Storage Access Framework does not guarantee unrestricted sibling enumeration from every single-document URI, so the same-location picker remains a truthful fallback rather than silently guessing a file.

## 4. CLIENT version identity

```text
Package: CLIENT_CFv2.0.55_SWRLZ.zip
versionCode: 82
versionName: 2.0.55-forge-repeat-notifications-sibling-v1
SHA-256: e48d69e5f172856ce319bbad6d2cef5e29ee4ae024a11a44695ab81439510547
```

The visible CLIENT footer continues to derive its CF version from Android build identity.

## Static and package evidence

- 28 bounded static verification checks passed.
- All changed Kotlin files passed structural delimiter-balance checks.
- No explicit `androidx.compose.foundation.layout.weight` import remains.
- No `OpenDocumentTree` contract remains in the Forge source-selection flow.
- Only one Forge event notifier implementation remains.
- The final ZIP passed compressed-data integrity testing.
- The final ZIP digest matches the sibling SHA-256 receipt.

A local Gradle build could not complete because the isolated execution environment could not reach the Gradle distribution host. GitHub Actions remains the authoritative compilation gate.

## Evidence classification

- User-observed first-upload-only symptom: device-reported.
- Exact historical single root cause: not conclusively proven.
- Transaction/postcondition repair: source and static-verified.
- Notification and bubble event implementation: source and static-verified.
- Build-watch implementation: source and static-verified.
- Same-location SHA behavior: source and static-verified.
- ZIP/SHA pair: package-verified locally.
- Clean GitHub APK build: pending.
- Two successive uploads from one installed CLIENT: pending device evidence.
- Notification and bubble terminal outcomes: pending device evidence.
- Artifact build watch across leaving Forge: pending device evidence.

## Acceptance test

1. Install or update to CLIENT `CFv2.0.55` using the same signing certificate.
2. Upload one CLIENT or SERVER ZIP/SHA pair and record transaction ID, commit SHA, branch head, workflow run, and notification.
3. Without reinstalling or clearing app data, stage a different version and upload again.
4. Confirm the second transaction ID differs from the first.
5. Confirm the second commit is the target branch head and the changed package paths contain the expected blobs.
6. Confirm staging clears only after branch and path verification.
7. Confirm the CLIENT bubble and notification show upload success or failure.
8. Leave Forge open or navigate elsewhere while the build runs; confirm the foreground status service reports artifact build success or failure once.
9. Select a ZIP from Downloads and confirm exact sibling SHA matching occurs automatically when provider access permits, otherwise through the same-location companion picker.

## Exclusions

This patch does not claim runtime acceptance before the acceptance test passes. It does not fabricate build percentages, infer workflow success from elapsed time, grant authority through notifications, or bypass Android document-provider permissions.