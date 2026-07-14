# CLIENT Lane

This lane holds the active client source ZIP, SHA file, report, and any client-specific lineage receipts.

## Naming pattern

- CLIENT_CFvX.Y.Z_SWRLZ.zip
- CLIENT_CFvX.Y.Z_SWRLZ.sha256
- CLIENT_CFvX.Y.Z_SWRLZ.md

## Archive rule

Move older client source outputs into:

- SOURCES/CLIENT/OLD_PATCHES/

Core law: integrate, do not overwrite.
