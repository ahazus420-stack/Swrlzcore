# SWRLZ AI Context

This file is the canonical quick-start context for continuing SWRLZ development.

## Purpose
Read this file first before redesigning repository structure, build flow, or UI assumptions.

## Core Doctrine
- Integrate, do not overwrite.
- Preserve lineage.
- Repository defines truth.
- Artifacts reflect truth.
- Keep the build pipeline minimal.
- Keep Android-first workflows mobile-friendly.

## Current Build Model
- One repository.
- Two lanes: CLIENT and SERVER.
- GitHub Actions should only build when a lane has a complete versioned source pair.
- The workflow should find the newest versioned ZIP automatically.
- Older source outputs should be archived into OLD_PATCHES after successful builds.

## Current Naming Model
Use lane-first names that are easy to scan on Android:

- CLIENT_CFvX.Y.Z_SWRLZ.zip
- CLIENT_CFvX.Y.Z_SWRLZ.sha256
- CLIENT_CFvX.Y.Z_SWRLZ.md

- SERVER_CFvX.Y.Z_SWRLZ.zip
- SERVER_CFvX.Y.Z_SWRLZ.sha256
- SERVER_CFvX.Y.Z_SWRLZ.md

## Current Repo Structure
- SOURCES/
  - CLIENT/
  - SERVER/
  - OLD_PATCHES/
- BUILD_REQUESTS/
- RELEASES/
- BUILD_WORK/
- UPDATES/
- docs/
- .github/workflows/

## Current Request Model
Use the lane selector only:

- target=CLIENT
- target=SERVER

The workflow determines the newest versioned ZIP in that lane.

## UI Direction
- Dragon Mission OS for users.
- Engineering Console for developer access.
- One engine. Two perspectives.

## Documentation Direction
Maintain living docs for:
- Origin
- Foundation Laws
- Vision 2035
- UI Atlas
- Motion Bible
- Component Bible
- Engineering Atlas
- Android Architecture
- Server Architecture
- Developer Handbook
- Theme Bible
- Evolution Gallery

## Resume Rule
When returning after a gap, greet naturally and give a concise recap of the last completed SWRLZ decision before continuing.

## GitHub Workflow Rule
Batch changes when possible. Avoid unnecessary GitHub actions or tiny edits. Prefer one coherent sync.

## Notes
- Use Better # numbering when iterating on design choices.
- Give each Better a title so it is easy to reference later.
- Keep filename and folder conventions optimized for Android file browsers.
