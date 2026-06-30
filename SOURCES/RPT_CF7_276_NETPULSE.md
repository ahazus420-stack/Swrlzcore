# SWRLZ-Core CF7 Network Pulse / Live Admin Handoff Report

## 1. Files changed

- `android/app/build.gradle.kts`
- `android/app/src/main/AndroidManifest.xml`
- `android/app/src/main/res/xml/backup_rules.xml`
- `android/app/src/main/res/xml/data_extraction_rules.xml`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`
- `scripts/test_cf7_netpulse.py`
- `RPT_CF7_276_NETPULSE.md`

## 2. Features updated

- Added persistent Network Auto Refresh setting.
- Added persistent Network Refresh Interval setting.
- Added active-tab-only polling behavior for Network Pulse.
- Added live server dashboard refresh from `/status`, `/discovery/signature`, `/presence/summary`, `/presence/groups`, `/presence/devices`, `/admin/devices`, `/admin/queues`, and `/admin/ledger` where available.
- Added scrollable popup dialogs for groups, devices, online devices, queue, and ledger views.
- Added admin-only section hiding: admin controls appear only when app admin mode is enabled.
- Added admin device actions using existing server routes: force-offline, trust, block, clear-queue, reset-key, and forget.
- Added group creation dialog using existing `/groups/create` server route.
- Added group rename/delete info notice because server `0.7.3` does not expose rename/delete group routes yet.
- Enabled ordinary settings backup/restore support for Android backup/device-transfer where available.
- Preserved warning that Android Keystore-backed secret tokens may still require admin re-login after full uninstall/reinstall.

## 3. Non-UI logic tested

Source-level checks validated:

- CF7 build identity strings.
- CF7 source name string.
- Auto-refresh persisted key path.
- Refresh interval persisted key path.
- Active-tab-only polling loop marker.
- Admin-hidden UI marker.
- Admin endpoints present in source.
- Backup rules enabled and secret store excluded.

## 4. Mock scenarios tested

- Auto refresh ON/OFF setting exists and is saved.
- Refresh interval cycles through saved options.
- Network tab polling loop is tied to the composable lifecycle.
- Non-admin view hides admin actions.
- Admin view exposes device and queue controls.
- Reinstall retention is treated as best-effort backup for ordinary settings, not a false guarantee for encrypted admin session secrets.

## 5. Commands/scripts run

```bash
python3 scripts/test_cf7_netpulse.py
./gradlew --offline :app:assembleDebug
```

## 6. Results

```text
Source-level tested: PASS
Logic simulated: PASS
Build-tested: NOT COMPLETED locally
Device-tested: NOT RUN by assistant
```

The Python source-level test passed.

The local Android Gradle build could not complete because this sandbox cannot reach `services.gradle.org` to download the Gradle distribution. This is the same network limitation seen in prior source prep and is not proof of a source failure.

## 7. What still requires Adam's real-device testing

- Verify Network Auto Refresh remains ON/OFF after tab switch.
- Verify refresh interval remains saved after tab switch and app restart.
- Verify polling pauses when leaving Network tab and resumes when returning.
- Verify groups/devices/online/queue panels show live server state.
- Verify admin actions are hidden when admin mode is off.
- Verify admin device actions work when admin mode is enabled and server accepts admin token.
- Verify Android backup/restore behavior across real uninstall/reinstall if Android backup is enabled on the device.

## 8. Known limitations

- Android app updates keep local state. A full uninstall normally deletes app-local data unless Android backup/device-transfer restores it.
- CF7 enables backup for ordinary app settings and discovery settings, but encrypted Android Keystore secrets may not restore safely after reinstall. Admin may need to log in again.
- Server `0.7.3` has device admin action routes but does not yet expose group rename/delete routes. CF7 displays this clearly instead of pretending those destructive actions exist.
- Current disconnect/kick behavior maps to server `force-offline`; a true client-side disconnect reason display needs a matching server packet/reason route and client listener in a later patch.

## 9. Recommended next test steps

1. Upload `SRC_CF7_276_NETPULSE.zip`, `SHA_CF7_276_NETPULSE.txt`, and `RPT_CF7_276_NETPULSE.md` after top-level `SOURCES/` is clean.
2. Set `BUILD_REQUESTS/000_CURRENT.request` to `APK_CF7_276_NETPULSE`.
3. Build APK.
4. Install and open Network tab.
5. Test Auto Refresh persistence and interval persistence.
6. Start server and verify live groups/devices/queue refresh.
7. Enable admin mode and test admin panels/actions.
