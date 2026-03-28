package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF006C4C),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF89F8C7),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF002114),
    secondary = androidx.compose.ui.graphics.Color(0xFF4D6357),
    onSecondary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFCFE9D9),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF0A1F16),
    tertiary = androidx.compose.ui.graphics.Color(0xFF3D6373),
    onTertiary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFFC1E8FB),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF001F29),
    error = androidx.compose.ui.graphics.Color(0xFFBA1A1A),
    errorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
    background = androidx.compose.ui.graphics.Color(0xFFFBFDF9),
    onBackground = androidx.compose.ui.graphics.Color(0xFF191C1A),
    surface = androidx.compose.ui.graphics.Color(0xFFFBFDF9),
    onSurface = androidx.compose.ui.graphics.Color(0xFF191C1A),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFDCE5DD),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF404943)
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF6CDBAC),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003826),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF005138),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF89F8C7),
    secondary = androidx.compose.ui.graphics.Color(0xFFB3CCBD),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF1F352A),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF364B40),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFCFE9D9),
    tertiary = androidx.compose.ui.graphics.Color(0xFFA5CCDF),
    onTertiary = androidx.compose.ui.graphics.Color(0xFF073543),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF254B5A),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFFC1E8FB),
    error = androidx.compose.ui.graphics.Color(0xFFFFB4AB),
    errorContainer = androidx.compose.ui.graphics.Color(0xFF93000A),
    onError = androidx.compose.ui.graphics.Color(0xFF690005),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    background = androidx.compose.ui.graphics.Color(0xFF191C1A),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE1E3DF),
    surface = androidx.compose.ui.graphics.Color(0xFF191C1A),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE1E3DF),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF404943),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFC0C9C1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
