# SWRLZ-Core 2.7.6 CF4 Signing + Reconnect Patch Report

## Identity

- versionName: `0.2.7.6-cf4-sign-reconnect`
- versionCode: `26`
- Patch: `2.7.6-CF4-SIGNING-RECONNECT`
- Source: `SRC_2.7.6_CF4_SIGN_RECONNECT.zip`

## Objectives

1. Add Connection Memory / Last-Good Node Auto-Reconnect.
2. Add stable dev signing readiness so install-over-old can work once GitHub Secrets are configured.
3. Keep mobile-readable naming conventions for source, report, SHA, APK, and bundle files.

## Connection Memory behavior

- Saves last-good node only after a valid `/discovery/signature` response.
- Auto reconnect checks saved Core Node first, then last-good fallback.
- Auto reconnect toggle is saved and defaults ON.
- Last-good node can be used manually or cleared without deleting Core Node config.

## Signing behavior

- Adds `scripts/generate_swrlz_dev_signing_key.sh` to create a stable local dev keystore.
- Adds `docs/SIGN_2.7.6_STABLE_DEV_KEY.md` with the required GitHub Secrets.
- Adds Gradle support for `SWRLZ_DEV_KEYSTORE_FILE`, `SWRLZ_DEV_KEYSTORE_PASSWORD`, `SWRLZ_DEV_KEY_ALIAS`, and `SWRLZ_DEV_KEY_PASSWORD`.
- Install-over-old still requires the same stable signing certificate across builds.

## Output naming target

- Source: `SRC_2.7.6_CF4_SIGN_RECONNECT.zip`
- SHA: `SHA_2.7.6_CF4_SIGN_RECONNECT.txt`
- Report: `REPORT_2.7.6_CF4_SIGN_RECONNECT.md`
- APK target: `APK_2.7.6_CF4_SIGN_RECONNECT_DEBUG.apk`
- Bundle target: `BUNDLE_2.7.6_CF4_SIGN_RECONNECT.zip`
