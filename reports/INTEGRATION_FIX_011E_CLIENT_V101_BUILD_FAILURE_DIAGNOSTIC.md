# INTEGRATION-FIX-011E — CLIENT v1.0.1 Build Failure Diagnostic

Generated: `2026-07-18T22:34:01Z`

## Scope

- Source: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip`
- Expected SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`
- Failed workflow run: `29655230341`
- Failed job: `88108225341`
- This diagnostic did **not** invoke Gradle, assemble an APK, modify `main`, publish, release, deploy, or install.

## Extracted failure evidence

```text
2026-07-18T18:07:27.2433097Z   inflating: /home/runner/work/Swrlzcore/Swrlzcore/BUILD_WORK/CLIENT_CFv1.0.1_SWRLZ/extracted/CLIENT_CFv1.0.0_SWRLZ/scripts/test_cf8_admin_fallback.py
2026-07-18T18:07:27.2464591Z ##[group]Run set -euo pipefail
2026-07-18T18:07:27.2464937Z set -euo pipefail
2026-07-18T18:07:27.2465225Z fail() { echo "::error::$1"; exit 1; }
2026-07-18T18:07:27.2465674Z [[ -n "${OPENAI_API_KEY:-}" ]] || fail "OPENAI_API_KEY secret is missing or empty."
2026-07-18T18:07:27.2466229Z [[ -n "${SWRLZ_API_TOKEN:-}" ]] || fail "SWRLZ_API_TOKEN secret is missing or empty."
2026-07-18T18:07:27.2466770Z [[ -d "$PROJECT_DIR" ]] || fail "Project directory does not exist: $PROJECT_DIR"
2026-07-18T18:07:27.2467399Z BACKEND_ENV_DIR="$(find "$PROJECT_DIR" -maxdepth 3 -type d -name backend | sort | head -n 1 || true)"
...
2026-07-18T18:08:54.1150561Z > Task :app:checkDebugDuplicateClasses
2026-07-18T18:09:00.7144352Z > Task :app:mergeDebugStartupProfile
2026-07-18T18:09:24.0161002Z > Task :app:compileDebugKotlin
2026-07-18T18:09:27.3153026Z e: file:///home/runner/work/Swrlzcore/Swrlzcore/BUILD_WORK/CLIENT_CFv1.0.1_SWRLZ/extracted/CLIENT_CFv1.0.0_SWRLZ/android/app/src/main/java/sh/swurlz/core/net/Api.kt:723:73 Unresolved reference 'request'.
2026-07-18T18:09:30.7142802Z
2026-07-18T18:09:30.7147217Z > Task :app:mergeExtDexDebug
2026-07-18T18:10:15.1159199Z > Task :app:compileDebugKotlin FAILED
2026-07-18T18:10:37.3182166Z
2026-07-18T18:10:37.3210215Z FAILURE: Build failed with an exception.
2026-07-18T18:10:37.3239113Z
2026-07-18T18:10:37.3249602Z * What went wrong:
2026-07-18T18:10:37.3270205Z Execution failed for task ':app:compileDebugKotlin'.
2026-07-18T18:10:37.3300614Z > A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
2026-07-18T18:10:37.3329311Z    > Compilation error. See log for more details
2026-07-18T18:10:37.3350252Z
2026-07-18T18:10:37.3359464Z * Try:
2026-07-18T18:10:37.3390036Z > Run with --info or --debug option to get more log output.
2026-07-18T18:10:37.3420551Z > Run with --scan to get full insights.
2026-07-18T18:10:37.3421398Z > Get more help at https://help.gradle.org.
2026-07-18T18:10:37.3449086Z
2026-07-18T18:10:37.3479291Z * Exception is:
2026-07-18T18:10:37.3509696Z org.gradle.api.tasks.TaskExecutionException: Execution failed for task ':app:compileDebugKotlin'.
2026-07-18T18:10:37.3511824Z 	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.lambda$executeIfValid$1(ExecuteActionsTaskExecuter.java:130)
2026-07-18T18:10:37.3539686Z 	at org.gradle.internal.Try$Failure.ifSuccessfulOrElse(Try.java:282)
2026-07-18T18:10:37.3569941Z 	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:128)
2026-07-18T18:10:37.3585567Z 	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:116)
...
2026-07-18T18:10:37.3792678Z 	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:380)
2026-07-18T18:10:37.3794172Z 	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
2026-07-18T18:10:37.3795652Z 	at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
2026-07-18T18:10:37.3797266Z Caused by: org.jetbrains.kotlin.gradle.tasks.CompilationErrorException: Compilation error. See log for more details
2026-07-18T18:10:37.3799152Z 	at org.jetbrains.kotlin.gradle.tasks.TasksUtilsKt.throwExceptionIfCompilationFailed(tasksUtils.kt:21)
2026-07-18T18:10:37.3800756Z 	at org.jetbrains.kotlin.compilerRunner.GradleKotlinCompilerWork.run(GradleKotlinCompilerWork.kt:119)
2026-07-18T18:10:37.3802703Z 	at org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction.execute(GradleCompilerRunnerWithWorkers.kt:76)
2026-07-18T18:10:37.3804518Z 	at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
```

## Referenced source context

### `CLIENT_CFv1.0.0_SWRLZ/android/app/src/main/java/sh/swurlz/core/net/Api.kt:723:73`

```text
  715 |         if (adminToken.isNotBlank()) header(HttpHeaders.Authorization, "Bearer $adminToken")
  716 |     }
  717 |
  718 |     private fun enc(value: String): String = URLEncoder.encode(value, "UTF-8")
  719 |
  720 |     private fun validate(response: HttpResponse, body: String) {
  721 |         if (!response.status.isSuccess()) {
  722 |             val detail = body.take(240).ifBlank { response.status.description }
  723 |             throw CoreNodeHttpException(response.status.value, response.request.url.encodedPath, detail)
  724 |         }
  725 |         val type = response.headers[HttpHeaders.ContentType].orEmpty().lowercase()
  726 |         if (type.isNotBlank() && !type.contains("application/json")) {
  727 |             throw CoreNodeProtocolException("Expected JSON but received $type")
  728 |         }
  729 |     }
  730 |
  731 |     private fun JsonObject.stringAny(vararg keys: String): String? = keys.firstNotNullOfOrNull { key ->
```


## Status

Root-cause classification and a minimal repair patch are prepared separately on this diagnostic branch after review of this evidence.
