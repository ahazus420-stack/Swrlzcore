# SWRLZ-Core 2.7.6 CODEFIX2: UI Identity Sync / Network Discovery Entry / Output Filename Truth

## Status

Planned and staged as a forge stability/UI truth patch.

## Triggering field evidence

After installing the 2.7.6 CODEFIX1 artifact, the app showed:

- `versionCode` updated to `20`
- `versionName` still displayed as `v0.2.7.5-admin-registry`
- Home Build Identity panel still displayed `2.7.4-COCKPIT-SHELL`
- Network Discovery was not visible in the app UI

Therefore CODEFIX1 was only partially successful: the forge lane and versionCode moved, but user-facing identity constants and app UI did not fully sync.

## Patch label

- Release request: `SWRLZ_CORE_2.7.6_CODEFIX2_UI_IDENTITY_NETWORK_DISCOVERY`
- App versionName target: `v0.2.7.6-codefix2-ui-identity`
- App versionCode target: `21`
- Patch target: `2.7.6-CODEFIX2-UI-IDENTITY-NETWORK-DISCOVERY`

## Required changes

1. Patch all decoded UTF-8 project files, not only Kotlin/Gradle/JSON files.
2. Include web assets such as `.html`, `.css`, `.js`, shell scripts, and templates.
3. Replace stale Home Build Identity strings from 2.7.4 Cockpit Shell.
4. Add visible Network Discovery UI/receipt panel into HTML/webview assets when possible.
5. Rename final APK/bundle outputs to match the safe release name before upload and manifest generation.
6. Preserve truthful source line: base 2.7.5 Admin Registry source plus visible CODEFIX2 forge patch.

## Acceptance tests

1. Artifact name must be `SWRLZ_CORE_2.7.6_CODEFIX2_UI_IDENTITY_NETWORK_DISCOVERY_APK_DOWNLOAD`.
2. APK filename inside artifact should match `SWRLZ_CORE_2.7.6_CODEFIX2_UI_IDENTITY_NETWORK_DISCOVERY_debug.apk`.
3. App top identity should show `v0.2.7.6-codefix2-ui-identity` and `code 21`.
4. Home Build Identity should show:
   - Patch: `2.7.6-CODEFIX2-UI-IDENTITY-NETWORK-DISCOVERY`
   - Roadmap: `2.7.6`
   - Build line: `Network Discovery / UI identity sync / output filename truth / permission onboarding / Admin Registry bridge`
5. App UI should visibly include a Network Discovery section/panel or entry.
6. Manifest should point to CODEFIX2 release and CODEFIX2-named output files.

## Roadmap position

Previous:

- 2.7.6 CODEFIX1 Forge Identity / Install Continuity / Permission Onboarding

Current:

- 2.7.6 CODEFIX2 UI Identity Sync / Network Discovery Entry / Output Filename Truth

Next:

- 0.7.3 Discovery Signature field test
- 0.8.0 Node Host APK
