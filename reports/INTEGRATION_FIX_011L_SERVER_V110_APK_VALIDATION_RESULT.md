# INTEGRATION-FIX-011L — SERVER v1.1.0 Android APK Validation Result

- **Status:** Complete — build success
- **Source checkpoint branch:** `checkpoint/server-presence-registry-011k`
- **Source checkpoint base commit:** `784eb684ff0b7432c7e8d7ca24c9923289d47735`
- **Source candidate:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **Source SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- **Workflow run:** `29692202761`
- **Workflow job:** `88206793131`
- **Trigger PR:** `#14`, closed without merge
- **Gradle task:** `clean assembleDebug`
- **Build duration:** `4m 40s`
- **Task result:** `39 actionable tasks: 38 executed, 1 up-to-date`

## APK evidence

- **Artifact name:** `SERVER_CFv1.1.0_SWRLZ_VALIDATION_APK_DOWNLOAD`
- **Artifact ID:** `8443966000`
- **Artifact size:** `20,305,615 bytes`
- **Artifact archive SHA-256:** `42e3e0a4fd1ee0a779eeba94f2d6362f4fcdb6e231423068d2020820093e113f`
- **Artifact expiry:** `2026-08-18T15:13:05Z`
- **APK filename:** `SERVER_CFv1.1.0_SWRLZ_VALIDATION_debug.apk`
- **APK size:** `21,673,789 bytes`
- **APK SHA-256:** `98c31a7e18309c2df23f76077f132ef3ffe40b227fb7a22ca8df5ea1bb1eb105`
- **Signing mode:** `debug-runner-default`

The downloaded artifact ZIP was checksum-verified against the GitHub Actions artifact digest and passed ZIP integrity validation.

## Android validation result

The exact checksum-gated SERVER candidate successfully completed:

- candidate materialization and SHA verification;
- ZIP integrity verification;
- Android SDK setup;
- KSP processing;
- Kotlin compilation;
- Java compilation;
- Hilt dependency aggregation and Java compilation;
- bytecode transformation;
- dexing;
- resource and manifest processing;
- debug signing validation;
- APK packaging and assembly.

Gradle reported:

```text
BUILD SUCCESSFUL in 4m 40s
39 actionable tasks: 38 executed, 1 up-to-date
```

## Non-fatal warnings

Two Kotlin warnings remain:

1. `NodeRuntime.kt:450` — nullable Java `hostAddress` platform type used as a non-null map key.
2. `RuntimeDiagnostics.kt:52` — nullable `ThreadLocal<SimpleDateFormat>.get()` platform type dereferenced without a safe/non-null assertion.

Neither warning blocked KSP, Hilt, Kotlin/Java compilation, dexing, signing validation, packaging, or APK assembly. They remain reviewable cleanup items and are not represented as fixed by this checkpoint.

## Guard verification

- `main` promotion: **not performed**
- canonical `SOURCES/SERVER` placement: **not performed**
- CLIENT modification: **not performed**
- release artifact commit: **false**
- GitHub release: **not created**
- deployment: **not performed**
- installation: **not performed**
- LAN writes: **not enabled**
- trust/admin/mission authority: **not expanded**
- Truth Firewall: **not weakened**

This APK is a validation artifact, not a stable-signed release artifact.
