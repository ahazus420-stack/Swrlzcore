# SWRLZ-Core CF5 Engine Test Harness Handoff Report

## 1. Files changed

Added:

- `scripts/swrlz_engine_core.py`
- `scripts/test_swrlz_engine.py`
- `scripts/validate_build_request.py`
- `tests/fixtures/mock_devices.json`
- `tests/fixtures/mock_missions.json`
- `tests/fixtures/mock_approvals.json`
- `tests/fixtures/mock_ghost_lineage.json`
- `tests/fixtures/build_request_cf5.request`
- `tests/ENGINE_TEST_REPORT_TEMPLATE.md`
- `tests/ENGINE_TEST_LAST_RUN.md`
- `docs/workflow/SWRLZ_CORE_ENGINE_FIRST_TESTING_STANDARD.md`
- `docs/workflow/SWRLZ_CORE_CF5_ENGINE_TEST_HARNESS.md`
- `RPT_CF5_276_ENGTEST.md`

Updated:

- `core/version.json`
- `android/app/build.gradle.kts`

## 2. Features updated

- Added Engine-First command testing harness.
- Added mock fixture sets for devices, missions, approvals, and ghost lineage.
- Added packet format validation.
- Added state transition validation for queued, running, paused, waiting, rerouted, completed, failed, archived, blocked, and ghosted state classes.
- Added build request naming validation for the short mobile-readable naming standard.

## 3. Non-UI logic tested

- Device Registry uniqueness and stable key checks.
- Ghost/retired device lineage links to an active device.
- Mission Queue transition rules.
- Approval Router decisions for auto-approve safe, deny, timeout, and manual input.
- Workaround Cycling fallback route selection.
- Packet/message required field validation.
- Build request source/SHA naming relationship.

## 4. Mock scenarios tested

- Active controller phone plus active node phone.
- Ghost old profile linked to active phone.
- Retired node profile linked to active node phone.
- Safe mission auto-approval.
- Denied restricted mission.
- Timed-out approval that can reroute.
- Manual/user-visible approval that remains waiting.
- Blocked Word-install style route rerouted to an existing safe editor.
- Failed saved-node route rerouted to current subnet scan.

## 5. Commands/scripts run

```bash
python3 scripts/test_swrlz_engine.py --root . --write-report tests/ENGINE_TEST_LAST_RUN.md
python3 scripts/validate_build_request.py --root . --file tests/fixtures/build_request_cf5.request --allow-missing
```

## 6. Results

- Source-level tested: PASS
- Logic simulated: PASS
- Build-tested: NOT RUN in this handoff
- Device-tested: NOT RUN in this handoff

The command tests completed successfully in the source tree before packaging.

## 7. What still requires Adam's real-device testing

- APK build result in GitHub Actions or Codespaces.
- Install/update behavior on Android.
- Whether stable signing is configured and install-over-old works.
- Network Discovery UI behavior.
- Connection Memory / Last-Good Node UI behavior.
- Android permissions and restricted settings.
- Real device performance and feel.

## 8. Known limitations

- The engine harness is a Python simulation layer, not a replacement for Android tests.
- It does not exercise Compose UI, Accessibility Service, notifications, or Android lifecycle events.
- It validates expected rules against fixtures; future production logic should progressively move into reusable engine modules so command tests cover more real code.

## 9. Recommended next test steps

1. Upload `SRC_CF5_276_ENGTEST.zip`, `SHA_CF5_276_ENGTEST.txt`, and `RPT_CF5_276_ENGTEST.md` into `SOURCES/`.
2. Set `BUILD_REQUESTS/000_CURRENT.request` to the CF5 source and SHA.
3. Build the APK.
4. Confirm the app identity shows `2.7.6-CF5-ENGINE-TEST-HARNESS`.
5. Continue device testing CF4/CF5 UI behavior and signing behavior.
