# CLIENT/SERVER Device Binding and Disconnect Contract V1

## Identity separation

SWRLZ distinguishes:

- physical-device binding;
- app installation identity;
- SERVER registration identity;
- live presence;
- proof state;
- trust state.

These identities must not collapse into one field.

## Privacy-preserving binding

The CLIENT derives a SHA-256 binding fingerprint from Android-scoped stable identity, application namespace, and a SWRLZ binding-version domain separator. Raw IMEI, serial number, phone number, advertising ID, and similar invasive hardware identifiers are prohibited.

The binding fingerprint is the deduplication anchor. The installation UUID remains lineage and may change after app-data loss or reinstall.

## Registration matching

The SERVER resolves registration in this order:

1. exact device-binding fingerprint;
2. existing non-archived stable device ID;
3. exact node ID.

A new installation on a known device updates installation lineage rather than creating a second User Mode device record.

Conflicting identity or proof evidence must enter an explicit verification/failure state rather than silently merging.

## Disconnect

`POST /nodes/disconnect` intentionally ends live presence and heartbeat while preserving:

- durable registration;
- saved SERVER configuration;
- trust and proof records;
- installation lineage.

Disconnect is not unregister, forget, revoke, retire, or delete.

## Invariants

```text
Discovered != Registered
Registered != Trusted
Connected != Authorized
Proof Presented != Proof Accepted
Online != Mission-Capable
Offline != Deleted
Disconnect != Unregister
New Installation != New Physical Device
```
