# BUILD-WF-014 Legacy Workflow Index

Checkpoint branch: `checkpoint/build-wf-014`
Base commit: `e6621447b1e666a5c121a6a69045be8e87c506ab`
Replacement: `.github/workflows/swrlz-apk-router.yml`

The files below are removed from the active `.github/workflows/` surface. Their exact content remains recoverable through Git history and the recorded blob SHA.

| Former workflow | Blob SHA | Disposition |
|---|---:|---|
| `build-swrlz-apk_target_client_latest.yml` | `498475387f1200e17b41b7fce4089dff7288fe45` | CLIENT build/signing/provenance behavior integrated into router helpers. |
| `build-swrlz-android-project-e0af72.yml` | `a6f9aad3553abf2e6ba8ecf875182d0c71b33b40` | Duplicate CLIENT implementation retired. |
| `build-swrlz-server-apk.yml` | `7f6dc268f06fe90d7de4410ae01846ef7c5008d6` | SERVER Gradle discovery, variants, and provenance integrated. |
| `build-swrlz-core-android-foundation.yml` | `7c2c903b0035d6d7771037662d248ed0c925c247` | CORE source verification and reduction invariants integrated. |
| `build-swrlz-keyboard-base.yml` | `2a4809e95fe65024c5dbaccb016fa505f048b947` | KEYBOARD build/evidence logic generalized. |
| `build-swrlz-launcher-base.yml` | `71b261ce7c4f5b47a9041c86e590ca462ee90853` | LAUNCHER build/evidence logic generalized. |
| `build-swrlz-apk_multi_target_ready.yml` | `5eab516eed1eb3902c2c8b03bbf7eb0444fb62ba` | Root-Gradle prototype retired; its unused targets input and broad push trigger are not retained. |
| `locate-stale-ui-source.yml` | `35ba2e24eacd433edf7d7266f84cffe9869c26d2` | Completed checkpoint-specific forensic workflow retired. |
| `server-contract-catchup-010d-promote-v103.yml` | `e484ca9d7c50879428d8c52cc5da9fd57736369a` | Invalid/superseded one-shot promotion workflow retired. |

BUILD-WF-014 did not manually dispatch a workflow or perform a local APK build. An early checkpoint commit changed `BUILD_REQUESTS/000_CURRENT.request` while some legacy path-triggered workflows still existed on the branch. The available connector returned no commit statuses for that commit but cannot conclusively enumerate push-triggered workflow runs, so this index does not claim that zero automatic runs occurred.
