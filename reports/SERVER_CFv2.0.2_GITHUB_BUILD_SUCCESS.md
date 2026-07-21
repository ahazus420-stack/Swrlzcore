# SERVER CFv2.0.2 GitHub Build Success

## Result

The GitHub Actions workflow successfully compiled the SWURLZER Android application from the canonical SERVER source lane.

- **Visible product identity:** SWURLZER
- **Repository/build transport identity:** SERVER
- **Source ZIP:** `SERVER_CFv2.0.2_SWRLZ.zip`
- **Source SHA-256:** `6978332cfe46a3a1d7154c25fe8f677e48bed2fee24cdf506307cfb830af057a`
- **Android versionCode:** `5`
- **Android versionName:** `2.0.2`
- **Workflow:** `SWRLZ APK Router — Manual / Auto`
- **Workflow file:** `.github/workflows/swrlz-apk-router.yml`
- **Trigger:** push to `main`
- **Commit:** `cf68aaeb216ff4bb52e7f67a277537c3f5cb800e`
- **Run result:** SUCCESS
- **Recorded duration:** 6m 32s
- **Artifact count:** 1

## Repair lineage

SERVER CFv2.0.0 introduced the SWURLZER Glitch Dragon Citadel interface but failed during Kotlin compilation because two `LazyColumn` node lists used:

```kotlin
items(state.nodes, key = NodeEntity::id)
```

`NodeEntity` does not define `id`. Its authoritative Room primary key is:

```kotlin
@PrimaryKey val nodeId: String
```

SERVER CFv2.0.1 repaired both affected call sites to:

```kotlin
items(state.nodes, key = NodeEntity::nodeId)
```

Affected source paths:

- `app/src/main/java/sh/swurlz/nodehost/ui/HostScreen.kt`
- `app/src/main/java/sh/swurlz/nodehost/ui/SwurlzerUserScreen.kt`

SERVER CFv2.0.2 preserved that repair and synchronized the source filename, Android version identity, checksum receipt, and lineage metadata.

## Checksum and archive incident

Two transport failures occurred before the successful build:

1. A source ZIP and checksum receipt did not match, so the resolver failed closed during canonical source verification.
2. A later uploaded byte stream matched its replacement checksum but was not a structurally valid ZIP, so extraction failed with an end-of-central-directory error.

The final CFv2.0.2 pair was regenerated from the valid source, tested for ZIP integrity, and hashed only after final packaging.

Required packaging order carried forward:

1. Finish source changes.
2. Create the final ZIP.
3. Test ZIP structure and extraction.
4. Calculate SHA-256 from the final ZIP bytes.
5. Write the matching checksum receipt.
6. Deliver the ZIP and checksum together without modifying the ZIP afterward.

Checksums must never be regenerated merely to accept unknown or structurally invalid bytes.

## Preserved architecture

The successful build did not require changes to:

- Room schema
- `nodeId` authority
- protocol identifiers or versions
- trust or enrollment contracts
- cryptographic boundaries
- presence-registry authority
- local-versus-remote distinctions
- Truth Firewall behavior
- SERVER source transport naming
- CLIENT source

## Verification boundary

GitHub compilation and artifact generation are confirmed by the successful workflow result and one produced artifact.

Device installation and runtime behavior remain separate evidence and are not claimed by this report.

Core law: integrate, do not overwrite.
