# SWRLZ-BOARD-BUILD-INFRA-VAL-001 Evidence

## Status

Static validation completed on `checkpoint/swrlz-board-build-infra-001`.

No workflow was dispatched and no APK was built.

## Files reviewed

- `.github/workflows/build-swrlz-keyboard-apk.yml`
- `.github/workflows/build-score-launcher-apk.yml`
- `BUILD_REQUESTS/000_CURRENT.request`
- `SOURCES/SWRLZ-BOARD/README.md`
- `SOURCES/SCORE-LAUNCHER/README.md`

## Confirmed routing

### SWRLZ Keyboard

- request flag: `build_keyboard`
- source lane: `SOURCES/SWRLZ-BOARD`
- source pattern: `KEYBOARD_CFv*_SWRLZ.zip`
- matching checksum: same basename with `.sha256`
- workflow: `.github/workflows/build-swrlz-keyboard-apk.yml`

### sCore Launcher

- request flag: `build_score_launcher`
- source lane: `SOURCES/SCORE-LAUNCHER`
- source pattern: `SCORE_LAUNCHER_CFv*_SWRLZ.zip`
- matching checksum: same basename with `.sha256`
- workflow: `.github/workflows/build-score-launcher-apk.yml`

## Corrections made during validation

Both workflows were tightened to:

1. require the configured source lane to exist;
2. require exactly one active matching source ZIP when lane resolution is used;
3. reject zero or multiple active source ZIPs instead of silently selecting one;
4. require a matching `.sha256` file;
5. require the checksum token to be exactly 64 hexadecimal characters;
6. compare checksums case-insensitively;
7. reject ZIP entries containing absolute paths or parent-directory traversal;
8. require exactly one Gradle wrapper in the extracted source;
9. require `gradle/wrapper/gradle-wrapper.properties`;
10. require either `settings.gradle.kts` or `settings.gradle`;
11. require at least one APK in the expected variant output directory before artifact upload.

`BUILD_REQUESTS/000_CURRENT.request` was reconciled to state the single-active-source rule explicitly.

## Fail-closed behavior

Without a manual source path, each workflow stops before extraction when:

- its application build flag is not `true`;
- its source lane is missing;
- no active source ZIP exists;
- more than one active matching ZIP exists;
- the checksum file is missing or malformed;
- the checksum does not match;
- the ZIP contains an unsafe entry;
- the standalone Gradle project structure is ambiguous or incomplete.

A manual source path remains an explicit workflow-dispatch override and still requires a valid matching SHA-256 file.

## Static validation result

| Area | Result |
|---|---|
| Keyboard request routing | PASS |
| Launcher request routing | PASS |
| Independent lane separation | PASS |
| Single-active-source enforcement | PASS |
| SHA-256 format and equality checks | PASS |
| ZIP traversal guard | PASS |
| Gradle-root preflight checks | PASS |
| APK existence pre-upload check | PASS |
| GitHub Actions runtime execution | NOT RUN |
| Gradle compilation | NOT RUN |
| APK production | NOT RUN |

## Remaining runtime assumptions

The first accepted source packages must contain a standalone Android Gradle root with:

- `gradlew`;
- `gradle/wrapper/gradle-wrapper.properties`;
- `settings.gradle.kts` or `settings.gradle`;
- an `app` module whose APK output appears under `app/build/outputs/apk/<variant>/`.

These assumptions are now checked by the workflows, but they cannot be proven until an accepted source ZIP is promoted and a separately authorized workflow run occurs.

## Not performed

- no source ZIP created or modified;
- no build flag enabled;
- no workflow dispatched;
- no Gradle command executed;
- no APK or release artifact produced;
- no CLIENT or SERVER source changed;
- no merge to `main`;
- no release or deployment.
