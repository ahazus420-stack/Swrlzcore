# SERVER Room Migration 2 → 3: Device Binding and Installation Lineage

Checkpoint: INT-CONNECT-021A

## Added columns on `nodes`

- `deviceBindingFingerprint TEXT NOT NULL DEFAULT ''`
- `installationLineage TEXT NOT NULL DEFAULT '[]'`
- `installationCount INTEGER NOT NULL DEFAULT 1`

## Added indexes

- `index_nodes_deviceBindingFingerprint`
- `index_nodes_deviceId`

## Migration behavior

- Existing registrations are preserved.
- Existing rows begin without a binding fingerprint until a CLIENT next registers.
- Registration may reconcile by stable device ID and then attach the new binding.
- Other active rows for the same stable device ID are marked archived/superseded rather than deleted.
- User Mode should show the canonical non-archived device; Developer Mode may expose lineage.

## Evidence state

- SOURCE IMPLEMENTED
- STATIC VERIFICATION PASS
- MIGRATION NOT EXECUTED
- RUNTIME ACCEPTANCE PENDING

## Acceptance checks

1. Upgrade SERVER without uninstalling.
2. Confirm prior registered rows remain.
3. Connect the same physical CLIENT under a new installation UUID.
4. Confirm one active device card remains.
5. Confirm prior installation is retained as lineage/superseded evidence.
