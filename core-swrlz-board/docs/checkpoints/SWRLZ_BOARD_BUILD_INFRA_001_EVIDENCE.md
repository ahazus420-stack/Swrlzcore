# SWRLZ-BOARD-BUILD-INFRA-001 Evidence

## Status

Infrastructure prepared on `checkpoint/swrlz-board-build-infra-001`.

## Added source lanes

- `SOURCES/SWRLZ-BOARD/` — standalone SWRLZ Keyboard source lane
- `SOURCES/SWRLZ-BOARD/OLD_PATCHES/` — Keyboard lineage archive
- `SOURCES/SCORE-LAUNCHER/` — standalone sCore Launcher source lane
- `SOURCES/SCORE-LAUNCHER/OLD_PATCHES/` — Launcher lineage archive

CLIENT and SERVER source lanes were not moved or modified.

## Added workflows

- `.github/workflows/build-swrlz-keyboard-apk.yml`
- `.github/workflows/build-score-launcher-apk.yml`

Both workflows are manual `workflow_dispatch` workflows. They:

1. read `BUILD_REQUESTS/000_CURRENT.request`;
2. resolve their own independent source lane and filename pattern;
3. require the matching SHA-256 file;
4. verify source integrity before extraction;
5. locate a Gradle wrapper inside the source ZIP;
6. run a bounded Android debug or release assemble task;
7. upload APKs, the Gradle log, and build provenance as GitHub Actions artifacts.

Both fail closed when their request flag is false and no manual source ZIP is provided.

## Request contract additions

`BUILD_REQUESTS/000_CURRENT.request` now includes independent configuration for:

- CLIENT
- SERVER
- SWRLZ Keyboard
- sCore Launcher

Keyboard and Launcher default to disabled until accepted source ZIPs and checksums are promoted to their lanes.

## Not performed

- no Keyboard source ZIP created;
- no Launcher source ZIP created;
- no APK built;
- no workflow dispatched;
- no release artifacts committed;
- no CLIENT or SERVER source changed;
- no merge to `main`;
- no release or deployment.

## Validation limitation

The YAML definitions were created as repository infrastructure only. They have not been executed by GitHub Actions, so runtime success is not yet claimed.