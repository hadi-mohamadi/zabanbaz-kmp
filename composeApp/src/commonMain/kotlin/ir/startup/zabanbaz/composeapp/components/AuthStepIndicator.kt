package ir.startup.zabanbaz.composeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.startup.zabanbaz.feature.auth.presentation.AuthStep

@Composable
fun AuthStepIndicator(
    currentStep: AuthStep,
    mobileLabel: String,
    codeLabel: String,
    modifier: Modifier = Modifier,
) {
    val isCodeStep = currentStep == AuthStep.EnterCode
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepDot(
            active = !isCodeStep,
            completed = isCodeStep,
            label = mobileLabel,
        )
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    if (isCodeStep) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                ),
        )
        StepDot(
            active = isCodeStep,
            completed = false,
            label = codeLabel,
        )
    }
}

@Composable
private fun StepDot(
    active: Boolean,
    completed: Boolean,
    label: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    val dotColor = when {
        active || completed -> colorScheme.primary
        else -> colorScheme.outlineVariant
    }
    val textColor = when {
        active || completed -> colorScheme.primary
        else -> colorScheme.onSurfaceVariant
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(if (active) 10.dp else 8.dp)
                .clip(CircleShape)
                .background(dotColor),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
        )
    }
}
