# SWRLZ-Core Device Ledger: Ghost Device Vault / Legacy Device Archive

## Status

Accepted architecture rule.

## Context

SWRLZ-Core may show an old device profile after the same physical device receives a newer profile because of registration changes, build updates, identity-anchor changes, or device-ledger evolution.

That old profile should not automatically be treated as corrupt data. If the profile was historically true, it is a lineage artifact.

## Core decision

Do not hard-delete historically valid device profiles by default.

Treat outdated-but-valid profiles as one of:

- Ghost Device
- Legacy Anchor
- Retired Device Profile
- Device Ledger Archive Object

The active UI should stay clean, but the deeper ledger should preserve the historical truth.

## Design rule

Active/current device profiles should remain clean in the main device dashboard.

Outdated but historically valid profiles should move into a Ghost Device Vault, Legacy Device Archive, or Device Ledger history section.

These profiles should be hidden from normal active-device controls unless the user enables debug/history view.

They should remain available for:

- audit trails
- duplicate-device detection
- identity repair
- merge recovery
- build-history debugging
- lineage tracking
- future migration/transmutation logic

## Recommended profile status fields

```json
{
  "status": "ghost | retired | legacy",
  "active": false,
  "linkedActiveDeviceId": "<current device profile id>",
  "retiredReason": "re-registered / profile shifted after build update",
  "createdBuildVersion": "<version>",
  "retiredBuildVersion": "<version>",
  "lastSeenAt": "<timestamp>",
  "preserveLogs": true
}
```

## Recommended actions

Ghost/legacy profiles may support:

- Link to active device
- Merge logs into active profile
- Archive profile
- Restore as active if needed
- Permanently delete only behind a strong confirmation step

## UI behavior

Default active-device dashboard:

- show active devices only
- do not show ghost profiles as normal online/offline devices
- do not allow routine controls on ghost profiles

Debug/history view:

- show ghost/retired/legacy profiles
- show lineage links
- show created/retired build versions
- show last seen timestamp
- show logs and merge/recovery tools

## Strong-delete rule

Permanent deletion requires a high-friction confirmation step.

A strong delete flow should clearly warn that deletion removes historical identity evidence and may affect audit trails, duplicate detection, recovery, and debugging.

## Core principle

Never hard-delete a device profile by default if it is historically true.

Retire, archive, or link it first.

SWRLZ should keep the active UI clean while preserving the deeper historical truth of the system.

## Mythic engineering note

A ghost device can function like a lineage artifact: not an active worker, but a preserved identity trace useful for future debugging, merge recovery, and device transmutation logic.

In project language, it can act like a Horadric Cube object for identity reconstruction rather than trash data.
