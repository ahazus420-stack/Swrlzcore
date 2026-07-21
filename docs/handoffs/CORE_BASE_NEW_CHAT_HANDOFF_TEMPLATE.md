# CORE_BASE New-Chat Handoff Template

Use this when starting a new CORE_BASE engineering chat.

## Scope

This chat is dedicated only to canonical CORE_BASE. Do not modify CLIENT, NODE_HOST, Keyboard, or Launcher.

## Read first

1. `docs/build/CORE_BASE_CANONICAL_BUILD_AND_HANDOFF_PROCESS.md`
2. `BUILD_REQUESTS/000_CURRENT.request`
3. `.github/workflows/build-swrlz-core-android-foundation.yml`
4. active ZIP and sibling SHA under `SOURCES/CORE_BASE/`
5. `SOURCES/CORE_BASE/OLD_PATCHES/README.md`
6. `skills/swrlz-core-base-canonical-build-engineer/SKILL.md`

## Operating rules

- GitHub is authoritative.
- Integrate; never overwrite lineage.
- Verify ZIP SHA before extraction or build.
- Build from the request-selected immutable archive in an isolated workspace.
- Produce APK checksum and provenance.
- Never claim completion without repository/run evidence.
- Do not commit, push, merge, publish, release, deploy, or install without active checkpoint authorization.

## State fields

- Repository:
- Branch:
- Source ZIP:
- Source SHA-256:
- Application ID:
- Version name/code:
- Request ID:
- Workflow:
- Last completed checkpoint:
- Last build run/artifact:
- Current gate:
