# INTEGRATION-FIX-011N — Installed NODE_HOST Package and Signer Capture Plan

- **Status:** Complete — read-only capture plan prepared
- **Checkpoint:** `INTEGRATION-FIX-011N`
- **Installed package target:** `sh.swrlz.nodehost`
- **Candidate APK:** `SERVER_CFv1.1.0_SWRLZ_VALIDATION_debug.apk`
- **Candidate APK SHA-256:** `98c31a7e18309c2df23f76077f132ef3ffe40b227fb7a22ca8df5ea1bb1eb105`

## Candidate signer baseline

The v1.1.0 APK signing block contains one signer certificate:

- Subject: `CN=Android Debug, O=Android, C=US`
- Issuer: `CN=Android Debug, O=Android, C=US`
- Certificate serial: `1`
- Certificate SHA-256: `ADA947A8C3637ABCE8D015FD068C9F185DC3C8BA2F38A073810EEB595FB13CA1`
- Certificate SHA-1: `C37B1198D4A8BFB616120F34E61F4F379384BE95`

The SHA-256 certificate fingerprint is the update-compatibility comparison value.

## Preferred read-only capture method

Use Android Debug Bridge from a trusted computer with USB debugging already enabled or deliberately enabled by the user. These commands only query package metadata and copy the installed APK; they do not install, uninstall, clear, start, stop, or modify the package.

### 1. Confirm device connection

```bash
adb devices -l
```

Expected: one authorized device. Stop if the device is unauthorized, unexpected, or duplicated.

### 2. Capture installed package metadata

```bash
adb shell dumpsys package sh.swrlz.nodehost > installed-node-host-dumpsys.txt
adb shell dumpsys package sh.swrlz.nodehost | grep -E 'versionName=|versionCode=|codePath=|firstInstallTime=|lastUpdateTime='
```

Also capture the APK path:

```bash
adb shell pm path sh.swrlz.nodehost | tee installed-node-host-pm-path.txt
```

Expected output resembles:

```text
package:/data/app/.../base.apk
```

If multiple split APK paths are returned, the first `base.apk` path is the signer source. Preserve the complete list for provenance.

### 3. Pull the installed base APK without modifying app data

```bash
APK_PATH="$(adb shell pm path sh.swrlz.nodehost | sed -n 's/^package://p' | tr -d '\r' | grep '/base.apk$' | head -n 1)"
test -n "$APK_PATH"
adb pull "$APK_PATH" installed-SWRLZ_NODE_HOST-base.apk
sha256sum installed-SWRLZ_NODE_HOST-base.apk | tee installed-SWRLZ_NODE_HOST-base.apk.sha256
```

`adb pull` copies the package file only. It does not read or alter private app data.

### 4. Extract installed signer certificate fingerprint

Preferred Android SDK command:

```bash
apksigner verify --verbose --print-certs installed-SWRLZ_NODE_HOST-base.apk \
  | tee installed-SWRLZ_NODE_HOST-apksigner.txt
```

Record the line labeled:

```text
Signer #1 certificate SHA-256 digest:
```

Normalize by removing colons and converting to uppercase.

Fallback when `apksigner` is unavailable: use another trusted APK-signature parser that reads APK Signature Scheme v2/v3 blocks. Do not use `keytool -printcert -jarfile` as the sole method because modern APKs may be signed only with v2/v3 and appear as “Not a signed jar file.”

### 5. Compare against the candidate signer

Candidate signer SHA-256:

```text
ADA947A8C3637ABCE8D015FD068C9F185DC3C8BA2F38A073810EEB595FB13CA1
```

Decision rule:

- **Exact match:** signer continuity is proven for this certificate. Continue only after confirming candidate `versionCode` is greater than the installed `versionCode` and after a separate installation authorization.
- **Different fingerprint:** in-place update is not compatible. Do not attempt `adb install -r`, package-installer update, or uninstall as part of this checkpoint.
- **No fingerprint / ambiguous output:** update readiness remains unproven. Preserve evidence and stop.
- **Multiple current signers or lineage:** inspect the complete signer lineage. Do not treat a partial match as sufficient without Android signing-lineage evidence.

## Optional package-only metadata checks

These remain read-only:

```bash
adb shell cmd package list packages --show-versioncode | grep '^package:sh.swrlz.nodehost '
adb shell dumpsys package sh.swrlz.nodehost | sed -n '/Package \[sh.swrlz.nodehost\]/,/User 0:/p'
```

Do not rely on package name or version metadata as proof of signer compatibility.

## Android-only fallback

Without ADB, ordinary Android settings can confirm the app label and sometimes version name, but normally cannot expose the signing certificate SHA-256 fingerprint. A file-manager copy of `/data/app/.../base.apk` is generally unavailable without elevated access. Therefore the safe no-root path is ADB pull from an authorized computer.

Do not root the device, use accessibility automation, install a package-management helper, or grant broad storage/debug privileges solely for this comparison.

## Evidence package to return for comparison

Return these text outputs or screenshots, with personal paths redacted if desired:

1. `adb devices -l` result showing one authorized device;
2. version name and version code lines;
3. `pm path` output;
4. installed base APK SHA-256;
5. complete `apksigner --print-certs` output.

Do not share pairing tokens, group keys, device keys, Android serial numbers, or unrelated package listings.

## Guards

- Device controlled: **no**
- APK installed/uninstalled: **no**
- App data read or cleared: **no**
- Source modified: **no**
- APK built: **no**
- Workflow triggered: **no**
- `main` modified: **no**
- Release/deployment: **no**

## Next decision

After the installed fingerprint is captured:

- exact signer match + valid version-code increase → eligible for a separately approved in-place update test;
- signer mismatch → stable-key recovery or deliberate fresh-install migration is required;
- uncertain evidence → no installation approval.