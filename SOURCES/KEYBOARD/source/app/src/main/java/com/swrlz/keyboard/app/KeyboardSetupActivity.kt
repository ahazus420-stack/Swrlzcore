package com.swrlz.keyboard.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class KeyboardSetupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(24), dp(24), dp(24), dp(24))
        }

        content.addView(TextView(this).apply {
            text = getString(R.string.setup_title)
            textSize = 26f
            gravity = Gravity.CENTER
        })

        content.addView(TextView(this).apply {
            text = getString(R.string.setup_body)
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, dp(16), 0, dp(24))
        })

        content.addView(Button(this).apply {
            text = getString(R.string.open_input_settings)
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
        })

        content.addView(Button(this).apply {
            text = getString(R.string.show_keyboard_picker)
            setOnClickListener {
                getSystemService(InputMethodManager::class.java).showInputMethodPicker()
            }
        })

        setContentView(content)
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
