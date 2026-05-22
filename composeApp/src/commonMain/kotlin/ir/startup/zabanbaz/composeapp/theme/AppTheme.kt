package ir.startup.zabanbaz.composeapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import ir.startup.zabanbaz.composeapp.l10n.AppLocale

private val LightColorScheme = lightColorScheme(
    primary = AppSchemeColors.LightPrimary,
    onPrimary = AppColors.Neutral0,
    primaryContainer = AppSchemeColors.LightPrimaryContainer,
    onPrimaryContainer = AppSchemeColors.LightOnPrimaryContainer,
    secondary = AppSchemeColors.LightSecondary,
    onSecondary = AppColors.Neutral0,
    secondaryContainer = AppSchemeColors.LightSecondaryContainer,
    onSecondaryContainer = AppSchemeColors.LightOnSecondaryContainer,
    tertiary = AppSchemeColors.LightTertiary,
    onTertiary = AppColors.Neutral0,
    tertiaryContainer = AppSchemeColors.LightTertiaryContainer,
    onTertiaryContainer = AppSchemeColors.LightOnTertiaryContainer,
    error = AppSchemeColors.LightError,
    onError = AppColors.Neutral0,
    errorContainer = AppSchemeColors.LightErrorContainer,
    onErrorContainer = AppSchemeColors.LightOnErrorContainer,
    background = AppColors.Neutral10,
    onBackground = AppSchemeColors.LightOnSurface,
    surface = AppColors.Neutral10,
    onSurface = AppSchemeColors.LightOnSurface,
    surfaceDim = AppSchemeColors.LightSurfaceDim,
    surfaceBright = AppColors.Neutral0,
    surfaceContainerLowest = AppColors.Neutral0,
    surfaceContainerLow = AppSchemeColors.LightSurfaceContainerLow,
    surfaceContainer = AppSchemeColors.LightSurfaceContainer,
    surfaceContainerHigh = AppSchemeColors.LightSurfaceContainerHigh,
    surfaceContainerHighest = AppSchemeColors.LightSurfaceContainerHighest,
    onSurfaceVariant = AppSchemeColors.LightOnSurfaceVariant,
    outline = AppSchemeColors.LightOutline,
    outlineVariant = AppSchemeColors.LightOutlineVariant,
    inverseSurface = AppSchemeColors.LightInverseSurface,
    inverseOnSurface = AppSchemeColors.LightOnInverseSurface,
    inversePrimary = AppSchemeColors.LightInversePrimary,
    surfaceTint = AppSchemeColors.LightSurfaceTint,
)

private val DarkColorScheme = darkColorScheme(
    primary = AppSchemeColors.DarkPrimary,
    onPrimary = AppSchemeColors.DarkOnPrimary,
    primaryContainer = AppSchemeColors.DarkPrimaryContainer,
    onPrimaryContainer = AppSchemeColors.DarkOnPrimaryContainer,
    secondary = AppSchemeColors.DarkSecondary,
    onSecondary = AppSchemeColors.DarkOnSecondary,
    secondaryContainer = AppSchemeColors.DarkSecondaryContainer,
    onSecondaryContainer = AppSchemeColors.DarkOnSecondaryContainer,
    tertiary = AppSchemeColors.DarkTertiary,
    onTertiary = AppSchemeColors.DarkOnTertiary,
    tertiaryContainer = AppSchemeColors.DarkTertiaryContainer,
    onTertiaryContainer = AppSchemeColors.DarkOnTertiaryContainer,
    error = AppSchemeColors.DarkError,
    onError = AppSchemeColors.DarkOnError,
    errorContainer = AppSchemeColors.DarkErrorContainer,
    onErrorContainer = AppSchemeColors.DarkOnErrorContainer,
    background = AppColors.AppDarkBackground,
    onBackground = AppSchemeColors.DarkOnSurface,
    surface = AppColors.AppDarkBackground,
    onSurface = AppSchemeColors.DarkOnSurface,
    surfaceDim = AppColors.AppDarkBackground,
    surfaceBright = AppSchemeColors.DarkSurfaceBright,
    surfaceContainerLowest = AppSchemeColors.DarkSurfaceContainerLowest,
    surfaceContainerLow = AppSchemeColors.DarkSurfaceContainerLow,
    surfaceContainer = AppSchemeColors.DarkSurfaceContainer,
    surfaceContainerHigh = AppSchemeColors.DarkSurfaceContainerHigh,
    surfaceContainerHighest = AppSchemeColors.DarkSurfaceContainerHighest,
    onSurfaceVariant = AppSchemeColors.DarkOnSurfaceVariant,
    outline = AppSchemeColors.DarkOutline,
    outlineVariant = AppSchemeColors.DarkOutlineVariant,
    inverseSurface = AppSchemeColors.DarkInverseSurface,
    inverseOnSurface = AppSchemeColors.DarkOnInverseSurface,
    inversePrimary = AppSchemeColors.DarkInversePrimary,
    surfaceTint = AppSchemeColors.DarkSurfaceTint,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val semanticColors = AppSemanticColors(
        success = AppColors.Success,
        warning = AppColors.Warning,
        info = AppColors.Info,
    )

    CompositionLocalProvider(
        LocalAppSemanticColors provides semanticColors,
        LocalLayoutDirection provides AppLocale.current.layoutDirection,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

/** Splash / marketing gradients derived from brand tokens. */
object AppGradients {
    val splashLight = listOf(
        Color(0xFFE8EFFF),
        AppColors.Neutral10,
        Color(0xFFF3EEFF),
    )
    val splashDark = listOf(
        AppColors.Neutral1000,
        AppColors.AppDarkBackground,
        Color(0xFF0F1729),
    )
    val logoMark = listOf(
        AppSchemeColors.LightPrimary,
        AppColors.BrandSecondary,
        AppColors.BrandTertiary,
    )
}
