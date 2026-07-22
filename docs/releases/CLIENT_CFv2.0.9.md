# CLIENT CFv2.0.9 Release Notes

Checkpoint: INT-BUILD-020A

## Fixed

- Repaired four Kotlin references to the nonexistent `CoreNodeCheckRecord.connected` property.
- The CLIENT now uses the canonical `CoreNodeCheckRecord.success` field for automatic connection truth.

## Changed

- Android versionCode advanced to 40.
- Android versionName advanced to `0.2.7.14-cfv2.0.9-int-build-020a`.
- Embedded CLIENT workflow artifact naming advanced to CFv2.0.9.

## Security and trust boundaries

No identity, registration, proof, trust, authorization, or Truth Firewall behavior changed.

## Verification

- Static source guards passed.
- ZIP integrity passed.
- Gradle/APK build was not run in the source-packaging environment.

## Known issues

Runtime and build acceptance remain pending GitHub workflow execution.