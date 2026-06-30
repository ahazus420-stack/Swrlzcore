# SWRLZ-Core CF6 Boot-Sync / Permission Refresh Handoff Report

## 1. Files changed

- `android/app/build.gradle.kts`
- `android/app/src/main/java/sh/swurlz/core/net/CoreNodeAutoDiscovery.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/HomeScreen.kt`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/PermissionsScreen.kt`
- `scripts/test_cf6_bootsync.py`
- `RPT_CF6_276_BOOTSYNC.md`

## 2. Features updated

- App-launch Core Node boot-sync.
- Saved Core Node / last-good node verification.
- Trimmed subnet auto-scan when saved/last-good nodes fail.
- `/discovery/signature` validation plus `/status` server truth test.
- Permission board lifecycle refresh after returning from Android settings.
- CF6 build identity and compact source naming.

## 3. Non-UI logic tested

- Candidate ordering: saved URL first, last-good second, device subnet next.
- Loopback sweep exclusion by default.
- Device subnet generation for `x.x.x.1-254`.
- Permission board recomputation after the second permission return.

## 4. Mock scenarios tested

- No saved node, device IP exists.
- Saved node and last-good node both exist.
- Loopback range disabled.
- Accessibility already granted, overlay becomes granted second.

## 5. Commands/scripts run

```bash
python3 scripts/test_cf6_bootsync.py
python3 scripts/test_swrlz_engine.py --root . --write-report tests/ENGINE_TEST_LAST_RUN.md
python3 scripts/validate_build_request.py --root . --file tests/fixtures/build_request_cf5.request --allow-missing
cd android && chmod +x gradlew && ./gradlew :app:assembleDebug --no-daemon
```

## 6. Results

- Source-level tested: PASS
- Logic simulated: PASS
- Build-tested: NOT COMPLETED in local sandbox; Gradle wrapper attempted but could not download `services.gradle.org` because sandbox internet is unavailable.
- Device-tested: NOT RUN by assistant

## 7. What still requires Adam's real-device testing

- Open app with Termux/server running and confirm Home auto-connects without opening Network first.
- Confirm Home shows Core Node bridge green and server truth details.
- Enable Accessibility then Overlay, or Overlay then Accessibility, and confirm both status cards refresh green without pressing manual Refresh.
- Confirm install-over-old behavior remains blocked until stable signing certificate migration is completed.

## 8. Known limitations

- Stable signing still requires GitHub Secrets or a committed debug-only dev keystore policy. If the installed app was signed by a previous random debug cert, the first stable-cert migration still requires one uninstall/reinstall.
- Auto-scan is intentionally trimmed and will not sweep `127.0.0.2-254` unless debug loopback range scanning is separately enabled from Network Discovery.
- Build workflow visual warning patch is repo-level and should be applied to `.github/workflows/build-swrlz-apk.yml`; it is documented as part of CF6 but must be verified through GitHub Actions.

## 9. Recommended next test steps

1. Upload `SRC_CF6_276_BOOTSYNC.zip`, `SHA_CF6_276_BOOTSYNC.txt`, and `RPT_CF6_276_BOOTSYNC.md` into `SOURCES/`.
2. Point `BUILD_REQUESTS/000_CURRENT.request` at CF6.
3. Build APK.
4. Install fresh if signing mismatch blocks update-over-old.
5. Start local node server.
6. Launch app and verify boot auto-connect.
7. Test both permission enable orders.
