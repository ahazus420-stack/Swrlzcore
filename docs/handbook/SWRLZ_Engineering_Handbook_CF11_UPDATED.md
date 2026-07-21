# SWRLZ Engineering Handbook (Living Blueprint)

Version: CF11 Foundation

## Purpose

This document is the master continuity file for future ChatGPT
conversations.

### Conversation Bootstrap Instructions

1.  Upload this handbook.
2.  Say:
    -   "Continue SWRLZ development from this handbook."
    -   "Treat this as the canonical engineering state."
3.  Upload the newest source ZIP if code changes are needed.
4.  Continue from the latest roadmap rather than rebuilding context.

------------------------------------------------------------------------

# Core Doctrine

-   Integrate, do not overwrite.
-   Preserve lineage.
-   Repository defines truth.
-   Artifacts reflect truth.
-   One active release in `SOURCES/`.
-   Historical releases belong in `SOURCES/OLD_PATCHES/`.

------------------------------------------------------------------------

# GitHub Release Workflow

Develop → Create branch → Update source → Update BUILD_REQUEST → Commit
→ Compare → Pull Request → Merge → GitHub Actions → APK Artifact →
Install → Field Test → Next CF

## Confirmed GitHub Capabilities

-   Repository reads
-   Branch creation
-   Branch comparison
-   File updates
-   Commit inspection
-   Draft Pull Requests

------------------------------------------------------------------------

# CF11 Goals

-   Fix discovery to use the current active network first.
-   Derive subnet from current IP.
-   Scan port 8787.
-   Validate discovery signature.
-   Use cached network values only after current-network discovery
    fails.

------------------------------------------------------------------------

# CF11.1 Release Custodian

After a successful APK build:

-   Publish artifact
-   Archive previous release payloads
-   Keep only current release in `SOURCES/`
-   Preserve lineage in `SOURCES/OLD_PATCHES/`

------------------------------------------------------------------------

# Maturity Snapshot

Identity ██████████ 100% Device Anchor ██████████ 100% Ghost Lineage
█████████▌ 95% Discovery ████████░░ 85% GitHub Pipeline ██████████ 100%
APK Automation ████████░░ 80% Release Custodian ███████░░░ 70% Mission
Runtime ███░░░░░░░ 35% Multi-device ███░░░░░░░ 30%

------------------------------------------------------------------------

# Server Device (Termux)

Project directory:

``` bash
cd ~/swrlz-server/SWRLZ_LOCAL_NODE_SERVER_0.7.3_DISCOVERY_SIGNATURE_FULL
```

Start server:

``` bash
bash start-termux.sh
```

Verify server is running:

``` bash
curl http://127.0.0.1:8787/status
```

Expected result: - Server responds successfully. - Client should
discover it over the current network.

------------------------------------------------------------------------

# Client Testing Checklist

-   Connect both devices to the same network.
-   Launch server.
-   Start Android client.
-   Verify automatic discovery.
-   Verify identity signature.
-   Verify node registration.
-   Capture screenshots of regressions.

------------------------------------------------------------------------

# Roadmap

CF11 --- Discovery Authority CF11.1 --- Release Custodian CF12 ---
Mission Runtime CF13 --- Node Orchestration CF14 --- Multi-device
Automation CF15 --- Observer & Mission Console

------------------------------------------------------------------------

# Notes for Future Conversations

Always continue from this handbook. Do not recreate project structure
unless requested. Preserve release lineage. Keep the roadmap updated
after each milestone.


---

CF12 Architectural Principles (Draft)

SWRLZ Foundation-First Doctrine
- Build today's features so they naturally support tomorrow's architecture.
- The server owns permanent knowledge.
- Clients execute missions and report observations.
- Knowledge is additive. Integrate, do not overwrite.
- Software updates never overwrite user knowledge.

Mission Authority
- Server plans and dispatches missions.
- Clients execute and report results.
- Heartbeats synchronize status, capabilities, mission state, and user preferences.

Guilds
- Invite / accept trust model.
- Guild membership authorizes routine missions.
- Device mission mode (Auto, Ask, DND) is synchronized to the server.

Knowledge Federation
- Servers may form trusted federations.
- Exchange knowledge, not secrets.
- Merge additively with preserved lineage.
- Community submissions are reviewed before official release.

Data Classification
- Shareable: knowledge, recovery, compatibility.
- Private: local logs, settings, guilds.
- Secret: credentials, API keys, Wi-Fi passwords, tokens.

Update Policy
- Update software.
- Preserve user data.
- Migrate knowledge.