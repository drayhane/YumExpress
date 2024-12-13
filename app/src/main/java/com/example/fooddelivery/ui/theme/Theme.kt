package com.example.fooddelivery.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define light and dark color schemes (you can customize these)
private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5)
)

@Composable
fun FoodDeliveryTheme(
    darkTheme: Boolean = false, // Set to true for dark theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    // Apply the theme using MaterialTheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}
