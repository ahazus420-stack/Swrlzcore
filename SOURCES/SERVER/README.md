# SERVER Source Lane

This lane contains the current active Android NODE_HOST source set and the minimum metadata required to verify and build it.

## Active source set

- `SERVER_CFv1.0.4_SWRLZ.zip`
- `SERVER_CFv1.0.4_SWRLZ.sha256`
- Declared ZIP SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`

The active source ZIP and its matching checksum remain paired at the top of this lane. A version-specific Markdown report may remain beside them when one exists.

## Naming contract

- `SERVER_CFvX.Y.Z_SWRLZ.zip`
- `SERVER_CFvX.Y.Z_SWRLZ.sha256`
- `SERVER_CFvX.Y.Z_SWRLZ.md`

## Download-suffix normalization

Android and browser download managers may append a terminal duplicate-download suffix such as `(1)`, `(2)`, or `(3)` immediately before a file extension. Such a suffix is transport metadata, not part of the canonical SERVER source identity.

A tool may normalize that suffix for candidate matching only when it fails closed unless all required checks pass:

- the unsuffixed canonical basename and extension are valid;
- the ZIP is structurally valid;
- the matching checksum resolves to the same normalized canonical basename;
- the calculated digest matches the declared digest;
- multiple suffixed candidates that normalize to one canonical name are rejected unless byte identity is proven.

Repository storage remains canonical and unsuffixed after verification.

## Hygiene and lineage

Keep only the active complete source set at this lane root. Move superseded sources, legacy transport bundles, old discovery packages, and their documentation into:

- `SOURCES/SERVER/OLD_PATCHES/`

Do not delete lineage by default. Archived files retain their original canonical names and Git history unless a separately approved collision-resolution checkpoint requires otherwise.

## Build selection

`.github/workflows/build-swrlz-server-apk.yml` is responsible for selecting and verifying the active SERVER archive. Workflow behavior may evolve through separately bounded checkpoints; this source-lineage checkpoint does not modify that workflow.

Core law: integrate, do not overwrite.
