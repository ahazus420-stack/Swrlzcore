# SWRLZ-KBD-IPC-001 Acceptance Matrix

Status: Accepted design evidence

| ID | Requirement | Required evidence at implementation checkpoint |
|---|---|---|
| IPC-01 | Explicit bound CLIENT service is the primary v1 transport. | Manifest/service declaration review and integration test. |
| IPC-02 | Service binding is protected by a signature-level permission. | Manifest evidence and negative bind test. |
| IPC-03 | CLIENT verifies Binder caller UID, package, and signing certificate. | Unit tests and instrumented spoofing tests. |
| IPC-04 | Signing continuity alone does not create enrollment. | Negative enrollment test. |
| IPC-05 | Enrollment requires explicit CLIENT approval. | UI flow evidence and state transition test. |
| IPC-06 | Keyboard receives the existing device identity association, not a new physical-device record. | Registry and server aggregation evidence. |
| IPC-07 | Keyboard receives unique surface and installation identities. | Persistence and reinstall tests. |
| IPC-08 | Credentials are scoped to keyboard operations only. | Scope-denial tests. |
| IPC-09 | Credential material is non-exportable or wrapped where supported. | Keystore/storage review. |
| IPC-10 | Every request has protocol, schema, request ID, timestamp, and nonce. | Serialization tests. |
| IPC-11 | Replayed nonce/request IDs are rejected. | Replay tests. |
| IPC-12 | Stale requests are rejected within documented clock-skew rules. | Timestamp boundary tests. |
| IPC-13 | Unknown operations fail closed. | Forward-compatibility negative tests. |
| IPC-14 | Ordinary typing works without CLIENT IPC. | Process-death and package-disabled tests. |
| IPC-15 | Keyboard never silently connects directly to SERVER when CLIENT is unavailable. | Network inspection and negative tests. |
| IPC-16 | Protected fields never transmit text. | Password, payment, OTP, and ambiguous-field tests. |
| IPC-17 | AI transform requires explicit user action. | Interaction tests. |
| IPC-18 | Remote routing requires separate consent and Truth Firewall approval. | Route-policy tests. |
| IPC-19 | Local failure never silently falls back to remote. | Forced local failure tests. |
| IPC-20 | Responses identify the actual route used. | Contract serialization and UI evidence. |
| IPC-21 | Long operations use asynchronous operation IDs. | Lifecycle tests. |
| IPC-22 | Cancellation is supported and observable. | Cancellation race tests. |
| IPC-23 | Payload ceilings are enforced without crash. | Boundary and oversized-payload tests. |
| IPC-24 | Binder transaction failure returns a typed error. | Fault-injection tests. |
| IPC-25 | Error responses contain no text payloads, secrets, stack traces, or topology. | Error redaction review. |
| IPC-26 | Credential rotation records lineage and revokes superseded credentials. | Rotation and overlap-window tests. |
| IPC-27 | Surface retirement preserves history without retiring the device. | Registry lifecycle tests. |
| IPC-28 | Backup/restore cannot silently clone credentials to another device. | Restore test and security review. |
| IPC-29 | Version negotiation fails closed with no compatible overlap. | Protocol/schema matrix tests. |
| IPC-30 | Telemetry is content-free. | Event schema and log review. |
| IPC-31 | Surface/install IDs do not inflate physical-device statistics. | Server query and dashboard evidence. |
| IPC-32 | Keyboard cannot enumerate unrelated surfaces or credentials. | Authorization negative tests. |
| IPC-33 | CLIENT cannot use this contract for continuous keystroke capture. | API review and instrumentation evidence. |
| IPC-34 | Truth Firewall deny and confirmation states remain visible and enforceable. | Policy-path tests and UI evidence. |
| IPC-35 | No production implementation is represented as accepted until a later implementation checkpoint passes. | Checkpoint record and repository evidence. |

## Pass rule

A future implementation checkpoint must map every applicable row to source paths, test identifiers, and captured evidence. Any failed security, privacy, identity, routing, or protected-field row blocks acceptance.