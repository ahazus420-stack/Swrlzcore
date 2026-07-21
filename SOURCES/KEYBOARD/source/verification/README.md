# Verification

`verify-static.py` validates identity, manifest/service declarations, offline boundaries, key-operation seams, and prohibited capabilities.

`EditorContextClassifierVerification.kt` is compiled with the standalone Kotlin compiler together with the pure policy source. It verifies ordinary, protected, payment, OTP, password, and fail-closed unknown contexts without Android SDK dependencies.

These checks are source verification only. They do not build an APK and do not claim Android runtime behavior.
