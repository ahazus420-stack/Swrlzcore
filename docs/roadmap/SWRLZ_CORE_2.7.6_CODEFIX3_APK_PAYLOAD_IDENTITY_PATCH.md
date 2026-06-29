# SWRLZ-Core 2.7.6 CODEFIX3: APK Payload Identity Patch

## Status

Planned and staged as the next corrective forge patch.

## Triggering field evidence

After installing the CODEFIX2 artifact, the app still displayed:

- top identity: `v0.2.7.5-admin-registry • code 18`
- Home Build Identity patch: `2.7.4-COCKPIT-SHELL`
- Roadmap: `2.7.4`
- Source: `SWRLZ_CORE_2.7.4_COCKPIT_SHELL_SOURCE.zip`
- no visible Network Discovery entry/panel

This proves the previous source-level text patch did not reach the actual packaged UI payload.

## Root-cause hypothesis

The live UI is likely being served from packaged WebView/assets or generated build output that was not modified by the pre-build source patch.

Therefore CODEFIX3 patches the built APK payload after Gradle output is created, then re-signs the APK.

## Patch label

- Release request: `SWRLZ_CORE_2.7.6_CODEFIX3_APK_PAYLOAD_IDENTITY_PATCH`
- App display version target: `v0.2.7.6-codefix3-apk-payload`
- Display code target: `22`
- Patch target: `2.7.6-CODEFIX3-APK-PAYLOAD-IDENTITY-PATCH`

## Required changes

1. Build normally from the verified base source.
2. Rename output APK/bundle to the safe release name.
3. Unzip the built APK.
4. Patch decoded UTF-8 payload files inside the APK, especially assets, HTML, JS, CSS, JSON, XML, and text payloads.
5. Replace stale 2.7.4/2.7.5 identity strings in the actual packaged UI.
6. Inject or emit a visible Network Discovery panel into packaged HTML/assets.
7. Repack, zipalign, and re-sign the APK.
8. Include `SWRLZ_APK_PAYLOAD_PATCH_REPORT.txt` in the artifact.

## Acceptance tests

1. Artifact name must be `SWRLZ_CORE_2.7.6_CODEFIX3_APK_PAYLOAD_IDENTITY_PATCH_APK_DOWNLOAD`.
2. APK filename inside artifact must be `SWRLZ_CORE_2.7.6_CODEFIX3_APK_PAYLOAD_IDENTITY_PATCH_debug.apk`.
3. Artifact must include `SWRLZ_APK_PAYLOAD_PATCH_REPORT.txt`.
4. Report must list patched APK payload files or fail loudly if no payload was patchable.
5. Installed app should stop displaying the stale 2.7.4 Cockpit Shell Build Identity.
6. Installed app should visibly include a Network Discovery entry/panel.

## Truth note

This is a targeted APK payload correction, not a permanent substitute for source-level integration. Once the real UI source file is identified, the patch should be moved into the true source path.
