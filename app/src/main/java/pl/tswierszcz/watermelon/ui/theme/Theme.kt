package pl.tswierszcz.watermelon.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimaryGreen,
    primaryContainer = PrimaryContainerGreen,
    onPrimaryContainer = OnPrimaryContainerGreen,
    secondary = SecondaryGreen,
    onSecondary = OnSecondaryGreen,
    secondaryContainer = SecondaryContainerGreen,
    onSecondaryContainer = OnSecondaryContainerGreen,
    tertiary = TertiaryBlue,
    onTertiary = OnTertiaryBlue,
    tertiaryContainer = TertiaryContainerBlue,
    onTertiaryContainer = OnTertiaryContainerBlue,
    error = ErrorRed,
    onError = OnErrorRed,
    errorContainer = ErrorContainerRed,
    onErrorContainer = OnErrorContainerRed,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
)

// For now, we'll use a similar palette for dark mode or just keep it light-focused 
// as requested for a "clean, minimal" look. 
// I'll define a simple dark scheme but focus on the light one first.
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryContainerGreen,
    onPrimary = OnPrimaryContainerGreen,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = OnPrimaryGreen,
    // ... other mappings
)

@Composable
fun WaterMelonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
