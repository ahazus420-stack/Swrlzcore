# CLIENT CFv2.0.57 Forge Live Log and Connector Restore

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub APK compilation, Android backup restoration, and device acceptance pending.

## Scope

CLIENT `CFv2.0.57` extends the prepared `CFv2.0.56` baseline with:

1. a detailed, exportable Forge upload-session log available before terminal completion;
2. durable GitHub connector profile persistence;
3. eligible reinstall/device-transfer restoration and validated auto-connect;
4. explicit approval before disconnecting and erasing the saved connector state;
5. corrected CLIENT bubble-footer spacing and truncation.

No SERVER source change is included in this package.

## Forge upload-session diagnostics

A diagnostic session starts when the source picker opens or when an existing staged set is submitted. The append-only log records:

- source document name, size, provider, and resolved destination;
- exact sibling SHA matching and companion-picker fallback;
- staging additions and user-initiated clears;
- Forge transaction UUID and non-secret repository target;
- source ZIP content guard and ZIP/SHA validation;
- bounded byte-transfer checkpoints;
- blob, tree, commit, branch-update, retry, branch-confirmation, and uploaded-path verification phases;
- workflow dispatch and exact-commit workflow discovery;
- exceptions and terminal outcome.

The latest log can be exported while selection, validation, staging, upload, or workflow discovery is still active, and remains available after success or failure. The UI presents a `DOWNLOAD UPLOAD LOG` action. The log store retains a bounded set of files and caps individual logs to prevent unbounded storage growth.

## Credential redaction

Exported diagnostics sanitize:

- `Authorization` bearer/token headers;
- GitHub token prefixes such as `github_pat_` and `ghp_` families;
- access and refresh tokens;
- OAuth device codes;
- token-like values in logged error text.

The implementation does not intentionally write the GitHub token or OAuth device code to the diagnostic log.

## Connector persistence

### Restart and in-place update

The OAuth client ID, repository owner, repository name, branch, connected login, auto-connect intent, and explicit-disconnect state are stored synchronously in a backup-aware connection profile. Runtime authorization remains in Android Keystore-backed encrypted preferences. A temporary GitHub verification failure does not erase the stored credential.

### Uninstall/reinstall and device transfer

Android normally removes app-private data on uninstall. CLIENT therefore uses a custom backup agent instead of claiming unconditional persistence:

- the Keystore-encrypted preference file is excluded from ordinary backup because its original key is not portable;
- non-secret connection profile data is eligible for Android Auto Backup/device transfer;
- a temporary restore envelope containing the profile and token is emitted only when the backup transport reports client-side encryption or direct device-to-device transfer;
- after restore, the token is imported into the fresh install's new Keystore-backed secret store;
- CLIENT validates the restored token before showing the connector as connected;
- when only the profile is restored, the client ID and repository target remain populated, but one authorization is required to re-arm automatic connection.

Backup transport eligibility, retention, user account settings, and restore timing are controlled by Android. Restoration after every uninstall cannot be guaranteed by the application.

## Disconnect authority

Disconnecting GitHub now requires an approval dialog. Confirmed disconnect:

- deletes the runtime token;
- deletes the saved OAuth client ID and repository profile;
- disables auto-connect;
- records explicit disconnect intent so a restored profile cannot silently reconnect.

Cancelling the dialog leaves the connector intact.

## Bubble footer correction

The CLIENT bubble footer now separates:

```text
CLIENT · CFv2.0.57
VC 84
2.0.57-forge-live-logs-connector-restore-v1
```

The short CF identity, version code, and full Android version no longer concatenate into one unreadable line.

## Version and package identity

```text
Package: CLIENT_CFv2.0.57_SWRLZ.zip
versionCode: 84
versionName: 2.0.57-forge-live-logs-connector-restore-v1
SHA-256: c16b64d31473f90c4206bea0895a407389adc082420c9ab768825984eb473009
```

## Static and package evidence

- The final ZIP passed compressed-data integrity testing.
- The external SHA-256 receipt matches the final archive bytes.
- Android backup and data-extraction XML files parse successfully.
- Modified Kotlin files passed bounded structural checks.
- Upload-log hooks are present across source selection, SHA matching, validation, upload, Git object creation, branch confirmation, workflow discovery, retry, failure, and terminal completion.
- The connector profile, encrypted token store, backup agent, restore import, validation path, and disconnect approval are source/static-verified.
- Local Gradle compilation could not run because the isolated preparation environment could not download the Gradle distribution. GitHub Actions remains the authoritative compilation gate.

## Evidence classification

- Feature request and prior connector-loss symptom: device-reported.
- Forge log implementation: source/static-verified.
- Connector persistence and backup implementation: source/static-verified.
- Final ZIP/SHA pair: locally package-verified.
- Clean APK build: pending.
- Active-upload log export: pending device evidence.
- Secret-redaction acceptance: pending exported-log inspection.
- Restart/update auto-connect: pending device evidence.
- Eligible uninstall/reinstall or device-transfer restoration: pending Android backup evidence.

## Acceptance gate

1. Upload and build CLIENT `CFv2.0.57` through Forge.
2. Export a log immediately after opening the source picker.
3. Export another snapshot while a large upload is in progress.
4. Export logs after one successful and one failed transaction.
5. Verify transaction UUIDs, repository phases, retries, commit SHA, and workflow-discovery phases are present.
6. Verify no token, authorization header, access token, refresh token, or device code appears.
7. Restart the CLIENT and verify the connector validates and reconnects without re-entry.
8. Install a same-signed update and verify the connector remains valid.
9. Exercise an eligible encrypted backup/device-transfer restore and verify restored validation and auto-connect.
10. Confirm the disconnect approval dialog can cancel safely and that confirmed disconnect prevents later automatic reconnect.
