package ir.startup.zabanbaz.composeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import ir.startup.zabanbaz.composeapp.theme.AppColors
import ir.startup.zabanbaz.composeapp.theme.AppGradients
import ir.startup.zabanbaz.composeapp.theme.AppSchemeColors
import ir.startup.zabanbaz.composeapp.theme.zabanbazScreenInsets

@Composable
fun SplashBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (dark) AppGradients.splashDark else AppGradients.splashLight,
                ),
            ),
    ) {
        val primaryGlow = if (dark) {
            AppSchemeColors.DarkPrimary.copy(alpha = 0.14f)
        } else {
            AppSchemeColors.LightPrimary.copy(alpha = 0.18f)
        }
        val secondaryGlow = if (dark) {
            AppColors.BrandSecondary.copy(alpha = 0.12f)
        } else {
            AppColors.BrandSecondary.copy(alpha = 0.10f)
        }

        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-72).dp, y = (-48).dp)
                .blur(80.dp)
                .background(primaryGlow, CircleShape),
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 220.dp, y = 420.dp)
                .blur(72.dp)
                .background(secondaryGlow, CircleShape),
        )

        Box(modifier = Modifier.fillMaxSize().zabanbazScreenInsets()) {
            content()
        }
    }
}
