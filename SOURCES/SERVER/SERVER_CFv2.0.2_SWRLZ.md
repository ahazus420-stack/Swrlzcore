# SERVER CFv2.0.2 SWRLZ Lineage Receipt

**Visible product identity:** SWURLZER  
**Repository/build transport identity:** SERVER  
**Canonical baseline:** `SERVER_CFv2.0.1_SWRLZ.zip`  
**Active source:** `SERVER_CFv2.0.2_SWRLZ.zip`  
**SHA-256:** `6978332cfe46a3a1d7154c25fe8f677e48bed2fee24cdf506307cfb830af057a`  
**Android identity:** `versionCode 5` / `versionName 2.0.2`

## Scope

CFv2.0.2 is the version-synchronized, checksum-paired SERVER source checkpoint that successfully compiled through the owner's GitHub Actions workflow.

Applied source-package changes from CFv2.0.1:

- `app/build.gradle.kts`: `versionCode 4` → `5`
- `app/build.gradle.kts`: `versionName 2.0.1` → `2.0.2`
- active source identity advanced to `SERVER_CFv2.0.2_SWRLZ`
- release notes and static verifier advanced to CFv2.0.2
- final ZIP regenerated from valid source
- final SHA-256 generated only after ZIP integrity verification

## Preserved Kotlin repair

CFv2.0.0 used the invalid property reference:

```kotlin
items(state.nodes, key = NodeEntity::id)
```

The authoritative Room primary key is:

```kotlin
@PrimaryKey val nodeId: String
```

CFv2.0.1 repaired both node lists, and CFv2.0.2 preserves:

```kotlin
items(state.nodes, key = NodeEntity::nodeId)
```

in:

- `app/src/main/java/sh/swurlz/nodehost/ui/HostScreen.kt`
- `app/src/main/java/sh/swurlz/nodehost/ui/SwurlzerUserScreen.kt`

Regression requirements:

- do not reintroduce `NodeEntity::id`
- do not add a duplicate `id` field
- do not rename `nodeId`
- do not key node rows by list index, display label, URL, connection state, trust state, or last-seen time
- key node-specific animation and expansion state by `node.nodeId`

## Source integrity lineage

A CFv2.0.1 upload first failed because the ZIP bytes did not match the declared checksum. A later byte stream matched its replacement checksum but was not a valid ZIP archive.

CFv2.0.2 corrected the artifact process by:

1. restoring the valid source archive
2. updating the internal Android version
3. creating a fresh final ZIP
4. testing the ZIP central directory and archive entries
5. calculating SHA-256 from the final bytes
6. writing the matching receipt
7. uploading the pair without modifying the ZIP afterward

The checksum is an integrity assertion, not a mechanism for approving unknown bytes.

## GitHub build evidence

- Workflow: `SWRLZ APK Router — Manual / Auto`
- Workflow file: `.github/workflows/swrlz-apk-router.yml`
- Trigger: push to `main`
- Commit: `cf68aaeb216ff4bb52e7f67a277537c3f5cb800e`
- Result: SUCCESS
- Duration shown by GitHub: 6m 32s
- Artifact count: 1

Detailed evidence:

`reports/SERVER_CFv2.0.2_GITHUB_BUILD_SUCCESS.md`

## Preserved boundaries

No intentional change was made to:

- Room schema
- node identity semantics
- protocol identifiers or protocol versions
- trust, enrollment, or revocation contracts
- cryptographic storage or proof boundaries
- presence-registry authority
- Truth Firewall behavior
- local-versus-remote identity separation
- visible SWURLZER interface behavior
- SERVER transport prefix
- CLIENT source

## Verification status

- Static source guard: PASS
- ZIP integrity: PASS
- ZIP/checksum pairing: PASS
- GitHub compilation: PASS
- GitHub artifact generation: PASS
- Device installation and runtime verification: not established by this receipt

Core law: integrate, do not overwrite.
