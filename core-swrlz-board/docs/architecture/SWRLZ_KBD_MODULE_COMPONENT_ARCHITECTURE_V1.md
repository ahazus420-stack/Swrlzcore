# SWRLZ Keyboard Module and Android Component Architecture V1

Status: accepted design contract
Checkpoint: SWRLZ-KBD-MOD-001
Version: 1.0
Implementation authorization: none

## 1. Purpose

Define implementation-ready Kotlin, Gradle, Android-component, dependency, process, storage, security, and test boundaries for the SWRLZ Android keyboard while preserving offline-first typing, CLIENT authority, Truth Firewall enforcement, identity lineage, and protocol-version discipline.

## 2. Required Gradle modules

- `:core-swrlz-board:keyboard:app` — installable IME application, manifest, settings entry point, service registration, composition root.
- `:core-swrlz-board:keyboard:ime` — `InputMethodService`, editor-session lifecycle, input connection adapter, sensitive-context gate.
- `:core-swrlz-board:keyboard:ui` — Compose keyboard surface, candidate strip, tool panel, accessibility semantics, themes.
- `:core-swrlz-board:keyboard:domain` — pure Kotlin policies, state machines, use cases, command models, route decisions.
- `:core-swrlz-board:keyboard:data` — repositories, local persistence adapters, migration orchestration, queued operational evidence.
- `:core-swrlz-board:keyboard:security` — keystore facade, credential envelope handling, caller/package verification helpers, redaction policy.
- `:core-swrlz-board:keyboard:client-bridge` — Binder client, protocol negotiation, enrollment, revocation, health, cancellation.
- `:core-swrlz-board:keyboard:contracts` — versioned DTOs and interfaces shared across keyboard modules only.
- `:core-swrlz-board:shared:identity-contracts` — accepted device/surface/install/session identity semantics.
- `:core-swrlz-board:shared:design-system` — typography, glyphs, dimensions, icons, motion, color tokens.
- `:core-swrlz-board:shared:telemetry-contracts` — content-free event schemas and validation.
- `:core-swrlz-board:keyboard:test-fixtures` — fake editor contexts, Binder fakes, policy vectors, deterministic clocks/nonces.

## 3. Dependency direction

Allowed direction:

`app -> ime/ui/client-bridge/data/security/domain/contracts/shared`

`ime -> domain/contracts/client-bridge/security`

`ui -> domain/contracts/shared-design-system`

`client-bridge -> domain/contracts/security/shared-identity`

`data -> domain/contracts/security/shared-telemetry`

`security -> contracts`

`domain -> contracts`

Pure domain modules must not depend on Android framework, Compose, Binder, Room, DataStore, network clients, or Hilt.

Circular Gradle dependencies are prohibited.

## 4. Android component boundaries

### 4.1 `SwrlzInputMethodService`

Responsible for:

- Android IME lifecycle;
- creating and closing editor sessions;
- reading only the minimum required `EditorInfo` metadata;
- invoking local policy classification before exposing SWRLZ actions;
- committing ordinary key events directly through `InputConnection`;
- delegating explicit SWRLZ operations to domain use cases;
- clearing ephemeral editor state when the input target changes.

It must not own enrollment authority, persistent credentials, remote routing policy, analytics uploading, or canonical device identity.

### 4.2 Settings and onboarding activity

Responsible for:

- explaining activation and keyboard selection;
- displaying enrollment and trust state;
- presenting privacy controls and route disclosures;
- exposing diagnostics that contain no typed content;
- linking to Android IME enable/select surfaces.

It must not bypass CLIENT approval or mint credentials.

### 4.3 Binder client

A keyboard-side client binds explicitly to the CLIENT-owned broker defined by `SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md`.

It must:

- verify the resolved CLIENT package and signing identity before binding;
- negotiate protocol/schema versions;
- use bounded asynchronous requests;
- enforce timeout and cancellation;
- fail closed for privileged SWRLZ actions;
- leave ordinary typing unaffected when unavailable.

No exported keyboard-owned Binder service is required for v1.

## 5. Process model

V1 uses the keyboard application's default process for the IME and local keyboard modules. A separate process is not required.

A future process split requires a separate checkpoint because it changes memory boundaries, initialization, IPC, failure modes, and attack surface.

Long-running AI execution is prohibited inside the IME process. Execution belongs to approved CLIENT/NODE_HOST routes.

## 6. State ownership

- Editor-session state: in-memory, scoped to the active editor session.
- UI state: lifecycle-scoped and reconstructable.
- Keyboard preferences: encrypted DataStore owned by `keyboard:data`.
- Enrollment metadata: repository abstraction in `keyboard:data`; secrets delegated to `keyboard:security`.
- Surface credentials/private keys: Android Keystore, non-exportable where platform capability permits.
- Operational queue: bounded local store; content fields prohibited.
- Canonical device and trust registry: CLIENT-owned, never duplicated by the keyboard.
- Dictionary and language assets: local, versioned, independently replaceable, with no implicit cloud synchronization.

Room is permitted only where relational querying, migration, and bounded operational queues justify it. Simple preferences must use DataStore rather than Room.

## 7. Dependency injection

Hilt is the preferred composition mechanism for Android modules.

Required scopes:

- singleton scope for process-wide repositories, keystore facade, Binder connection coordinator, clocks, and protocol codec;
- service scope or explicit IME session scope for editor-session coordinators;
- activity-retained/view-model scopes for settings UI;
- no static service locator;
- no Context retained by domain objects.

The service must tolerate process recreation and rehydrate only non-sensitive durable state.

## 8. Core domain interfaces

Implementation must provide interfaces equivalent in responsibility to:

- `EditorContextClassifier`
- `SensitiveFieldPolicy`
- `KeyboardSessionCoordinator`
- `ExplicitActionAuthorizer`
- `RouteResolver`
- `TruthFirewallEvaluator`
- `EnrollmentRepository`
- `SurfaceCredentialStore`
- `ClientBridge`
- `OperationalEvidenceSink`
- `Clock`
- `NonceSource`

Names may change during implementation, but responsibilities and dependency direction may not be collapsed without a new accepted decision.

## 9. Sensitive editor policy

Sensitive-field classification executes locally before text can enter any SWRLZ action pipeline.

Protected or ambiguous-sensitive contexts must:

- disable transformation, generation, remote, clipboard-assist, and telemetry paths involving content;
- avoid retaining composing text beyond Android input requirements;
- show an understandable disabled state;
- fail closed when editor classification is uncertain.

Ordinary key entry remains available.

## 10. Offline-first behavior

The keyboard must launch and provide ordinary typing with:

- CLIENT absent;
- Binder unavailable;
- NODE_HOST absent;
- LAN unavailable;
- internet unavailable;
- enrollment expired or revoked.

Only privileged SWRLZ actions may degrade. Degradation must be explicit and must never silently redirect to a remote provider.

## 11. Build variants and feature flags

Minimum variants:

- `debug` — developer diagnostics, fake bridge support, verbose content-free logs;
- `release` — production signing and diagnostics restrictions.

Optional internal variants may be added later, but production behavior must not depend on debug-only bypasses.

Feature flags must be typed, locally inspectable, default-safe, and incapable of disabling the Truth Firewall, sensitive-field policy, signature verification, or remote-consent requirements.

## 12. Logging and telemetry

Logs and telemetry must exclude:

- committed text;
- composing text;
- selections;
- clipboard content;
- generated text;
- prompts;
- passwords, OTPs, payment data, and editor content snapshots.

Permitted evidence includes protocol version, categorical route, error code, duration bucket, enrollment state, and content-length bucket where approved by the telemetry contract.

## 13. Testing architecture

- `domain`: JVM unit tests for state machines, policy tables, route decisions, and Truth Firewall outcomes.
- `security`: JVM/Android tests for envelope validation, redaction, keystore failure, replay rejection.
- `client-bridge`: contract tests with fake Binder endpoints and version mismatch vectors.
- `ime`: instrumentation tests with controlled `EditorInfo` and `InputConnection` fakes.
- `ui`: Compose tests for key actions, protected-state affordances, accessibility, and restoration.
- `data`: migration, corruption, quota, and bounded-queue tests.
- `app`: end-to-end activation/enrollment tests only after implementation authorization.

Test fixtures must never contain real user secrets or production credentials.

## 14. Package structure

Recommended root namespace:

`com.swrlz.board.keyboard`

Recommended package lanes:

- `.app`
- `.ime`
- `.ui`
- `.domain`
- `.data`
- `.security`
- `.bridge`
- `.contracts`
- `.telemetry`
- `.testing`

Final application ID and signing lineage remain implementation-gated decisions.

## 15. Non-goals

This checkpoint does not authorize:

- creation of Gradle modules;
- Kotlin source;
- manifests, services, activities, providers, receivers, or permissions;
- application IDs or signing keys;
- Room/DataStore schemas;
- APK builds, tests, workflows, merges, releases, or deployment.

## 16. Compatibility

This architecture is subordinate to and must remain compatible with:

- `SWRLZ_IDENTITY_SURFACES_CONTRACT_V1.md`;
- `SWRLZ_KBD_CONTRACT_V1.md`;
- `SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md`.

Where an implementation choice conflicts with an accepted contract, the accepted contract wins and implementation must stop pending a bounded decision.
