# SWRLZ-Core Engine-First Testing Standard

Status: Accepted permanent development workflow standard  
Applies to: All future SWRLZ-Core source updates

## Core rule

From now on, whenever updating the SWRLZ-Core source, structure the project so as many features as possible can be tested command-wise before APK build/install.

The goal is to reduce Adam's manual testing phase down mostly to:

- UI testing
- real-device behavior
- Android permission checks
- install/update behavior
- final feel and feedback

Permanent phrasing:

> Make the mind testable without needing the full Android body every time.

## Two-layer model

Treat SWRLZ-Core as two major layers.

### 1. SWRLZ-Core Engine / Logic Layer

This layer should be separated from Android-only code wherever practical and made testable with local scripts, mock data, JSON fixtures, fake device profiles, fake mission queues, and simulated approval responses.

Engine / Logic Layer includes:

- Device Registry
- Mission Queue
- Approval Router
- Workaround Cycling
- Packet / Message Format
- State Ledger
- Ghost / Retired Device Lineage
- Capability Registry
- Preflight Checks
- Pause / Resume State
- Notification Decision Logic
- RouteSeed / Cartographer-style mission planning
- Any pure logic that does not require Android runtime or physical device APIs

### 2. Android App Shell / Runtime Layer

This layer still requires APK build/install or a real Android runtime/emulator for proper validation.

Android App Shell / Runtime Layer includes:

- UI screens
- Android permissions
- Accessibility Service
- Notifications
- Real device file/app control
- APK install/run behavior
- Touch/input automation
- Android lifecycle behavior
- Device-specific hardware/runtime behavior

## Required update workflow

For every source update, do the following where possible:

1. Inspect the project structure before modifying files.
2. Identify which features can be tested without a full APK build.
3. Add or update local test scripts for those features.
4. Use mock device data to test Device Registry behavior.
5. Use fake mission packets to test Mission Queue behavior.
6. Use simulated approval outcomes to test Approval Router behavior.
7. Use blocked/warning scenarios to test Workaround Cycling behavior.
8. Use ghost/retired/duplicate device cases to test lineage handling.
9. Validate JSON/config/schema files.
10. Validate routing tables and packet formats.
11. Validate state transitions such as queued, running, paused, waiting, rerouted, completed, failed, archived, and ghosted.
12. Run any available local tests before packaging the source.
13. If tests cannot be run, explain exactly why.
14. Never claim a full APK/device test was completed unless the APK was actually built, installed, and run on a real device or emulator.
15. Clearly separate source-level tested, logic simulated, build-tested, and device-tested.

## Source handoff testing report

Every source handoff should include a testing report with these sections:

1. Files changed
2. Features updated
3. Non-UI logic tested
4. Mock scenarios tested
5. Commands/scripts run
6. Results
7. What still requires Adam's real-device testing
8. Known limitations
9. Recommended next test steps

## Responsibility split

### SWRLZ / assistant handles

- source inspection
- code patching
- logic testing
- mock simulation
- packet validation
- registry validation
- queue validation
- approval flow validation
- handoff documentation

### Adam handles

- APK build if needed
- install/update
- UI testing
- real Android permission checks
- actual device behavior
- final feel/feedback

## Testing labels

Use these exact labels in future handoffs and reports:

### Source-level tested

The source tree was inspected or modified, static files were validated, and scripts/configs were checked where possible. This does not imply an APK was built or run.

### Logic simulated

A feature was tested through command-line scripts, fixtures, mocked devices, fake packets, or simulated approval outcomes. This does not imply Android runtime behavior was verified.

### Build-tested

The Android build or another build process completed successfully in a build environment. This does not imply install/update behavior or UI behavior was verified on Adam's device.

### Device-tested

The APK or runtime behavior was actually tested on a real device or emulator. This label must not be used unless that test truly occurred.

## Desired end-state

Every update cycle should become faster and cleaner by verifying as much of the SWRLZ-Core mind as possible before Adam needs to test the Android body.

The preferred development pattern is:

```text
inspect source
→ patch engine/app code
→ run logic scripts and mock scenarios
→ validate schemas/packets/state transitions
→ package source
→ build APK only when needed
→ Adam tests UI, permissions, real-device behavior, and feel
```

This standard is permanent unless explicitly superseded by a later accepted SWRLZ-Core development standard.
