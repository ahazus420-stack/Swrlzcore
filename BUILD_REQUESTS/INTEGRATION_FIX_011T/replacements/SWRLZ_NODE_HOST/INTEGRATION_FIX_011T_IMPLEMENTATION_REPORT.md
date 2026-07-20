# INTEGRATION-FIX-011T — Paired-LAN Authorization and Device Resolution Candidate

- **Status:** Temporary-branch source candidate; static/type-stub validation only
- **Approved scope:** Paired local-LAN authorization, device-proof verification infrastructure, and non-mutating `POST /devices/resolve`
- **Protocol/schema:** `1 / 1`
- **Canonical input:** `SERVER_CFv1.1.0_SWRLZ.zip`
- **Canonical input SHA-256:** `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- **Successor source identity:** `SERVER_CFv1.1.1_SWRLZ`
- **Implementation owner:** Android NODE_HOST / SERVER lane

## Purpose

This candidate implements the first bounded SERVER source slice from the accepted `SERVER Paired-LAN Bootstrap Route Surface v1` contract:

```text
approved local-LAN peer
+ valid SERVER pairing authorization
+ request ID
+ bounded timestamp and nonce
+ HMAC-SHA256 device proof when a verification key is bound
+ read-only stable identity lookup
```

It does not change CLIENT behavior and does not implement registration hardening, membership restoration, atomic create-and-join, heartbeat hardening, administrator routes, mission authority, or hosted/public transport.

## Added route

```http
POST /devices/resolve
```

The route requires JSON, explicit protocol/schema `1 / 1`, `X-SWRLZ-Request-Id`, and valid pairing authorization. Non-loopback access is accepted only when the SERVER listener is bound to an approved local interface and the peer address is IPv4 site-local or link-local. Loopback diagnostics retain compatibility with the pre-existing `X-Swurlz-Pairing-Token` spelling, while paired LAN resolution requires the canonical `X-SWRLZ-Pairing-Token` header.

The route never creates or updates a device, group, membership, session, audit record, trust record, or authority record.

## Device-proof verification

Known-device proof infrastructure implements HMAC-SHA256 over the accepted canonical text:

```text
<METHOD>\n
<PATH>\n
<PROTOCOL_VERSION>\n
<SCHEMA_VERSION>\n
<DEVICE_ID>\n
<TIMESTAMP>\n
<NONCE>\n
<REQUEST_ID>\n
<SHA256_HEX_OF_EXACT_REQUEST_BODY>
```

The implementation:

- requires the canonical device-proof headers and rejects duplicate security-critical headers;
- hashes the exact raw request-body bytes before JSON parsing;
- rejects malformed UTF-8 instead of signing or parsing replacement characters;
- accepts decimal epoch milliseconds or ISO-8601 instants while signing the exact transmitted timestamp text;
- enforces a 120-second clock-skew window;
- retains accepted `(device_id, nonce)` pairs for a bounded five-minute replay window;
- bounds the in-memory replay set to 4,096 entries;
- compares HMAC values with constant-time byte equality;
- verifies the submitted device-key fingerprint after proof validation;
- rejects identity mismatch, stale proof, invalid proof, and replay distinctly;
- never interprets an unknown device's self-generated proof as SERVER-recognized identity.

Replay memory is process-local. A restart clears the nonce cache, while the timestamp window continues to bound replay usefulness. Persisting replay state would require a separately approved storage decision.

## Non-mutating registry resolution

`PresenceRegistry.resolveDevice()` classifies the submitted stable device ID and strong identity hints without calling `commit()` or advancing registry revision.

Internal classification preserves these distinctions:

```text
KNOWN_CANDIDATE
UNKNOWN
RETIRED_CANDIDATE
REPLACED_CANDIDATE
BLOCKED_CANDIDATE
IDENTITY_CONFLICT
ACTION_REQUIRED
```

The HTTP route exposes only accepted contract states after the appropriate proof boundary:

```text
KNOWN
UNKNOWN
RETIRED
REPLACED
BLOCKED
IDENTITY_CONFLICT
ACTION_REQUIRED
```

An exact known record with conflicting stored identity evidence never degrades to `UNKNOWN`. A different submitted device ID matching strong stored identity hints returns `IDENTITY_CONFLICT` without enumerating the registry. Ghost/Legacy successor lineage is returned only after valid known-device proof.

## Existing proof-key compatibility boundary

The v1.1.0 registry intentionally stores device credentials only as salted `PBKDF2WithHmacSHA256` verifiers. A verifier can validate a supplied raw secret but cannot recover the original device key required to verify an HMAC request proof.

This checkpoint explicitly prohibits a registry/database migration and prohibits changing registration behavior. Therefore this candidate does **not** fabricate a proof key, use the PBKDF2 verifier as an HMAC key, accept the raw device key in `/devices/resolve`, or treat pairing as device ownership.

Production wiring uses:

```text
DeviceProofKeyResolver.UNBOUND
```

Consequences:

- `UNKNOWN` and identity-conflict resolution are operational under paired LAN authorization;
- known/retired/replaced/blocked records require proof headers;
- when no independently bound proof key exists, the route returns `ACTION_REQUIRED` without exposing device or membership detail;
- the HMAC verifier is type-checked and behavior-tested with an injected bounded test key resolver;
- a later separately approved key-binding checkpoint is required before existing registered devices can resolve as `KNOWN` over paired LAN.

This limitation is deliberate and fail-closed. It preserves the no-migration and no-registration-change boundary rather than silently weakening the accepted contract.

## Existing write behavior preserved

The pre-existing mutation routes remain unchanged in this candidate:

```http
POST /groups/create
POST /devices/register
POST /groups/join
POST /devices/checkin
```

Their implementation blocks are checksum-locked by the `011T` static validator. They remain loopback-only, continue using the existing legacy pairing header adapter, and retain their prior request/response and registry semantics. This checkpoint does not enable paired-LAN mutation.

Discovery advertises the additive capabilities:

```text
presence.device.resolve.v1
presence.device.proof.v1
```

It separately declares:

```text
presenceWriteExposure: loopback-only
presenceResolveExposure: paired-lan
presenceDeviceProofBinding: required
```

## Local-versus-remote boundary

This candidate accepts only loopback or explicit local-LAN peers on the existing approved-interface listener. It does not define public-internet authentication, NAT traversal, relay transport, cloud callbacks, paid runtime dependencies, or remote administration.

Network reachability, a caller-supplied device ID, Dev Mode, local admin toggles, group membership, or pairing alone never establishes device proof, administrator authority, mission authority, or trust-root authority.

## Changed source paths

Relative to exact SERVER v1.1.0, this successor changes only:

1. `INTEGRATION_FIX_011T_IMPLEMENTATION_REPORT.md`
2. `app/build.gradle.kts`
3. `app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt`
4. `app/src/main/java/sh/swrlz/nodehost/service/NodeRuntime.kt`
5. `app/src/main/java/sh/swrlz/nodehost/service/PairedLanRequestAuthority.kt`
6. `app/src/main/java/sh/swrlz/nodehost/service/PresenceProtocol.kt`
7. `app/src/main/java/sh/swrlz/nodehost/service/PresenceRegistry.kt`
8. `scripts/test_paired_lan_resolve_011t.py`

No CLIENT, workflow, release, deployment, installation, admin, mission, database migration, or canonical `main` path is changed.

## Validation boundary

Completed without Gradle:

- deterministic source-delta checks;
- static contract checks;
- preserved legacy mutation-block hashes;
- pure Kotlin type-stub compilation of authorization, protocol, registry, and runtime integration surfaces;
- executable Kotlin HMAC, header, clock-skew, replay, loopback-compatibility, and unbound-key tests;
- independent Python HMAC reference vector and source-law checks.

**No Gradle task, Android dependency resolution, KSP, Hilt, Compose compilation, APK assembly, signing, workflow, installation, release, or deployment is authorized or proven by this checkpoint.**

## Truth Firewall and lineage boundary

The new route cannot grant administrator or mission authority, alter trust roots, suppress objection/refusal state, delete lineage, rotate identity, create a replacement device, or bypass the Truth Firewall. Resolution is observation plus proof verification only.
