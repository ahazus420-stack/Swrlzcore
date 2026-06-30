# SWRLZ-Core CF9 Command Dock UI Handoff Report

## 1. Patch identity

- Patch: `CF9_276_COMMAND_DOCK_UI`
- APK release target: `APK_CF9_276_COMMAND_DOCK_UI`
- Source ZIP: `SRC_CF9_276_COMMAND_DOCK_UI.zip`
- Visible app version: `0.2.7.6-cf9-commanddock`
- Version code: `32`
- Patch label: `2.7.6-CF9-COMMAND-DOCK-UI`

## 2. Why this patch exists

CF8 fixed RadarLive server-truth metric binding. CF9 begins the larger interface restructuring Adam requested: the Android client now presents as a SWRLZ Command Dock instead of a horizontal pile of feature tabs. The patch keeps the working RadarLive screen and wraps the app with a five-zone command shell.

## 3. Files changed

- `android/app/build.gradle.kts`
- `android/app/src/main/java/sh/swurlz/core/MainActivity.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NodesScreen.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/VaultScreen.kt`
- `scripts/test_cf8_radarlive.py`
- `scripts/test_cf8_radarmenu.py`
- `scripts/test_cf9_commanddock_ui.py`
- `README_LATEST_2.7.6.md`
- `RPT_CF9_276_COMMAND_DOCK_UI.md`

## 4. Features updated

- Adds persistent SWRLZ Command Dock shell.
- Reorganizes primary navigation into five zones:
  - `Cockpit`
  - `Radar`
  - `Missions`
  - `Nodes`
  - `Vault`
- Adds a top command identity strip showing the active dock zone and current route.
- Adds a bottom five-button command dock sized for phone thumb use.
- Adds a persistent quick-command strip with contextual suggestions:
  - Cockpit: mission / teach / overlay direction.
  - Radar: scan / signature / server console direction.
  - Missions: approvals / pause / archive direction.
  - Nodes: inspect / ghost / node truth direction.
  - Vault: build identity / permissions / receipts direction.
- Adds a `NodesScreen` as the first device-roster command face.
- Adds a `VaultScreen` as the first build identity / receipts / settings command face.
- Keeps CF8 RadarLive route mapped to the new `Radar` dock zone.
- Keeps existing screens reachable through the Vault, Nodes, and command chips.
- Keeps admin truth doctrine: UI wording states unsupported/destructive server-backed admin actions stay hidden until server support exists.

## 5. Preserved CF8 behavior

- `NetworkDiscoveryScreen.kt` was not removed or rewritten.
- CF8 live count labels remain bound for:
  - `GROUPS`
  - `DEVICES`
  - `ONLINE`
  - `QUEUE`
- Existing CF8 source-level RadarLive tests were adjusted only so they accept CF9 patch identity while still checking RadarLive behavior.

## 6. Source-level testing

Commands run:

```bash
python3 scripts/test_cf9_commanddock_ui.py
python3 scripts/test_cf8_radarlive.py
python3 scripts/test_cf8_radarmenu.py
```

Result:

```text
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

This sandbox still cannot resolve/download the Gradle distribution. GitHub Actions remains the real build validation lane.

## 8. Device testing still required

Adam should field-test:

1. Install `APK_CF9_276_COMMAND_DOCK_UI` after GitHub Actions builds it.
2. Confirm app launches into the new Command Dock shell.
3. Confirm bottom dock shows `Cockpit`, `Radar`, `Missions`, `Nodes`, and `Vault`.
4. Tap each dock button and confirm navigation works.
5. Open Radar and confirm CF8 `GROUPS`, `DEVICES`, `ONLINE`, and `QUEUE` counts still update from server truth.
6. Open Nodes and confirm the new device roster command face renders.
7. Open Vault and confirm build identity shows CF9 patch data.
8. Confirm old utility screens are still reachable from Vault/Nodes command paths.
9. Confirm no admin-only destructive action appears unless routed through existing admin-safe screens.

## 9. Known limitation

CF9 is a client-shell UI patch, not a server endpoint patch. It does not add server 0.7.4 routes, group edit routes, device profile persistence, true disconnect packets, or server process control. Those still belong to the next server-side patch.

## 10. Recommended next patch

- `SERVER_0.7.4_NODE_CONSOLE_ENDPOINTS`: add capability manifest, normalized admin data, group edit routes, device profile edit, audit log, true disconnect reason, and queue action endpoints.
- `SERVER_APP_0.1_NODE_CONSOLE`: begin the separate server app/interface that can view local node state and provide server admin console panels.
- `CF10_276_NODE_ADMINFLOW`: bind the client Nodes/Vault admin surfaces to server-backed routes after server support exists.
