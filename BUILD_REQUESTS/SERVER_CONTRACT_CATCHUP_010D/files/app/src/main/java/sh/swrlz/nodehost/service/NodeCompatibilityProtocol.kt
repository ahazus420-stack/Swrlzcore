package sh.swrlz.nodehost.service

/**
 * Read-only compatibility surface used by the SWRLZ CLIENT network dashboard.
 *
 * These routes report only state that the Android NODE_HOST can prove locally.
 * Presence collections remain empty until a separately accepted persistent
 * presence/group/device registry is implemented. Empty is represented as a
 * successful response; no demo rows or synthetic devices are exposed.
 */
internal data class NodeCompatibilitySnapshot(
    val phase: String,
    val identityReady: Boolean,
    val discoveryHealthy: Boolean,
    val privateHealthy: Boolean,
    val nodeId: String?,
    val installationId: String?,
    val hostVersion: String,
    val lanUrls: List<String> = emptyList(),
)

internal object NodeCompatibilityProtocol {
    const val STATUS_PATH = "/status"
    const val PRESENCE_SUMMARY_PATH = "/presence/summary"
    const val PRESENCE_GROUPS_PATH = "/presence/groups"
    const val PRESENCE_DEVICES_PATH = "/presence/devices"

    private const val PROTOCOL_VERSION = 1
    private const val SCHEMA_VERSION = 1
    private const val DISPLAY_NAME = "SWRLZ Node Host"
    private const val NODE_TYPE = "android-node-host"

    private val paths = setOf(
        STATUS_PATH,
        PRESENCE_SUMMARY_PATH,
        PRESENCE_GROUPS_PATH,
        PRESENCE_DEVICES_PATH,
    )

    fun handles(path: String): Boolean = path in paths

    fun handle(
        request: DiscoveryHttpRequest,
        snapshot: NodeCompatibilitySnapshot,
    ): DiscoveryHttpResponse {
        if (!handles(request.path)) {
            return error(404, "Not Found", "NOT_FOUND", "Compatibility route not found")
        }
        if (request.method != "GET") {
            return error(
                405,
                "Method Not Allowed",
                "METHOD_NOT_ALLOWED",
                "Only GET is allowed for compatibility routes",
                mapOf("Allow" to "GET"),
            )
        }
        if (request.contentLength > 0L) {
            return error(400, "Bad Request", "REQUEST_BODY_NOT_ALLOWED", "Compatibility GET requests have no body")
        }
        val accept = request.headers["accept"]
        if (accept != null && !acceptsJson(accept)) {
            return error(406, "Not Acceptable", "JSON_REQUIRED", "Accept must permit application/json")
        }
        if (!snapshot.identityReady || snapshot.nodeId.isNullOrBlank() || snapshot.installationId.isNullOrBlank()) {
            return error(503, "Service Unavailable", "IDENTITY_UNAVAILABLE", "Durable NODE_HOST identity is unavailable")
        }
        if (!NodeIdentityStore.isValidId(snapshot.nodeId, NodeIdentityStore.NODE_ID_PREFIX) ||
            !NodeIdentityStore.isValidId(snapshot.installationId, NodeIdentityStore.INSTALLATION_ID_PREFIX)
        ) {
            return error(503, "Service Unavailable", "IDENTITY_UNAVAILABLE", "Durable NODE_HOST identity is invalid")
        }
        if (snapshot.hostVersion.isBlank()) {
            return error(503, "Service Unavailable", "VERSION_UNAVAILABLE", "NODE_HOST version is unavailable")
        }

        val body = when (request.path) {
            STATUS_PATH -> statusJson(snapshot)
            PRESENCE_SUMMARY_PATH -> presenceSummaryJson(snapshot)
            PRESENCE_GROUPS_PATH -> presenceGroupsJson(snapshot)
            PRESENCE_DEVICES_PATH -> presenceDevicesJson(snapshot)
            else -> return error(404, "Not Found", "NOT_FOUND", "Compatibility route not found")
        }
        return DiscoveryHttpResponse(
            statusCode = 200,
            reason = "OK",
            body = body,
            headers = mapOf("Cache-Control" to "no-store"),
        )
    }

    private fun statusJson(snapshot: NodeCompatibilitySnapshot): String = buildString {
        append('{')
        append("\"ok\":true,")
        append("\"node\":").append(jsonString(DISPLAY_NAME)).append(',')
        append("\"node_name\":").append(jsonString(DISPLAY_NAME)).append(',')
        append("\"node_type\":").append(jsonString(NODE_TYPE)).append(',')
        append("\"server_version\":").append(jsonString(snapshot.hostVersion)).append(',')
        append("\"version\":").append(jsonString(snapshot.hostVersion)).append(',')
        append("\"protocolVersion\":").append(PROTOCOL_VERSION).append(',')
        append("\"schemaVersion\":").append(SCHEMA_VERSION).append(',')
        append("\"identity\":{")
        append("\"nodeId\":").append(jsonString(snapshot.nodeId.orEmpty())).append(',')
        append("\"installationId\":").append(jsonString(snapshot.installationId.orEmpty()))
        append("},")
        append("\"runtime\":{")
        append("\"phase\":").append(jsonString(snapshot.phase)).append(',')
        append("\"identityReady\":").append(snapshot.identityReady).append(',')
        append("\"discoveryHealthy\":").append(snapshot.discoveryHealthy).append(',')
        append("\"privateHealthy\":").append(snapshot.privateHealthy)
        append("},")
        append("\"connection\":{")
        append("\"local_url\":\"http://127.0.0.1:8787\",")
        append("\"lan_urls\":[")
        snapshot.lanUrls.distinct().forEachIndexed { index, url ->
            if (index > 0) append(',')
            append(jsonString(url))
        }
        append("]},")
        append("\"paid_ai_required\":false,")
        append("\"message\":\"Local NODE_HOST compatibility surface is online\"")
        append('}')
    }

    private fun presenceSummaryJson(snapshot: NodeCompatibilitySnapshot): String = buildString {
        append(commonPrefix(snapshot))
        append("\"state\":\"empty\",")
        append("\"group_count\":0,")
        append("\"device_count\":0,")
        append("\"online_count\":0,")
        append("\"groups\":[],")
        append("\"devices\":[],")
        append("\"message\":\"No presence group or device records exist in this NODE_HOST build\"")
        append('}')
    }

    private fun presenceGroupsJson(snapshot: NodeCompatibilitySnapshot): String = buildString {
        append(commonPrefix(snapshot))
        append("\"state\":\"empty\",")
        append("\"count\":0,")
        append("\"groups\":[],")
        append("\"message\":\"No presence groups are registered\"")
        append('}')
    }

    private fun presenceDevicesJson(snapshot: NodeCompatibilitySnapshot): String = buildString {
        append(commonPrefix(snapshot))
        append("\"state\":\"empty\",")
        append("\"count\":0,")
        append("\"online_count\":0,")
        append("\"devices\":[],")
        append("\"message\":\"No presence devices are registered\"")
        append('}')
    }

    private fun commonPrefix(snapshot: NodeCompatibilitySnapshot): String = buildString {
        append('{')
        append("\"ok\":true,")
        append("\"protocolVersion\":").append(PROTOCOL_VERSION).append(',')
        append("\"schemaVersion\":").append(SCHEMA_VERSION).append(',')
        append("\"nodeId\":").append(jsonString(snapshot.nodeId.orEmpty())).append(',')
        append("\"authoritative\":true,")
        append("\"data_source\":\"android-node-host-local\",")
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
