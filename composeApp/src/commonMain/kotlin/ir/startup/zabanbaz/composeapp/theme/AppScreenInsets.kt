package ir.startup.zabanbaz.composeapp.theme

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Status bar + navigation bar safe area, with a little extra space below the status bar. */
fun Modifier.zabanbazScreenInsets(): Modifier =
    statusBarsPadding()
        .padding(top = 12.dp)
        .navigationBarsPadding()
