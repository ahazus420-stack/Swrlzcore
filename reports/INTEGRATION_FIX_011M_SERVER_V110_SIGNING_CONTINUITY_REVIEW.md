# INTEGRATION-FIX-011M — SERVER v1.1.0 Signing Continuity and Safe Installation Readiness

- **Status:** Complete — in-place update readiness not proven
- **Checkpoint:** `INTEGRATION-FIX-011M`
- **Reviewed candidate:** `SERVER_CFv1.1.0_SWRLZ_VALIDATION_debug.apk`
- **Candidate APK SHA-256:** `98c31a7e18309c2df23f76077f132ef3ffe40b227fb7a22ca8df5ea1bb1eb105`
- **Candidate signing mode:** `debug-runner-default`
- **Candidate source:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **Candidate source SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`

## Confirmed package/version lineage

- Application ID remains `sh.swrlz.nodehost`.
- Canonical SERVER v1.0.3 source declares `versionCode = 2` and `versionName = "1.0.3"`.
- The v1.1.0 candidate identifies itself as version `1.1.0` and is intended as a successor, not a parallel package.

## Signing evidence

The retained repository SERVER artifact provenance records `assembleDebug` / debug variant. The v1.1.0 validation provenance also records `clean assembleDebug` and `debug-runner-default` signing.

No repository evidence records:

- a stable SERVER signing keystore;
- a stable signing certificate SHA-256 fingerprint;
- proof that v1.0.3 and v1.1.0 were signed by the same private key;
- an Android signing lineage permitting key rotation.

A debug build from a separate GitHub-hosted runner is not sufficient evidence of signing continuity. Matching package name and higher version metadata do not bypass Android's signer-equality requirement.

## Readiness decision

**Do not attempt an in-place update with the current v1.1.0 validation APK.**

Safe update installation is authorized only after one of these is proven:

1. the installed v1.0.3 APK signing certificate fingerprint exactly matches the v1.1.0 APK signer; or
2. a stable SERVER signing key that matches the installed app is recovered and used for a separately approved successor build; or
3. the installed app is deliberately backed up/uninstalled and v1.1.0 is installed as a fresh trust reset under separately approved migration instructions.

The present validation APK may be useful on a separate test device or isolated Android profile, but it is not approved as an update to the active NODE_HOST installation.

## Data continuity impact

Because the package ID is unchanged, an Android signer mismatch normally prevents update installation. The usual workaround is uninstalling the existing package, which deletes app-private data unless separately backed up through a supported mechanism.

Potentially affected NODE_HOST-local state includes:

- pairing token and pairing configuration;
- NODE_HOST identity/install state;
- local settings and diagnostics;
- future presence registry data after v1.1.0 is installed and used.

CLIENT-local identity is separate and is not automatically preserved by SERVER app-data removal.

## Guards

- Source modified: **no**
- APK built: **no**
- Workflow triggered: **no**
- `main` modified: **no**
- APK installed/uninstalled: **no**
- Release/deployment: **no**
- Pairing/trust/mission authority changed: **no**

## Smallest safe next action

Capture the installed NODE_HOST package certificate fingerprint and version metadata from the Android device through a read-only diagnostic method, then compare it with a locally extracted v1.1.0 signer fingerprint. If direct extraction cannot be completed reliably, establish a stable signing lane and treat migration as a fresh-install checkpoint rather than claiming update compatibility.
