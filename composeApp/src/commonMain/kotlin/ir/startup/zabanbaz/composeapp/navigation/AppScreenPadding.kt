package ir.startup.zabanbaz.composeapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.startup.zabanbaz.composeapp.theme.zabanbazScreenInsets

/** Safe area + scaffold bottom inset (snackbar) for screens without [SplashBackground]. */
@Composable
fun AppScreenPadding(
    padding: PaddingValues,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zabanbazScreenInsets()
            .padding(bottom = padding.calculateBottomPadding()),
    ) {
        content()
    }
}
