package ir.startup.zabanbaz.composeapp

import androidx.compose.runtime.Composable
import ir.startup.zabanbaz.composeapp.theme.AppTheme

/** Android entry is via [ir.startup.zabanbaz.android.MainActivity]; this wraps theme for reuse. */
@Composable
fun ZabanbazRoot() {
    AppTheme {
        ZabanbazApp()
    }
}
