package com.swrlz.keyboard.ime

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.TextView

/**
 * Non-functional checkpoint scaffold.
 *
 * This service intentionally performs no text capture, transformation,
 * enrollment, routing, telemetry, or CLIENT communication.
 */
class SwrlzInputMethodService : InputMethodService() {
    override fun onCreateInputView(): View = TextView(this).apply {
        text = "SWRLZ Keyboard scaffold — typing not implemented"
        contentDescription = text
        isFocusable = false
    }
}
