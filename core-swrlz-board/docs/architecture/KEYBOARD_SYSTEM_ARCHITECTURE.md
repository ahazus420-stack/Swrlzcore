# SWRLZ Keyboard System Architecture

Status: draft scaffold
Checkpoint: SWRLZ-KBD-CON-001

## Scope

The keyboard is a dedicated Android Input Method Editor surface that integrates with the canonical SWRLZ CLIENT without becoming a separate physical device or NODE_HOST.

## Proposed module boundaries

- `keyboard-app` — IME service, settings activity, onboarding, and keyboard UI.
- `keyboard-domain` — editor-context policy, commands, transformations, and state machines.
- `keyboard-data` — encrypted local preferences, enrollment credential storage, and queued events.
- `keyboard-ui` — key layouts, candidate strip, glyph vault, themes, and accessibility semantics.
- `identity-contracts` — device/surface/install/session identifiers and lifecycle states.
- `client-bridge` — explicit enrollment, revocation, scoped requests, and health checks.
- `core-gateway` — local-first routing to CLIENT or NODE_HOST under accepted trust policy.
- `design-system` — fonts, glyphs, dimensions, motion, and iconography.
- `telemetry-contracts` — operational events with text-content exclusion.

## Hard boundaries

- Ordinary typing remains functional without CLIENT, NODE_HOST, LAN, or internet.
- The IME never becomes the canonical registry or root identity authority.
- The keyboard never receives a `nodeId` unless a future accepted contract explicitly makes it node-capable.
- Password and other sensitive editor contexts disable AI and content-processing actions.
- Remote processing requires deliberate user action and visible route disclosure.
- Discovery cannot grant authority.

## Data-flow summary

1. Android supplies an editor context to the IME.
2. Policy classifies the context locally.
3. Ordinary key events are committed directly to the target editor.
4. Explicit SWRLZ actions create a bounded request from selected or user-confirmed text.
5. The route resolver prefers approved local execution.
6. Any remote route is disclosed and requires accepted consent policy.
7. Only operational, content-free evidence may be emitted as telemetry.

Implementation details remain pending contract acceptance.