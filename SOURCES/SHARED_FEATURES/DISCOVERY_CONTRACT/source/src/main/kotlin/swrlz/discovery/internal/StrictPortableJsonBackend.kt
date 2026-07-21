package swrlz.discovery.internal

internal class StrictPortableJsonBackend : DiscoveryJsonBackend {
    override fun parse(json: String): JsonParseResult = try {
        val parser = Parser(json)
        val value = parser.parseValue()
        parser.skipWhitespace()
        if (!parser.atEnd()) error("trailing content at offset ${parser.position()}")
        JsonParseResult.Parsed(value)
    } catch (failure: ParseFailure) {
        JsonParseResult.Failed(failure.message ?: "invalid JSON")
    }

    override fun encodeCanonical(value: JsonValue): String = buildString { appendValue(value) }

    private fun StringBuilder.appendValue(value: JsonValue) {
        when (value) {
            is JsonValue.ObjectValue -> {
                append('{')
                value.fields.entries.forEachIndexed { index, entry ->
                    if (index > 0) append(',')
                    appendString(entry.key)
                    append(':')
                    appendValue(entry.value)
                }
                append('}')
            }
            is JsonValue.ArrayValue -> {
                append('[')
                value.values.forEachIndexed { index, item ->
                    if (index > 0) append(',')
                    appendValue(item)
                }
                append(']')
            }
            is JsonValue.StringValue -> appendString(value.value)
            is JsonValue.NumberValue -> append(value.raw)
            is JsonValue.BooleanValue -> append(if (value.value) "true" else "false")
            JsonValue.NullValue -> append("null")
        }
    }

    private fun StringBuilder.appendString(value: String) {
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
                else -> if (character.code < 0x20) append("\\u%04x".format(character.code)) else append(character)
            }
        }
        append('"')
    }

    private class Parser(private val input: String) {
        private var index: Int = 0
        fun position(): Int = index
        fun atEnd(): Boolean = index >= input.length
        fun skipWhitespace() { while (!atEnd() && input[index] in " \t\r\n") index++ }

        fun parseValue(): JsonValue {
            skipWhitespace()
            if (atEnd()) fail("unexpected end of input")
            return when (input[index]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> JsonValue.StringValue(parseString())
                't' -> { expectLiteral("true"); JsonValue.BooleanValue(true) }
                'f' -> { expectLiteral("false"); JsonValue.BooleanValue(false) }
                'n' -> { expectLiteral("null"); JsonValue.NullValue }
                '-', in '0'..'9' -> JsonValue.NumberValue(parseNumber())
                else -> fail("unexpected character '${input[index]}' at offset $index")
            }
        }

        private fun parseObject(): JsonValue.ObjectValue {
            expect('{')
            skipWhitespace()
            val fields = linkedMapOf<String, JsonValue>()
            if (consume('}')) return JsonValue.ObjectValue(fields)
            while (true) {
                skipWhitespace()
                if (atEnd() || input[index] != '"') fail("object key must be a string at offset $index")
                val key = parseString()
                if (fields.containsKey(key)) fail("duplicate object key '$key'")
                skipWhitespace(); expect(':')
                fields[key] = parseValue()
                skipWhitespace()
                if (consume('}')) break
                expect(',')
            }
            return JsonValue.ObjectValue(fields)
        }

        private fun parseArray(): JsonValue.ArrayValue {
            expect('[')
            skipWhitespace()
            val values = mutableListOf<JsonValue>()
            if (consume(']')) return JsonValue.ArrayValue(values)
            while (true) {
                values += parseValue()
                skipWhitespace()
                if (consume(']')) break
                expect(',')
            }
            return JsonValue.ArrayValue(values)
        }

        private fun parseString(): String {
            expect('"')
            val output = StringBuilder()
            while (!atEnd()) {
                val character = input[index++]
                when (character) {
                    '"' -> return output.toString()
                    '\\' -> {
                        if (atEnd()) fail("unterminated escape")
                        when (val escaped = input[index++]) {
                            '"', '\\', '/' -> output.append(escaped)
                            'b' -> output.append('\b')
                            'f' -> output.append('\u000C')
                            'n' -> output.append('\n')
                            'r' -> output.append('\r')
                            't' -> output.append('\t')
                            'u' -> output.append(parseUnicodeEscape())
                            else -> fail("invalid escape \\$escaped")
                        }
                    }
                    else -> {
                        if (character.code < 0x20) fail("unescaped control character")
                        output.append(character)
                    }
                }
            }
            fail("unterminated string")
        }

        private fun parseUnicodeEscape(): Char {
            if (index + 4 > input.length) fail("incomplete unicode escape")
            val hex = input.substring(index, index + 4)
            if (!hex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) fail("invalid unicode escape")
            index += 4
            return hex.toInt(16).toChar()
        }

        private fun parseNumber(): String {
            val start = index
            consume('-')
            if (atEnd()) fail("incomplete number")
            if (consume('0')) {
                if (!atEnd() && input[index].isDigit()) fail("leading zero")
            } else {
                if (atEnd() || input[index] !in '1'..'9') fail("invalid number")
                while (!atEnd() && input[index].isDigit()) index++
            }
            if (consume('.')) {
                if (atEnd() || !input[index].isDigit()) fail("invalid fraction")
                while (!atEnd() && input[index].isDigit()) index++
            }
            if (!atEnd() && (input[index] == 'e' || input[index] == 'E')) {
                index++
                if (!atEnd() && (input[index] == '+' || input[index] == '-')) index++
                if (atEnd() || !input[index].isDigit()) fail("invalid exponent")
                while (!atEnd() && input[index].isDigit()) index++
            }
            return input.substring(start, index)
        }

        private fun expectLiteral(literal: String) {
            if (!input.startsWith(literal, index)) fail("expected $literal")
            index += literal.length
        }

        private fun expect(character: Char) {
            skipWhitespace()
            if (atEnd() || input[index] != character) fail("expected '$character' at offset $index")
            index++
        }

        private fun consume(character: Char): Boolean {
            if (!atEnd() && input[index] == character) { index++; return true }
            return false
        }

        private fun fail(message: String): Nothing = throw ParseFailure(message)
    }

    private class ParseFailure(message: String) : RuntimeException(message)
}
