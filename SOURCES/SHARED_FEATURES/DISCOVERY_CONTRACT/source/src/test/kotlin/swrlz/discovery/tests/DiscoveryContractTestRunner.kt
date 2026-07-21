package swrlz.discovery.tests

import swrlz.discovery.api.*
import swrlz.discovery.internal.DiscoveryContractCodecImpl
import swrlz.discovery.internal.StrictPortableJsonBackend

object DiscoveryContractTestRunner {
    private const val NODE_ID = "swrlz-node-550e8400-e29b-41d4-a716-446655440000"
    private const val INSTALL_ID = "swrlz-install-e29b41d4-a716-4466-8550-e8400e2941d4"
    private const val CANONICAL = "{\"ok\":true,\"service\":\"swrlz-local-node\",\"endpoint\":\"discovery-signature\",\"protocolVersion\":1,\"schemaVersion\":1,\"nodeId\":\"$NODE_ID\",\"installationId\":\"$INSTALL_ID\",\"displayName\":\"SWRLZ Node Host\",\"hostVersion\":\"1.0.0\",\"port\":8787,\"capabilities\":[\"discovery\"],\"trust\":{\"policy\":\"pairing_required\",\"missionAuthorization\":\"trusted_only\"}}"
    private const val CANONICAL_ERROR = "{\"ok\":false,\"error\":{\"code\":\"IDENTITY_UNAVAILABLE\",\"message\":\"Durable identity is unavailable\"},\"protocolVersion\":1,\"schemaVersion\":1}"

    private val codec = DiscoveryContractCodecImpl(StrictPortableJsonBackend())
    private var passed = 0
    private var failed = 0

    @JvmStatic
    fun main(args: Array<String>) {
        test("DISCOVERY_V1_SUCCESS_CANONICAL encode bytes") {
            val encoded = codec.encodeSuccess(validInput()) as DiscoveryEncodeResult.Encoded
            check(encoded.json == CANONICAL) { encoded.json }
        }
        test("DISCOVERY_V1_SUCCESS_CANONICAL decode") {
            val result = codec.decode(CANONICAL) as DiscoveryDecodeResult.Compatible
            check(result.warnings.isEmpty())
            check(result.node.nodeId == NODE_ID && result.node.installationId == INSTALL_ID)
        }
        test("DISCOVERY_V1_ERROR_IDENTITY_UNAVAILABLE encode bytes") {
            val result = codec.encodeErrorBody(DiscoveryErrorInput("IDENTITY_UNAVAILABLE", "Durable identity is unavailable")) as DiscoveryEncodeResult.Encoded
            check(result.json == CANONICAL_ERROR)
        }
        test("DISCOVERY_V1_ERROR_IDENTITY_UNAVAILABLE decode") {
            val result = codec.decode(CANONICAL_ERROR) as DiscoveryDecodeResult.RemoteError
            check(result.error.code == "IDENTITY_UNAVAILABLE")
            check(result.error.protocolVersion == 1 && result.error.schemaVersion == 1)
        }

        compatibleWarning("DISCOVERY_V1_UNKNOWN_TOP_LEVEL_FIELD", CANONICAL.dropLast(1) + ",\"futureField\":1}", setOf(DiscoveryWarningCode.UNKNOWN_TOP_LEVEL_FIELDS_IGNORED))
        compatibleWarning("DISCOVERY_V1_UNKNOWN_NESTED_TRUST_FIELD", CANONICAL.replace("\"missionAuthorization\":\"trusted_only\"", "\"missionAuthorization\":\"trusted_only\",\"futureTrustField\":\"value\""), setOf(DiscoveryWarningCode.UNKNOWN_NESTED_FIELDS_IGNORED))
        compatibleWarning("DISCOVERY_V1_SCHEMA_2_ADDITIVE", CANONICAL.replace("\"schemaVersion\":1", "\"schemaVersion\":2").dropLast(1) + ",\"futureField\":true}", setOf(DiscoveryWarningCode.HIGHER_ADDITIVE_SCHEMA_ACCEPTED, DiscoveryWarningCode.UNKNOWN_TOP_LEVEL_FIELDS_IGNORED))
        compatibleWarning("DISCOVERY_V1_UNKNOWN_CAPABILITY", CANONICAL.replace("[\"discovery\"]", "[\"discovery\",\"future-capability\"]"), setOf(DiscoveryWarningCode.UNKNOWN_CAPABILITIES_IGNORED_FOR_EXECUTION))
        compatibleWarning("DISCOVERY_V1_DUPLICATE_CAPABILITY", CANONICAL.replace("[\"discovery\"]", "[\"discovery\",\"discovery\"]"), setOf(DiscoveryWarningCode.DUPLICATE_CAPABILITIES_NORMALIZED))

        incompatible("DISCOVERY_INVALID_JSON", CANONICAL.dropLast(1), DiscoveryReasonCode.INVALID_JSON)
        incompatible("DISCOVERY_ROOT_ARRAY", "[]", DiscoveryReasonCode.ROOT_NOT_OBJECT)
        incompatible("DISCOVERY_MISSING_NODE_ID", removeField(CANONICAL, "nodeId"), DiscoveryReasonCode.REQUIRED_FIELD_MISSING)
        incompatible("DISCOVERY_NULL_NODE_ID", replaceStringField(CANONICAL, "nodeId", "null", raw = true), DiscoveryReasonCode.REQUIRED_FIELD_NULL)
        incompatible("DISCOVERY_OK_FALSE_SUCCESS_SHAPE", CANONICAL.replaceFirst("\"ok\":true", "\"ok\":false"), DiscoveryReasonCode.ERROR_BODY_INVALID)
        incompatible("DISCOVERY_SERVICE_MISMATCH", CANONICAL.replace("\"service\":\"swrlz-local-node\"", "\"service\":\"other\""), DiscoveryReasonCode.SERVICE_MISMATCH)
        incompatible("DISCOVERY_ENDPOINT_MISMATCH", CANONICAL.replace("\"endpoint\":\"discovery-signature\"", "\"endpoint\":\"other\""), DiscoveryReasonCode.ENDPOINT_MISMATCH)
        incompatible("DISCOVERY_PROTOCOL_2", CANONICAL.replace("\"protocolVersion\":1", "\"protocolVersion\":2"), DiscoveryReasonCode.PROTOCOL_VERSION_UNSUPPORTED)
        incompatible("DISCOVERY_SCHEMA_0", CANONICAL.replace("\"schemaVersion\":1", "\"schemaVersion\":0"), DiscoveryReasonCode.SCHEMA_VERSION_INVALID)
        incompatible("DISCOVERY_NODE_ID_BAD_PREFIX", CANONICAL.replace(NODE_ID, "node-550e8400-e29b-41d4-a716-446655440000"), DiscoveryReasonCode.NODE_ID_INVALID)
        incompatible("DISCOVERY_NODE_ID_NOT_V4", CANONICAL.replace(NODE_ID, "swrlz-node-550e8400-e29b-11d4-a716-446655440000"), DiscoveryReasonCode.NODE_ID_INVALID)
        incompatible("DISCOVERY_INSTALLATION_ID_BAD", CANONICAL.replace(INSTALL_ID, "swrlz-install-not-a-uuid"), DiscoveryReasonCode.INSTALLATION_ID_INVALID)
        incompatible("DISCOVERY_DISPLAY_NAME_BLANK", CANONICAL.replace("\"displayName\":\"SWRLZ Node Host\"", "\"displayName\":\"   \""), DiscoveryReasonCode.DISPLAY_NAME_INVALID)
        incompatible("DISCOVERY_HOST_VERSION_BLANK", CANONICAL.replace("\"hostVersion\":\"1.0.0\"", "\"hostVersion\":\"\""), DiscoveryReasonCode.HOST_VERSION_INVALID)
        incompatible("DISCOVERY_PORT_ZERO", CANONICAL.replace("\"port\":8787", "\"port\":0"), DiscoveryReasonCode.PORT_OUT_OF_RANGE)
        incompatible("DISCOVERY_PORT_TOO_HIGH", CANONICAL.replace("\"port\":8787", "\"port\":65536"), DiscoveryReasonCode.PORT_OUT_OF_RANGE)
        incompatible("DISCOVERY_CAPABILITIES_NOT_ARRAY", CANONICAL.replace("\"capabilities\":[\"discovery\"]", "\"capabilities\":\"discovery\""), DiscoveryReasonCode.FIELD_TYPE_INVALID)
        incompatible("DISCOVERY_CAPABILITY_MISSING", CANONICAL.replace("[\"discovery\"]", "[]"), DiscoveryReasonCode.DISCOVERY_CAPABILITY_MISSING)
        incompatible("DISCOVERY_TRUST_MISSING", removeField(CANONICAL, "trust"), DiscoveryReasonCode.REQUIRED_FIELD_MISSING)
        incompatible("DISCOVERY_TRUST_POLICY_UNKNOWN", CANONICAL.replace("\"policy\":\"pairing_required\"", "\"policy\":\"future_policy\""), DiscoveryReasonCode.TRUST_POLICY_UNSUPPORTED)
        incompatible("DISCOVERY_MISSION_AUTH_UNKNOWN", CANONICAL.replace("\"missionAuthorization\":\"trusted_only\"", "\"missionAuthorization\":\"future_auth\""), DiscoveryReasonCode.MISSION_AUTHORIZATION_UNSUPPORTED)

        producerRejected("PRODUCER_NODE_ID", validInput().copy(nodeId = "bad"), DiscoveryReasonCode.NODE_ID_INVALID)
        producerRejected("PRODUCER_INSTALL_ID", validInput().copy(installationId = "bad"), DiscoveryReasonCode.INSTALLATION_ID_INVALID)
        producerRejected("PRODUCER_DISPLAY_NAME", validInput().copy(displayName = ""), DiscoveryReasonCode.DISPLAY_NAME_INVALID)
        producerRejected("PRODUCER_HOST_VERSION", validInput().copy(hostVersion = ""), DiscoveryReasonCode.HOST_VERSION_INVALID)
        producerRejected("PRODUCER_PORT", validInput().copy(port = 0), DiscoveryReasonCode.PORT_OUT_OF_RANGE)
        producerRejected("PRODUCER_CAPABILITY", validInput().copy(capabilities = emptyList()), DiscoveryReasonCode.DISCOVERY_CAPABILITY_MISSING)
        producerRejected("PRODUCER_TRUST", validInput().copy(trustPolicy = "future"), DiscoveryReasonCode.TRUST_POLICY_UNSUPPORTED)
        producerRejected("PRODUCER_MISSION_AUTH", validInput().copy(missionAuthorization = "future"), DiscoveryReasonCode.MISSION_AUTHORIZATION_UNSUPPORTED)
        producerRejected("PRODUCER_PROTOCOL", validInput().copy(protocolVersion = 2), DiscoveryReasonCode.PROTOCOL_VERSION_UNSUPPORTED)
        producerRejected("PRODUCER_SCHEMA", validInput().copy(schemaVersion = 0), DiscoveryReasonCode.SCHEMA_VERSION_INVALID)

        test("DETERMINISM_SUCCESS_100") {
            val outputs = (1..100).map { (codec.encodeSuccess(validInput()) as DiscoveryEncodeResult.Encoded).json }.toSet()
            check(outputs == setOf(CANONICAL))
        }
        test("DETERMINISM_ERROR_100") {
            val outputs = (1..100).map { (codec.encodeErrorBody(DiscoveryErrorInput("IDENTITY_UNAVAILABLE", "Durable identity is unavailable")) as DiscoveryEncodeResult.Encoded).json }.toSet()
            check(outputs == setOf(CANONICAL_ERROR))
        }
        test("NO_HOST_SIDE_EFFECT_INPUTS") {
            val methods = DiscoveryContractCodec::class.java.methods.map { it.name }.toSet()
            check(methods.containsAll(setOf("encodeSuccess", "encodeErrorBody", "decode")))
            check(methods.none { it.contains("connect", true) || it.contains("persist", true) || it.contains("trustGrant", true) })
        }

        println("RESULT passed=$passed failed=$failed total=${passed + failed}")
        if (failed > 0) error("$failed tests failed")
    }

    private fun validInput() = DiscoverySuccessInput(
        nodeId = NODE_ID,
        installationId = INSTALL_ID,
        displayName = "SWRLZ Node Host",
        hostVersion = "1.0.0",
        port = 8787,
        capabilities = listOf("discovery"),
    )

    private fun compatibleWarning(name: String, json: String, expected: Set<DiscoveryWarningCode>) = test(name) {
        val result = codec.decode(json) as DiscoveryDecodeResult.Compatible
        check(result.warnings == expected) { "${result.warnings} != $expected" }
    }

    private fun incompatible(name: String, json: String, expected: DiscoveryReasonCode) = test(name) {
        val result = codec.decode(json) as DiscoveryDecodeResult.Incompatible
        check(result.code == expected) { "${result.code} != $expected (${result.detail})" }
    }

    private fun producerRejected(name: String, input: DiscoverySuccessInput, expected: DiscoveryReasonCode) = test(name) {
        val result = codec.encodeSuccess(input) as DiscoveryEncodeResult.Rejected
        check(result.code == expected) { "${result.code} != $expected" }
    }

    private fun replaceStringField(json: String, field: String, replacement: String, raw: Boolean = false): String {
        val regex = Regex("\\\"$field\\\":\\\"[^\\\"]*\\\"")
        val value = if (raw) "\"$field\":$replacement" else "\"$field\":\"$replacement\""
        return json.replace(regex, value)
    }

    private fun removeField(json: String, field: String): String {
        if (field == "trust") {
            return json.replace(",\"trust\":{\"policy\":\"pairing_required\",\"missionAuthorization\":\"trusted_only\"}", "")
        }
        val pattern = Regex(",?\\\"$field\\\":\\\"[^\\\"]*\\\",?")
        return json.replace(pattern) { match -> if (match.value.startsWith(",") && match.value.endsWith(",")) "," else "" }
    }

    private fun test(name: String, block: () -> Unit) {
        try {
            block()
            passed++
            println("PASS $name")
        } catch (failure: Throwable) {
            failed++
            println("FAIL $name :: ${failure.message}")
        }
    }
}
