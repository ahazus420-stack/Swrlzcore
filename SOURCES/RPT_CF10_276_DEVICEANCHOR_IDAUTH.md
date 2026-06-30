# SWRLZ-Core CF10 DeviceAnchor Identity Authority Handoff Report

## 1. Patch identity

- Patch: `CF10_276_DEVICEANCHOR_IDAUTH`
- APK release target: `APK_CF10_276_DEVICEANCHOR_IDAUTH`
- Source ZIP: `SRC_CF10_276_DEVICEANCHOR_IDAUTH.zip`
- Visible app version: `0.2.7.6-cf10-deviceanchor`
- Version code: `33`
- Patch label: `2.7.6-CF10-DEVICEANCHOR-IDAUTH`

## 2. Why this patch exists

This patch incorporates Adam's latest identity-authority requirement after CF8 and after the CF9 Command Dock UI shell. CF10 treats device identity as a server/client authority layer, not as UI polish.

The design rule is:

```text
Client proposes identity.
Server confirms identity.
Ledger preserves history.
Admin controls actions.
UI displays server truth.
Duplicates become lineage, not clutter.
```

## 3. Files changed

- `android/app/build.gradle.kts`
- `android/app/src/main/java/sh/swurlz/core/data/DeviceIdentityAuthority.kt`
- `android/app/src/main/java/sh/swurlz/core/data/ContextCollectors.kt`
- `android/app/src/main/java/sh/swurlz/core/data/Prefs.kt`
- `android/app/src/main/java/sh/swurlz/core/net/Api.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NodesScreen.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/VaultScreen.kt`
- `scripts/test_cf10_idauth.py`
- `scripts/test_cf9_commanddock_ui.py`
- `scripts/test_cf8_radarlive.py`
- `scripts/test_cf8_radarmenu.py`
- `README_LATEST_2.7.6.md`
- `RPT_CF10_276_DEVICEANCHOR_IDAUTH.md`

## 4. Features updated

### Client identity authority

- Adds `DeviceIdentityAuthority.kt`.
- Generates a best-effort stable SWRLZ Device Anchor using allowed Android app-scope/device-scope inputs.
- Generates redacted hashes for server duplicate detection:
  - `device_anchor`
  - `fingerprint_hash`
  - `install_seed_hash`
  - `android_id_scope_hash`
- Does not expose raw Android ID or raw hardware-ish fingerprint input.
- Adds explicit identity schema versioning.

### Relay identity lock

- Updates relay identity behavior so ordinary reconnects and device-key rotation do not create a new random visible profile.
- Existing old `swrlz-node-*` style identities are treated as legacy/random and replaced with the stable anchor path.
- `DeviceContextCollector.collect(ctx).deviceNodeId` now resolves through `DeviceIdentityAuthority`.

### Core Node payloads and headers

- Adds identity headers to Core Node requests:
  - `X-SWRLZ-Device-UUID`
  - `X-SWRLZ-Device-Anchor`
  - `X-SWRLZ-Fingerprint-Hash`
  - `X-SWRLZ-Identity-Schema`
  - `X-SWRLZ-Profile-Merge-Key`
- Adds `registerDevice(ctx)` for `POST /devices/register`.
- Enriches group join and device check-in payloads with:
  - `device_uuid`
  - `device_anchor`
  - `fingerprint_hash`
  - `client_version`
  - `patch_label`
  - `capabilities`
- Auto presence now attempts `/devices/register` but does not let a missing route break CF8/CF9 behavior.

### Group creator/admin delete law

- Adds a client-side request function for group delete using the rule `creator_owner_or_server_admin_only`.
- Group creation payloads include creator identity fields so server-side group ownership can be enforced once the matching server endpoint exists.

### UI updates

- Nodes screen now surfaces Identity Authority:
  - Device UUID
  - fingerprint hash preview
  - install seed hash preview
  - schema number
  - duplicate/ghost doctrine
- Vault screen now includes a CF10 Identity Authority panel.
- CF9 Command Dock shell remains intact.
- CF8 RadarLive remains intact.

## 5. Preserved behavior

- CF9 five-zone Command Dock remains:
  - `Cockpit`
  - `Radar`
  - `Missions`
  - `Nodes`
  - `Vault`
- CF8 RadarLive labels remain bound for:
  - `GROUPS`
  - `DEVICES`
  - `ONLINE`
  - `QUEUE`
- CF10 does not fake unsupported server routes. The server must still implement registry authority, merge, audit, creator delete checks, and disconnect reasons.

## 6. Source-level testing

Commands run:

```bash
python3 scripts/test_cf10_idauth.py
python3 scripts/test_cf9_commanddock_ui.py
python3 scripts/test_cf8_radarlive.py
python3 scripts/test_cf8_radarmenu.py
```

Result:

```text
CF10 identity authority source-level test PASS
CF9 command dock source-level test PASS
CF8 radar livecount source-level test PASS
CF8 radar livecount source-level test PASS
```

## 7. Build testing

Attempted local Android build:

```bash
cd android
./gradlew --no-daemon :app:assembleDebug
```

Result:

```text
NOT COMPLETED LOCALLY
UnknownHostException: services.gradle.org
```

The sandbox still cannot resolve/download the Gradle distribution. GitHub Actions remains the real build validation lane.

## 8. Device testing still required

Adam should field-test:

1. Install `APK_CF10_276_DEVICEANCHOR_IDAUTH` after GitHub Actions builds it.
2. Open `Nodes` and confirm the Identity Authority card renders.
3. Confirm the Device UUID stays stable across normal app restart.
4. Confirm the Device UUID stays stable across install-over-update if signing continuity allows update.
5. Start Termux server `0.7.3` and verify Radar still works.
6. Join/check in to a group and confirm the server receives identity headers/payloads if route logging is available.
7. Rotate relay/device key and confirm the visible device profile does not intentionally change.
8. Confirm old duplicate profiles are treated as ghost/lineage targets in UI doctrine, not hard-delete targets.

## 9. Known limitation

Android does not guarantee a permanent hardware UUID to normal apps. CF10 uses allowed best-effort identity and redacted hashes. The server must still own final identity authority, merge decisions, trust state, group ownership, and heartbeat truth.

Server `0.7.3` may not yet expose `/devices/register`, group ownership checks, true disconnect packets, or merge endpoints. CF10 prepares the client side and UI doctrine; it does not claim server-side completion.

## 10. Recommended next patch

- `SERVER_0.7.4_IDENTITY_AUTHORITY`: implement `/devices/register`, duplicate detection, heartbeat lease truth, group creator ownership, audit log, and merge/ghost endpoints.
- `SERVER_APP_0.1_NODE_CONSOLE_FORGE`: create the separate server APK forge lane and first server console app shell.
- `CF11_276_NODE_ADMINFLOW`: bind client node/profile controls to server-backed identity authority routes after the server exposes them.
