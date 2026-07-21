# SWRLZ Discovery Contract Portable API and Wire Vectors v1

- **Status:** Planned API and evidence specification; implementation not authorized
- **Checkpoint:** CORE-PLAN-008
- **Capsule ID:** `swrlz.discovery.contract`
- **Capsule version:** `0.1.0`
- **Contract version:** `1`
- **Discovery protocol:** `1`
- **Base schema:** `1`

## 1. Purpose

Define the exact public behavior planned for the portable discovery contract capsule. This document specifies API shape and immutable wire vectors only. It does not create Kotlin source or change CLIENT or SERVER.

## 2. Canonical constants

```kotlin
object DiscoveryContractConstants {
    const val CAPSULE_ID: String = "swrlz.discovery.contract"
    const val CAPSULE_VERSION: String = "0.1.0"
    const val CONTRACT_VERSION: Int = 1

    const val PROTOCOL_VERSION: Int = 1
    const val BASE_SCHEMA_VERSION: Int = 1

    const val SERVICE: String = "swrlz-local-node"
    const val ENDPOINT: String = "discovery-signature"

    const val NODE_ID_PREFIX: String = "swrlz-node-"
    const val INSTALLATION_ID_PREFIX: String = "swrlz-install-"

    const val TRUST_POLICY_PAIRING_REQUIRED: String = "pairing_required"
    const val MISSION_AUTHORIZATION_TRUSTED_ONLY: String = "trusted_only"
    const val REQUIRED_CAPABILITY_DISCOVERY: String = "discovery"
}
```

HTTP port, path, methods, headers, listener behavior, addresses, and route selection are host-owned and are not public capsule constants.

## 3. Planned public models

### 3.1 Producer input

```kotlin
data class DiscoverySuccessInput(
    val nodeId: String,
    val installationId: String,
    val displayName: String,
    val hostVersion: String,
    val port: Int,
    val capabilities: List<String>,
    val trustPolicy: String = DiscoveryContractConstants.TRUST_POLICY_PAIRING_REQUIRED,
    val missionAuthorization: String = DiscoveryContractConstants.MISSION_AUTHORIZATION_TRUSTED_ONLY,
    val protocolVersion: Int = DiscoveryContractConstants.PROTOCOL_VERSION,
    val schemaVersion: Int = DiscoveryContractConstants.BASE_SCHEMA_VERSION,
)
```

The SERVER adapter supplies host-owned truth. The capsule validates the supplied values but does not retrieve them.

### 3.2 Raw wire document

```kotlin
data class DiscoveryWireDocument(
    val ok: Boolean?,
    val service: String?,
    val endpoint: String?,
    val protocolVersion: Int?,
    val schemaVersion: Int?,
    val nodeId: String?,
    val installationId: String?,
    val displayName: String?,
    val hostVersion: String?,
    val port: Int?,
    val capabilities: List<String>?,
    val trust: DiscoveryWireTrust?,
)

data class DiscoveryWireTrust(
    val policy: String?,
    val missionAuthorization: String?,
)
```

Raw fields are nullable only to preserve typed validation evidence for missing or explicit-null fields. A valid protocol-v1 success document contains no null required fields.

### 3.3 Validated discovery identity

```kotlin
data class ValidatedDiscoveryNode(
    val nodeId: String,
    val installationId: String,
    val displayName: String,
    val hostVersion: String,
    val port: Int,
    val protocolVersion: Int,
    val schemaVersion: Int,
    val knownCapabilities: Set<KnownDiscoveryCapability>,
    val unknownCapabilities: Set<String>,
    val trustPolicy: DiscoveryTrustPolicy,
    val missionAuthorization: DiscoveryMissionAuthorization,
)

enum class KnownDiscoveryCapability {
    DISCOVERY,
}

enum class DiscoveryTrustPolicy {
    PAIRING_REQUIRED,
}

enum class DiscoveryMissionAuthorization {
    TRUSTED_ONLY,
}
```

The validated model is an advertisement model, not proof of identity or authority.

### 3.4 Error body

```kotlin
data class DiscoveryErrorInput(
    val code: String,
    val message: String,
    val protocolVersion: Int = DiscoveryContractConstants.PROTOCOL_VERSION,
    val schemaVersion: Int = DiscoveryContractConstants.BASE_SCHEMA_VERSION,
)

data class DiscoveryRemoteError(
    val code: String,
    val message: String,
    val protocolVersion: Int?,
    val schemaVersion: Int?,
)
```

HTTP status selection remains host-owned. The capsule may encode or decode the JSON error body only.

## 4. Planned result types

```kotlin
sealed interface DiscoveryEncodeResult {
    data class Encoded(val json: String) : DiscoveryEncodeResult
    data class Rejected(
        val code: DiscoveryReasonCode,
        val detail: String,
    ) : DiscoveryEncodeResult
}

sealed interface DiscoveryDecodeResult {
    data class Compatible(
        val node: ValidatedDiscoveryNode,
        val warnings: Set<DiscoveryWarningCode>,
    ) : DiscoveryDecodeResult

    data class Incompatible(
        val code: DiscoveryReasonCode,
        val detail: String,
        val observedProtocolVersion: Int? = null,
        val observedSchemaVersion: Int? = null,
    ) : DiscoveryDecodeResult

    data class RemoteError(
        val error: DiscoveryRemoteError,
    ) : DiscoveryDecodeResult
}
```

## 5. Reason codes

```kotlin
enum class DiscoveryReasonCode {
    INVALID_JSON,
    ROOT_NOT_OBJECT,
    REQUIRED_FIELD_MISSING,
    REQUIRED_FIELD_NULL,
    FIELD_TYPE_INVALID,
    OK_NOT_TRUE,
    SERVICE_MISMATCH,
    ENDPOINT_MISMATCH,
    PROTOCOL_VERSION_UNSUPPORTED,
    SCHEMA_VERSION_INVALID,
    NODE_ID_INVALID,
    INSTALLATION_ID_INVALID,
    DISPLAY_NAME_INVALID,
    HOST_VERSION_INVALID,
    PORT_OUT_OF_RANGE,
    CAPABILITIES_INVALID,
    DISCOVERY_CAPABILITY_MISSING,
    TRUST_OBJECT_INVALID,
    TRUST_POLICY_UNSUPPORTED,
    MISSION_AUTHORIZATION_UNSUPPORTED,
    PRODUCER_INPUT_INVALID,
    ERROR_BODY_INVALID,
    INTERNAL_CODEC_FAILURE,
}
```

These codes classify discovery contract behavior only. Capsule-package compatibility codes such as source checksum mismatch or integration-manifest invalidity remain part of the broader capsule integrator contract.

## 6. Warning codes

```kotlin
enum class DiscoveryWarningCode {
    HIGHER_ADDITIVE_SCHEMA_ACCEPTED,
    UNKNOWN_TOP_LEVEL_FIELDS_IGNORED,
    UNKNOWN_NESTED_FIELDS_IGNORED,
    UNKNOWN_CAPABILITIES_IGNORED_FOR_EXECUTION,
    DUPLICATE_CAPABILITIES_NORMALIZED,
}
```

Warnings must not silently become trust or authorization claims.

## 7. Planned codec API

```kotlin
interface DiscoveryContractCodec {
    fun encodeSuccess(input: DiscoverySuccessInput): DiscoveryEncodeResult

    fun encodeErrorBody(input: DiscoveryErrorInput): DiscoveryEncodeResult

    fun decode(json: String): DiscoveryDecodeResult
}
```

The implementation must be stateless and deterministic for identical input.

The API intentionally omits:

- HTTP status and headers;
- URLs and addresses;
- timeouts and retries;
- candidate scanning;
- observation timestamps;
- persistence;
- identity generation;
- tokens and proof material;
- pairing and trust mutation;
- mission authorization.

## 8. Validation rules

### 8.1 Required success fields

All protocol-v1 fields are required and non-null.

### 8.2 Sentinels

```text
service  == swrlz-local-node
endpoint == discovery-signature
```

Both comparisons are exact and case-sensitive.

### 8.3 Versions

- `protocolVersion == 1` is required.
- `schemaVersion >= 1` is required.
- schema `1` is canonical.
- a higher schema with protocol `1` is accepted only when all required v1 fields remain valid.
- unsupported protocol versions are rejected.

### 8.4 IDs

`nodeId` must:

- start with `swrlz-node-`;
- contain a 36-character UUID suffix;
- parse as canonical UUID text;
- be UUID version 4.

`installationId` must meet the same requirements with prefix `swrlz-install-`.

The capsule validates advertised IDs. It does not create or persist them.

### 8.5 Display name

- length: 1 through 64 characters;
- no C0 control characters or DEL;
- no leading or trailing normalization requirement beyond host policy;
- must not be interpreted as identity authority.

### 8.6 Host version

- nonblank;
- maximum planned length: 64 characters;
- no control characters.

### 8.7 Port

- integer range `1..65535`;
- does not cause the capsule to connect to that port.

### 8.8 Capabilities

- non-null array of nonblank stable strings;
- must include exact capability `discovery`;
- duplicate values normalize to one semantic entry and produce a warning;
- unknown values are retained for diagnostics and ignored for execution;
- capability advertisement never grants authorization.

### 8.9 Trust object

Protocol-v1 requires:

```text
policy = pairing_required
missionAuthorization = trusted_only
```

Unknown values fail closed. The capsule must not map them to trusted or permitted states.

## 9. Canonical serialized success vector

Vector ID: `DISCOVERY_V1_SUCCESS_CANONICAL`

Input:

```text
nodeId: swrlz-node-550e8400-e29b-41d4-a716-446655440000
installationId: swrlz-install-e29b41d4-a716-4466-8550-e8400e2941d4
displayName: SWRLZ Node Host
hostVersion: 1.0.0
port: 8787
capabilities: [discovery]
```

Required canonical one-line JSON bytes:

```json
{"ok":true,"service":"swrlz-local-node","endpoint":"discovery-signature","protocolVersion":1,"schemaVersion":1,"nodeId":"swrlz-node-550e8400-e29b-41d4-a716-446655440000","installationId":"swrlz-install-e29b41d4-a716-4466-8550-e8400e2941d4","displayName":"SWRLZ Node Host","hostVersion":"1.0.0","port":8787,"capabilities":["discovery"],"trust":{"policy":"pairing_required","missionAuthorization":"trusted_only"}}
```

Expected result:

```text
Compatible
warnings: []
```

## 10. Canonical serialized error vector

Vector ID: `DISCOVERY_V1_ERROR_IDENTITY_UNAVAILABLE`

Required canonical one-line JSON bytes:

```json
{"ok":false,"error":{"code":"IDENTITY_UNAVAILABLE","message":"Durable identity is unavailable"},"protocolVersion":1,"schemaVersion":1}
```

Expected decode result:

```text
RemoteError
code: IDENTITY_UNAVAILABLE
protocolVersion: 1
schemaVersion: 1
```

The host owns the corresponding HTTP `503` response.

## 11. Positive and compatibility vectors

### `DISCOVERY_V1_UNKNOWN_TOP_LEVEL_FIELD`

Add:

```json
"futureField": 1
```

Expected:

```text
Compatible
warning: UNKNOWN_TOP_LEVEL_FIELDS_IGNORED
```

### `DISCOVERY_V1_UNKNOWN_NESTED_TRUST_FIELD`

Add under `trust`:

```json
"futureTrustField": "value"
```

Expected:

```text
Compatible
warning: UNKNOWN_NESTED_FIELDS_IGNORED
```

### `DISCOVERY_V1_SCHEMA_2_ADDITIVE`

Set:

```json
"protocolVersion": 1,
"schemaVersion": 2,
"futureField": true
```

Keep every required v1 field valid.

Expected:

```text
Compatible
warnings:
- HIGHER_ADDITIVE_SCHEMA_ACCEPTED
- UNKNOWN_TOP_LEVEL_FIELDS_IGNORED
```

### `DISCOVERY_V1_UNKNOWN_CAPABILITY`

Capabilities:

```json
["discovery", "future-capability"]
```

Expected:

```text
Compatible
knownCapabilities: [DISCOVERY]
unknownCapabilities: [future-capability]
warning: UNKNOWN_CAPABILITIES_IGNORED_FOR_EXECUTION
```

### `DISCOVERY_V1_DUPLICATE_CAPABILITY`

Capabilities:

```json
["discovery", "discovery"]
```

Expected:

```text
Compatible
knownCapabilities: [DISCOVERY]
warning: DUPLICATE_CAPABILITIES_NORMALIZED
```

## 12. Negative vectors

| Vector ID | Mutation | Expected reason code |
|---|---|---|
| `DISCOVERY_INVALID_JSON` | truncated object | `INVALID_JSON` |
| `DISCOVERY_ROOT_ARRAY` | root is `[]` | `ROOT_NOT_OBJECT` |
| `DISCOVERY_MISSING_NODE_ID` | omit `nodeId` | `REQUIRED_FIELD_MISSING` |
| `DISCOVERY_NULL_NODE_ID` | `nodeId: null` | `REQUIRED_FIELD_NULL` |
| `DISCOVERY_OK_FALSE_SUCCESS_SHAPE` | `ok: false` without valid error | `OK_NOT_TRUE` or `ERROR_BODY_INVALID` according to root classification |
| `DISCOVERY_SERVICE_MISMATCH` | `service: other` | `SERVICE_MISMATCH` |
| `DISCOVERY_ENDPOINT_MISMATCH` | `endpoint: other` | `ENDPOINT_MISMATCH` |
| `DISCOVERY_PROTOCOL_2` | `protocolVersion: 2` | `PROTOCOL_VERSION_UNSUPPORTED` |
| `DISCOVERY_SCHEMA_0` | `schemaVersion: 0` | `SCHEMA_VERSION_INVALID` |
| `DISCOVERY_NODE_ID_BAD_PREFIX` | `node-...` | `NODE_ID_INVALID` |
| `DISCOVERY_NODE_ID_NOT_V4` | valid non-v4 UUID suffix | `NODE_ID_INVALID` |
| `DISCOVERY_INSTALLATION_ID_BAD` | malformed suffix | `INSTALLATION_ID_INVALID` |
| `DISCOVERY_DISPLAY_NAME_BLANK` | blank string | `DISPLAY_NAME_INVALID` |
| `DISCOVERY_HOST_VERSION_BLANK` | blank string | `HOST_VERSION_INVALID` |
| `DISCOVERY_PORT_ZERO` | `0` | `PORT_OUT_OF_RANGE` |
| `DISCOVERY_PORT_TOO_HIGH` | `65536` | `PORT_OUT_OF_RANGE` |
| `DISCOVERY_CAPABILITIES_NOT_ARRAY` | string value | `FIELD_TYPE_INVALID` |
| `DISCOVERY_CAPABILITY_MISSING` | `[]` | `DISCOVERY_CAPABILITY_MISSING` |
| `DISCOVERY_TRUST_MISSING` | omit `trust` | `REQUIRED_FIELD_MISSING` |
| `DISCOVERY_TRUST_POLICY_UNKNOWN` | `future_policy` | `TRUST_POLICY_UNSUPPORTED` |
| `DISCOVERY_MISSION_AUTH_UNKNOWN` | `future_auth` | `MISSION_AUTHORIZATION_UNSUPPORTED` |

Each vector must assert that no trust, identity mutation, network attempt, or host storage mutation occurs.

## 13. Producer validation vectors

`encodeSuccess` must reject invalid host-supplied inputs using the same domain-specific reason codes before producing JSON.

Required producer tests:

- invalid `nodeId`;
- invalid `installationId`;
- blank display name;
- blank host version;
- port outside range;
- missing `discovery` capability;
- unsupported trust policy;
- unsupported mission authorization;
- protocol other than `1`;
- schema lower than `1`.

## 14. Determinism requirements

For identical valid input and codec version:

- success encoding returns byte-identical UTF-8 JSON;
- error-body encoding returns byte-identical UTF-8 JSON;
- capability output order follows the validated input order after exact duplicate removal;
- no timestamps, random values, environment values, whitespace variation, or platform line endings are added;
- decoding returns stable reason codes and warnings.

JSON member order is not a general protocol semantic, but the first capsule version must preserve the canonical vector order above to minimize SERVER migration risk and enable exact byte evidence.

## 15. Compatibility nonclaims

A `Compatible` result means only:

- the payload satisfies the supported discovery wire contract;
- protocol/schema rules are compatible;
- required advertised fields validate.

It does not mean:

- the network source is authentic;
- the host owns the advertised IDs;
- pairing exists;
- a token is valid;
- missions are authorized;
- a remote route is safe;
- trust should be inherited;
- the endpoint should be saved without host policy.

## 16. Evidence output planned for implementation

The standalone capsule checkpoint must produce:

```text
canonical-success.json
canonical-error-identity-unavailable.json
positive-vectors.jsonl
negative-vectors.jsonl
producer-validation-results.txt
decode-compatibility-results.txt
determinism-results.txt
terminology-review.txt
```

Each result must include vector ID, expected result, actual result, reason code, warnings, and pass/fail state.

## 17. Approval boundary

This document specifies planned API and test vectors. It does not authorize source creation, serializer dependencies, Gradle edits, CLIENT/SERVER changes, builds, workflows, packaging, merge, release, deployment, or installation.