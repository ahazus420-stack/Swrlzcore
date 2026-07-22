# SERVER CFv2.0.5 SWURLZER Delivery Receipt

**Checkpoint:** `INT-UI-013A`

- Baseline: `SERVER_CFv2.0.4_SWRLZ.zip`
- Baseline SHA-256: `f33365786a04d729afc1546cd1a63dc1b1efb2560a01fe8094b4c2be72aefd88`
- Source: `SERVER_CFv2.0.5_SWRLZ.zip`
- SHA-256: `e23565e8b0520caa846a9cf0fdc8143f81404cd3b69a97683318ee02b3275cf0`
- Android identity: `versionCode 8` / `versionName 2.0.5`
- ZIP integrity: **PASS**
- Static checkpoint verifier: **PASS**
- APK/Gradle build: **NOT RUN**

## Integrated

- One state-correct SWURLZER runtime control: Start while inactive and Stop while starting/running.
- Added User Activity view for runtime, listener availability, actionable failures, nodes, trust summaries, and missions.
- User and Developer interfaces consume the same HostViewModel and repository-backed state.
- Developer Mode remains reserved for deeper diagnostics and engineering controls.
- Room schema remains version 1.

No protocol identifier, Room migration, live LAN endpoint, provider adapter, commit, push, workflow, release, or deployment was added.
