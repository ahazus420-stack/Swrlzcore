# CORE-BUILD-002A Device Install and Launch Verification

Status: verified on physical Android device
Date (UTC): 2026-07-21
Checkpoint: CORE-BUILD-002A
Repository: `ahazus420-stack/Swrlzcore`
Authoritative branch: `main`
Merge commit: `56bf5eecc4388a3a47e81cd866448cb0c1b0a210`

## Scope

This report records user-provided physical-device evidence for the verified CORE_BASE debug APK produced by workflow run `29790837016`.

## Verified device results

The supplied screenshots establish that:

- the APK installed successfully on a physical Android phone;
- Android registered the application in the launcher as `SWRLZ Core`;
- the application launched successfully;
- the process did not immediately crash on first launch;
- the expected minimal Compose UI rendered with centered text `SWRLZ Core`.

## Related build evidence

- Build report: `reports/CORE_BUILD_002A_FINAL_VERIFICATION.md`
- Workflow run: `29790837016`
- Artifact ID: `8480270199`
- APK SHA-256: `38282980952e80458a8bccaf4b3daa206af41c9c652d7bc312f2608326d0d5e9`
- Application ID: `com.swrlz.core.app`
- Version: `1.0.0` (`versionCode=1`)

## Evidence origin

The device screenshots were supplied directly by the repository owner in the checkpoint conversation after installing the verified debug APK. They show both the launcher entry and the running minimal CORE_BASE screen.

## Claims now verified

- physical-device installation;
- launcher registration;
- first launch;
- minimal runtime UI render;
- absence of an immediate startup crash during the observed launch.

## Claims still unverified

- extended runtime stability;
- process recreation and lifecycle recovery;
- background execution;
- upgrade or downgrade behavior;
- permission flows;
- release build behavior;
- release signing continuity;
- publication or deployment.

## Approval boundary

This evidence checkpoint authorized documentation only. It did not authorize source modification, release, publication, deployment, signing changes, or work on CLIENT, NODE_HOST, Keyboard, or Launcher.

## Recovery and lineage

Preserve this report with the build report and handoff. Future runtime verification should supersede or extend this record by explicit linkage rather than deleting it.
