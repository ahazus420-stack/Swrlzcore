# SERVER CFv2.0.10 Release Notes

Checkpoint: INT-BUILD-020A

## Changed

- Advanced SERVER source/application identity to versionCode 13 and versionName 2.0.10.
- Issued an unambiguous canonical source/checksum pair for SWRLZ APK Router resolution:
  - `SERVER_CFv2.0.10_SWRLZ.zip`
  - `SERVER_CFv2.0.10_SWRLZ.sha256`

## Runtime behavior

No SERVER runtime, lifecycle, registration, presence, protocol, Room, trust, identity, or UI behavior changed from CFv2.0.9.

## Verification

- Exact-basename checksum receipt generated.
- Static version guard passed.
- ZIP integrity passed.
- Gradle/APK build and workflow execution were not performed.

## Known issues

Workflow source-resolution acceptance remains pending upload of both exact companion files and a new GitHub build.