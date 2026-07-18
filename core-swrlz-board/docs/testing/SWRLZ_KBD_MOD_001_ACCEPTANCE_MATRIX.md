# SWRLZ-KBD-MOD-001 Acceptance Matrix

Status: accepted design verification matrix
Checkpoint: SWRLZ-KBD-MOD-001
Implementation authorization: none

An implementation may claim conformance only when every applicable item has objective evidence.

| ID | Requirement | Required evidence |
|---|---|---|
| MOD-01 | Ordinary typing works without CLIENT, NODE_HOST, LAN, or internet. | Instrumented offline test. |
| MOD-02 | Domain module has no Android/Compose/Binder/storage dependencies. | Gradle dependency report and source inspection. |
| MOD-03 | Gradle dependency graph is acyclic. | Dependency graph check. |
| MOD-04 | IME service owns only Android input lifecycle and orchestration. | Architecture review and source mapping. |
| MOD-05 | Canonical device/trust registry remains CLIENT-owned. | Repository and IPC review. |
| MOD-06 | Keyboard does not receive or create a node identity. | Identity-state test and schema review. |
| MOD-07 | Binder binding is explicit and verifies CLIENT signing identity. | Security test vectors. |
| MOD-08 | Privileged actions fail closed when bridge is unavailable. | Binder-death and unavailable-service tests. |
| MOD-09 | Ordinary key input remains unaffected by bridge failure. | Failure-injection instrumentation test. |
| MOD-10 | Sensitive classification happens locally before content routing. | Policy ordering test. |
| MOD-11 | Ambiguous-sensitive contexts fail closed. | EditorInfo classification vectors. |
| MOD-12 | Protected contexts disable content-processing features. | UI and domain tests. |
| MOD-13 | No silent local-to-remote fallback exists. | Route-resolution tests. |
| MOD-14 | Truth Firewall cannot be disabled by feature flags. | Configuration and mutation tests. |
| MOD-15 | Surface private material is delegated to Android Keystore. | Storage inspection and keystore tests. |
| MOD-16 | Preferences use encrypted DataStore or an accepted equivalent. | Persistence review and tests. |
| MOD-17 | Room is used only for justified relational or queue data. | Schema and architecture review. |
| MOD-18 | Operational queues are bounded. | Quota and overflow tests. |
| MOD-19 | Logs exclude editor, clipboard, prompt, generated, and secret content. | Automated log-scrub tests. |
| MOD-20 | Telemetry is content-free and schema-validated. | Contract tests. |
| MOD-21 | Process recreation does not restore ephemeral editor text. | Process-death test. |
| MOD-22 | Editor target changes clear session-scoped content. | Session lifecycle test. |
| MOD-23 | Long-running AI execution is absent from the IME process. | Process and source inspection. |
| MOD-24 | Hilt scopes match process, service/session, and settings lifetimes. | DI graph review. |
| MOD-25 | No static service locator or domain-held Context exists. | Static analysis and review. |
| MOD-26 | Protocol mismatch produces a typed, non-crashing failure. | Version-negotiation contract test. |
| MOD-27 | Binder requests support timeout and cancellation. | Concurrency tests. |
| MOD-28 | Debug-only bypasses cannot ship in release. | Variant and release artifact inspection. |
| MOD-29 | Accessibility semantics cover keyboard actions and disabled reasons. | Compose accessibility tests. |
| MOD-30 | Test fixtures contain no production credentials or real user content. | Repository secret/content scan. |
| MOD-31 | Package/module naming remains consistent and collision-free. | Build configuration review. |
| MOD-32 | Application ID and signing decisions remain separately gated. | Checkpoint evidence. |
| MOD-33 | All deviations from this architecture have accepted decision records. | Contract registry review. |
| MOD-34 | Implementation does not overwrite canonical CLIENT behavior. | Diff review and integration evidence. |
| MOD-35 | Local-versus-remote route disclosure remains visible and testable. | UI and route evidence tests. |

Passing this matrix does not itself authorize release or deployment.
