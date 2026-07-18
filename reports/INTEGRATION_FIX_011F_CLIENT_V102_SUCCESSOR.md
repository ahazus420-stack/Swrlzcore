# INTEGRATION-FIX-011F — CLIENT CFv1.0.2 Successor

## Result

**PASS — deterministic one-line successor created on the temporary diagnostic branch.**

- Input ZIP: `SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip`
- Input SHA-256: `9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7`
- Output ZIP: `SOURCES/CLIENT/CLIENT_CFv1.0.2_SWRLZ.zip`
- Output SHA-256: `e618938d662c9b39dc33a786eca40eeecd4b9675f6558a7bd4a328b5fa5b92c1`
- Changed archive entry: `CLIENT_CFv1.0.0_SWRLZ/android/app/src/main/java/sh/swurlz/core/net/Api.kt`
- Added source line: `import io.ktor.client.statement.request`
- Removed source lines: `0`
- Actual-route expression preserved: `response.request.url.encodedPath`
- Other archive entry contents changed: `0`
- Canonical v1.0.1 ZIP modified: `no`
- `main` modified: `no`
- APK built by this generator: `no`

The archive filename is versioned as CFv1.0.2. Its internal project root remains unchanged to keep the repair limited to the approved Kotlin import.
