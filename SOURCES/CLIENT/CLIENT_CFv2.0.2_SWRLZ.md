# CLIENT CFv2.0.2 SWRLZ Lineage Receipt

**Canonical baseline:** `CLIENT_CFv2.0.1_SWRLZ.zip`  
**Active source:** `CLIENT_CFv2.0.2_SWRLZ.zip`  
**SHA-256:** `95e84eeb74776a1d4fe719678fc0743f11088d3460d9b721abcc20d71b314d0c`  
**Android identity:** `versionCode 33` / `versionName 0.2.7.7-cfv2.0.2-verified-baseline`

## Scope

CLIENT CFv2.0.2 is a version and lineage synchronization checkpoint based on the successfully compiled CLIENT CFv2.0.1 source.

It preserves the CFv2.0.0 Glitch Dragon Glass dual-mode interface and the bounded CFv2.0.1 Compose compilation repair.

CFv2.0.2 does not claim implementation of the proposed Living Glitch Dragon effects. Those remain a separate future checkpoint.

## CFv2.0.1 Compose repair carried forward

CLIENT CFv2.0.0 failed during Kotlin compilation because `ClientShellScreen.kt` explicitly imported:

```kotlin
import androidx.compose.foundation.layout.calculateTopPadding
import androidx.compose.foundation.layout.weight
```

CFv2.0.1 removed those explicit imports while preserving receiver- and scope-resolved usages such as:

```kotlin
WindowInsets.statusBars
    .asPaddingValues()
    .calculateTopPadding()
```

and:

```kotlin
Modifier.weight(...)
```

CFv2.0.2 preserves that repair.

Regression requirements:

- do not reintroduce `androidx.compose.foundation.layout.calculateTopPadding`
- do not reintroduce `androidx.compose.foundation.layout.weight`
- keep `Modifier.weight(...)` inside the appropriate `RowScope` or `ColumnScope`
- resolve `calculateTopPadding()` through the supported `PaddingValues` receiver under the CLIENT dependency graph
- copy design intent between modules, not incompatible import lists

## Identity synchronization

CFv2.0.2 advances:

- `versionCode 32` → `33`
- `versionName 0.2.7.6-cfv2.0.1-compose-import-fix` → `0.2.7.7-cfv2.0.2-verified-baseline`
- source identity to `CLIENT_CFv2.0.2_SWRLZ`
- rollback metadata to identify CFv2.0.1 as the confirmed compiled baseline

## Preserved interface and architecture

CFv2.0.2 preserves:

- default User Mode
- switchable SWRLZ Dev Mode
- Glitch Dragon Glass visual language
- legacy engineering cockpit
- Core, Chat, Missions, Nodes, and Settings surfaces
- Truth Firewall visibility
- local-first behavior
- local-versus-remote identity separation
- Pause, Take Over, approval, and redirect boundaries
- CLIENT-to-NODE_HOST connectivity
- reduced-motion behavior
- CF8 admin fallback and Ktor compatibility repairs

## Non-changes

No intentional change was made to:

- protocols
- trust or pairing semantics
- database schema
- mission authorization behavior
- Truth Firewall behavior
- private-token handling
- NODE_HOST source
- SERVER source

## Verification status

- ZIP integrity: PASS
- ZIP/checksum pairing: PASS
- Compose import regression guard: PASS
- CFv2.0.1 GitHub compilation evidence: confirmed by the owner
- CFv2.0.2 GitHub compilation: pending until separately run
- Device runtime verification: not established by this receipt

Core law: integrate, do not overwrite.
