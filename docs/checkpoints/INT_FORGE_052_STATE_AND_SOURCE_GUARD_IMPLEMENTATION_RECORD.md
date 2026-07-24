# INT-FORGE-052 — Forge State Header and Source ZIP Guard

## Status

Source-prepared in CLIENT `CFv2.0.52`; runtime and GitHub Actions acceptance remain pending device build evidence.

## Scope

This additive CLIENT patch improves Forge observability and prevents obvious non-source archives from reaching GitHub.

## Authoritative Forge phase projection

When the Forge tab is selected, the global CLIENT header now displays the process-local Forge phase rather than the unrelated ambient mission state:

- `IDLE`
- `VALIDATING`
- `UPLOADING`
- `BUILDING`
- `DOWNLOADING`
- `ARTIFACTS READY`
- `COMPLETE`
- `FAILED`

The projection is derived from real upload state, active GitHub workflow runs, artifact retrieval, and failure outcomes. It does not invent compile percentages and grants no authority.

## Source ZIP Content Guard

A persisted `SOURCE ZIP CONTENT GUARD` toggle is enabled by default.

Before checksum verification or any GitHub network upload, Forge performs a bounded streaming inspection of every staged ZIP. The guard:

- rejects empty or unreadable ZIPs;
- blocks archives containing `.apk` or `classes*.dex` payloads that strongly indicate built APK/artifact bundles;
- requires recognizable project/source structure such as Gradle files, Android manifests, package metadata, or a credible quantity of code/resource files;
- avoids extraction and scans at most 2,500 entries;
- exposes an explicit bypass for intentionally nonstandard source archives.

Exact ZIP/basename-SHA pairing and SHA-256 verification remain mandatory independently of this heuristic guard.

## Files changed in the prepared CLIENT package

- `android/app/src/main/java/sh/swurlz/core/github/ForgeRuntimeState.kt`
- `android/app/src/main/java/sh/swurlz/core/github/GitHubForgeClient.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/GitHubForgeScreen.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/client/ClientShellScreen.kt`
- package-level changelog and checkpoint documentation

## Acceptance requirements

1. Select the Forge tab and confirm the header transitions through real phases.
2. Stage a valid CLIENT or SERVER source ZIP and confirm local guard acceptance.
3. Stage an artifact ZIP containing an APK and confirm upload is blocked before any network request.
4. Disable the guard and confirm the bypass is visibly indicated while checksum validation remains enforced.
5. Verify active builds remain phase-based and do not display fabricated compile percentages.
6. Confirm state returns to `IDLE`, `COMPLETE`, `ARTIFACTS READY`, or `FAILED` according to actual outcomes.
