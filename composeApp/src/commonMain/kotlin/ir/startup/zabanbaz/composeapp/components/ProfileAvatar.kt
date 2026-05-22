package ir.startup.zabanbaz.composeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.composeapp.theme.AppGradients

@Composable
fun ProfileAvatar(
    profile: UserProfile,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
) {
    val initial = profile.fullName?.firstOrNull()?.uppercaseChar()
        ?: profile.username?.firstOrNull()?.uppercaseChar()
        ?: profile.phone.lastOrNull()
        ?: "?"

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(AppGradients.logoMark),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
        )
    }
}
