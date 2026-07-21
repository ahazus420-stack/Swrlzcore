package swrlz.discovery.internal

internal sealed interface JsonValue {
    data class ObjectValue(val fields: LinkedHashMap<String, JsonValue>) : JsonValue
    data class ArrayValue(val values: List<JsonValue>) : JsonValue
    data class StringValue(val value: String) : JsonValue
    data class NumberValue(val raw: String) : JsonValue
    data class BooleanValue(val value: Boolean) : JsonValue
    data object NullValue : JsonValue
}

internal sealed interface JsonParseResult {
    data class Parsed(val value: JsonValue) : JsonParseResult
    data class Failed(val detail: String) : JsonParseResult
}

internal interface DiscoveryJsonBackend {
    fun parse(json: String): JsonParseResult
    fun encodeCanonical(value: JsonValue): String
}
