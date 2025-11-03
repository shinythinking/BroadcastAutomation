package com.shinythinking.broadcastautomation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue50,
    onPrimaryContainer = Blue600,

    secondary = Purple500,
    onSecondary = White,
    secondaryContainer = Purple50,
    onSecondaryContainer = Purple600,

    tertiary = Green500,
    onTertiary = White,
    tertiaryContainer = Green50,
    onTertiaryContainer = Green600,

    error = Error,
    onError = White,

    background = Gray50,
    onBackground = Gray900,

    surface = White,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,

    outline = Gray300,
    outlineVariant = Gray200
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue600,
    onPrimaryContainer = Blue50,

    secondary = Purple500,
    onSecondary = White,
    secondaryContainer = Purple600,
    onSecondaryContainer = Purple50,

    tertiary = Green500,
    onTertiary = White,
    tertiaryContainer = Green600,
    onTertiaryContainer = Green50,

    error = Error,
    onError = White,

    background = Gray900,
    onBackground = Gray50,

    surface = Gray800,
    onSurface = Gray50,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,

    outline = Gray600,
    outlineVariant = Gray700
)

@Composable
fun BroadcastAutomationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}