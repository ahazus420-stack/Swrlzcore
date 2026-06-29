# SWRLZ-Core 2.7.6 CODEFIX3: Stale UI Source Locator

## Status

Staged as a diagnostic forge patch after CODEFIX2 field testing.

## Triggering field evidence

The CODEFIX2 artifact name and manifest moved forward, but the installed app still displayed:

- `v0.2.7.5-admin-registry • code 18`
- Patch: `2.7.4-COCKPIT-SHELL`
- Roadmap: `2.7.4`
- Source: `SWRLZ_CORE_2.7.4_COCKPIT_SHELL_SOURCE.zip`
- no visible Network Discovery entry

## Meaning

The stale UI identity is coming from a real app payload/source location that previous broad source patching did not reach.

## Decision

Do not continue blind text-replacement patches.

CODEFIX3 must locate the exact file(s) that contain stale UI identity strings in:

1. extracted source tree before build
2. built APK contents after build, read-only inspection
3. generated APK_DOWNLOAD folder and build logs

## Output report

The forge should emit:

`SWRLZ_STALE_UI_SOURCE_LOCATOR_REPORT.txt`

The report should include matches for:

- `2.7.4-COCKPIT-SHELL`
- `v0.2.7.5-admin-registry`
- `SWRLZ_CORE_2.7.4_COCKPIT_SHELL_SOURCE.zip`
- `Cockpit Shell`
- `Roadmap:`
- `Build line:`
- `NETWORK DISCOVERY`
- `swrlz-network-discovery`

## Acceptance test

The diagnostic build is successful if it produces an artifact containing:

- APK output
- build summary
- signing mode
- `SWRLZ_STALE_UI_SOURCE_LOCATOR_REPORT.txt`

The next actual codefix should patch the exact file path(s) named in that report.

## Roadmap truth

This is a locator diagnostic, not the final UI fix. It prevents false-success patches and preserves the SWRLZ law: no invisible patches, no mystery APKs, truthful build identity.
