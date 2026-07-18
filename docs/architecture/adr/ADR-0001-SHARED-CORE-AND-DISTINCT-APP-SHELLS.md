# ADR-0001: Shared Core Capabilities and Distinct Android App Shells

- **Status:** Accepted
- **Date:** 2026-07-18
- **Checkpoint:** SWRLZ-MODULAR-ARCH-001
- **Approval phrase:** `APPROVE SWRLZ-MODULAR-ARCH-001 SHARED CORE CAPABILITIES AND DISTINCT APP SHELLS`
- **Related contracts:** SWRLZ Device-Surface Identity Contract v1; SWRLZ Keyboard Trust, Privacy, and CLIENT Enrollment Contract

## Context

SWRLZ Core is evolving into a reusable platform for multiple Android applications, including Core, Keyboard, Launcher, CLIENT, and NODE_HOST. Early build lanes produced APKs from substantially identical Core source. This proved build independence but also exposed two identity problems:

1. APKs sharing one Android `applicationId` are treated as one application and cannot coexist.
2. APK updates require the same package identity and signing lineage as the installed version.

The project also requires selective composition. Some applications may host CLIENT capability, NODE_HOST capability, both, or neither. Keyboard and Launcher may later expose selected mission or node features without becoming identical monolithic applications.

## Decision

SWRLZ will use one modular platform with shared Core modules, optional capability modules, and distinct Android app shells.

Each app shell owns:

- a permanent Android `applicationId`;
- app label, icon, launcher presentation, and Android role;
- independent `versionCode` progression;
- stable signing lineage;
- an explicit capability composition manifest;
- app-specific navigation and presentation.

Shared behavior belongs in reusable modules. App shells compose those modules rather than copying and independently modifying the same implementation.

Initial conceptual structure:

```text
SWRLZ Platform
├── apps/
│   ├── core/
│   ├── keyboard/
│   ├── launcher/
│   ├── client/
│   └── nodehost/
├── core/
│   ├── identity/
│   ├── lineage/
│   ├── trust/
│   ├── storage/
│   ├── networking/
│   ├── missions/
│   └── update/
└── capabilities/
    ├── keyboard/
    ├── launcher/
    ├── client/
    └── nodehost/
```

This is a target architecture, not a requirement to rename or move every current source directory in one unbounded change.

## Requirements

1. Core remains the canonical reusable foundation.
2. Descendants integrate from Core; they do not overwrite canonical Core history.
3. Each installable app MUST have a distinct permanent Android application identity.
4. Future builds of the same app MUST retain its application identity and signing lineage.
5. Each app MUST advance `versionCode` monotonically for ordinary updates.
6. User-facing version or lineage labels MAY be separate from Android's internal `versionCode`.
7. A physical device and its software surfaces MUST remain separate identity concepts.
8. Shared device identity MUST NOT imply unrestricted shared authorization.
9. App composition MUST preserve offline-first operation, Truth Firewall behavior, lineage, local-versus-remote distinctions, and protocol-version discipline.
10. App-specific features SHOULD remain outside apps that do not need them unless a deliberate capability composition decision includes them.
11. CLIENT and NODE_HOST MAY be packaged independently or together in a compatible app shell, provided role, identity, trust, lifecycle, and authority remain explicit.

## Initial app identity intent

The final accepted identifiers must be verified against existing CLIENT and NODE_HOST identities before implementation.

| App shell | Identity intent | Update lineage |
|---|---|---|
| Core | unique permanent Core package | Core updates Core only |
| Keyboard | unique permanent Keyboard package | Keyboard updates Keyboard only |
| Launcher | unique permanent Launcher package | Launcher updates Launcher only |
| CLIENT | preserve accepted existing package when possible | CLIENT updates CLIENT only |
| NODE_HOST | preserve accepted existing package when possible | NODE_HOST updates NODE_HOST only |

## New-app derivation procedure

A new SWRLZ Android app derived from Core MUST document these steps:

1. identify the canonical Core source checkpoint and checksum;
2. create a new app shell rather than mutating Core identity;
3. assign a unique permanent `applicationId`;
4. define app label, icon, Android role, and entry points;
5. define independent `versionCode` and visible version metadata;
6. establish stable signing lineage before distributing durable test builds;
7. declare included and excluded capability modules;
8. remove inherited manifest declarations that do not belong to the new role;
9. add role-specific manifest declarations and permissions;
10. build independently and record artifact checksums;
11. verify side-by-side installation with other SWRLZ apps;
12. verify a later build updates only the prior installation of the same app;
13. record implementation and on-device evidence.

## Alternatives considered

### Independent full source copies

Rejected as the default because fixes and security requirements would drift across codebases and require repeated manual synchronization.

### One monolithic application containing every role

Rejected as the only distribution model because the project explicitly requires standalone CLIENT, standalone NODE_HOST, combined operation, and specialized Android surfaces.

### One shared package identity for all build lanes

Rejected because Android treats those APKs as replacements for one another and prevents simultaneous installation.

## Consequences

### Positive

- shared fixes can propagate through modules;
- apps can coexist on one device;
- each app can update its own installed lineage;
- capabilities can be composed differently per app;
- standalone and combined CLIENT/NODE_HOST deployments remain possible;
- new apps can be started from a documented repeatable process.

### Costs and risks

- Gradle configuration becomes more sophisticated;
- signing keys and version codes require disciplined management;
- module boundaries must avoid circular dependencies;
- application roles and process lifecycles require explicit contracts;
- combined-role apps must not blur trust or authority distinctions.

## Implementation boundary

This ADR authorizes and records the approved modular architecture direction. It does not by itself authorize:

- public release or deployment;
- Play Store publication;
- automatic APK download or installation;
- changes to accepted network protocols;
- silent merging of CLIENT and NODE_HOST authority;
- removal of accepted lineage or trust evidence.

## Verification

Implementation evidence should demonstrate:

- unique app IDs;
- stable signing fingerprints per lineage;
- monotonically increasing version codes;
- side-by-side installation;
- successful same-lineage update installation;
- independent workflow outputs;
- documented capability composition;
- no regression of accepted trust and offline-first invariants.

## Related decisions

- ADR-0002: Modular Capability and Entitlement Gates
