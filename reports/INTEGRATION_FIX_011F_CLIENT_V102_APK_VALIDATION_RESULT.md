# INTEGRATION-FIX-011F — CLIENT CFv1.0.2 APK Validation Result

## Result

**PASS — the one-line Ktor import successor compiled and produced a downloadable debug APK artifact.**

## Source identity

- Candidate branch: `checkpoint/client-v101-build-failure-011e`
- Build source commit: `667641948264cc0d5d9b066ad9d42b4102a165b2`
- Candidate ZIP: `SOURCES/CLIENT/CLIENT_CFv1.0.2_SWRLZ.zip`
- Candidate ZIP SHA-256: `e618938d662c9b39dc33a786eca40eeecd4b9675f6558a7bd4a328b5fa5b92c1`
- Approved source change: `import io.ktor.client.statement.request`
- Other archive entry contents changed from canonical v1.0.1: `0`

## Build identity

- Workflow run: `29664342663`
- Workflow job: `88132155117`
- Workflow conclusion: `success`
- Gradle conclusion: `BUILD SUCCESSFUL in 3m 34s`
- Kotlin compile task: `success`
- Android `versionName`: `0.2.7.6-cf8-admin-fallback`
- Android `versionCode`: `30`
- Signing mode: `debug-runner-default`
- Release-artifact commit step: `skipped`

## APK evidence

- Actions artifact ID: `8435320193`
- Artifact name: `CLIENT_CFv1.0.2_SWRLZ_VALIDATION_APK_DOWNLOAD`
- Artifact size: `38,920,370 bytes`
- Artifact archive SHA-256: `42c627d9da904e61c15e78b6dd6aca8fdd13702e859de0c3f21476b136a52316`
- Artifact expiry: `2026-08-17T23:00:38Z`
- APK file: `CLIENT_CFv1.0.2_SWRLZ_VALIDATION_debug.apk`
- APK size: `20,028,470 bytes`
- APK SHA-256: `c706818dc4b16bfded38d3de0274ef4f116974d02e0801f3f450d0c10f8b8a49`
- Bundle file: `BUNDLE_CF7_276_NETPULSE.zip`
- Bundle SHA-256: `e2ecec687ce652aa9e6627ecdde285f8f1c691a74164946eba4039277ea02e9f`

## Build observations

- The prior `Api.kt:723` unresolved-reference failure did not recur.
- The build emitted one existing Java deprecation warning in `SwurlzAccessibilityService.kt:71`.
- Native-library stripping reported that two libraries would be packaged unchanged; the task continued successfully.
- No release, deployment, installation, update-manifest write, or `main` modification occurred.

## Retained evidence

- `BUILD_REQUESTS/INTEGRATION_FIX_011F/build_android_debug.log`
- `BUILD_REQUESTS/INTEGRATION_FIX_011F/GITHUB_ACTIONS_BUILD_SUMMARY.md`
- `reports/INTEGRATION_FIX_011F_CLIENT_V102_SUCCESSOR.md`
- `reports/INTEGRATION_FIX_011F_CLIENT_V102_BUILD_DISPATCH.md`
- This result report
