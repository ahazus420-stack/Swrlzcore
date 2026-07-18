# SWRLZ Device-Surface Identity Contract v1

Status: accepted design contract
Checkpoint: SWRLZ-IDENTITY-SURFACES-001

## Normative model

A physical Android phone MUST be represented by one enrolled `deviceId` within a trust domain. SWRLZ CLIENT, Keyboard, Launcher, Widget Host, and NODE_HOST MUST remain separately identifiable as surfaces without inflating the physical-device count.

### Identifiers

- `deviceId` — one enrolled physical device; randomly provisioned; never hardware-derived.
- `surfaceType` — `client`, `keyboard`, `launcher`, `widget_host`, or `node_host`.
- `surfaceInstanceId` — one logical enrolled surface beneath a device.
- `installationId` — one concrete package installation; changes after clean reinstall or app-data wipe.
- `nodeId` — one logical NODE_HOST identity; not assigned to ordinary UI surfaces.
- `sessionId` — ephemeral runtime or interaction session.

## Aggregation

- Physical devices: distinct active `deviceId`.
- SWRLZ surfaces: distinct active `surfaceInstanceId`.
- Package installations: distinct `installationId`.
- NODE_HOST nodes: distinct active `nodeId`.

## Enrollment and trust

- CLIENT is the recommended initial Device Identity Broker.
- Each surface MUST receive a separate scoped credential.
- Shared `deviceId` MUST NOT imply shared unrestricted authorization.
- Discovery MUST NOT grant trust or capability.
- Reinstall MUST create replacement installation lineage, not a new physical device.
- Retired surfaces MUST remain auditable but MUST NOT appear as active controls.

## Keyboard telemetry exclusions

The keyboard MUST NOT emit ordinary keystrokes, password content, silent clipboard history, or background text capture. Operational events MAY be recorded only when they exclude user text content.

## Compatibility

This contract adds a device-and-surface layer above existing NODE_HOST `nodeId` and `installationId` semantics. It does not replace accepted NODE_HOST identity, trust, discovery, or protocol-version discipline.