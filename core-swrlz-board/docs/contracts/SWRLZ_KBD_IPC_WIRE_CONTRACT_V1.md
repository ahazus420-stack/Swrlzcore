# SWRLZ Keyboard IPC and Enrollment Wire Contract v1

- Checkpoint: `SWRLZ-KBD-IPC-001`
- Status: Accepted design contract
- Contract version: 1
- Schema version: 1
- Implementation authorization: Not authorized
- Scope: Local Android IPC between SWRLZ CLIENT and SWRLZ Keyboard

## 1. Purpose

This contract defines the concrete local wire boundary used to enroll, authenticate, operate, revoke, and recover the SWRLZ Keyboard as a scoped surface beneath the canonical SWRLZ CLIENT device identity.

The contract preserves offline-first behavior, explicit trust, Truth Firewall authority, lineage, local-versus-remote distinctions, and protocol-version discipline.

## 2. Selected IPC architecture

The preferred v1 mechanism is an explicit bound Android service owned by SWRLZ CLIENT and accessed only through an exported, signature-protected Binder boundary.

Normative requirements:

- CLIENT owns the enrollment and command-broker service.
- Keyboard acts as the requesting client.
- The service must require a custom signature-level permission.
- CLIENT must verify caller UID, package name, and signing-certificate digest on every security-sensitive call.
- Package-signature continuity alone does not grant enrollment or operation authority.
- Dynamic broadcast receivers, implicit intents, world-readable files, clipboard handoff, and localhost HTTP are not accepted as the primary v1 enrollment channel.
- Binder payloads must remain bounded and must never carry unrestricted keystroke streams.

A future transport may be added only through a later protocol-version checkpoint.

## 3. Trust roles

### CLIENT

CLIENT is the canonical Device Identity Broker and local policy authority. It owns:

- device identity association;
- surface enrollment approval;
- scoped credential issuance;
- credential rotation and revocation;
- Truth Firewall policy decisions;
- local route authorization;
- lineage records;
- user-visible repair and recovery.

### Keyboard

Keyboard owns:

- its package-local installation identity;
- IME lifecycle and ordinary typing;
- explicit action requests;
- protected-field suppression;
- local UI state;
- secure storage of its scoped credential handle.

Keyboard is not a NODE_HOST, device identity authority, or autonomous remote-routing authority.

## 4. Service identity

The implementation must define one explicit CLIENT service component with a stable contract name and one signature-level permission.

Conceptual identifiers:

```text
service: com.swrlz.client.ipc.KeyboardBrokerService
permission: com.swrlz.permission.BIND_KEYBOARD_BROKER
interface: ISwrlzKeyboardBrokerV1
```

Exact package names remain implementation-time constants, but changing them after release requires migration documentation.

## 5. Envelope

Every request and response uses a versioned envelope.

```json
{
  "protocolFamily": "keyboard-ipc",
  "protocolVersion": 1,
  "schemaVersion": 1,
  "requestId": "uuid",
  "timestampEpochMs": 0,
  "nonce": "base64url-random",
  "callerPackage": "string",
  "callerInstallationId": "swrlz-install-...",
  "surfaceInstanceId": "swrlz-surface-keyboard-... or null",
  "operation": "string",
  "payload": {}
}
```

Requirements:

- `requestId` must be unique per request.
- `nonce` must contain at least 128 bits of cryptographically secure randomness.
- CLIENT must reject stale timestamps outside the accepted clock-skew window.
- CLIENT must reject replayed nonces and duplicate request IDs within the replay-retention window.
- Unknown fields must be ignored only when explicitly forward-compatible; unknown required operations must fail closed.
- Raw typed text must never appear in generic envelope logs.

## 6. Enrollment operations

### 6.1 `beginEnrollment`

Keyboard submits:

```json
{
  "requestedSurfaceType": "keyboard",
  "installationId": "swrlz-install-...",
  "packageName": "string",
  "signingCertificateSha256": ["hex"],
  "requestedScopes": ["string"],
  "keyboardContractVersion": 1,
  "capabilities": ["glyphs", "local-transform"]
}
```

CLIENT validates:

- caller UID resolves to the declared package;
- package is installed and enabled;
- signing certificate is accepted by current signing policy;
- installation ID format and persistence rules are valid;
- requested surface type is `keyboard`;
- requested scopes are within the allowlist;
- no conflicting active enrollment exists without lineage handling.

Successful response returns an enrollment challenge, not an active credential.

### 6.2 `confirmEnrollment`

Enrollment must require explicit user confirmation in CLIENT unless an accepted recovery policy states otherwise.

The confirmation response includes:

```json
{
  "enrollmentId": "uuid",
  "deviceId": "swrlz-device-...",
  "surfaceInstanceId": "swrlz-surface-keyboard-...",
  "credentialHandle": "opaque-reference",
  "grantedScopes": ["string"],
  "lifecycleState": "active",
  "issuedAtEpochMs": 0,
  "expiresAtEpochMs": 0,
  "contractVersion": 1
}
```

The credential material itself must be non-exportable where Android-backed key storage permits. The wire response should return an opaque handle or wrapped credential, not a reusable plaintext secret.

### 6.3 `getEnrollmentState`

Returns only the caller's own keyboard-surface state:

```text
pending_enrollment
active
suspended
revoked
retired
replaced
recovery_required
```

### 6.4 `rotateCredential`

Rotation requires:

- valid current credential or explicit CLIENT recovery approval;
- fresh nonce and request ID;
- lineage record linking prior and replacement credential states;
- revocation of the superseded credential after the overlap window.

### 6.5 `retireSurface`

Retirement preserves lineage and history. It must not delete the physical device, CLIENT, launcher, NODE_HOST, or historical keyboard record.

## 7. Operational calls

Accepted v1 operation families:

```text
getBrokerStatus
getKeyboardPolicySnapshot
requestTextTransform
cancelOperation
getOperationStatus
acknowledgeResult
getGlyphManifest
reportContentFreeEvent
```

No generic arbitrary-command endpoint is permitted.

## 8. Text-transform request

A transform request is allowed only after an explicit user action in the keyboard UI.

```json
{
  "operationId": "uuid",
  "actionType": "rewrite|summarize|translate|custom-approved",
  "inputClassification": "ordinary|sensitive-denied|ambiguous-denied",
  "routePreference": "local-only|local-preferred|approved-remote",
  "textPayload": "present only after explicit consent",
  "textSha256": "optional diagnostic hash",
  "clientPolicyVersion": 1,
  "userConsentReceiptId": "uuid"
}
```

Requirements:

- Password, payment-secret, OTP, authentication-code, and denied-sensitive fields must never send text.
- `approved-remote` requires a distinct user-visible consent event and Truth Firewall approval.
- Failure of local processing must not silently fall back to LAN or remote processing.
- CLIENT must return the selected route classification in the response.
- Results must be returned only to the requesting keyboard surface.
- Payload logging is prohibited by default.

## 9. Result model

```json
{
  "operationId": "uuid",
  "status": "accepted|running|completed|cancelled|denied|failed",
  "routeUsed": "keyboard-local|client-local|node-host-lan|remote-approved|none",
  "resultText": "present only for completed authorized transforms",
  "policyDecision": "allow|deny|require-confirmation",
  "error": null,
  "completedAtEpochMs": 0
}
```

The route distinction is normative and must not be collapsed into a generic `online` flag.

## 10. Error model

Errors use stable machine codes and human-safe summaries.

Required v1 codes:

```text
IPC_UNAVAILABLE
CALLER_NOT_ALLOWED
PACKAGE_UID_MISMATCH
SIGNATURE_MISMATCH
PROTOCOL_UNSUPPORTED
SCHEMA_UNSUPPORTED
REQUEST_REPLAYED
REQUEST_STALE
MALFORMED_REQUEST
PAYLOAD_TOO_LARGE
NOT_ENROLLED
ENROLLMENT_PENDING
SURFACE_SUSPENDED
SURFACE_REVOKED
SURFACE_RETIRED
CREDENTIAL_INVALID
CREDENTIAL_EXPIRED
SCOPE_DENIED
SENSITIVE_FIELD_DENIED
CONSENT_REQUIRED
TRUTH_FIREWALL_DENIED
LOCAL_ROUTE_UNAVAILABLE
REMOTE_ROUTE_NOT_AUTHORIZED
OPERATION_NOT_FOUND
OPERATION_CANCELLED
INTERNAL_FAILURE
```

Errors must not expose private text, cryptographic material, stack traces, filesystem paths, or internal server topology.

## 11. Timeouts and cancellation

- Enrollment request timeout: implementation-defined, documented, and user-visible when exceeded.
- Binder synchronous calls should be short and non-blocking.
- Long-running transforms must return an operation ID and proceed asynchronously.
- Keyboard may call `cancelOperation` at any time.
- CLIENT must make cancellation best-effort and return the final state.
- Process death must not convert an unapproved operation into an approved one.

## 12. Payload limits

V1 limits must be declared centrally and tested.

Recommended initial ceilings:

```text
Envelope metadata: <= 32 KiB
Explicit text-transform input: <= 64 KiB UTF-8
Result text: <= 64 KiB UTF-8
Glyph manifest metadata: <= 128 KiB
```

Larger transfers require a later bounded transport contract. Binder transaction failure must be handled as a typed error, not a crash.

## 13. Credential storage

- Keyboard stores only its own scoped credential material or opaque handle.
- Credential keys should use Android Keystore when available.
- Credentials must not be written to logs, backups, exported preferences, clipboard, or world-readable storage.
- Backup/restore must not silently clone a credential to another physical device.
- Reinstall creates a new installation identity and requires lineage-aware re-enrollment.

## 14. Offline and unavailable behavior

Ordinary typing must continue when CLIENT is absent, stopped, updating, crashed, or unreachable.

When CLIENT IPC is unavailable:

- ordinary typing remains functional;
- keyboard-local features may continue if authorized locally;
- CLIENT-dependent actions show an unavailable state;
- no duplicate device or surface identity is created;
- no silent direct server connection is attempted by the keyboard;
- pending requests are not replayed unless they remain valid and the user explicitly retries or accepted queue policy allows it.

## 15. Truth Firewall

CLIENT remains the policy authority for any action that leaves keyboard-local execution.

The keyboard must preserve and display:

- deny decisions;
- require-confirmation decisions;
- selected route;
- local-versus-remote status;
- unresolved objections or safety alternatives.

No protocol message may encode obedience-only behavior or bypass valid dissent.

## 16. Telemetry

Permitted telemetry is content-free and includes identifiers at the correct aggregation level.

Permitted examples:

```text
enrollment_started
enrollment_approved
enrollment_denied
credential_rotated
surface_suspended
surface_revoked
transform_requested
transform_completed
transform_denied
ipc_unavailable
protocol_mismatch
```

Prohibited telemetry includes:

- keystroke streams;
- raw text payloads;
- password or OTP content;
- clipboard history;
- reconstructed text from event timing;
- treating installation or surface IDs as separate physical-device counts.

## 17. Version negotiation

Keyboard begins with `getBrokerStatus` and declares supported protocol and schema ranges.

CLIENT returns:

```json
{
  "protocolFamily": "keyboard-ipc",
  "supportedProtocolMin": 1,
  "supportedProtocolMax": 1,
  "supportedSchemaMin": 1,
  "supportedSchemaMax": 1,
  "requiredKeyboardContractMin": 1
}
```

No compatible overlap means fail closed with `PROTOCOL_UNSUPPORTED` or `SCHEMA_UNSUPPORTED`.

## 18. Security invariants

- Caller identity is verified from Binder/OS identity, not trusted from payload declarations.
- Every privileged operation checks enrollment state, credential, and scope.
- Replay protection is mandatory.
- Enrollment and recovery require explicit visible authority.
- Keyboard cannot enumerate other surfaces or credentials.
- CLIENT cannot request continuous keystroke capture through this contract.
- Unknown operations fail closed.
- Sensitive-field denial cannot be overridden by remote policy.
- Discovery is not authorization.
- Shared device identity does not imply shared unrestricted trust.

## 19. Compatibility

This contract extends the accepted identity-surfaces and keyboard privacy contracts. It does not replace NODE_HOST discovery, node identity, CLIENT registry behavior, or server schemas.

Implementation must integrate with canonical CLIENT and SERVER sources rather than overwrite them.

## 20. Acceptance condition

This design contract is complete when the accompanying acceptance matrix is reviewable and all future implementation checkpoints can map code and evidence to its clauses.

No source implementation, manifest change, APK build, workflow execution, commit to `main`, release, or deployment is authorized by this contract.