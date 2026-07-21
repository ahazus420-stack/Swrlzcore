package com.swrlz.keyboard.app

import android.graphics.Color
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.inputmethodservice.InputMethodService
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.swrlz.keyboard.app.policy.EditorContextClassifier
import com.swrlz.keyboard.app.policy.EditorContextDescriptor

/**
 * Minimal offline-first IME.
 *
 * This service commits ordinary key events directly to the active editor.
 * It contains no network route, telemetry sink, clipboard history, CLIENT
 * enrollment, NODE_HOST attachment, mission execution, or AI action.
 */
class KeyboardImeService : InputMethodService() {
    private var statusView: TextView? = null

    override fun onCreateInputView(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(6), dp(6), dp(6), dp(8))
            setBackgroundColor(Color.rgb(17, 19, 24))
        }

        statusView = TextView(this).apply {
            setTextColor(Color.rgb(86, 224, 255))
            textSize = 12f
            gravity = Gravity.CENTER
            text = getString(R.string.ordinary_mode)
            setPadding(0, 0, 0, dp(4))
        }
        root.addView(
            statusView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            ),
        )

        listOf("qwertyuiop", "asdfghjkl", "zxcvbnm").forEach { row ->
            root.addView(createCharacterRow(row))
        }

        root.addView(createControlRow())
        return root
    }

    override fun onStartInputView(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(attribute, restarting)
        val info = attribute ?: return
        val descriptor = EditorContextDescriptor(
            inputType = info.inputType,
            imeOptions = info.imeOptions,
            packageName = info.packageName,
            hintText = info.hintText?.toString(),
            privateImeOptions = info.privateImeOptions,
            autofillHints = emptyList(),
        )
        val policy = EditorContextClassifier.classify(descriptor)
        statusView?.setText(
            if (policy.protected) R.string.protected_mode else R.string.ordinary_mode,
        )
    }

    override fun onEvaluateFullscreenMode(): Boolean = false

    private fun createCharacterRow(characters: String): LinearLayout =
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            characters.forEach { character ->
                addView(
                    keyButton(character.toString()) {
                        currentInputConnection?.commitText(character.toString(), 1)
                    },
                    weightedKeyParams(),
                )
            }
        }

    private fun createControlRow(): LinearLayout =
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(keyButton("space", ::commitSpace), weightedKeyParams(3f))
            addView(keyButton("⌫", ::commitBackspace), weightedKeyParams())
            addView(keyButton("↵", ::commitEnter), weightedKeyParams())
        }

    private fun keyButton(label: String, action: () -> Unit): Button =
        Button(this).apply {
            text = label
            isAllCaps = false
            minWidth = 0
            minimumWidth = 0
            setOnClickListener { action() }
        }

    private fun weightedKeyParams(weight: Float = 1f): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(0, dp(48), weight).apply {
            setMargins(dp(2), dp(2), dp(2), dp(2))
        }

    private fun commitSpace() {
        currentInputConnection?.commitText(" ", 1)
    }

    private fun commitBackspace() {
        val connection = currentInputConnection ?: return
        val selected = connection.getSelectedText(0)
        if (!selected.isNullOrEmpty()) {
            connection.commitText("", 1)
            return
        }

        if (!connection.deleteSurroundingText(1, 0)) {
            sendDeleteKey(connection)
        }
    }

    private fun sendDeleteKey(connection: InputConnection) {
        connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
    }

    private fun commitEnter() {
        val connection = currentInputConnection ?: return
        val action = currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION)
            ?: EditorInfo.IME_ACTION_NONE

        val handledAction = action in setOf(
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_GO,
            EditorInfo.IME_ACTION_NEXT,
            EditorInfo.IME_ACTION_PREVIOUS,
            EditorInfo.IME_ACTION_SEARCH,
            EditorInfo.IME_ACTION_SEND,
        ) && connection.performEditorAction(action)

        if (!handledAction) {
            connection.commitText("\n", 1)
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
