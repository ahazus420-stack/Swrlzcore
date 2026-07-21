package swrlz.discovery.api

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

enum class KnownDiscoveryCapability { DISCOVERY }
enum class DiscoveryTrustPolicy { PAIRING_REQUIRED }
enum class DiscoveryMissionAuthorization { TRUSTED_ONLY }

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

sealed interface DiscoveryEncodeResult {
    data class Encoded(val json: String) : DiscoveryEncodeResult
    data class Rejected(val code: DiscoveryReasonCode, val detail: String) : DiscoveryEncodeResult
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

    data class RemoteError(val error: DiscoveryRemoteError) : DiscoveryDecodeResult
}

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

enum class DiscoveryWarningCode {
    HIGHER_ADDITIVE_SCHEMA_ACCEPTED,
    UNKNOWN_TOP_LEVEL_FIELDS_IGNORED,
    UNKNOWN_NESTED_FIELDS_IGNORED,
    UNKNOWN_CAPABILITIES_IGNORED_FOR_EXECUTION,
    DUPLICATE_CAPABILITIES_NORMALIZED,
}

interface DiscoveryContractCodec {
    fun encodeSuccess(input: DiscoverySuccessInput): DiscoveryEncodeResult
    fun encodeErrorBody(input: DiscoveryErrorInput): DiscoveryEncodeResult
    fun decode(json: String): DiscoveryDecodeResult
}
