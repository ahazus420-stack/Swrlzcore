package verification

import com.swrlz.keyboard.app.policy.EditorContextClass
import com.swrlz.keyboard.app.policy.EditorContextClassifier
import com.swrlz.keyboard.app.policy.EditorContextDescriptor

private data class Case(
    val name: String,
    val descriptor: EditorContextDescriptor,
    val expected: EditorContextClass,
    val protected: Boolean,
)

fun main() {
    val cases = listOf(
        Case("ordinary text", EditorContextDescriptor(0x00000001), EditorContextClass.ORDINARY_TEXT, false),
        Case("multiline text", EditorContextDescriptor(0x00020001), EditorContextClass.MULTILINE_TEXT, false),
        Case("email", EditorContextDescriptor(0x00000021), EditorContextClass.EMAIL_OR_RECIPIENT, false),
        Case("web email", EditorContextDescriptor(0x000000d1), EditorContextClass.EMAIL_OR_RECIPIENT, false),
        Case("url", EditorContextDescriptor(0x00000011), EditorContextClass.SEARCH_OR_URL, false),
        Case("search hint", EditorContextDescriptor(0x00000001, hintText = "Search"), EditorContextClass.SEARCH_OR_URL, false),
        Case("numeric", EditorContextDescriptor(0x00000002), EditorContextClass.NUMERIC, false),
        Case("phone", EditorContextDescriptor(0x00000003), EditorContextClass.PHONE, false),
        Case("text password", EditorContextDescriptor(0x00000081), EditorContextClass.PASSWORD_OR_SECRET, true),
        Case("visible password", EditorContextDescriptor(0x00000091), EditorContextClass.PASSWORD_OR_SECRET, true),
        Case("web password", EditorContextDescriptor(0x000000e1), EditorContextClass.PASSWORD_OR_SECRET, true),
        Case("number password", EditorContextDescriptor(0x00000012), EditorContextClass.PASSWORD_OR_SECRET, true),
        Case("otp", EditorContextDescriptor(0x00000002, autofillHints = listOf("smsOTPCode")), EditorContextClass.ONE_TIME_CODE, true),
        Case("payment", EditorContextDescriptor(0x00000002, hintText = "Card number"), EditorContextClass.PAYMENT_OR_FINANCIAL_SECRET, true),
        Case("unknown sensitive", EditorContextDescriptor(0x00000001, privateImeOptions = "sensitive"), EditorContextClass.UNKNOWN_SENSITIVE, true),
        Case("unknown class", EditorContextDescriptor(0x00000000), EditorContextClass.UNKNOWN_SENSITIVE, true),
    )

    var passed = 0
    cases.forEach { case ->
        val actual = EditorContextClassifier.classify(case.descriptor)
        check(actual.contextClass == case.expected) {
            "${case.name}: expected ${case.expected}, got ${actual.contextClass}"
        }
        check(actual.protected == case.protected) {
            "${case.name}: protected mismatch"
        }
        check(actual.swrlzActionsAllowed == !case.protected) {
            "${case.name}: SWRLZ action gate mismatch"
        }
        check(!actual.contentTelemetryAllowed) {
            "${case.name}: content telemetry must remain disabled"
        }
        passed += 1
        println("PASS ${case.name}")
    }

    println("RESULT $passed passed / 0 failed")
}
