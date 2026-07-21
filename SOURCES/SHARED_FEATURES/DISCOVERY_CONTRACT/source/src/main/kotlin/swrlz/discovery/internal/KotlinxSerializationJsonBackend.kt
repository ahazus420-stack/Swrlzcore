package swrlz.discovery.internal

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull

internal class KotlinxSerializationJsonBackend : DiscoveryJsonBackend {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = false
        coerceInputValues = false
        encodeDefaults = true
    }

    override fun parse(json: String): JsonParseResult = try {
        JsonParseResult.Parsed(this.json.parseToJsonElement(json).toNeutralTree())
    } catch (failure: Exception) {
        JsonParseResult.Failed(failure.message ?: failure::class.simpleName ?: "invalid JSON")
    }

    override fun encodeCanonical(value: JsonValue): String = json.encodeToString(JsonElement.serializer(), value.toKotlinxTree())

    private fun JsonElement.toNeutralTree(): JsonValue = when (this) {
        is JsonObject -> JsonValue.ObjectValue(entries.associateTo(linkedMapOf()) { it.key to it.value.toNeutralTree() })
        is JsonArray -> JsonValue.ArrayValue(map { it.toNeutralTree() })
        is JsonPrimitive -> when {
            this === JsonNull -> JsonValue.NullValue
            isString -> JsonValue.StringValue(content)
            booleanOrNull != null -> JsonValue.BooleanValue(boolean)
            else -> JsonValue.NumberValue(content)
        }
    }

    private fun JsonValue.toKotlinxTree(): JsonElement = when (this) {
        is JsonValue.ObjectValue -> JsonObject(fields.mapValues { it.value.toKotlinxTree() })
        is JsonValue.ArrayValue -> JsonArray(values.map { it.toKotlinxTree() })
        is JsonValue.StringValue -> JsonPrimitive(value)
        is JsonValue.NumberValue -> JsonPrimitive(raw.toLongOrNull() ?: raw.toDouble())
        is JsonValue.BooleanValue -> JsonPrimitive(value)
        JsonValue.NullValue -> JsonNull
    }
}
