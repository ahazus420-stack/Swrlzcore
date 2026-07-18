# SWRLZ Keyboard System Architecture

Status: accepted architecture index
Checkpoint lineage: SWRLZ-KBD-CON-001, SWRLZ-KBD-IPC-001, SWRLZ-KBD-MOD-001

## Scope

The keyboard is a dedicated Android Input Method Editor surface that integrates with the canonical SWRLZ CLIENT without becoming a separate physical device or NODE_HOST.

## Canonical architecture documents

- `SWRLZ_KBD_MODULE_COMPONENT_ARCHITECTURE_V1.md` — accepted module, package, Android component, dependency, process, storage, DI, variant, and testing architecture.
- `../contracts/SWRLZ_KBD_CONTRACT_V1.md` — trust, privacy, CLIENT enrollment, routing, telemetry, failure, and verification requirements.
- `../contracts/SWRLZ_KBD_IPC_WIRE_CONTRACT_V1.md` — CLIENT-owned Binder transport and enrollment wire protocol.
- `../identity/SWRLZ_IDENTITY_SURFACES_CONTRACT_V1.md` — physical-device, surface, installation, node, session, and lineage semantics.
- `../testing/SWRLZ_KBD_MOD_001_ACCEPTANCE_MATRIX.md` — implementation conformance criteria for the module architecture.

## Module boundary summary

- installable app and composition root;
- Android IME lifecycle module;
- Compose keyboard UI module;
- pure Kotlin domain module;
- persistence/data module;
- security and keystore module;
- CLIENT Binder bridge module;
- versioned keyboard contracts;
- shared identity, design-system, and telemetry contracts;
- reusable test fixtures.

## Hard boundaries

- Ordinary typing remains functional without CLIENT, NODE_HOST, LAN, or internet.
- The IME never becomes the canonical registry or root identity authority.
- The keyboard never receives a `nodeId` unless a future accepted contract explicitly makes it node-capable.
- Password and other sensitive editor contexts disable AI and content-processing actions.
- Remote processing requires deliberate user action and visible route disclosure.
- Discovery cannot grant authority.
- Long-running AI execution does not run in the IME process.
- Truth Firewall and signature-verification controls cannot be disabled by feature flags.

## Data-flow summary

1. Android supplies an editor context to the IME.
2. Local policy classifies the context.
3. Ordinary key events are committed directly to the target editor.
4. Explicit SWRLZ actions create a bounded request from selected or user-confirmed text.
5. The route resolver prefers approved local execution.
6. Any remote route is disclosed and governed by accepted consent policy.
7. Only operational, content-free evidence may be emitted as telemetry.

This index and its linked documents define architecture only. Implementation remains separately gated.
