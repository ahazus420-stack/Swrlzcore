# SWRLZ CFv2.0.x Conversation Update Record

**Recorded:** 2026-07-24  
**Scope:** Forge, bubble authority, branding, packaging, workflow integrity, Android installability, and documentation continuity  
**Current baselines:** CLIENT `CFv2.0.51`; SERVER `CFv2.0.36`

## 1. Purpose and evidence classes

This additive record consolidates the latest CFv2.0.x implementation reports, observed workflow failure, architecture decisions, and documentation corrections. It preserves historical records while explicitly identifying which newer decisions supersede older guidance.

Evidence classes used here:

- **Package-verified:** confirmed from the supplied source ZIP and SHA-256 pair.
- **Log-verified:** confirmed by the supplied GitHub Actions workflow logs.
- **Source-reported:** documented by the latest packaged checkpoint and implementation records; clean CI and device verification may still be pending.
- **Architecture decision:** accepted repository policy or design direction, not automatically proof of runtime completion.

## 2. Documentation audit coverage

The review covered:

- 166 Markdown documents in CLIENT `CFv2.0.51`;
- 70 Markdown documents in SERVER `CFv2.0.36`;
- the full DOCX conversation update record and its condensed Markdown companion;
- current repository-level Forge/identity architecture, Forge/identity checkpoint, package-integrity checkpoint, CFv2.1.0 entry gate, root README, and Blueprint Council log.

The audit found two material historical conflicts that require dated supersession rather than deletion:

1. **Bubble model:** older records adopted one Android-managed CLIENT bubble; CLIENT `CFv2.0.47` restored a CLIENT-owned role-specific bubble cluster gated by verified administrative state.
2. **Package contract:** `INT-PKG-022A` established mandatory ZIP/checksum/manifest triples; current Forge delivery and the supplied authoritative baselines use ZIP+SHA pairs without sibling manifests.

## 3. Product and authority model

- **SWRLZ** is the Android CLIENT identity and local control surface.
- **SWURLZER** is the SERVER/node-host identity.
- **SWURVER** is not a third application. It is a fused privileged state projected by the CLIENT when the CLIENT is authenticated to a selected SERVER and possesses the required approved administrative capabilities.
- Persisted layout, icon, page, or bubble state never grants authority.
- SWURLZER and SWURVER surfaces must downgrade or disappear when the session expires, trust is revoked, the selected SERVER is lost, or required capabilities are removed.

## 4. Bubble control-plane direction

The latest accepted CFv2.0.x direction is a **CLIENT-owned role-specific bubble cluster**. The three surfaces are projections of one CLIENT capability layer, not three separately authoritative applications.

| Surface | Authority source | Primary actions |
|---|---|---|
| SWRLZ | Local CLIENT | Client chat, local status, missions, pause, permissions, local logs, emergency stop |
| SWURLZER | Selected authenticated SERVER context | Server chat, health, connected clients, queue, logs, maintenance, disconnect |
| SWURVER | Fused CLIENT state with approved admin capabilities | Cross-boundary approvals, deployment, unified health, trust, audit, artifacts, coordinated stop |

Required invariants:

- SWRLZ remains available without a SERVER.
- SWURLZER and SWURVER are gated by verified session and capability state.
- Visual fusion never elevates trust.
- Every sensitive action remains command-authorized.
- Bubble state must derive from the same repositories and capability services used by the full CLIENT.
- Sensitive-screen suppression and accessible linear fallback remain required.

This section supersedes earlier repository wording that permanently rejected every three-surface presentation. It does **not** authorize independent fake SERVER authority or persisted visual-state authorization.

## 5. Reported CLIENT checkpoint history

### CFv2.0.47 - Bubble authority

- Restored the CLIENT-owned interactive bubble-cluster foundation.
- Kept SWRLZ available without a SERVER.
- Gated SWURLZER and SWURVER behind verified admin-session state.
- Prevented persisted layout choices from granting authority.

### CFv2.0.48 - Forge, bubble, and installability

- Additional file selections merge into existing staging instead of replacing it.
- Duplicate URI selections are removed without clearing prior staged files.
- Local CLIENT/SERVER ZIP and SHA-256 validation runs before upload.
- Workflow observer displays actual GitHub jobs and steps without fabricated percentages.
- Bubble shortcut assets were tightened to reduce unintended padding.
- Persistent development-signing environment support was documented.

### CFv2.0.49 - Automatic checksum matching

- Added default-on exact ZIP/SHA sibling matching.
- Added persisted Storage Access Framework source-folder access.
- Treated ZIP and SHA as one logical source package.
- Kept manual multi-file selection available.
- Blocked commit when a staged ZIP lacks a readable, valid, matching checksum.

### CFv2.0.50 - Forge reliability and workflow logs

- Repeated staging replaces entries targeting the same repository path rather than creating duplicate destinations.
- Staging clears only after the target branch resolves to the newly created commit.
- Transfer completion is distinct from commit confirmation and workflow discovery.
- GitHub credentials persist synchronously in Keystore-backed encrypted preferences.
- Temporary network/authentication verification failures do not erase a valid saved credential.
- Each workflow card can download the GitHub Actions run-log ZIP.

### CFv2.0.51 - Asset hygiene and documentation

- Removed only resources with no direct Kotlin, Java, manifest, adaptive-icon, XML, or build reference.
- Preserved theme-selectable launcher families and active bubble resources.
- Added conservative asset-audit tooling.
- Continued the CFv2.0.x stabilization line without claiming CFv2.1.0 completion.

## 6. Reported SERVER checkpoint history

### CFv2.0.33 - Administrative capability vocabulary

- Added typed administrative capabilities and `GET /capabilities/admin`.
- Capability discovery explicitly reports that it does not grant authority.
- Actual grants remain authenticated, client-bound, server-bound, session-bound, expiring, revocable, and auditable.

### CFv2.0.34 - Kotlin compilation repair

- Corrected visibility alignment for `AdminCapabilityManifest` and internal discovery HTTP types.
- No protocol or runtime behavior was intentionally changed.

### CFv2.0.35 - Identity and installability

- Corrected the launcher and legacy outputs to the violet SWURLZER identity.
- Tightened SERVER and fused bubble assets.
- Documented same-applicationId, higher-versionCode, and same-signing-certificate requirements for in-place updates.

### CFv2.0.36 - Asset hygiene and documentation

- Removed unused SERVER resources after reference analysis.
- Preserved launcher aliases, notification crystal, active SERVER visuals, and referenced bubble resources.
- Added conservative asset-audit tooling.

## 7. Forge UX and reliability requirements

- Selecting more files merges them into the existing staging set.
- A repeated destination path replaces the prior staged source for that destination.
- Selecting a source ZIP auto-matches the exact sibling `.sha256` when default-on matching and folder access are available.
- ZIP and SHA display as one logical package card.
- Transfer completion, Git object creation, branch confirmation, workflow discovery, build execution, artifact discovery, artifact transfer, and artifact verification remain separate phases.
- Staging clears only after branch confirmation.
- Workflow detail comes from actual GitHub jobs and steps.
- Every workflow card exposes run-log ZIP download.
- Temporary verification failures do not silently erase encrypted GitHub credentials.
- A second CLIENT or SERVER update in the same app session must create a new commit and associate a new workflow run.

## 8. Package-integrity policy amendment

### Current CFv2.0.x operational contract

A complete required source package is:

1. `<base>.zip`
2. `<base>.sha256`

A sibling `<base>.manifest.json` is **optional** and is validated when present.

Reasoning:

- the current authoritative CLIENT and SERVER deliveries are ZIP+SHA pairs;
- Forge auto-matching and local validation are implemented around ZIP+SHA pairing;
- a mandatory manifest should exist only when it has a defined downstream purpose such as routing, version enforcement, provenance, release metadata, or policy attestation.

This amendment preserves the historical `INT-PKG-022A` triple-verification evidence but supersedes its mandatory-manifest rule for the active CFv2.0.x Forge contract.

### Required implementation alignment

The repository verification script and workflow must be aligned to the documented policy:

- ZIP and SHA are mandatory;
- checksum basename and digest are validated;
- manifest is validated only when present;
- absence of a manifest is not itself an integrity failure.

## 9. Log-verified integrity failure

The supplied GitHub Actions logs selected:

```text
SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.zip
```

The verifier then failed with:

```text
Missing manifest: SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.manifest.json
```

The workflow exited with code 1 at the manifest check. The logs do **not** show a ZIP hash mismatch. Therefore the verified defect is a contract mismatch between current Forge delivery and a verifier that still treated the manifest as mandatory.

## 10. Workflow progress presentation

- **Upload:** exact bytes, total bytes when known, transfer rate, and ETA.
- **Commit:** create blobs/tree, create commit, update branch, confirm branch.
- **Workflow:** queued, in progress, and completed from GitHub state.
- **Build detail:** actual job and step names.
- **Artifact:** discovered, downloading, verified, saved, install-ready.
- **Failure:** exact failed phase/step and a Download Logs action.

No build percentage may be fabricated from elapsed time.

## 11. Branding and resource ownership

- CLIENT launcher and recent-apps identity: cyan SWRLZ.
- SERVER launcher and recent-apps identity: violet SWURLZER.
- Fused SWURVER artwork: authenticated CLIENT state only.
- Adaptive foregrounds must avoid excessive transparent or white margins.
- Launcher, round launcher, adaptive foreground/background, legacy density, monochrome notification, full-color notification, bubble, and recent-apps presentation must be tested separately.
- Resource deletion requires reference analysis across manifest aliases, theme switching, Kotlin/Compose, XML, adaptive-icon definitions, and build scripts.

## 12. Android in-place update requirements

An update-capable APK requires:

- the same `applicationId`;
- a higher `versionCode`;
- the exact same signing certificate.

One persistent signing key must be used for every build channel intended to update an existing installation. A one-time uninstall/reinstall may be unavoidable when moving from historical debug signing to the permanent project key.

## 13. Acceptance checklist

- [ ] Selecting a second file preserves the first staged file.
- [ ] Re-selecting a destination replaces that staged destination without duplication.
- [ ] Selecting a ZIP auto-adds and verifies its exact sibling SHA when folder access is available.
- [ ] A repeated SERVER update in the same session creates a new commit and workflow run.
- [ ] Forge never equates a 100-percent transfer bar with branch-confirmed success.
- [ ] GitHub credentials survive process restart and valid in-place update.
- [ ] Every workflow card can download its run-log ZIP.
- [ ] CLIENT and SERVER launcher/recent-apps identities are correct.
- [ ] Bubble artwork has no unintended white padding.
- [ ] SWURLZER and SWURVER surfaces disappear or downgrade when authorization is lost.
- [ ] Integrity workflow behavior matches the ZIP+SHA-required, manifest-optional contract.
- [ ] CLIENT and SERVER APKs update over prior installations signed by the same permanent key.

## 14. Current evidence summary

- The supplied CLIENT `CFv2.0.51` and SERVER `CFv2.0.36` ZIPs match their supplied SHA-256 values.
- The packaged documentation supports the listed checkpoint progression and latest source-reported behavior.
- The workflow logs verify a missing-manifest failure, not a checksum mismatch.
- Clean CI, repeated same-session upload, branch-confirmation behavior, credential persistence across update, bubble revocation behavior, launcher persistence, and in-place APK installation still require current runtime evidence before final acceptance.