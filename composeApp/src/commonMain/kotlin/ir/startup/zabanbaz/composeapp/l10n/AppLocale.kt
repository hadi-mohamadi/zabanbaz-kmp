package ir.startup.zabanbaz.composeapp.l10n

import androidx.compose.ui.unit.LayoutDirection

enum class AppLocale {
    English,
    Persian,
    ;

    val layoutDirection: LayoutDirection
        get() = when (this) {
            English -> LayoutDirection.Ltr
            Persian -> LayoutDirection.Rtl
        }

    companion object {
        /** App UI language; layout direction follows this, not the device locale. */
        val current: AppLocale = English
    }
}
