# SWRLZ-Core CF8 RadarLive Handoff Report

## 1. Patch identity

- Patch: `CF8_276_RADARLIVE`
- APK release target: `APK_CF8_276_RADARLIVE`
- Source ZIP: `SRC_CF8_276_RADARLIVE.zip`
- Visible app version: `0.2.7.6-cf8-radarlive`
- Version code: `31`
- Patch label: `2.7.6-CF8-RADAR-LIVECOUNT-MENUS`

## 2. Why this correction exists

The previous CF8 menu-only package focused on making the Radar metric boxes tappable, but Adam clarified the live counts were still stuck at zero. This corrected CF8 keeps the mini floating menus and adds server-truth count binding.

## 3. Files changed

- `android/app/build.gradle.kts`
- `android/app/src/main/java/sh/swurlz/core/ui/screens/NetworkDiscoveryScreen.kt`
- `scripts/test_cf8_radarlive.py`
- `scripts/test_cf8_radarmenu.py`
- `RPT_CF8_276_RADARLIVE.md`

## 4. Features updated

- Adds visible live count labels for `GROUPS`, `DEVICES`, `ONLINE`, and `QUEUE`.
- Count boxes now display server-derived values instead of remaining stuck at fallback zero.
- Count derivation uses multiple server truth sources:
  - `/status` for public group/device totals where available.
  - `/presence/summary` for presence summary fields where available.
  - `/presence/groups` and `/presence/devices` for non-admin payloads where available.
  - `/admin/devices` and `/admin/queues` when admin mode is enabled and authorized.
- Non-admin queue count displays `ADMIN` instead of pretending the queue is zero.
- Failed/missing endpoints show `AUTH`, `N/A`, `ERR`, or `—` depending on the failure type.
- Tapping each metric box opens the existing mini floating diagnostic/admin menu.
- Keeps admin actions hidden unless admin mode is enabled.

## 5. Source-level testing

Commands run:

```bash
python3 scripts/test_cf8_radarlive.py
python3 scripts/test_cf8_radarmenu.py
```

Result:

```text
CF8 radar livecount source-level test PASS
```

## 6. Build testing

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

This sandbox cannot resolve/download the Gradle distribution. GitHub Actions remains the real build validation lane.

## 7. Device testing still required

Adam should field-test:

1. Install `APK_CF8_276_RADARLIVE`.
2. Start Termux server `0.7.3`.
3. Open Network/Radar screen.
4. Confirm `GROUPS`, `DEVICES`, `ONLINE`, and `QUEUE` boxes no longer stay stuck at `0` when server truth says otherwise.
5. Tap each box and confirm mini floating menu opens.
6. Test non-admin mode and admin mode separately.
7. Confirm queue shows `ADMIN` when admin mode is off.

## 8. Known limitation

Server `0.7.3` may still lack some destructive admin endpoints for rename/delete/profile-edit flows. CF8 does not fake those actions. It shows honest endpoint-needed notices where server support is not yet present.

## 9. Likely next patch

- `SERVER_0.7.4_ADMIN_MENU_ENDPOINTS`: add exact group rename/delete, device profile edit, true disconnect reason packet, and queue sub-actions.
- `CF9_276_ADMINFLOW`: bind UI action buttons to those new server endpoints after the server exposes them.
