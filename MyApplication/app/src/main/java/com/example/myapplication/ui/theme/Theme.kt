package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DeepBlue80,
    onPrimary = DeepBlue10,
    primaryContainer = DeepBlue30,
    onPrimaryContainer = DeepBlue90,
    secondary = SlateGray80,
    onSecondary = SlateGray10,
    secondaryContainer = SlateGray30,
    onSecondaryContainer = SlateGray90,
    tertiary = AccentGold80,
    onTertiary = DeepBlue10,
    background = DeepBlue10,
    onBackground = SlateGray90,
    surface = SlateGray20,
    onSurface = SlateGray90,
    surfaceVariant = SlateGray30,
    onSurfaceVariant = SlateGray80,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue40,
    onPrimary = Color.White,
    primaryContainer = DeepBlue90,
    onPrimaryContainer = DeepBlue10,
    secondary = SlateGray40,
    onSecondary = Color.White,
    secondaryContainer = SlateGray90,
    onSecondaryContainer = SlateGray10,
    tertiary = AccentGold,
    onTertiary = DeepBlue10,
    background = Color(0xFFF8F9FA),
    onBackground = DeepBlue10,
    surface = Color.White,
    onSurface = DeepBlue10,
    surfaceVariant = SlateGray90,
    onSurfaceVariant = SlateGray40,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
        content = content
    )
}
