package ir.startup.zabanbaz.composeapp.ui.discussion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.components.AppSecondaryButton
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.discussion.presentation.DiscussionCallPhase
import ir.startup.zabanbaz.feature.discussion.presentation.DiscussionCallViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DiscussionCallScreen(
    sessionId: Int,
    onFinished: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: DiscussionCallViewModel = koinViewModel { parametersOf(sessionId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    SplashBackground(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                IconButton(
                    onClick = { viewModel.onLeave(onFinished) },
                    enabled = !state.isLeaving,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = AppStrings.profileBack,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (state.phase != DiscussionCallPhase.SignalingComplete &&
                        state.phase != DiscussionCallPhase.PartnerLeft
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Text(
                        text = state.partnerDisplayName?.let {
                            AppStrings.discussionCallWith(it)
                        } ?: AppStrings.discussionCallTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.statusMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )

                    if (state.phase == DiscussionCallPhase.SignalingComplete) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                        ) {
                            Text(
                                text = AppStrings.discussionVideoComingSoon,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    AppSecondaryButton(
                        text = AppStrings.discussionLeave,
                        onClick = { viewModel.onLeave(onFinished) },
                        enabled = !state.isLeaving,
                    )
                }
            }
        }
    }
}
