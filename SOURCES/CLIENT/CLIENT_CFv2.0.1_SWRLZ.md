# CLIENT CFv2.0.1 Build Repair and UI Lineage Receipt

## Purpose

This receipt records the bounded repair applied after the CLIENT CFv2.0.0 Glitch Dragon Glass interface overhaul failed during GitHub Actions Kotlin compilation.

It exists so future CLIENT and SERVER interface work can reuse the successful compatibility lesson instead of repeating the same Compose import failure.

## Lineage

- Known-good pre-overhaul compile baseline: `CLIENT_CFv1.0.2_SWRLZ`
- Major interface overhaul source: `CLIENT_CFv2.0.0_SWRLZ`
- Bounded compile repair: `CLIENT_CFv2.0.1_SWRLZ`
- Canonical archive checksum:

```text
5cee9adf5725a75b9e326cda04e145c511467ca3753644e8b90fe66b6af40a8d  CLIENT_CFv2.0.1_SWRLZ.zip
```

## Failure observed in CFv2.0.0

GitHub Actions reached Kotlin compilation and failed in:

```text
android/app/src/main/java/sh/swurlz/core/ui/client/ClientShellScreen.kt
```

The relevant compiler failures were:

```text
Unresolved reference: calculateTopPadding
Cannot access 'RowColumnParentData?.weight': it is internal
```

The terminal `Gradle exit code: 1` was only the workflow-level consequence. The source defect was isolated to two explicit Jetpack Compose layout imports.

## Bounded repair applied in CFv2.0.1

The following imports were removed from `ClientShellScreen.kt`:

```kotlin
import androidx.compose.foundation.layout.calculateTopPadding
import androidx.compose.foundation.layout.weight
```

The existing call sites were intentionally preserved:

```kotlin
WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
Modifier.weight(...)
```

### Why removal was correct

`calculateTopPadding()` resolves through the Compose `PaddingValues` API available at the call site and did not require the unavailable explicit package import.

`Modifier.weight()` is a public scope extension supplied by `RowScope` or `ColumnScope`. The explicit import selected or exposed an internal parent-data implementation path and caused the compiler-access error. Allowing Kotlin to resolve the extension from the active layout scope restored the intended public API usage.

## Verification status

- Archive integrity and checksum generation were completed for CFv2.0.1.
- The user subsequently reported that the CFv2.0.1 GitHub Actions build completed successfully and installed the resulting APK.
- Runtime screenshots showed both surfaces remained available:
  - the preserved engineering/developer interface;
  - the new Glitch Dragon Glass user interface with Core, Chat, Missions, Nodes, and Settings navigation.

This receipt records the successful result as user-reported runtime evidence. The authoritative workflow run and APK artifact remain the stronger build evidence when separately preserved in the repository.

## SERVER interface-overhaul compatibility rule

When the SERVER receives a comparable Compose UI redesign, apply these checks before the first GitHub build:

1. Do not copy explicit imports for scope-bound Compose modifiers merely because a CLIENT file used the same call syntax.
2. For `Modifier.weight()`, verify every call occurs inside an active `RowScope` or `ColumnScope` and prefer scope resolution over an explicit `foundation.layout.weight` import.
3. For padding helpers such as `calculateTopPadding()`, verify the actual receiver type and Compose BOM/API version before adding an explicit import.
4. Compare imported symbols against the SERVER module's own Compose BOM and dependency graph; visual parity does not guarantee binary or source compatibility.
5. Run a Kotlin compile checkpoint before packaging or promotion when possible:

```text
./gradlew :app:compileDebugKotlin
```

6. Preserve the previous known-good SERVER UI and behavior as the rollback baseline. Integrate the new interface; do not overwrite protocol, trust, discovery, Truth Firewall, local-first, or operator-control behavior.

## Scope boundary

This repair changed only the invalid imports required to restore compilation. It did not authorize or intentionally alter:

- protocol behavior;
- node identity or trust semantics;
- local-versus-remote distinctions;
- Truth Firewall behavior;
- permissions or sharing approvals;
- mission execution behavior;
- legacy developer-interface availability;
- release or deployment policy.

## Reuse summary

For future Compose redesigns, the key lesson is:

```text
Copy the design intent and component behavior, not every import line.
Scope-bound Compose APIs must resolve in the destination module's actual scope and dependency version.
```
