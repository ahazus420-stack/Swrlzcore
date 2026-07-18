# Keyboard Trust and Privacy Baseline

Status: draft for SWRLZ-KBD-CON-001

## Protected data classes

- ordinary keystrokes;
- composing text and editor contents;
- passwords, payment fields, authentication codes, and private keys;
- clipboard contents;
- selected text submitted to an explicit SWRLZ action;
- enrollment credentials and identity lineage.

## Required controls

1. Ordinary typing MUST remain local to the Android editor path.
2. Sensitive editor contexts MUST disable AI actions, history, previews, and content telemetry.
3. Text MUST NOT be sent to CLIENT, NODE_HOST, or a remote service without a deliberate user action.
4. The chosen route MUST be visible before remote transmission.
5. Local processing MUST be preferred when an approved capable local route exists.
6. Enrollment credentials MUST be scoped to the keyboard surface and stored using Android-backed secure storage where available.
7. Clipboard access MUST be user-visible, bounded, and disabled when not required.
8. Logs and crash reports MUST redact editor text and clipboard data.
9. Revocation MUST stop privileged integration while preserving ordinary keyboard operation.
10. Truth Firewall objections and safety pauses MUST remain active across local and remote routes.

## Threat categories

- malicious target application probing IME behavior;
- compromised or spoofed CLIENT enrollment endpoint;
- credential replay across surfaces;
- accidental sensitive-field processing;
- telemetry content leakage;
- clipboard overcollection;
- stale or retired surface credentials;
- route confusion between local and remote execution;
- accessibility regressions that hide consent state.

A full threat model and abuse-case matrix remain required before implementation approval.