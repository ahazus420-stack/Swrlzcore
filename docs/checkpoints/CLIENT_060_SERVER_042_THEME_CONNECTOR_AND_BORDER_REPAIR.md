# CLIENT CFv2.0.60 + SERVER CFv2.0.42 — Theme Connector and Bubble Border Repair

**Recorded:** 2026-07-24  
**Status:** CLIENT CFv2.0.58 GitHub build confirmed successful by device report; CLIENT CFv2.0.60 and SERVER CFv2.0.42 source/static and ZIP/SHA verification complete; new GitHub builds and device acceptance pending.

## Runtime evidence received

CLIENT `CFv2.0.58` completed the APK Router successfully and produced a usable installation. Device screenshots confirm:

- Dragon Kamileon Theme Armor rendering in the CLIENT cockpit;
- Kamileon CLIENT bubble identity;
- Forge workflow observation inside the Android bubble;
- visible `CLIENT · CFv2.0.58 / VC 85` provenance;
- Forge upload-success notification with the current cyan dragon identity.

The device also reported that switching Theme Armor caused Forge to present a new GitHub device-code flow.

## CLIENT GitHub continuity diagnosis

The saved Android Keystore token and the visible account login were initialized separately. After a Theme Armor launcher/task refresh:

- the runtime token could still exist;
- `connectedLogin` started blank;
- asynchronous token validation had not completed;
- the UI rendered the disconnected OAuth controls during that validation window.

This was a false-disconnected presentation and allowed unnecessary reauthorization before the saved token could prove itself.

## CLIENT CFv2.0.60 repair

CLIENT `CFv2.0.60`:

- initializes the visible login from the persisted connection profile whenever a runtime token exists;
- treats saved-token presence as connected/pending-validation, not disconnected;
- shows an explicit saved-token validation state;
- hides OAuth device-code and manual-token controls while a saved token exists;
- retains the credential on temporary validation failure;
- continues to require approval for deliberate disconnect;
- makes `REPOSITORY TARGET` and `SOURCE PACKAGE MATCHING` collapsible;
- defaults both panels collapsed for the compact bubble cockpit;
- persists each panel's expanded/collapsed state.

Theme Armor and launcher identity remain presentation-only and cannot clear GitHub authority.

## SERVER CFv2.0.40 log diagnosis

The supplied APK Router log reached `:app:compileDebugKotlin` and failed at:

```text
ServerBubbleActivity.kt:176:30 Unresolved reference 'border'.
```

The SERVER bubble uses `Modifier.border(...)`, but `androidx.compose.foundation.border` was not imported.

The earlier `CFv2.0.41` preflight corrected real configuration risks—Activity Compose API availability and Kotlin 2.x Compose compiler authority—but did not include this missing import. Therefore `CFv2.0.41` would still encounter the logged compiler blocker.

## SERVER CFv2.0.42 repair

SERVER `CFv2.0.42` adds:

```kotlin
import androidx.compose.foundation.border
```

It preserves:

- Activity Compose `1.9.2`;
- Kotlin/Compose `2.0.20` single compiler authority;
- full SWURLZER Forge capability;
- Dragon Kamileon Theme Armor;
- User Mode, Developer Mode, and bubble Forge access.

## Package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.60_SWRLZ.zip
versionCode: 87
versionName: 2.0.60-theme-connector-collapse-v1
SHA-256: da0115e02dffb34d42bff9c31a83c4f0f6b44ba321c7372011babecf0a8a27cf
```

### SERVER

```text
Package: SERVER_CFv2.0.42_SWRLZ.zip
versionCode: 43
versionName: 2.0.42-server-bubble-border-import-v1
SHA-256: 83ba88ba4921c9bd3afbe044340feca280ca2bf8e605e550b7420606b52a4322
```

Both final archives passed compressed-data integrity testing and match their sibling SHA-256 receipts.

## Evidence classification

- CLIENT CFv2.0.58 build success and visual behavior: device-reported with screenshots.
- Theme-switch false-disconnected root cause: source-verified and consistent with device behavior.
- CLIENT CFv2.0.60 continuity and collapsible-panel changes: source/static verified.
- SERVER CFv2.0.40 missing-border failure: workflow-log verified.
- SERVER CFv2.0.42 import repair: source/static verified.
- final ZIP/SHA pairs: locally package verified.
- clean CLIENT CFv2.0.60 and SERVER CFv2.0.42 GitHub builds: pending.

## Acceptance gate

1. Build and install CLIENT `CFv2.0.60` with the same signing certificate.
2. Connect GitHub once, switch every Theme Armor family and display variant, and confirm no new device code appears.
3. Confirm saved-token validation is visible during any task refresh and that temporary network failure does not expose reauthorization controls.
4. Confirm Repository Target and Source Package Matching expand, collapse, and remember state.
5. Build SERVER `CFv2.0.42` and confirm Kotlin compilation passes beyond `ServerBubbleActivity.kt:176`.
6. Install SERVER and validate Forge from User Mode, Developer Mode, and the bubble.
