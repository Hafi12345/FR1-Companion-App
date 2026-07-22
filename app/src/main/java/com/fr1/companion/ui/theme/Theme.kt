package com.fr1.companion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Dynamic/system color is intentionally never used — design.md requires the
// red/white emergency-medical identity to stay intact, not follow wallpaper colors.
private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryRedDark,
    onPrimaryContainer = OnPrimary,
    secondary = TextSecondary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Surface,
    onSurfaceVariant = TextSecondary,
    outline = Divider,
    error = SeveritySevere,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryRedDarkMode,
    onPrimary = TextPrimaryDark,
    primaryContainer = PrimaryRedDark,
    onPrimaryContainer = TextPrimaryDark,
    secondary = TextSecondary,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    outline = Divider,
    error = SeveritySevere,
)

@Composable
fun FR1CompanionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
