# INT-BUILD-020A Implementation Record

## Approval

`APPROVE INT-BUILD-020A — Repair CLIENT connection-state compilation, correct SERVER CFv2.0.9 source/checksum resolution, and update all required repository documentation`

## Objective

Repair the CLIENT CFv2.0.8 Kotlin compilation failure and provide an unambiguous SERVER source/checksum pair that the SWRLZ APK Router can resolve.

## Canonical baselines

- CLIENT CFv2.0.8
- SERVER CFv2.0.9

## Delivered lineage

- CLIENT CFv2.0.9
- SERVER CFv2.0.10

## Confirmed facts

### CLIENT

The uploaded GitHub build log reached `:app:compileDebugKotlin` and failed because `CoreNodeCheckRecord` has a `success` field but the CLIENT UI referenced `truth.auto.connected` four times.

### SERVER

The SERVER workflow stopped before compilation with: `No matching checksum file exists for the selected source ZIP`.

## Engineering changes

### CLIENT CFv2.0.9

- Replaced all four invalid `truth.auto.connected` references with `truth.auto.success`.
- Preserved combined connection truth as automatic-check success OR manual-check success.
- Advanced Android version identity to versionCode 40 and `0.2.7.14-cfv2.0.9-int-build-020a`.
- Advanced embedded CLIENT workflow artifact naming to CFv2.0.9.

### SERVER CFv2.0.10

- Advanced source/application identity to versionCode 13 and versionName 2.0.10.
- Issued a new exact same-basename source/checksum pair:
  - `SERVER_CFv2.0.10_SWRLZ.zip`
  - `SERVER_CFv2.0.10_SWRLZ.sha256`
- Added source-resolution documentation inside the package.
- No SERVER runtime behavior was changed.

## Verification performed

- `truth.auto.connected` absence guard: PASS.
- canonical `truth.auto.success` references present: PASS.
- CLIENT version guard: PASS.
- SERVER version guard: PASS.
- CLIENT ZIP integrity: PASS.
- SERVER ZIP integrity: PASS.
- SHA-256 files generated with exact package basenames.

## Evidence status

- SOURCE IMPLEMENTED
- STATIC VERIFICATION PASS
- BUILD NOT RUN
- RUNTIME NOT TESTED
- WORKFLOW NOT TRIGGERED
- REPOSITORY DOCUMENTATION PUBLISHED
- SOURCE PACKAGES NOT PUBLISHED BY THIS CHECKPOINT

## Exclusions

No protocol, Room schema, registration, trust, identity, Truth Firewall, lifecycle, provider, release, or deployment change was authorized or performed.

## Next evidence gate

Upload each ZIP together with its exact `.sha256` companion, run the CLIENT and SERVER GitHub workflows, and inspect the first causal error if either fails.