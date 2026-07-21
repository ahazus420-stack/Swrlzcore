package com.swrlz.keyboard.app.policy

/**
 * Pure, Android-independent classification seam for editor privacy policy.
 *
 * The numeric constants mirror Android InputType values so this file can be
 * verified with the standalone Kotlin compiler. The IME service adapts
 * EditorInfo into this neutral descriptor.
 */
data class EditorContextDescriptor(
    val inputType: Int,
    val imeOptions: Int = 0,
    val packageName: String? = null,
    val hintText: String? = null,
    val privateImeOptions: String? = null,
    val autofillHints: List<String> = emptyList(),
)

enum class EditorContextClass {
    ORDINARY_TEXT,
    MULTILINE_TEXT,
    SEARCH_OR_URL,
    EMAIL_OR_RECIPIENT,
    NUMERIC,
    PHONE,
    PASSWORD_OR_SECRET,
    PAYMENT_OR_FINANCIAL_SECRET,
    ONE_TIME_CODE,
    UNKNOWN_SENSITIVE,
}

data class EditorContextPolicy(
    val contextClass: EditorContextClass,
    val protected: Boolean,
    val swrlzActionsAllowed: Boolean,
    val contentTelemetryAllowed: Boolean,
)

object EditorContextClassifier {
    private const val TYPE_MASK_CLASS = 0x0000000f
    private const val TYPE_MASK_VARIATION = 0x00000ff0

    private const val TYPE_CLASS_TEXT = 0x00000001
    private const val TYPE_CLASS_NUMBER = 0x00000002
    private const val TYPE_CLASS_PHONE = 0x00000003

    private const val TYPE_TEXT_VARIATION_URI = 0x00000010
    private const val TYPE_TEXT_VARIATION_EMAIL_ADDRESS = 0x00000020
    private const val TYPE_TEXT_VARIATION_PASSWORD = 0x00000080
    private const val TYPE_TEXT_VARIATION_VISIBLE_PASSWORD = 0x00000090
    private const val TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS = 0x000000d0
    private const val TYPE_TEXT_VARIATION_WEB_PASSWORD = 0x000000e0
    private const val TYPE_NUMBER_VARIATION_PASSWORD = 0x00000010
    private const val TYPE_TEXT_FLAG_MULTI_LINE = 0x00020000

    private val paymentTokens = setOf(
        "card number",
        "credit card",
        "debit card",
        "cvv",
        "cvc",
        "security code",
        "routing number",
        "bank account",
        "account number",
    )

    private val oneTimeCodeTokens = setOf(
        "one time code",
        "one-time code",
        "otp",
        "sms otp",
        "verification code",
        "authentication code",
    )

    private val unknownSensitiveTokens = setOf(
        "sensitive",
        "secret",
        "private",
        "no_personalized_learning",
        "noextractui",
    )

    fun classify(descriptor: EditorContextDescriptor): EditorContextPolicy {
        val searchable = buildList {
            descriptor.hintText?.let(::add)
            descriptor.privateImeOptions?.let(::add)
            addAll(descriptor.autofillHints)
        }.joinToString(" ").lowercase()

        val contextClass = when {
            paymentTokens.any(searchable::contains) ->
                EditorContextClass.PAYMENT_OR_FINANCIAL_SECRET

            oneTimeCodeTokens.any(searchable::contains) ->
                EditorContextClass.ONE_TIME_CODE

            isPasswordInput(descriptor.inputType) ->
                EditorContextClass.PASSWORD_OR_SECRET

            unknownSensitiveTokens.any(searchable::contains) ->
                EditorContextClass.UNKNOWN_SENSITIVE

            else -> classifyOrdinaryType(descriptor.inputType, searchable)
        }

        val protected = contextClass in setOf(
            EditorContextClass.PASSWORD_OR_SECRET,
            EditorContextClass.PAYMENT_OR_FINANCIAL_SECRET,
            EditorContextClass.ONE_TIME_CODE,
            EditorContextClass.UNKNOWN_SENSITIVE,
        )

        return EditorContextPolicy(
            contextClass = contextClass,
            protected = protected,
            swrlzActionsAllowed = !protected,
            contentTelemetryAllowed = false,
        )
    }

    private fun classifyOrdinaryType(inputType: Int, searchable: String): EditorContextClass {
        val inputClass = inputType and TYPE_MASK_CLASS
        val variation = inputType and TYPE_MASK_VARIATION

        return when (inputClass) {
            TYPE_CLASS_TEXT -> when {
                variation == TYPE_TEXT_VARIATION_EMAIL_ADDRESS ||
                    variation == TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS ->
                    EditorContextClass.EMAIL_OR_RECIPIENT

                variation == TYPE_TEXT_VARIATION_URI ||
                    searchable.contains("search") ||
                    searchable.contains("url") ->
                    EditorContextClass.SEARCH_OR_URL

                inputType and TYPE_TEXT_FLAG_MULTI_LINE != 0 ->
                    EditorContextClass.MULTILINE_TEXT

                else -> EditorContextClass.ORDINARY_TEXT
            }

            TYPE_CLASS_NUMBER -> EditorContextClass.NUMERIC
            TYPE_CLASS_PHONE -> EditorContextClass.PHONE
            else -> EditorContextClass.UNKNOWN_SENSITIVE
        }
    }

    private fun isPasswordInput(inputType: Int): Boolean {
        val inputClass = inputType and TYPE_MASK_CLASS
        val variation = inputType and TYPE_MASK_VARIATION

        return when (inputClass) {
            TYPE_CLASS_TEXT -> variation in setOf(
                TYPE_TEXT_VARIATION_PASSWORD,
                TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
                TYPE_TEXT_VARIATION_WEB_PASSWORD,
            )

            TYPE_CLASS_NUMBER -> variation == TYPE_NUMBER_VARIATION_PASSWORD
            else -> false
        }
    }
}
