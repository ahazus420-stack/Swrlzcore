# SERVER CFv2.0.38 Bubble Namespace Build Repair

**Recorded:** 2026-07-24  
**Status:** SERVER CFv2.0.37 failure log-verified; CFv2.0.38 source and ZIP/SHA repair prepared; clean GitHub build and device acceptance pending.

## Scope

This checkpoint records the failure of the first separate SWURLZER SERVER bubble-interface build and the bounded repair prepared as SERVER `CFv2.0.38`.

No protocol, persistence, trust, registration, mission, lifecycle, or fusion-interface behavior is changed by this repair.

## SERVER CFv2.0.37 failure evidence

The APK Router successfully:

- resolved the SERVER package;
- verified its supplied checksum;
- extracted the Android project;
- completed Android resource and manifest processing;
- reached `:app:compileDebugKotlin`.

Kotlin compilation then failed in the SERVER bubble source. The first authoritative diagnostics included:

```text
ServerBubbleActivity.kt:84:54 Unresolved reference 'BubbleStateStore'
ServerBubbleActivity.kt:85:51 Unresolved reference 'BubbleStateStore'
ServerBubbleActivity.kt:92:35 Unresolved reference 'BubbleSection'
ServerBubbleActivity.kt:100:32 Unresolved reference 'BubbleLayoutMode'
ServerBubbleActivity.kt:103:25 Unresolved reference 'ServerBubbleController'
ServerBubbleController.kt:30:44 Unresolved reference 'ServerBubbleActivity'
```

Later type-inference failures, non-exhaustive `when` diagnostics, and composable-context errors were cascades from the unresolved bubble namespace.

## Root cause

`ServerBubbleActivity.kt` declared:

```kotlin
package sh.swurlz.nodehost.bubble
```

The activity's directory and all related types use:

```kotlin
package sh.swrlz.nodehost.bubble
```

The additional `u` created a separate Kotlin namespace. The filesystem path looked correct, but Kotlin symbol resolution follows the package declaration, not the directory name.

The activity also contained an explicit:

```kotlin
import androidx.compose.foundation.layout.weight
```

That import is unnecessary for contextual `Modifier.weight(...)` calls inside `RowScope` and `ColumnScope`, and the active CLIENT toolchain had already demonstrated that the explicit import can resolve to an inaccessible internal parent-data property. It was therefore removed preventively.

## SERVER CFv2.0.38 repair

The prepared repair:

- changes the activity package to `sh.swrlz.nodehost.bubble`;
- restores shared-package access to `BubbleStateStore`, `BubbleSection`, `BubbleLayoutMode`, and `ServerBubbleController`;
- removes the explicit Compose `weight` import;
- preserves contextual `Modifier.weight(...)` calls;
- preserves the separate violet SWURLZER operations interface;
- preserves the visible SERVER build footer;
- advances Android `versionCode` from `38` to `39`;
- sets `versionName` to `2.0.38-server-bubble-build-repair-v1`.

Prepared package:

```text
SERVER_CFv2.0.38_SWRLZ.zip
SHA-256: 61eacf4d0012e8cca366b16035a917fe4b678c13481c0baada7c0903eed9aae6
```

## Static and package evidence

- All Kotlin files in the SERVER bubble directory now declare `sh.swrlz.nodehost.bubble`.
- `ServerBubbleActivity`, `BubbleStateStore`, `BubbleSection`, `BubbleLayoutMode`, and `ServerBubbleController` share one package.
- No explicit `androidx.compose.foundation.layout.weight` import remains in SERVER source.
- The ZIP passed compressed-data integrity testing.
- The final ZIP digest matches the sibling SHA-256 receipt.

A local Gradle compile was attempted, but the isolated execution container could not download Gradle from `services.gradle.org`. This is an environment limitation rather than a new source diagnostic. GitHub Actions remains the authoritative compilation gate.

## Evidence classification

- SERVER CFv2.0.37 compiler failure: log-verified.
- Namespace root cause: source and log-verified.
- SERVER CFv2.0.38 source repair: static/source-verified.
- SERVER CFv2.0.38 ZIP/SHA: package-verified locally.
- Clean GitHub build: pending.
- APK artifact: pending.
- Installation and bubble rendering: pending.

## Next evidence gate

1. Upload SERVER `CFv2.0.38` ZIP and SHA through Forge.
2. Confirm Source Package Integrity accepts the package without a manifest.
3. Confirm APK Router passes `:app:compileDebugKotlin` and produces a SERVER APK artifact.
4. Install over a prior SERVER build signed with the same certificate.
5. Verify the bubble footer displays `SERVER · CFv2.0.38` and the separate SERVER interface renders correctly.
