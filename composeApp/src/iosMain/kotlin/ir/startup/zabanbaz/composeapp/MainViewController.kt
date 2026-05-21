package ir.startup.zabanbaz.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import ir.startup.zabanbaz.composeapp.di.presentationModule
import ir.startup.zabanbaz.composeapp.theme.AppTheme
import ir.startup.zabanbaz.core.di.initZabanbazKoin
import platform.UIKit.UIViewController

private var isKoinInitialized = false

private fun ensureKoinInitialized() {
    if (!isKoinInitialized) {
        initZabanbazKoin(
            isDebug = false,
            extraModules = listOf(presentationModule()),
        )
        isKoinInitialized = true
    }
}

@Suppress("FunctionName")
fun MainViewController(): UIViewController {
    ensureKoinInitialized()
    return ComposeUIViewController {
        AppTheme {
            ZabanbazApp()
        }
    }
}
