# Core SWRLZ Board Documentation

This directory is the documentation source for the SWRLZ Keyboard lane.

## Documentation map

- `architecture/` — system boundaries, modules, data flow, and Android component design.
- `contracts/` — accepted and proposed normative contracts.
- `security/` — threat models, privacy rules, trust boundaries, and sensitive-input handling.
- `identity/` — device, surface, installation, node, session, enrollment, revocation, and lineage.
- `android-ime/` — Input Method Editor lifecycle, editor contexts, keyboard UI, and platform constraints.
- `integration/` — CLIENT broker, local IPC, NODE_HOST access, and offline synchronization.
- `design-system/` — typography, glyphs, icons, motion, accessibility, and themes.
- `testing/` — unit, integration, instrumentation, privacy, security, compatibility, and acceptance plans.
- `operations/` — versioning, release evidence, migration, rollback, observability, and incident handling.
- `adr/` — Architecture Decision Records.
- `checkpoints/` — bounded checkpoint evidence and approvals.

## Documentation rules

1. Separate facts, requirements, assumptions, recommendations, and unresolved questions.
2. Use MUST, MUST NOT, SHOULD, SHOULD NOT, and MAY only in normative contracts.
3. Every implemented behavior must trace to an accepted contract and test evidence.
4. Never treat discovery as authorization.
5. Never merge physical-device statistics with package-installation or surface statistics.
6. Never collect ordinary keystrokes or sensitive text as telemetry.
7. Preserve retired identities and replacement lineage without showing ghost surfaces as active controls.