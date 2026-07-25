# CFv2.1.0 Autonomous Update and Repair Architecture v1

**Status:** Accepted CFv2.1.x architecture direction; implementation/build/device acceptance remain evidence-gated.
**Recorded:** 2026-07-25

## Purpose

CFv2.1.0 extends SWRLZ Chat/Forge orchestration into a closed-loop update pipeline. CLIENT remains the primary edge orchestrator. It may discover a downloaded source package, verify its sibling checksum, stage and upload through Forge, observe the exact associated GitHub Actions runs, analyze failures, retry bounded repairs, retrieve successful artifacts, and hand verified APKs to Android install/update flows.

## Update flow

```text
User requests update
-> resolve exact downloaded ZIP + SHA
-> verify local pair and source lineage
-> present/record update transaction
-> Forge streams source to GitHub
-> confirm commit and branch head
-> discover commit-bound workflow runs
-> announce meaningful Chat milestones
-> success? retrieve and verify artifact
-> failure? retrieve job/step logs
-> classify failure
-> known deterministic repair? patch/repackage/re-hash/retry
-> uncertain? escalate to SWURVER / configured external model
-> bounded retry or stop for user decision
```

## Failure analysis tiers

1. Deterministic local signatures and known repair playbooks.
2. CLIENT local knowledge/procedural memory.
3. SWURVER reasoning/knowledge.
4. External provider or multi-provider reasoning only when lower tiers are insufficient.

Every diagnosis preserves evidence classification: verified log evidence, inferred cause, recommended repair, confidence, and source lineage.

## Safety invariants

- Never overwrite the last known-good package.
- Every repair attempt creates an immutable lineage checkpoint/diff.
- Retry count is bounded.
- Ambiguous architectural/security/signing failures stop or require explicit approval.
- A changed failure class triggers reassessment rather than blind repetition.
- Credentials/secrets are excluded from exported or model-submitted logs.
- GitHub/API success does not imply Android install success.

## SERVER update continuity

When CLIENT updates SERVER, CLIENT remains alive:

```text
CLIENT mission alive
-> download + verify SERVER artifact
-> request graceful SERVER stop/disconnect
-> Android in-place update when package/signing continuity permits
-> restart/reconnect SERVER
-> verify version/capability manifest/session
-> report completion in Chat
```

Uninstall/reinstall is a fallback only because uninstall can destroy application data and trust continuity.

## CLIENT self-update continuity

CLIENT must not rely on its own process surviving replacement. Before installation it writes a durable `UpdateHandoff` record containing transaction identity, expected version/hash, prior state, and post-install verification steps. A trusted updater/installer handoff or SWURVER coordination performs the install. The new CLIENT resumes from the handoff record and verifies the update before declaring success.

## Chat presentation

Meaningful milestones become persistent Chat events; high-frequency byte/job progress updates mutate live operational cards.

Examples:

- Found update package
- SHA verified
- Forge upload started
- Upload officially Swurlzed
- Workflow started
- Workflow failed; analyzing logs
- Known repair found; retry attempt N/M
- Workflow succeeded
- Artifact downloaded and verified
- Installing SERVER/CLIENT update
- Updated component reconnected and verified

## Evidence gate

The architecture is accepted when documented. Runtime acceptance requires compile/build evidence, real workflow evidence, artifact verification, Android install/update tests, reconnect/resume tests, and failure-loop regression tests.
