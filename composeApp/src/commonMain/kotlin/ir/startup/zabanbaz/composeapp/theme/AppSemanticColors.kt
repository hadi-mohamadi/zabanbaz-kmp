package ir.startup.zabanbaz.composeapp.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppSemanticColors(
    val success: Color,
    val warning: Color,
    val info: Color,
)

val LocalAppSemanticColors = staticCompositionLocalOf {
    AppSemanticColors(
        success = AppColors.Success,
        warning = AppColors.Warning,
        info = AppColors.Info,
    )
}
