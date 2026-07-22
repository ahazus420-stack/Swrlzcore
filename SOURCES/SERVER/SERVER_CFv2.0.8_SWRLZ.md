# SERVER CFv2.0.8 SWURLZER Delivery Receipt

Checkpoint: INT-RECOVERY-017A  
Baseline: SERVER CFv2.0.7  
Target: SERVER CFv2.0.8

Implemented:
- Added generation-aware listener ownership so retired listener threads cannot mark a newer runtime generation failed.
- Restart now performs intentional transition marking, socket closure, worker interruption, bounded thread joins, reference cleanup, and fresh-generation startup in order.
- Intentional stop, restart, and shutdown listener exits no longer produce `ListenerTerminated` failures.
- `FAILED` can be recovered with **Initialize Server** without reinstalling.
- Added separate `ACTIVE` / `INACTIVE` lifecycle badge beside the existing health badge, preserving `ACTIVE + DEGRADED + LOCAL-ONLY` truth.
- Advanced Android identity to `versionCode 11` and `versionName 2.0.8`.

Verification:
- Static generation/lifecycle guards: PASS.
- Static UI status guard: PASS.
- ZIP integrity: PASS.
- Gradle/APK build and device runtime test: NOT RUN.
- Protocol version, trust boundaries, and destructive persistence changes: NONE.

SHA-256: `71145ca2c181f1503a6d3b308f3f954c05bf6df37e83b0603419f84ae8e80013`
