# SERVER Lane

This lane holds the active server source ZIP, SHA file, report, and any server-specific lineage receipts.

## Naming pattern

- SERVER_CFvX.Y.Z_SWRLZ.zip
- SERVER_CFvX.Y.Z_SWRLZ.sha256
- SERVER_CFvX.Y.Z_SWRLZ.md

## Archive rule

Move older server source outputs into:

- SOURCES/SERVER/OLD_PATCHES/

Core law: integrate, do not overwrite.
