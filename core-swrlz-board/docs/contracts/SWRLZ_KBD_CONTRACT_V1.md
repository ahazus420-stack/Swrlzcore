# SWRLZ Keyboard Trust, Privacy, and CLIENT Enrollment Contract

- **Checkpoint:** SWRLZ-KBD-CON-001
- **Contract family:** Keyboard / Android IME
- **Contract version:** 1
- **Schema version:** 1
- **Status:** Accepted design contract
- **Implementation authorization:** Not authorized by this contract
- **Applies to:** SWRLZ Keyboard surface, SWRLZ CLIENT identity broker, local integration boundary, optional NODE_HOST routing

## 1. Purpose

This contract defines the mandatory trust, privacy, identity, enrollment, routing, telemetry, and failure behavior for a SWRLZ Android keyboard implemented as an Input Method Editor (IME).

The keyboard is a trusted user-facing surface, but it is not a physical device, not a NODE_HOST, and not an independent authority over SWRLZ identity or policy.

## 2. Normative language

The terms **MUST**, **MUST NOT**, **SHOULD**, **SHOULD NOT**, and **MAY** are normative requirements.

## 3. Governing invariants

The implementation MUST preserve all of the following:

1. Offline-first operation.
2. Explicit local-versus-remote distinction.
3. Identity, trust, and authorization separation.
4. Truth Firewall dissent and objection behavior.
5. No obedience-only mode.
6. No paid runtime loop without explicit user authorization.
7. No silent text capture, storage, or transmission.
8. No device-count inflation caused by the keyboard surface.
9. Durable lineage for enrollment, reinstall, revocation, and retirement.
10. Protocol- and schema-version discipline.

## 4. System roles

### 4.1 SWRLZ Keyboard

The keyboard:

- provides ordinary text entry;
- provides optional SWRLZ actions initiated by the user;
- renders approved fonts, glyphs, symbols, and reaction assets;
- holds only a surface-scoped credential;
- MUST NOT become the root device identity authority;
- MUST NOT classify itself as a node unless a future accepted contract explicitly authorizes node capability.

### 4.2 SWRLZ CLIENT

The CLIENT is the initial Device Identity Broker and user-facing trust authority for keyboard enrollment.

The CLIENT:

- owns or resolves the canonical `deviceId`;
- approves, denies, suspends, revokes, retires, and repairs keyboard enrollment;
- issues surface-scoped credentials;
- presents clear user-visible trust state;
- MUST NOT silently grant the keyboard all CLIENT privileges.

### 4.3 NODE_HOST

NODE_HOST is an optional processing target.

The keyboard:

- MAY request local processing through accepted CLIENT/NODE_HOST contracts;
- MUST NOT discover-and-use a NODE_HOST as implicit authorization;
- MUST expose whether a request is local, LAN, or remote before execution when that distinction affects privacy, cost, or trust.

### 4.4 Server

The server:

- records the keyboard as a surface beneath an existing `deviceId`;
- MUST NOT count the keyboard as a separate physical device;
- MUST aggregate telemetry by the correct identity dimension;
- MUST reject a keyboard credential presented as a CLIENT, launcher, or node credential.

## 5. Identity model

The keyboard MUST use the accepted device-surface identity hierarchy:

- `deviceId`: enrolled physical Android device within the SWRLZ trust domain;
- `surfaceType`: `keyboard`;
- `surfaceInstanceId`: logical keyboard enrollment;
- `installationId`: concrete keyboard application installation;
- `sessionId`: ephemeral keyboard process or interaction session;
- `nodeId`: absent unless a future contract explicitly grants node capability.

### 5.1 Reinstall behavior

A clean keyboard reinstall or app-data wipe MUST:

- create a new `installationId`;
- require enrollment repair or re-enrollment;
- preserve the prior keyboard surface and installation as retired or replaced lineage;
- retain the same physical `deviceId` only after approved recovery or re-enrollment;
- never create a second active physical-device record merely because the package was reinstalled.

## 6. Enrollment state machine

The keyboard enrollment state MUST be one of:

- `unavailable`
- `not_enrolled`
- `pending_user_approval`
- `pending_client_confirmation`
- `active`
- `suspended`
- `revoked`
- `retired`
- `recovery_required`
- `incompatible`

Allowed high-level transitions:

```text
not_enrolled
  -> pending_user_approval
  -> pending_client_confirmation
  -> active

active -> suspended -> active
active -> revoked
active -> retired
active -> recovery_required
recovery_required -> pending_user_approval
revoked -> pending_user_approval
```

The keyboard MUST fail closed for privileged SWRLZ actions unless state is `active`.

Ordinary local typing MUST remain available whenever Android permits the IME to operate, even if SWRLZ enrollment is unavailable.

## 7. Enrollment protocol requirements

A keyboard enrollment request MUST contain:

- protocol version;
- schema version;
- surface type `keyboard`;
- keyboard package name;
- signing-certificate lineage evidence or an approved equivalent;
- keyboard `installationId`;
- requested scopes;
- user-visible keyboard display name;
- nonce or replay-resistant challenge data;
- creation timestamp;
- optional prior lineage references.

The CLIENT enrollment decision MUST contain:

- approved or denied state;
- canonical `deviceId` when approved;
- assigned `surfaceInstanceId`;
- granted scopes;
- credential identifier;
- credential expiry or rotation policy;
- contract and schema versions;
- audit timestamp;
- denial or repair reason when applicable.

Enrollment MUST require deliberate user approval in the CLIENT. Package signature matching alone MUST NOT silently authorize enrollment.

## 8. Credential model

The keyboard credential MUST be:

- scoped to `surfaceType=keyboard`;
- bound to the current `surfaceInstanceId` and `installationId`;
- revocable independently of the phone and NODE_HOST;
- stored using Android-protected app-private storage;
- unusable by unrelated packages;
- rotated after material trust changes;
- invalidated when enrollment is revoked or retired.

Initial allowable scopes MAY include:

- `keyboard.status.read`
- `keyboard.profile.read`
- `keyboard.glyphs.read`
- `keyboard.local_transform.request`
- `keyboard.remote_transform.request`
- `keyboard.approval.present`
- `keyboard.telemetry.operational.write`

The keyboard MUST NOT receive registry administration, node administration, release, deployment, billing, unrestricted filesystem, unrestricted clipboard, or unrestricted mission-execution authority.

## 9. Input classification

The keyboard MUST classify the active editor context before exposing or executing SWRLZ actions.

Required classes:

- `ordinary_text`
- `multiline_text`
- `search_or_url`
- `email_or_recipient`
- `numeric`
- `phone`
- `password_or_secret`
- `payment_or_financial_secret`
- `one_time_code`
- `unknown_sensitive`

### 9.1 Sensitive classes

For `password_or_secret`, `payment_or_financial_secret`, `one_time_code`, and `unknown_sensitive`:

- AI transformation actions MUST be disabled;
- remote processing MUST be unavailable;
- clipboard history MUST NOT be captured;
- typed content MUST NOT be logged;
- preview surfaces MUST avoid retaining content;
- the UI SHOULD visibly indicate protected mode without revealing field content.

The implementation MUST prefer false-positive protection over false-negative exposure when editor metadata is ambiguous.

## 10. Ordinary typing boundary

Ordinary typing is not a SWRLZ mission.

The keyboard MUST NOT:

- transmit ordinary keystrokes;
- create a persistent keystroke log;
- store complete field contents by default;
- infer consent from keyboard activation;
- convert every keystroke into an AI or analytics event;
- require CLIENT or NODE_HOST availability for standard typing.

Autocorrect, suggestions, and personalization MUST be separately classified as local, user-enabled features. Their storage and retention rules MUST be explicit.

## 11. Explicit SWRLZ actions

A SWRLZ action begins only after deliberate user interaction, such as selecting text and pressing a named action.

Initial action classes MAY include:

- rewrite;
- summarize;
- expand;
- shorten;
- tone adjustment;
- spelling and grammar review;
- translate;
- ask SWRLZ;
- save selected text to an approved CLIENT destination;
- insert an approved glyph, sticker, or reaction asset.

Every text-transform action MUST display:

- the selected source text boundary;
- the requested operation;
- the selected execution route;
- whether content leaves the device;
- any paid or metered implication;
- an explicit execute control.

Generated output MUST remain previewable before insertion unless the action is deterministic and locally reversible.

## 12. Local, LAN, and remote routing

Execution routes are distinct trust states:

- `local_keyboard`: processing entirely inside the keyboard package;
- `local_client`: processing through the local CLIENT;
- `local_node`: processing through an explicitly trusted local NODE_HOST;
- `lan_node`: processing through an explicitly trusted LAN node;
- `remote_service`: processing through an approved remote service;
- `unavailable`: no approved route.

The keyboard MUST NOT silently fall back from local to remote.

A route change that increases exposure, cost, latency, or authority MUST require explicit user confirmation.

When offline:

- ordinary typing MUST continue;
- locally supported transformations MAY continue;
- unavailable actions MUST report unavailable state honestly;
- requests MAY be saved as drafts only with explicit user approval;
- queued text MUST NOT be silently transmitted later.

## 13. Truth Firewall behavior

The keyboard MUST preserve Truth Firewall objection and dissent behavior.

For an unsafe, contradictory, unauthorized, or privacy-increasing request, the keyboard or downstream SWRLZ component MUST be able to:

- refuse;
- warn;
- request clarification;
- propose a safer local alternative;
- pause pending approval;
- preserve the objection in the resulting audit state.

No style, theme, hidden mode, developer option, or user preference may disable the Truth Firewall entirely.

## 14. Clipboard contract

Clipboard access MUST be user-visible, purpose-limited, and time-bounded.

The keyboard:

- MUST NOT continuously harvest clipboard data;
- MUST NOT retain clipboard entries by default;
- MUST respect Android sensitive-clipboard indicators and expiration behavior;
- MUST require deliberate user action to show clipboard content;
- MUST exclude password, payment-secret, one-time-code, and protected content from history;
- MUST provide a clear control to clear any optional local clipboard history.

Optional clipboard history MUST be local-first, encrypted at rest, disabled by default, and governed by a separate retention setting.

## 15. Voice input boundary

Voice input, if implemented, is a distinct capture mode.

It MUST:

- display an unmistakable active-capture indicator;
- stop immediately on user command;
- disclose local versus remote transcription;
- never activate silently;
- avoid protected editor contexts;
- define temporary audio retention and deletion behavior before implementation.

Voice implementation is not authorized by this contract.

## 16. Fonts, glyphs, emoji, and stickers

The keyboard MAY bundle SWRLZ fonts, glyphs, icons, emoji-like assets, and stickers.

Rules:

- custom Private Use Area glyphs MUST be labeled as compatibility-limited;
- the keyboard MUST NOT imply that unsupported apps will render custom glyphs correctly;
- sticker/image insertion MUST use Android-supported content-commit paths where available;
- plain-text fallbacks SHOULD exist for critical symbols;
- fonts and assets MUST have documented origin, license, checksum, and version lineage before release;
- visual themes MUST NOT obscure protected mode, route state, consent, or warnings.

## 17. Telemetry contract

Telemetry MUST be content-free by default.

Allowed operational events include:

- keyboard enabled or disabled;
- enrollment state changed;
- surface credential rotated;
- action type requested;
- route class selected;
- action succeeded, failed, cancelled, or refused;
- glyph pack changed;
- protected mode entered;
- compatibility error encountered.

Forbidden default telemetry includes:

- raw keystrokes;
- full source text;
- full generated text;
- password or secret contents;
- clipboard contents;
- contact, recipient, URL, or message bodies;
- screenshots of typed content;
- per-word behavioral profiling.

Telemetry records MUST carry `deviceId`, `surfaceInstanceId`, `installationId`, `surfaceType`, protocol version, schema version, event type, timestamp, and result state as applicable.

## 18. Storage and retention

The keyboard MAY persist only data required for:

- enrollment and scoped credentials;
- user settings;
- theme and layout preferences;
- approved dictionaries or personalization;
- content-free operational audit records;
- explicitly saved drafts or clipboard history.

User content retention MUST be opt-in, bounded, inspectable, and deletable.

Credential data and sensitive settings MUST use encrypted app-private storage where supported.

Clearing keyboard data MUST invalidate local credentials and require repair or re-enrollment.

## 19. Failure and mismatch behavior

Privileged SWRLZ actions MUST fail closed when:

- CLIENT identity cannot be verified;
- credential scope is insufficient;
- installation identity changes unexpectedly;
- signing lineage conflicts;
- the surface is suspended, revoked, or retired;
- protocol or schema versions are incompatible;
- route trust cannot be established;
- sensitive editor classification prohibits processing;
- consent is absent or stale.

Failure MUST produce an actionable, non-deceptive state such as:

- reconnect to CLIENT;
- review enrollment;
- update required;
- local route unavailable;
- remote route requires approval;
- protected field: action disabled.

Ordinary typing SHOULD remain functional despite SWRLZ integration failure.

## 20. User controls

The keyboard settings surface MUST provide:

- enrollment status;
- active device and surface identity summary;
- granted scopes;
- local versus remote route preferences;
- AI actions enabled/disabled;
- telemetry enabled/disabled where policy permits;
- clipboard-history state;
- personalization retention controls;
- glyph and theme selection;
- clear local data;
- open CLIENT trust management;
- revoke keyboard enrollment.

Security-critical controls MUST NOT be hidden exclusively behind Easter eggs or developer-style unlock sequences.

## 21. Android package and process boundary

The recommended implementation is a separate keyboard application package or separately sandboxed deliverable sharing accepted libraries and signing lineage.

The keyboard MUST NOT read CLIENT private storage directly.

CLIENT integration MUST use an explicit, permission-protected IPC mechanism with:

- package and signature verification;
- challenge-response or equivalent replay resistance;
- schema-version checks;
- bounded request sizes;
- timeouts;
- cancellation;
- audit-safe error codes;
- no implicit exported component access.

The exact IPC technology remains an implementation decision requiring a later checkpoint.

## 22. Security requirements

The implementation MUST include defenses for:

- malicious apps impersonating CLIENT;
- malicious apps binding to keyboard integration services;
- replayed enrollment responses;
- stolen or copied surface credentials;
- downgrade to older incompatible contracts;
- oversized or malformed text requests;
- denial of service through repeated IPC calls;
- accidental text leakage through logs, crash reports, previews, or backups;
- overlay and tapjacking risks on consent surfaces;
- debug builds accepting production credentials.

Production credentials MUST be environment-bound and MUST NOT be accepted by untrusted debug packages.

## 23. Testing obligations

Implementation authorization MUST require evidence for at least:

1. ordinary typing with CLIENT absent;
2. protected-field suppression;
3. no raw text in logs or telemetry;
4. enrollment approval, denial, revocation, repair, and reinstall lineage;
5. one device with CLIENT plus keyboard counted as one physical device;
6. credential scope enforcement;
7. local-to-remote fallback prevention;
8. offline behavior;
9. Truth Firewall refusal persistence;
10. malformed IPC and signature-mismatch rejection;
11. clipboard non-harvesting behavior;
12. accessibility and screen-reader operation;
13. locale, RTL, orientation, and configuration changes;
14. process death and state restoration;
15. uninstall and retirement behavior.

## 24. Out of scope

This contract does not authorize or fully specify:

- keyboard source implementation;
- Android manifest entries;
- concrete IPC classes;
- server database migrations;
- launcher implementation;
- voice transcription implementation;
- cloud-provider selection;
- model selection;
- font or sticker binary production;
- APK build, install, signing, distribution, release, or deployment.

## 25. Acceptance criteria

This contract is satisfied at design level when:

- identity roles are unambiguous;
- keyboard enrollment is explicit and revocable;
- ordinary typing is independent from SWRLZ availability;
- sensitive fields disable AI and remote processing;
- remote routing never occurs silently;
- telemetry is content-free by default;
- one keyboard surface does not increase physical-device count;
- Truth Firewall behavior remains armed;
- reinstall and retirement preserve lineage;
- implementation remains blocked behind a later explicit checkpoint.

## 26. Required follow-on checkpoints

Recommended sequence:

1. `SWRLZ-KBD-IPC-001` — select and define CLIENT-keyboard IPC and enrollment wire schema.
2. `SWRLZ-KBD-UX-001` — define layouts, protected mode, route indicators, consent, and accessibility.
3. `SWRLZ-KBD-IMPL-001` — authorize bounded keyboard skeleton implementation only.
4. `SWRLZ-KBD-VERIFY-001` — authorize build and verification evidence only.

No follow-on checkpoint is implicitly approved by acceptance of this contract.
