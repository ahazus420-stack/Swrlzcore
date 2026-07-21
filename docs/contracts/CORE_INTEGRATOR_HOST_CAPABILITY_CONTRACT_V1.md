# CORE Integrator Host-Capability Contract v1

- **Status:** Proposed
- **Version:** 1
- **Checkpoint:** CORE-ARCH-003
- **Related ADR:** `docs/architecture/adr/ADR-0003-CORE-INTEGRATOR-AND-HOST-CAPABILITY-COMPOSITION.md`

## 1. Purpose

This contract defines how reusable SWRLZ capability modules are packaged into distinct Android app shells without copying whole applications, merging identities, or inheriting unrestricted authority.

## 2. Definitions

- **Integrator:** a reusable compile-time capability module implementing this contract.
- **Host:** an installable SWRLZ app shell such as Core, Keyboard, Launcher, CLIENT, or NODE_HOST.
- **Host adapter:** the narrow bridge exposing host services to an integrator.
- **Composition manifest:** the authoritative build-time declaration of integrators packaged by one host.
- **Host profile:** a named, restricted integrator configuration approved for one host role.

## 3. Required integrator descriptor

Every integrator MUST declare:

- `integratorId`;
- semantic `integratorVersion`;
- `contractVersion`;
- source lineage and checksum identity;
- supported host types;
- required host capabilities;
- optional host capabilities;
- required Android permissions and components;
- storage namespace and migration version;
- local, LAN, and remote network behavior;
- lifecycle requirements;
- failure policy;
- protocol/schema compatibility range;
- Truth Firewall and audit impact;
- supported host profiles.

## 4. Host capability declaration

A host MUST expose an explicit capability set. Capabilities MAY include foreground service support, background work, VPN service, accessibility integration, IME context, HOME role, overlay UI, secure storage, IPC, local networking, LAN networking, remote networking, notifications, and diagnostics.

Absence of a capability MUST produce an explicit unsupported or unavailable result. Integrators MUST NOT infer availability from Android context, package name, signature matching, or reflection.

## 5. Composition rules

1. Integrators are packaged only through an explicit composition manifest.
2. Build-time inclusion and runtime authorization remain separate decisions.
3. A host MUST reject startup of an integrator whose contract, protocol, host type, profile, permission, or storage requirements are incompatible.
4. A host MUST expose the reason for rejection.
5. Integrators MUST NOT add Android components or permissions silently; required manifest contributions must be declared and reviewed.
6. A descendant app shell MUST consume shared modules rather than copy and mutate the canonical implementation.

## 6. Lifecycle

The initial lifecycle contract is:

```text
inspect descriptor
→ validate compatibility
→ initialize
→ start
→ pause/resume as applicable
→ stop
→ migrate or retire explicitly
```

Initialization MUST be idempotent or return an explicit conflict. Stop MUST release owned resources. Host process death and restart behavior MUST be documented per integrator.

## 7. Storage and migration

- Storage MUST be namespaced by integrator identity and host installation identity.
- Shared storage across app shells is prohibited unless an accepted IPC or provider contract explicitly authorizes it.
- Schema changes require versioned migrations.
- Failed migration MUST fail closed for protected data and preserve recovery evidence.
- Removal or retirement MUST preserve lineage and rollback instructions.

## 8. Trust and authority

- Packaging does not grant trust, enrollment, entitlement, or execution authority.
- Shared device identity does not grant shared unrestricted authorization.
- Keyboard, Launcher, CLIENT, NODE_HOST, and Core remain separate surfaces.
- Host profiles MUST implement least authority.
- Entitlement cannot override safety, privacy, identity, trust, protocol, or Truth Firewall requirements.

## 9. Routing and offline behavior

- Offline-first operation is mandatory where the capability can function locally.
- Route classes MUST remain explicit: local, LAN, or remote.
- No silent local-to-remote fallback is allowed.
- Remote dependencies, cost implications, trust requirements, and failure behavior MUST be declared.
- A network failure MUST NOT create an obedience-only or policy-bypass mode.

## 10. Failure isolation

- Optional integrator failure MUST NOT crash unrelated host startup.
- Mandatory integrators require an accepted fail-closed declaration.
- Repeated failure SHOULD enter a quarantined or unavailable state with reason code and audit evidence.
- One integrator MUST NOT mutate another integrator's storage, lifecycle, or policy state directly.

## 11. UI contributions

Integrators MAY expose typed UI contributions, but MUST NOT own host navigation or assume a specific shell layout. The host decides placement, visibility, accessibility, and role-appropriate presentation. UI hiding is not an authorization boundary.

## 12. Phoenix Firewall profiles

The initial proposed profiles are:

| Profile | Intended host | Boundary |
|---|---|---|
| `CORE_FULL` | Core | full local policy administration and diagnostics |
| `KEYBOARD_RESTRICTED` | Keyboard | input-path protections only; no unrelated content capture or unrestricted network authority |
| `LAUNCHER_RESTRICTED` | Launcher | app-launch, intent, and surface policy only |
| `CLIENT_SCOPED` | CLIENT | enrollment, route, and user-approval policy within CLIENT authority |
| `NODE_HOST_SCOPED` | NODE_HOST | node execution and route policy within NODE_HOST authority |

Profiles MUST configure one shared engine; they MUST NOT become divergent source forks.

## 13. Evidence requirements

Implementation checkpoints MUST produce:

- module and dependency graph;
- composition manifest per host;
- descriptor and profile evidence;
- source lineage and checksums;
- permission and manifest diff;
- compatibility and failure-isolation tests;
- storage migration tests;
- offline and routing tests;
- Truth Firewall preservation evidence;
- build and on-device evidence per host.

## 14. Non-authorization

This contract does not authorize implementation, Gradle changes, permissions, app-lane changes, builds, workflow runs, releases, deployments, installations, or dynamic executable plugin loading.