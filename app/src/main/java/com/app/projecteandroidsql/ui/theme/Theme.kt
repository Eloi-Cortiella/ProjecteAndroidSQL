package com.app.projecteandroidsql.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenBlue,
    onPrimary = Color.White,
    secondary = PistachoGreen,
    onSecondary = Brown,
    tertiary = LighGreen,
    onTertiary = Brown,
    background = Brown,
    onBackground = Color.White,
    surface = LightGrey,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = PistachoGreen,
    onPrimary = Color.White,
    primaryContainer = LightGrey,
    onPrimaryContainer = Color.White,
    secondary = LighGreen,
    onSecondary = Brown,
    tertiary = LightGrey,
    background = Brown,
    onBackground = Color.White,
    surface = LightGrey,
    onSurface = Color.White,
    surfaceVariant = LightGrey,
    onSurfaceVariant = Color.White,
    outline = PistachoGreen
)

@Composable
fun ProjecteAndroidSQLTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}