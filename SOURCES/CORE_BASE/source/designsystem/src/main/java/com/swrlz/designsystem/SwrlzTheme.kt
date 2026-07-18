package com.swrlz.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF2B6CB0),
    secondary = androidx.compose.ui.graphics.Color(0xFF4A5568)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF90CDF4),
    secondary = androidx.compose.ui.graphics.Color(0xFFA0AEC0)
)

@Composable
fun SwrlzTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors) {
        content()
    }
}
