# SWRLZ-Core 2.7.6 CODEFIX1: Forge Identity / Install Continuity / Permission Onboarding

## Status

Planned and staged as a forge stability patch.

## Purpose

Stabilize the 2.7.6 forge lane before adding more network-node features.

This CODEFIX exists because field testing found three practical issues:

1. A successful workflow run produced an artifact labeled `SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_APK_DOWNLOAD`, meaning the forge resolver could still select the wrong request/release target.
2. Installing the new APK over the old APK failed and required uninstall/reinstall, most likely because the previous and current debug APKs were signed with different certificates.
3. Android restricted settings for sideloaded apps may not visibly appear until the user first visits/toggles the relevant permissions/app settings area.

## Patch label

- Client patch lane: `2.7.6-CODEFIX1-FORGE-IDENTITY-INSTALL-CONTINUITY`
- Release request: `SWRLZ_CORE_2.7.6_CODEFIX1_FORGE_IDENTITY_INSTALL_CONTINUITY`
- Base source: `SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_SOURCE.zip`
- Applied by visible forge patch, not by mystery APK.

## Forge resolver rule

The workflow should prefer a current build request in this order:

1. `BUILD_REQUESTS/000_CURRENT.request`
2. `BUILD_REQUESTS/CURRENT.request`
3. newest `.request` / `.trigger` fallback
4. explicit manual workflow inputs always override request file fields

This prevents old request files from accidentally controlling a later build.

## Stable signing lane

Install-over-old APK requires all of the following:

- same package name
- compatible/higher `versionCode`
- same signing certificate

If GitHub Actions signs debug APKs with different temporary debug keys, Android will reject update-over-install and force uninstall/reinstall.

The recommended fix is a stable SWRLZ development signing key configured as GitHub secrets:

- `SWRLZ_DEV_KEYSTORE_BASE64`
- `SWRLZ_DEV_KEYSTORE_PASSWORD`
- `SWRLZ_DEV_KEY_ALIAS`
- `SWRLZ_DEV_KEY_PASSWORD`

Until these secrets exist, the workflow can only report that stable signing is not active.

## Restricted settings onboarding

SWRLZ cannot bypass Android restricted-setting protections, unlock restricted settings automatically, or skip device authentication.

SWRLZ can provide a first-run Permission Onboarding Wizard that guides the user through:

1. Open App Info.
2. Visit permissions / restricted settings area if Android has not revealed the option yet.
3. Return to App Info.
4. Tap the three-dot menu if available.
5. Tap Allow restricted settings.
6. Authenticate with phone security.
7. Return to SWRLZ.
8. Open Accessibility / Notifications / Overlay settings.
9. Verify readiness inside SWRLZ.

## Acceptance tests

1. GitHub Actions artifact name must match `SWRLZ_CORE_2.7.6_CODEFIX1_FORGE_IDENTITY_INSTALL_CONTINUITY_APK_DOWNLOAD`.
2. Build summary must report the request file used.
3. Build summary must report signing mode: stable dev signing active or inactive.
4. If stable signing secrets are active, APK should install over the previous SWRLZ APK without uninstalling.
5. App or docs must clearly guide Android restricted-settings onboarding without claiming SWRLZ can bypass OS security.

## Roadmap position

Previous:

- 2.7.6 Network Discovery forge patch
- Ghost Device Vault architecture rule

Current:

- 2.7.6 CODEFIX1 Forge Identity / Install Continuity / Permission Onboarding

Next:

- 0.7.3 Discovery Signature field test
- 0.8.0 Node Host APK
