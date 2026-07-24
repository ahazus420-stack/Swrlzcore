# INT-FORGE-017A + INT-ID-018A Implementation Record

**Originally recorded:** 2026-07-23  
**Extended:** 2026-07-24  
**Status:** Source-reported and partially device-observed foundation implemented; final runtime acceptance remains gated by repeat uploads, branch confirmation, credential persistence, package-workflow alignment, launcher persistence, installability, and bubble authorization evidence.

## Scope

This record captures the implementation sequence that established and then stabilized:

- `INT-FORGE-017A` — authenticated CLIENT-owned GitHub Forge upload, workflow observation, logs, artifact retrieval, and large-file streaming;
- `INT-ID-018A` — canonical CLIENT, SERVER, and SWURVER identities across launcher, notification, bubble, and administrative surfaces;
- role-specific CLIENT-owned bubble authority required before the `CFv2.1.0` Chat control plane;
- source-package validation and Android update-signing continuity.

## Source lineage

Initial observed progression:

- CLIENT advanced through `CFv2.0.36` to `CFv2.0.46`.
- SERVER advanced through `CFv2.0.31` to `CFv2.0.32`.
- Forge uploaded SERVER source, created commits, observed Actions runs, and returned SERVER APK artifacts to Android.

Current packaged baselines:

- CLIENT `CFv2.0.51`;
- SERVER `CFv2.0.36`.

Patch numbering remains app-specific during `CFv2.0.x`. CLIENT and SERVER advance together to `CFv2.1.0` only when shared Chat, mission-envelope, authorization, artifact, and event contracts satisfy the entry gate.

## INT-FORGE-017A requirements

### CLIENT-owned GitHub Forge

Forge is a first-class CLIENT capability in User Mode. Developer Mode and Settings may retain compatibility shortcuts, but normal Forge operation must not require legacy Developer Mode.

```text
Authenticate GitHub account
-> stage CLIENT and SERVER packages
-> validate package integrity
-> route packages to authoritative repository paths
-> stream source with bounded memory
-> create one atomic tree/commit
-> confirm target branch
-> discover and observe GitHub Actions
-> display real jobs and steps
-> download run logs and artifacts
-> expose results to future Chat and Mission layers
```

### Canonical routing

```text
CLIENT_* -> SOURCES/CLIENT/
SERVER_* -> SOURCES/SERVER/
unknown  -> configured fallback requiring visible confirmation
```

The staged preview shows filename, size, component, logical package grouping, and final repository path.

### Staging continuity

- Additional selections merge into the existing staging set.
- Duplicate URI selections do not clear earlier files.
- A new source targeting an existing repository path replaces that staged destination.
- ZIP and SHA display as one logical package.
- Staging clears only after the branch head confirms the new commit.

### ZIP and SHA auto-matching

Forge defaults to exact-basename ZIP/SHA pairing through a user-granted Storage Access Framework folder.

```text
<base>.zip
<base>.sha256
```

Requirements:

- exact sibling match only;
- persisted folder grant and toggle preference;
- manual pair selection remains supported;
- local checksum validation before upload;
- commit blocked when a staged ZIP lacks a valid readable checksum.

### Package-policy amendment

The active CFv2.0.x operational package contract requires ZIP plus SHA. A sibling manifest is optional and is validated when present.

The historical `INT-PKG-022A` triple-verification implementation remains valid evidence for that reissue, but its mandatory-manifest rule is superseded for current Forge delivery until a manifest has an accepted downstream purpose.

### Authentication and permission boundary

Forge uses fine-grained GitHub credentials with the minimum permissions required by enabled features.

Typical repository permissions:

```text
Contents  read/write
Actions   read/write
Metadata  read-only
```

Credentials are:

- stored in Android Keystore-backed encrypted preferences;
- synchronously persisted before connection success is reported;
- never written to logs;
- retained across process restart and valid in-place updates;
- not erased by temporary network or verification failures;
- removable through explicit disconnect.

### Atomic commit and branch confirmation

```text
resolve branch head
-> create file blobs
-> create Git tree
-> create commit
-> update branch ref
-> resolve branch head again
-> confirm new commit
```

A transfer bar reaching 100 percent is not commit success. Forge reports commit success only after branch confirmation and shows the resulting commit identity.

### Streaming upload and memory safety

The accepted upload path avoids whole-file heap amplification:

```text
ContentResolver / InputStream
-> bounded buffer
-> streamed encoding/request body
-> GitHub
```

Requirements:

- no whole-ZIP `ByteArray` allocation;
- bounded working memory;
- live transferred-byte reporting;
- actual source-byte progress;
- explicit size validation;
- cancellation;
- mobile-network-aware timeouts and retry classification;
- repeated CLIENT or SERVER uploads in one app session create a new commit and new associated run.

### Workflow observer

The observer displays:

- workflow and run identity;
- branch and event;
- queued, in-progress, completed, success, failure, cancelled, or skipped state;
- timestamps and duration;
- actual job and step state;
- workflow link;
- artifact action;
- run-log ZIP action.

GitHub does not expose a universally trustworthy compile percentage. Forge may show known jobs/steps, elapsed time, and indeterminate active state, but it must not fabricate a percentage from time.

### Workflow logs

Every workflow card provides a user-initiated **Download Logs** action that retrieves the GitHub Actions run-log ZIP. Exported logs must not contain GitHub credentials added by SWRLZ.

### Artifact progress

Workflow progress and artifact transfer remain distinct:

1. **Workflow/build:** phase-based and possibly indeterminate.
2. **Artifact download:** exact bytes, percentage, speed, and ETA when content length is known.

```text
artifact discovered
-> download queued
-> bytes transferred
-> integrity verification
-> saved
-> install/share/open actions
```

## INT-ID-018A requirements

### Canonical identities

```text
SWRLZ CLIENT     cyan/blue crystal signal dragon
SWURLZER SERVER  violet crystal guardian dragon
SWURVER ADMIN    cyan-violet fusion dragon inside authenticated CLIENT state
```

CLIENT and SERVER artwork must never be reversed. SWURVER is not a third installable app.

### Android launcher and recent-apps identity

Identity must resolve consistently across:

- application icon and round icon;
- launcher activity/alias;
- adaptive foreground/background;
- legacy density resources;
- theme switching;
- package update and reboot;
- launcher and recent-apps presentation.

Artwork must respect adaptive safe zones without excessive transparent or white padding.

### Notification identity

- Large notification icons use alpha-transparent full-color artwork.
- Small notification icons use separate monochrome Android silhouettes.
- Status and expanded notifications use the correct app identity.
- SWURVER artwork appears only in authenticated fused/admin context.

### Asset hygiene

CLIENT `CFv2.0.51` and SERVER `CFv2.0.36` perform conservative cleanup. Resources are removed only after reference analysis across Kotlin/Java, manifests, activity aliases, adaptive-icon XML, theme selection, other XML resources, and build scripts.

Selectable launcher families remain retained when referenced, even when not the default identity.

## Bubble authority continuation

Earlier revisions adopted one Android-managed CLIENT bubble and rejected a three-overlay experiment because every role window opened the same CLIENT surface without real authority separation.

CLIENT `CFv2.0.47` restored a corrected model: a **CLIENT-owned role-specific bubble cluster** whose role surfaces are gated by authoritative session state.

```text
SWRLZ bubble      local CLIENT authority
SWURLZER bubble   selected authenticated SERVER context
SWURVER bubble    fused CLIENT state with approved admin capabilities
```

This is not three independent applications. It is one CLIENT capability layer projecting three role surfaces.

Required behavior:

- SWRLZ remains available without a SERVER.
- SWURLZER and SWURVER require verified admin-session state.
- persisted bubble layout cannot grant authority;
- session expiry, revocation, lost SERVER selection, or lost capabilities downgrade the surfaces;
- command-level authorization remains mandatory;
- bubble artwork remains tightly cropped and transparent;
- TalkBack fallback and sensitive-screen suppression remain required.

This continuation supersedes the earlier permanent single-bubble decision while preserving its security constraints against fake remote authority and visual-state trust elevation.

## Android installability

CLIENT and SERVER in-place updates require:

- unchanged `applicationId`;
- higher `versionCode`;
- the exact same signing certificate.

Every update-capable build channel must use the same persistent project key. A one-time uninstall/reinstall may be required when moving from a historical debug key to the permanent key.

## Log-verified package-integrity defect

The supplied workflow logs selected:

```text
SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.zip
```

The verifier failed at:

```text
Missing manifest: SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.manifest.json
```

The logs do not show a ZIP hash mismatch. The verified failure is a package-contract mismatch: current Forge delivery used ZIP/SHA pairing while the verifier still required a manifest.

Required alignment:

- ZIP and SHA mandatory;
- checksum validation mandatory;
- manifest validation conditional on presence;
- missing optional manifest not an integrity failure.

## Runtime acceptance gate

Final acceptance requires repeated evidence for:

- CLIENT and SERVER large-package streaming without OOM;
- additive staging and destination replacement;
- exact ZIP/SHA auto-matching and local verification;
- repeated same-session SERVER updates creating new commits and runs;
- branch confirmation before staging clear or success;
- workflow jobs/steps and run-log ZIP retrieval;
- encrypted credential persistence across restart and valid update;
- artifact download with correct naming and content;
- launcher/recent-apps identity across update, reboot, and theme changes;
- correct CLIENT/SERVER notification mapping;
- clean alpha transparency and bubble crop;
- role-specific bubble downgrade on authorization loss;
- same-key APK updates over prior installations;
- integrity workflow matching the documented package contract.

## Excluded authority

This record does not authorize:

- fabricated build percentages;
- plaintext token storage;
- unrestricted GitHub permissions by default;
- automatic direct-to-main mutation without confirmation or policy;
- release signing keys stored in ordinary CLIENT storage;
- trust elevation through visual fusion;
- persisted UI state as authorization;
- claims of complete Chat or Mission functionality before `CFv2.1.0` evidence.