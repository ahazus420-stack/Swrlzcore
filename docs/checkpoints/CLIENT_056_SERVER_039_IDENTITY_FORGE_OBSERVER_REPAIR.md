# CLIENT CFv2.0.56 + SERVER CFv2.0.39 Stabilization Repair

**Recorded:** 2026-07-24  
**Status:** Source implementation and ZIP/SHA verification complete; GitHub APK compilation and device acceptance pending.

## Scope

This checkpoint records a coordinated CFv2.0.x stabilization pass covering:

1. the CLIENT `CFv2.0.55` Kotlin build failure;
2. duplicate Source Package Integrity workflow runs;
3. stale SERVER launcher and Theme Armor icon families;
4. bubble icons that did not follow interface-theme changes;
5. delayed and visually noisy Forge workflow observation;
6. missing CLIENT and SERVER version identity on the main application pages.

It preserves the successfully rendered separate CLIENT and SERVER bubble interfaces and does not begin the fused SWURVER interface overhaul.

## 1. CLIENT CFv2.0.55 build failure

### Log-verified compiler diagnostic

The APK Router reached `:app:compileDebugKotlin` and failed with:

```text
ForgeBuildWatchStore.kt:71:50 Unresolved reference 'takeLast'.
```

The failing expression applied `takeLast` directly to a mutable `Set`:

```kotlin
outcomes.takeLast(80).toSet()
```

The active Kotlin standard library exposes the required bounded operation after conversion to a list. CLIENT `CFv2.0.56` uses:

```kotlin
outcomes.toList().takeLast(80).toSet()
```

No protocol, upload authority, build-watch identity, or notification behavior is changed by this compiler repair.

## 2. Duplicate integrity-run root cause and repository correction

The duplicate cards were not merely a CLIENT rendering problem. The repository contained two workflow files with the same workflow name and the same `SOURCES/**/*.zip`, `.sha256`, and optional-manifest push paths:

- obsolete `.github/workflows/swrlz-package-integrity.yml`;
- current `.github/workflows/source-package-integrity.yml`.

Both therefore started for one source-package commit and appeared as two successful `SWRLZ Source Package Integrity` runs.

The obsolete workflow was removed in repository commit:

```text
a8dcc074e7c482f9d5536655cf53ae26ee698917
```

The retained `source-package-integrity.yml` remains the authoritative workflow and enforces the current ZIP+SHA-required, manifest-optional policy.

## 3. CLIENT Forge workflow observer

CLIENT `CFv2.0.56` now:

- queries up to 50 recent workflow runs;
- refreshes every 2 seconds while any run is active and every 5 seconds while idle;
- polls immediately after a verified upload for a run whose `head_sha` equals the new commit;
- keeps every queued or in-progress run visible;
- shows 4 completed runs by default;
- provides a slider for 2–20 completed runs;
- collapses display duplicates sharing workflow name, commit SHA, and event;
- continues to treat workflow discovery separately from repository upload success.

The display dedupe is defensive. The repository-level duplicate trigger was removed rather than hidden only in the CLIENT.

## 4. Launcher and Theme Armor identity

### SERVER

SERVER `CFv2.0.39` rebuilds:

- the application fallback `ic_launcher` and round icon;
- the enabled Glitch Dragon launcher alias;
- Original Core, Glitch Neon, Pharaoh Emerald, and Void Jester aliases;
- adaptive foreground variants.

Every variant now derives from the current violet coiled SWURLZER crystal-dragon artwork rather than the retired launcher family. The selected alias is reasserted after application update/start to reduce fallback to stale or default launcher identity.

### CLIENT

CLIENT `CFv2.0.56` similarly rebuilds its default and Theme Armor launcher families from the current cyan SWRLZ crystal-dragon artwork so theme switching no longer returns to the older icon family.

## 5. Theme-reactive bubble identity

The CLIENT and SERVER bubble contents were reported as rendering correctly and are preserved.

The role icons now resolve from both role and active Theme Armor family:

- CLIENT/SWRLZ;
- selected SERVER/SWURLZER;
- fused SWURVER.

Theme changes republish or refresh the relevant bubble identity so the shortcut artwork follows the interface theme without changing authority. Visual theme state still never grants SERVER or fusion privileges.

## 6. Main-page version provenance

The main interfaces now include persistent compact footers:

```text
CLIENT · CFv2.0.56    VC 83
SERVER · CFv2.0.39    VC 40
```

Footers are present in both User and Developer main shells and derive their CF number from Android `BuildConfig.VERSION_NAME`.

## 7. Version and package identity

### CLIENT

```text
Package: CLIENT_CFv2.0.56_SWRLZ.zip
versionCode: 83
versionName: 2.0.56-forge-observer-identity-footer-buildfix-v1
SHA-256: 6d623b4b17510d7755d452d3bfe510edc6388c3ab6e395f305658875644fe2f3
```

### SERVER

```text
Package: SERVER_CFv2.0.39_SWRLZ.zip
versionCode: 40
versionName: 2.0.39-theme-identity-footer-v1
SHA-256: 2f4f8c68c7ae2d98b0966d0d38a5e197bd43b7a724f2c30d074b961ec06ee2bb
```

Both final archives passed compressed-data integrity testing, and each final digest matches its sibling SHA-256 receipt.

## 8. Static evidence

- Modified Kotlin files passed structural delimiter-balance checks.
- All project XML resources parsed successfully.
- All PNG resources opened and verified successfully.
- New drawable and mipmap references resolve to existing resources.
- No explicit `androidx.compose.foundation.layout.weight` import remains.
- Application fallback icons and default aliases now use the same current artwork family.
- Local Gradle compilation could not complete because the isolated preparation environment could not reach the Gradle distribution host; GitHub Actions remains the authoritative compilation gate.

## Evidence classification

- CLIENT `CFv2.0.55` compiler failure: log-verified.
- Duplicate integrity trigger: repository-verified.
- Obsolete workflow removal: repository-committed.
- CLIENT `CFv2.0.56` source repair and observer changes: source/static-verified.
- SERVER `CFv2.0.39` identity changes: source/static-verified.
- ZIP/SHA packages: locally package-verified.
- Clean CLIENT and SERVER GitHub builds: pending.
- Launcher, theme alias, theme-reactive bubble, footer, and workflow-refresh behavior: pending device acceptance.

## Acceptance gate

1. Upload CLIENT `CFv2.0.56` ZIP/SHA and confirm exactly one Source Package Integrity run starts.
2. Confirm the CLIENT APK Router passes Kotlin compilation and produces an artifact.
3. Upload SERVER `CFv2.0.39` ZIP/SHA and confirm exactly one Source Package Integrity run starts.
4. Confirm the SERVER APK Router produces an artifact.
5. Install with the same signing certificate and verify the main-page footers.
6. Verify SERVER default launcher identity is the current violet SWURLZER dragon.
7. Switch every CLIENT and SERVER Theme Armor family and verify each launcher remains in the current artwork family.
8. Verify CLIENT, SERVER, and authorized fusion bubble icons refresh with theme changes.
9. Confirm a new workflow appears in Forge approximately 1–2 seconds after GitHub exposes it.
10. Confirm all active runs plus 4 completed runs appear by default and the history slider expands or contracts the completed history.
