package com.swrlz.keyboard.domain

enum class EditorSensitivity {
    ORDINARY,
    SENSITIVE,
    UNKNOWN,
}

interface EditorContextPolicy {
    fun classify(inputType: Int, privateImeOptions: String?): EditorSensitivity
}
