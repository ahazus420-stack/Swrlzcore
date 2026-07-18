# Core SWRLZ Board

Status: scaffold only
Checkpoint: SWRLZ-KBD-CON-001

This directory is the bounded engineering workspace for the SWRLZ Android keyboard and later Android shell surfaces. It does not replace the canonical CLIENT or NODE_HOST source lanes.

## Principles

- Integrate; do not overwrite.
- Preserve offline-first behavior.
- Preserve identity, trust, Truth Firewall, lineage, and local-versus-remote distinctions.
- Keep keyboard, CLIENT, launcher, widget, and NODE_HOST identities separate while linking them beneath one enrolled physical device.
- Require explicit authorization before implementation, build, workflow, commit to protected branches, release, or deployment.

## Structure

- `docs/` — engineering documentation and accepted contracts.
- `keyboard/` — future keyboard implementation lane.
- `shared/` — future shared contracts and design-system code.
- `integration/` — future CLIENT enrollment and IPC integration.
- `testing/` — future test fixtures, plans, and evidence.
- `tools/` — future local development utilities.

No production code is included in this scaffold.