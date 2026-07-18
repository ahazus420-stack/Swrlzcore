# SWRLZ-KBD-CON-001 Acceptance Matrix

- **Checkpoint:** SWRLZ-KBD-CON-001
- **Status:** Design acceptance matrix
- **Implementation evidence:** Not yet available

| ID | Requirement | Design status | Required future evidence |
|---|---|---:|---|
| KBD-001 | Ordinary typing works without CLIENT, NODE_HOST, LAN, or internet | Defined | Instrumented offline IME test |
| KBD-002 | Keyboard surface does not increment physical-device count | Defined | Server aggregation test with CLIENT + keyboard |
| KBD-003 | Enrollment requires explicit CLIENT approval | Defined | UI and IPC integration test |
| KBD-004 | Credential is bound to keyboard surface and installation | Defined | Credential misuse and reinstall tests |
| KBD-005 | Revocation disables privileged SWRLZ actions only | Defined | Revocation behavior test |
| KBD-006 | Password, payment-secret, OTP, and ambiguous-sensitive fields disable AI actions | Defined | Editor classification test matrix |
| KBD-007 | Raw keystrokes and field contents are absent from telemetry and logs | Defined | Log, analytics, crash, and backup inspection |
| KBD-008 | Remote processing requires explicit route disclosure and consent | Defined | Route transition tests |
| KBD-009 | No silent local-to-remote fallback | Defined | Forced local failure test |
| KBD-010 | Truth Firewall objections remain available and auditable | Defined | Refusal persistence tests |
| KBD-011 | Clipboard is not continuously harvested | Defined | Clipboard access instrumentation |
| KBD-012 | Optional content retention is opt-in, bounded, inspectable, and deletable | Defined | Settings and storage tests |
| KBD-013 | IPC rejects signature mismatch, replay, malformed payload, and incompatible versions | Defined | Security integration suite |
| KBD-014 | Reinstall creates new installation lineage without a duplicate physical device | Defined | Reinstall and repair test |
| KBD-015 | Retired keyboard surface remains in lineage but loses authority | Defined | Registry and credential tests |
| KBD-016 | Custom glyph compatibility limitations are disclosed | Defined | UX review across supported/unsupported apps |
| KBD-017 | Protected mode and route state remain accessible and visually distinguishable | Defined | Accessibility audit |
| KBD-018 | Configuration change and process death do not expose or corrupt content | Defined | Lifecycle test suite |
| KBD-019 | Debug packages cannot use production credentials | Defined | Build-variant security test |
| KBD-020 | No source implementation, build, release, or deployment is authorized by this contract | Confirmed | Checkpoint audit |

## Gate rule

No implementation checkpoint may claim completion until each applicable row has concrete evidence, an evidence location, and a pass/fail result. A design statement alone is not implementation evidence.
