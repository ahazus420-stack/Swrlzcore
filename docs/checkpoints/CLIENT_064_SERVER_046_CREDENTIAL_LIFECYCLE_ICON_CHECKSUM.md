# CLIENT CFv2.0.64 + SERVER CFv2.0.46 — Credential Lifecycle, Launcher Identity, and Automatic Checksum

**Recorded:** 2026-07-24  
**Status:** Source/static implementation and final ZIP/SHA verification complete; GitHub APK builds and device acceptance pending.

## Runtime findings

Device testing established three separate facts:

1. repeated CLIENT Forge uploads now create distinct confirmed commits and report success through the bubble and Android notification;
2. reconnecting GitHub temporarily clears `401 Bad credentials`, showing the source package and repository path were not the cause;
3. the installed SERVER still showed the stale themed launcher identity, and checksum selection could still expose a manual `LOCATE SHA-256` action.

## Credential lifecycle root cause

The prior Forges stored one access token but did not retain device-flow refresh-token or expiration metadata. Repeated authorization could therefore leave an older OAuth token stored after GitHub revoked or superseded it, while GitHub App user tokens could expire without a refresh path.

Manual token fallback also accepted pasted Authorization prefixes and embedded whitespace literally.

## Credential lifecycle repair

CLIENT CFv2.0.64 and SERVER CFv2.0.46 now:

- normalize manual and restored tokens before validation, storage, or request-header creation;
- classify token kind by non-secret prefix only;
- parse device-flow `refresh_token`, `expires_in`, `refresh_token_expires_in`, and `token_type` fields when supplied;
- store access and refresh tokens plus expiry metadata in Android Keystore-backed encrypted preferences;
- validate one immutable token snapshot against `/user` and the configured repository branch before source streaming;
- refresh GitHub App user tokens before expiry;
- refresh and retry one upload once when a refreshable credential receives 401 during transfer;
- retire an explicitly rejected credential instead of silently reusing it;
- preserve credentials during network and other non-authoritative validation failures;
- record only credential kind and refresh outcome in redacted diagnostics.

CLIENT also carries refresh metadata through its eligible encrypted Android backup/device-transfer envelope.

## Automatic checksum receipt

Both Forges preserve exact readable sibling lookup. When Android grants the ZIP but withholds sibling access, Forge now:

1. derives `<base>.sha256` from `<base>.zip`;
2. hashes the selected ZIP locally;
3. creates a canonical receipt containing the digest and exact ZIP filename;
4. stages that receipt automatically beside the ZIP for repository upload and validation.

`LOCATE SHA-256` remains only as an exceptional recovery action if both readable sibling resolution and local receipt creation fail. It is no longer the normal permission fallback.

## SERVER launcher identity

SERVER CFv2.0.46 preserves the CFv2.0.45 launcher repair:

- all adaptive monochrome layers use their matching SWURLZER dragon foreground instead of the retired generic crystal symbol;
- launcher alias component names are versioned (`*V2`) so the Android launcher cannot keep resolving the stale component cache;
- Theme Identity Manager targets those new aliases;
- default, Dragon Kamileon, Original Core, Glitch Neon, Pharaoh Emerald, and Void Jester families remain distinct.

## Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.64_SWRLZ.zip
versionCode: 91
versionName: 2.0.64-credential-lifecycle-preflight-v1
SHA-256: ae72f31d3ce3a01e765294825996b61ad0e486d0a4845d63bacdf9270c155bc5
```

### SERVER

```text
Package: SERVER_CFv2.0.46_SWRLZ.zip
versionCode: 47
versionName: 2.0.46-credential-lifecycle-preflight-v1
SHA-256: 9d1a08c7afa7e16ef09da70ca82a8c1dfef7b350f5845539e96564e866acb533
```

Both final archives passed compressed-data integrity testing and match their sibling SHA-256 receipts.

## Static evidence

- Kotlin PSI syntax parsing passed for all 123 Kotlin and Gradle Kotlin files across both packages.
- All 45 XML resources parse.
- All 199 PNG/JPEG/WebP resources open and verify.
- Device-flow refresh fields are parsed and securely persisted.
- Both upload paths emit `AUTH_PREFLIGHT` before `BLOB_UPLOAD`.
- Both upload paths include one bounded `AUTH_REFRESH_RETRY` path.
- Explicit GitHub 401 rejection invalidates the stored credential state.
- SERVER launcher aliases and Theme Identity Manager use the versioned V2 components.
- SERVER adaptive monochrome layers match their selected dragon foregrounds.
- Both Forges stage a readable sibling receipt or a generated canonical receipt automatically.

## Evidence classification

- repeated upload and success notification behavior: device-verified;
- recurring 401 and temporary reconnect recovery: device/log-reported;
- credential lifecycle root cause and repairs: source/static verified;
- SERVER launcher and checksum repairs: source/static verified;
- final ZIP/SHA pairs: locally package verified;
- clean CLIENT CFv2.0.64 and SERVER CFv2.0.46 GitHub builds: pending;
- automatic refresh, app-drawer icon replacement, and generated receipt behavior: pending device acceptance.

## Acceptance gate

1. Build and install CLIENT CFv2.0.64.
2. Reconnect GitHub once and confirm `AUTH_PREFLIGHT` occurs before transfer.
3. Confirm no repeated code/token prompt after restart or Theme Armor changes.
4. Confirm a rejected token stops before streaming and exposes reconnect controls.
5. Confirm a refreshable GitHub App token rotates without another device code.
6. Confirm ZIP selection automatically stages either the real sibling SHA or a generated receipt.
7. Build and install SERVER CFv2.0.46.
8. Confirm the app drawer uses the selected SWURLZER dragon icon rather than the stale purple fallback.
9. Repeat credential and checksum tests through SERVER User Mode, Developer Mode, and bubble Forge.
