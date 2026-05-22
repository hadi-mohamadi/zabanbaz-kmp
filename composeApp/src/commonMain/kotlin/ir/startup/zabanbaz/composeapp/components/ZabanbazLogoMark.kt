package ir.startup.zabanbaz.composeapp.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.startup.zabanbaz.composeapp.theme.AppGradients

/**
 * Vector logo mark: rounded tile with a stylized speech-wave (language / conversation).
 */
@Composable
fun ZabanbazLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val radius = w * 0.22f

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = AppGradients.logoMark,
                start = Offset(0f, 0f),
                end = Offset(w, h),
            ),
            size = Size(w, h),
            cornerRadius = CornerRadius(radius, radius),
        )

        val strokeWidth = w * 0.07f
        val waveColor = Color.White.copy(alpha = 0.95f)

        val path1 = Path().apply {
            moveTo(w * 0.28f, h * 0.58f)
            cubicTo(w * 0.38f, h * 0.38f, w * 0.48f, h * 0.72f, w * 0.58f, h * 0.52f)
        }
        drawPath(
            path = path1,
            color = waveColor,
            style = Stroke(width = strokeWidth),
        )

        val path2 = Path().apply {
            moveTo(w * 0.42f, h * 0.62f)
            cubicTo(w * 0.52f, h * 0.42f, w * 0.62f, h * 0.68f, w * 0.72f, h * 0.48f)
        }
        drawPath(
            path = path2,
            color = waveColor.copy(alpha = 0.75f),
            style = Stroke(width = strokeWidth * 0.85f),
        )

        drawCircle(
            color = waveColor,
            radius = w * 0.055f,
            center = Offset(w * 0.34f, h * 0.36f),
        )
    }
}
