package swrlz.discovery.internal
import java.util.UUID
import swrlz.discovery.api.*
import swrlz.discovery.api.DiscoveryContractConstants as C
import swrlz.discovery.api.DiscoveryReasonCode as R
import swrlz.discovery.api.DiscoveryWarningCode as W
import swrlz.discovery.api.DiscoveryEncodeResult as E
import swrlz.discovery.api.DiscoveryDecodeResult as D
internal class DiscoveryContractCodecImpl(
private val backend: DiscoveryJsonBackend
) : DiscoveryContractCodec {
override fun encodeSuccess(input: DiscoverySuccessInput): E = safeEncode {
producerValidation(input)?.let { return@safeEncode it }
val normalizedCapabilities = input.capabilities.distinct()
val document = JsonValue.ObjectValue(linkedMapOf(
"ok" to JsonValue.BooleanValue(true),
"service" to JsonValue.StringValue(C.SERVICE),
"endpoint" to JsonValue.StringValue(C.ENDPOINT),
"protocolVersion" to JsonValue.NumberValue(input.protocolVersion.toString()),
"schemaVersion" to JsonValue.NumberValue(input.schemaVersion.toString()),
"nodeId" to JsonValue.StringValue(input.nodeId),
"installationId" to JsonValue.StringValue(input.installationId),
"displayName" to JsonValue.StringValue(input.displayName),
"hostVersion" to JsonValue.StringValue(input.hostVersion),
"port" to JsonValue.NumberValue(input.port.toString()),
"capabilities" to JsonValue.ArrayValue(normalizedCapabilities.map(JsonValue::StringValue)),
"trust" to JsonValue.ObjectValue(linkedMapOf(
"policy" to JsonValue.StringValue(input.trustPolicy),
"missionAuthorization" to JsonValue.StringValue(input.missionAuthorization)
))
))
E.Encoded(backend.encodeCanonical(document))
}
override fun encodeErrorBody(input: DiscoveryErrorInput): E = safeEncode {
if (!validErrorToken(input.code) || !validHumanText(input.message, 256)) {
return@safeEncode reject(R.ERROR_BODY_INVALID, "error code or message is invalid")
}
if (input.protocolVersion != C.PROTOCOL_VERSION) {
return@safeEncode reject(R.PROTOCOL_VERSION_UNSUPPORTED, "unsupported protocol version ${input.protocolVersion}")
}
if (input.schemaVersion < C.BASE_SCHEMA_VERSION) {
return@safeEncode reject(R.SCHEMA_VERSION_INVALID, "schema version must be at least 1")
}
val document = JsonValue.ObjectValue(linkedMapOf(
"ok" to JsonValue.BooleanValue(false),
"error" to JsonValue.ObjectValue(linkedMapOf(
"code" to JsonValue.StringValue(input.code),
"message" to JsonValue.StringValue(input.message)
)),
"protocolVersion" to JsonValue.NumberValue(input.protocolVersion.toString()),
"schemaVersion" to JsonValue.NumberValue(input.schemaVersion.toString())
))
E.Encoded(backend.encodeCanonical(document))
}
override fun decode(json: String): D = try {
when (val parsed = backend.parse(json)) {
is JsonParseResult.Failed -> incompat(R.INVALID_JSON, parsed.detail)
is JsonParseResult.Parsed -> decodeRoot(parsed.value)
}
} catch (failure: Exception) {
incompat(R.INTERNAL_CODEC_FAILURE, failure.message ?: failure::class.simpleName ?: "codec failure")
}
private fun decodeRoot(value: JsonValue): D {
val root = value as? JsonValue.ObjectValue
?: return incompat(R.ROOT_NOT_OBJECT, "root JSON value must be an object")
val okResult = requiredBoolean(root, "ok")
if (okResult.error != null) return okResult.error
return if (okResult.value == true) decodeSuccess(root) else decodeRemoteError(root)
}
private fun decodeRemoteError(root: JsonValue.ObjectValue): D {
val errorValue = root.fields["error"] ?: return incompat(R.ERROR_BODY_INVALID, "error object is missing")
if (errorValue === JsonValue.NullValue) return incompat(R.ERROR_BODY_INVALID, "error object is null")
val error = errorValue as? JsonValue.ObjectValue
?: return incompat(R.ERROR_BODY_INVALID, "error must be an object")
val code = optionalString(error, "code") ?: return incompat(R.ERROR_BODY_INVALID, "error code is missing or invalid")
val message = optionalString(error, "message") ?: return incompat(R.ERROR_BODY_INVALID, "error message is missing or invalid")
if (!validErrorToken(code) || !validHumanText(message, 256)) {
return incompat(R.ERROR_BODY_INVALID, "error code or message is invalid")
}
val protocol = optionalInt(root, "protocolVersion")
val schema = optionalInt(root, "schemaVersion")
return D.RemoteError(DiscoveryRemoteError(code, message, protocol, schema))
}
private fun decodeSuccess(root: JsonValue.ObjectValue): D {
val protocolResult = requiredInt(root, "protocolVersion")
if (protocolResult.error != null) return protocolResult.error
val schemaResult = requiredInt(root, "schemaVersion")
if (schemaResult.error != null) return schemaResult.error
val protocol = protocolResult.value!!
val schema = schemaResult.value!!
if (protocol != 1) return incompat(R.PROTOCOL_VERSION_UNSUPPORTED, "unsupported protocol version $protocol", protocol, schema)
if (schema < 1) return incompat(R.SCHEMA_VERSION_INVALID, "schema version must be at least 1", protocol, schema)
val service = requiredString(root, "service"); if (service.error != null) return service.error
if (service.value != C.SERVICE) return incompat(R.SERVICE_MISMATCH, "service sentinel mismatch", protocol, schema)
val endpoint = requiredString(root, "endpoint"); if (endpoint.error != null) return endpoint.error
if (endpoint.value != C.ENDPOINT) return incompat(R.ENDPOINT_MISMATCH, "endpoint sentinel mismatch", protocol, schema)
val nodeId = requiredString(root, "nodeId"); if (nodeId.error != null) return nodeId.error
if (!validPrefixedUuidV4(nodeId.value!!, C.NODE_ID_PREFIX)) return incompat(R.NODE_ID_INVALID, "nodeId is not canonical prefixed UUIDv4", protocol, schema)
val installationId = requiredString(root, "installationId"); if (installationId.error != null) return installationId.error
if (!validPrefixedUuidV4(installationId.value!!, C.INSTALLATION_ID_PREFIX)) return incompat(R.INSTALLATION_ID_INVALID, "installationId is not canonical prefixed UUIDv4", protocol, schema)
val displayName = requiredString(root, "displayName"); if (displayName.error != null) return displayName.error
if (!validHumanText(displayName.value!!, 64)) return incompat(R.DISPLAY_NAME_INVALID, "displayName is invalid", protocol, schema)
val hostVersion = requiredString(root, "hostVersion"); if (hostVersion.error != null) return hostVersion.error
if (!validHumanText(hostVersion.value!!, 64)) return incompat(R.HOST_VERSION_INVALID, "hostVersion is invalid", protocol, schema)
val port = requiredInt(root, "port"); if (port.error != null) return port.error
if (port.value !in 1..65535) return incompat(R.PORT_OUT_OF_RANGE, "port must be in 1..65535", protocol, schema)
val capabilitiesResult = requiredStringArray(root, "capabilities")
if (capabilitiesResult.error != null) return capabilitiesResult.error
val capabilities = capabilitiesResult.value!!
if (capabilities.any { it.isBlank() || hasControl(it) }) return incompat(R.CAPABILITIES_INVALID, "capabilities contain invalid strings", protocol, schema)
if (C.REQUIRED_CAPABILITY_DISCOVERY !in capabilities) return incompat(R.DISCOVERY_CAPABILITY_MISSING, "discovery capability is required", protocol, schema)
val trustValue = root.fields["trust"] ?: return incompat(R.REQUIRED_FIELD_MISSING, "required field trust is missing", protocol, schema)
if (trustValue === JsonValue.NullValue) return incompat(R.REQUIRED_FIELD_NULL, "required field trust is null", protocol, schema)
val trust = trustValue as? JsonValue.ObjectValue ?: return incompat(R.TRUST_OBJECT_INVALID, "trust must be an object", protocol, schema)
val policy = requiredString(trust, "policy"); if (policy.error != null) return policy.error
if (policy.value != C.TRUST_POLICY_PAIRING_REQUIRED) return incompat(R.TRUST_POLICY_UNSUPPORTED, "unsupported trust policy", protocol, schema)
val missionAuthorization = requiredString(trust, "missionAuthorization"); if (missionAuthorization.error != null) return missionAuthorization.error
if (missionAuthorization.value != C.MISSION_AUTHORIZATION_TRUSTED_ONLY) return incompat(R.MISSION_AUTHORIZATION_UNSUPPORTED, "unsupported mission authorization", protocol, schema)
val warnings = linkedSetOf<W>()
if (schema > 1) warnings += W.HIGHER_ADDITIVE_SCHEMA_ACCEPTED
val knownTop = setOf("ok", "service", "endpoint", "protocolVersion", "schemaVersion", "nodeId", "installationId", "displayName", "hostVersion", "port", "capabilities", "trust")
if (root.fields.keys.any { it !in knownTop }) warnings += W.UNKNOWN_TOP_LEVEL_FIELDS_IGNORED
if (trust.fields.keys.any { it !in setOf("policy", "missionAuthorization") }) warnings += W.UNKNOWN_NESTED_FIELDS_IGNORED
if (capabilities.distinct().size != capabilities.size) warnings += W.DUPLICATE_CAPABILITIES_NORMALIZED
val unknownCapabilities = capabilities.filter { it != C.REQUIRED_CAPABILITY_DISCOVERY }.toCollection(linkedSetOf())
if (unknownCapabilities.isNotEmpty()) warnings += W.UNKNOWN_CAPABILITIES_IGNORED_FOR_EXECUTION
return D.Compatible(
node = ValidatedDiscoveryNode(
nodeId = nodeId.value,
installationId = installationId.value,
displayName = displayName.value,
hostVersion = hostVersion.value,
port = port.value!!,
protocolVersion = protocol,
schemaVersion = schema,
knownCapabilities = setOf(KnownDiscoveryCapability.DISCOVERY),
unknownCapabilities = unknownCapabilities,
trustPolicy = DiscoveryTrustPolicy.PAIRING_REQUIRED,
missionAuthorization = DiscoveryMissionAuthorization.TRUSTED_ONLY
),
warnings = warnings
)
}
private fun producerValidation(input: DiscoverySuccessInput): E.Rejected? {
if (input.protocolVersion != 1) return reject(R.PROTOCOL_VERSION_UNSUPPORTED, "unsupported protocol version ${input.protocolVersion}")
if (input.schemaVersion < 1) return reject(R.SCHEMA_VERSION_INVALID, "schema version must be at least 1")
if (!validPrefixedUuidV4(input.nodeId, C.NODE_ID_PREFIX)) return reject(R.NODE_ID_INVALID, "nodeId is invalid")
if (!validPrefixedUuidV4(input.installationId, C.INSTALLATION_ID_PREFIX)) return reject(R.INSTALLATION_ID_INVALID, "installationId is invalid")
if (!validHumanText(input.displayName, 64)) return reject(R.DISPLAY_NAME_INVALID, "displayName is invalid")
if (!validHumanText(input.hostVersion, 64)) return reject(R.HOST_VERSION_INVALID, "hostVersion is invalid")
if (input.port !in 1..65535) return reject(R.PORT_OUT_OF_RANGE, "port must be in 1..65535")
if (input.capabilities.any { it.isBlank() || hasControl(it) }) return reject(R.CAPABILITIES_INVALID, "capabilities contain invalid strings")
if (C.REQUIRED_CAPABILITY_DISCOVERY !in input.capabilities) return reject(R.DISCOVERY_CAPABILITY_MISSING, "discovery capability is required")
if (input.trustPolicy != C.TRUST_POLICY_PAIRING_REQUIRED) return reject(R.TRUST_POLICY_UNSUPPORTED, "unsupported trust policy")
if (input.missionAuthorization != C.MISSION_AUTHORIZATION_TRUSTED_ONLY) return reject(R.MISSION_AUTHORIZATION_UNSUPPORTED, "unsupported mission authorization")
return null
}
private inline fun safeEncode(block: () -> E): E = try { block() } catch (failure: Exception) {
reject(R.INTERNAL_CODEC_FAILURE, failure.message ?: failure::class.simpleName ?: "codec failure")
}
private data class FieldResult<T>(val value: T? = null, val error: D.Incompatible? = null)
private fun requiredString(objectValue: JsonValue.ObjectValue, name: String): FieldResult<String> {
val value = objectValue.fields[name] ?: return FieldResult(error = incompat(R.REQUIRED_FIELD_MISSING, "required field $name is missing"))
if (value === JsonValue.NullValue) return FieldResult(error = incompat(R.REQUIRED_FIELD_NULL, "required field $name is null"))
return if (value is JsonValue.StringValue) FieldResult(value.value) else FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name must be a string"))
}
private fun requiredBoolean(objectValue: JsonValue.ObjectValue, name: String): FieldResult<Boolean> {
val value = objectValue.fields[name] ?: return FieldResult(error = incompat(R.REQUIRED_FIELD_MISSING, "required field $name is missing"))
if (value === JsonValue.NullValue) return FieldResult(error = incompat(R.REQUIRED_FIELD_NULL, "required field $name is null"))
return if (value is JsonValue.BooleanValue) FieldResult(value.value) else FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name must be a boolean"))
}
private fun requiredInt(objectValue: JsonValue.ObjectValue, name: String): FieldResult<Int> {
val value = objectValue.fields[name] ?: return FieldResult(error = incompat(R.REQUIRED_FIELD_MISSING, "required field $name is missing"))
if (value === JsonValue.NullValue) return FieldResult(error = incompat(R.REQUIRED_FIELD_NULL, "required field $name is null"))
val raw = (value as? JsonValue.NumberValue)?.raw ?: return FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name must be an integer"))
if (raw.contains('.') || raw.contains('e', true)) return FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name must be an integer"))
return raw.toIntOrNull()?.let(::FieldResult) ?: FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name is outside integer range"))
}
private fun requiredStringArray(objectValue: JsonValue.ObjectValue, name: String): FieldResult<List<String>> {
val value = objectValue.fields[name] ?: return FieldResult(error = incompat(R.REQUIRED_FIELD_MISSING, "required field $name is missing"))
if (value === JsonValue.NullValue) return FieldResult(error = incompat(R.REQUIRED_FIELD_NULL, "required field $name is null"))
val array = value as? JsonValue.ArrayValue ?: return FieldResult(error = incompat(R.FIELD_TYPE_INVALID, "$name must be an array"))
val strings = array.values.map { (it as? JsonValue.StringValue)?.value ?: return FieldResult(error = incompat(R.CAPABILITIES_INVALID, "$name must contain only strings")) }
return FieldResult(strings)
}
private fun optionalString(objectValue: JsonValue.ObjectValue, name: String): String? = (objectValue.fields[name] as? JsonValue.StringValue)?.value
private fun optionalInt(objectValue: JsonValue.ObjectValue, name: String): Int? = (objectValue.fields[name] as? JsonValue.NumberValue)?.raw?.takeIf { !it.contains('.') && !it.contains('e', true) }?.toIntOrNull()
private fun validPrefixedUuidV4(value: String, prefix: String): Boolean {
if (!value.startsWith(prefix)) return false
val suffix = value.removePrefix(prefix)
if (suffix.length != 36 || suffix != suffix.lowercase()) return false
return try {
val uuid = UUID.fromString(suffix)
uuid.version() == 4 && uuid.toString() == suffix
} catch (_: IllegalArgumentException) { false }
}
private fun validHumanText(value: String, maximum: Int): Boolean = value.isNotBlank() && value.length <= maximum && !hasControl(value)
private fun validErrorToken(value: String): Boolean = value.length in 1..128 && value.all { it in 'A'..'Z' || it in '0'..'9' || it == '_' || it == '-' }
private fun hasControl(value: String): Boolean = value.any { it.code < 0x20 || it.code == 0x7f }
private fun reject(code: R, detail: String) = E.Rejected(code, detail)
private fun incompat(code: R, detail: String, protocol: Int? = null, schema: Int? = null) = D.Incompatible(code, detail, protocol, schema)
}
