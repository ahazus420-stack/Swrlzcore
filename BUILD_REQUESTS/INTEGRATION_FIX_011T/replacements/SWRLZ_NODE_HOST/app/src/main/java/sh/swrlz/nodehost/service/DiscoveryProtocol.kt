package sh.swrlz.nodehost.service

internal data class DiscoveryHttpRequest(
    val method: String,
    val path: String,
    val query: Map<String, String>,
    val headers: Map<String, String>,
    val contentLength: Long,
    val body: String,
    val bodySha256Hex: String,
    val duplicateHeaders: Set<String>,
    val isLoopback: Boolean,
    val isLocalLan: Boolean,
    val remoteAddress: String?,
)

internal data class DiscoveryHttpResponse(
    val statusCode: Int,
    val reason: String,
    val body: String,
    val headers: Map<String, String> = emptyMap(),
)

/** Pure protocol rules for SWRLZ-DISCOVERY-IDENTITY-001, protocol/schema v1. */
internal object DiscoveryProtocol {
    const val PORT = 8787
    const val PATH = "/discovery/signature"
    const val PROTOCOL_VERSION = 1
    const val SCHEMA_VERSION = 1

    private const val SERVICE = "swrlz-local-node"
    private const val ENDPOINT = "discovery-signature"
    private const val DISPLAY_NAME = "SWRLZ Node Host"

    fun handle(
        request: DiscoveryHttpRequest,
        identity: NodeHostIdentity?,
        hostVersion: String,
    ): DiscoveryHttpResponse {
        if (request.path != PATH) {
            return error(404, "Not Found", "NOT_FOUND", "Discovery route not found")
        }
        if (request.method != "GET") {
            return error(
                statusCode = 405,
                reason = "Method Not Allowed",
                code = "METHOD_NOT_ALLOWED",
                message = "Only GET is allowed for discovery",
                headers = mapOf("Allow" to "GET"),
            )
        }
        if (request.contentLength > 0L) {
            return error(400, "Bad Request", "REQUEST_BODY_NOT_ALLOWED", "Discovery requests have no body")
        }
        val accept = request.headers["accept"]
        if (accept == null || !acceptsJson(accept)) {
            return error(406, "Not Acceptable", "JSON_REQUIRED", "Accept must permit application/json")
        }
        if (identity == null) {
            return error(503, "Service Unavailable", "IDENTITY_UNAVAILABLE", "Durable identity is unavailable")
        }
        if (!NodeIdentityStore.isValidId(identity.nodeId, NodeIdentityStore.NODE_ID_PREFIX) ||
            !NodeIdentityStore.isValidId(identity.installationId, NodeIdentityStore.INSTALLATION_ID_PREFIX)
        ) {
            return error(503, "Service Unavailable", "IDENTITY_UNAVAILABLE", "Durable identity is invalid")
        }
        if (hostVersion.isBlank()) {
            return error(503, "Service Unavailable", "VERSION_UNAVAILABLE", "Host version is unavailable")
        }

        return DiscoveryHttpResponse(
            statusCode = 200,
            reason = "OK",
            body = successJson(identity, hostVersion),
            headers = mapOf("Cache-Control" to "no-store"),
        )
    }

    private fun successJson(identity: NodeHostIdentity, hostVersion: String): String = buildString {
        append('{')
        append("\"ok\":true,")
        append("\"service\":").append(jsonString(SERVICE)).append(',')
        append("\"endpoint\":").append(jsonString(ENDPOINT)).append(',')
        append("\"protocolVersion\":").append(PROTOCOL_VERSION).append(',')
        append("\"schemaVersion\":").append(SCHEMA_VERSION).append(',')
        append("\"nodeId\":").append(jsonString(identity.nodeId)).append(',')
        append("\"installationId\":").append(jsonString(identity.installationId)).append(',')
        append("\"displayName\":").append(jsonString(DISPLAY_NAME)).append(',')
        append("\"hostVersion\":").append(jsonString(hostVersion)).append(',')
        append("\"port\":").append(PORT).append(',')
        append("\"capabilities\":[")
        append("\"discovery.v1\",")
        append("\"presence.read.v1\",")
        append("\"presence.group.create.v1\",")
        append("\"presence.group.join.v1\",")
        append("\"presence.device.register.v1\",")
        append("\"presence.device.checkin.v1\",")
        append("\"presence.device.resolve.v1\",")
        append("\"presence.device.proof.v1\",")
        append("\"presence.write.v1\"],")
        append("\"presenceContractVersion\":1,")
        append("\"presenceWriteTransport\":\"local-link-paired\",")
        append("\"presenceWriteExposure\":\"loopback-only\",")
        append("\"presenceResolveTransport\":\"local-link-paired\",")
        append("\"presenceResolveExposure\":\"paired-lan\",")
        append("\"presenceDeviceProofBinding\":\"required\",")
        append("\"trust\":{")
        append("\"policy\":\"pairing_required\",")
        append("\"missionAuthorization\":\"trusted_only\"")
        append("}}")
    }

    private fun error(
        statusCode: Int,
        reason: String,
        code: String,
        message: String,
        headers: Map<String, String> = emptyMap(),
    ): DiscoveryHttpResponse = DiscoveryHttpResponse(
        statusCode = statusCode,
        reason = reason,
        body = buildString {
            append('{')
            append("\"ok\":false,")
            append("\"error\":{")
            append("\"code\":").append(jsonString(code)).append(',')
            append("\"message\":").append(jsonString(message))
            append("},")
            append("\"protocolVersion\":").append(PROTOCOL_VERSION).append(',')
            append("\"schemaVersion\":").append(SCHEMA_VERSION)
            append('}')
        },
        headers = headers + ("Cache-Control" to "no-store"),
    )

    private fun acceptsJson(value: String): Boolean = value.split(',').any { entry ->
        val parts = entry.split(';')
        val mediaType = parts.first().trim().lowercase()
        val quality = parts.drop(1)
            .map(String::trim)
            .firstOrNull { it.startsWith("q=", ignoreCase = true) }
            ?.substringAfter('=')
            ?.toDoubleOrNull()
            ?: 1.0
        quality > 0.0 && (
            mediaType == "application/json" ||
                mediaType == "application/*" ||
                mediaType == "*/*" ||
                mediaType.endsWith("+json")
            )
    }

    private fun jsonString(value: String): String = buildString {
        append('"')
        value.forEach { character ->
            when (character) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (character.code < 0x20) {
                    append("\\u").append(character.code.toString(16).padStart(4, '0'))
                } else {
                    append(character)
                }
            }
        }
        append('"')
    }
}
