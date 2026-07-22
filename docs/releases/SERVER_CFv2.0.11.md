# SERVER CFv2.0.11 Release Notes

Checkpoint: INT-CONNECT-021A

## Added

- `POST /nodes/disconnect`.
- Room schema v3 device-binding and installation-lineage fields.
- Duplicate-device reconciliation and superseded-record archiving.
- Registry startup diagnostics.

## Preserved

- Registration remains distinct from trust and proof acceptance.
- Intentional disconnect preserves durable registration.
- Offline and superseded records are not destructively deleted.

## Evidence

- SOURCE IMPLEMENTED
- STATIC VERIFICATION PASS
- MIGRATION NOT EXECUTED
- BUILD NOT RUN
- RUNTIME NOT TESTED
