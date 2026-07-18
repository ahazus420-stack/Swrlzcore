# SWRLZ Keyboard Verification Strategy

Status: scaffold

## Test lanes

- Unit: policy classification, state machines, routing, identifiers, and redaction.
- Android instrumentation: IME lifecycle, editor actions, rotations, process death, configuration changes, and accessibility.
- Integration: CLIENT enrollment, revocation, offline queueing, lineage, and scoped credentials.
- Privacy: sensitive-field suppression, clipboard boundaries, log redaction, and no-content telemetry.
- Security: package/signing validation, replay rejection, retired credential rejection, and cross-surface isolation.
- Compatibility: supported Android API levels, OEM keyboards/settings flows, work profiles, and multi-user behavior.
- Performance: input latency, memory, battery, cold start, and candidate rendering.
- Resilience: CLIENT absent, NODE_HOST absent, server unavailable, corrupt local state, and interrupted enrollment.
- Accessibility: TalkBack, switch access, font scaling, contrast, touch targets, and nonvisual consent state.

## Evidence requirements

Every accepted behavior requires:

1. contract identifier;
2. test identifier;
3. environment and build identity;
4. expected and observed result;
5. logs with user text redacted;
6. pass/fail disposition;
7. reviewer and timestamp.

No APK build or test execution is authorized by this scaffold.