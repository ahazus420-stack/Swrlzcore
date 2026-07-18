# SWRLZ-KBD-SCAF-VAL-001 — Scaffold Compatibility and Compile Validation

## Objective

Validate the non-functional keyboard scaffold against the canonical SWRLZ CLIENT Android toolchain and record exact compatibility status without adding functional keyboard behavior.

## Status

PARTIAL

## Confirmed repository facts

- Canonical CLIENT build evidence reports Java 17.
- Canonical CLIENT build evidence reports Gradle 8.7.
- Canonical CLIENT build evidence reports Kotlin 1.9.22 in the Gradle runtime.
- Canonical CLIENT build evidence shows a successful `clean assembleDebug` build.
- Installed Android SDK evidence includes Android platform 35 and build-tools 35.x.
- The scaffold originally declared AGP 8.7.3 and Kotlin Android plugin 2.0.21.

## Reconciliation decision

The scaffold Kotlin Android plugin is aligned downward to `1.9.22` to match the canonical CLIENT evidence and avoid introducing a Kotlin 2.x migration in this checkpoint.

AGP `8.7.3`, Java 17, compileSdk 35, targetSdk 35, and minSdk 26 remain provisional because the available canonical evidence confirms Gradle 8.7, Java 17, and SDK availability but does not expose the canonical CLIENT's exact AGP, compileSdk, targetSdk, or minSdk declarations.

## Files inspected

- `RELEASES/CLIENT_CFv1.0.0_SWRLZ_APK/BUILD_PROVENANCE_REPORT.md`
- `core-swrlz-board/keyboard/build.gradle.kts`
- `core-swrlz-board/keyboard/app/build.gradle.kts`
- `core-swrlz-board/keyboard/settings.gradle.kts`
- `core-swrlz-board/keyboard/app/src/main/AndroidManifest.xml`

## Files changed

- `core-swrlz-board/keyboard/build.gradle.kts`
- `core-swrlz-board/docs/checkpoints/SWRLZ_KBD_SCAF_VAL_001_EVIDENCE.md`

## Validation result

- Static compatibility review: PASS for Java 17 and Gradle 8.7 alignment.
- Kotlin plugin reconciliation: PASS; scaffold changed from 2.0.21 to 1.9.22.
- Android Gradle configuration execution: NOT RUN.
- Kotlin compilation: NOT RUN.
- APK build: NOT RUN.

## Known limitations

- No Gradle wrapper is present in the isolated keyboard scaffold.
- No Android SDK or Gradle process was executed through the GitHub connector.
- Exact canonical CLIENT AGP and SDK declarations remain unconfirmed from available repository text evidence.
- Compile safety is therefore not proven.

## Recommended next checkpoint

`SWRLZ-KBD-SCAF-BUILD-001` — add a bounded Gradle wrapper/build-validation lane or validate locally against an extracted canonical CLIENT toolchain without implementing typing behavior.

## Approval required to continue

- **Waiting for approval to:** perform bounded local or CI-neutral configuration/build validation of the keyboard scaffold.
- **Approval would authorize:** adding only build-support files needed for validation, running configuration/compile checks, and making scaffold-only repairs.
- **Approval would not authorize:** functional typing, CLIENT enrollment, Binder implementation, source-ZIP promotion, workflow dispatch, merge to main, release, or deployment.
- **Expected result:** a compile-verified non-functional scaffold or an evidence-backed first actionable blocker.
- **Approval phrase:** `APPROVE SWRLZ-KBD-SCAF-BUILD-001 — RUN BOUNDED KEYBOARD SCAFFOLD BUILD VALIDATION ONLY`

STOP — Current checkpoint ended. Await explicit approval before continuing.
