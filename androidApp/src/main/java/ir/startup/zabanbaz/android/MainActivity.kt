package ir.startup.zabanbaz.android

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.unit.LayoutDirection
import ir.startup.zabanbaz.composeapp.ZabanbazRoot
import ir.startup.zabanbaz.composeapp.l10n.AppLocale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = when (AppLocale.current.layoutDirection) {
            LayoutDirection.Ltr -> View.LAYOUT_DIRECTION_LTR
            LayoutDirection.Rtl -> View.LAYOUT_DIRECTION_RTL
        }
        enableEdgeToEdge()
        setContent {
            ZabanbazRoot()
        }
    }
}
