package ir.startup.zabanbaz.composeapp.ui.discussion

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchStatus
import ir.startup.zabanbaz.composeapp.components.AppPrimaryButton
import ir.startup.zabanbaz.composeapp.components.AppSecondaryButton
import ir.startup.zabanbaz.composeapp.components.InfoRow
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.discussion.presentation.DiscussionQueueViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscussionQueueScreen(
    onBack: () -> Unit,
    onNavigateToCall: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: DiscussionQueueViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.matchStatus, state.sessionId) {
        val sessionId = state.sessionId
        if (state.matchStatus == DiscussionMatchStatus.Matched && sessionId != null) {
            onNavigateToCall(sessionId)
        }
    }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    SplashBackground(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DiscussionQueueTopBar(
                onBack = { viewModel.onLeave(onFinished = onBack) },
                enabled = !state.isLeaving,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    state.isLoading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Text(
                                text = AppStrings.discussionJoining,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    state.matchStatus == DiscussionMatchStatus.Matched -> {
                        DiscussionMatchedContent(
                            partnerName = state.partnerDisplayName.orEmpty(),
                            onStartCall = { state.sessionId?.let(onNavigateToCall) },
                        )
                    }

                    state.matchStatus == DiscussionMatchStatus.Queued -> {
                        DiscussionSearchingContent(
                            languageName = state.learningLanguageName.orEmpty(),
                            cefrLevel = state.englishCefrLevel.orEmpty(),
                            onCancel = { viewModel.onLeave(onFinished = onBack) },
                            isLeaving = state.isLeaving,
                        )
                    }

                    else -> {
                        DiscussionSearchingContent(
                            languageName = state.learningLanguageName.orEmpty(),
                            cefrLevel = state.englishCefrLevel.orEmpty(),
                            onCancel = { viewModel.onLeave(onFinished = onBack) },
                            isLeaving = state.isLeaving,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscussionQueueTopBar(
    onBack: () -> Unit,
    enabled: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        IconButton(onClick = onBack, enabled = enabled) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = AppStrings.profileBack,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DiscussionSearchingContent(
    languageName: String,
    cefrLevel: String,
    onCancel: () -> Unit,
    isLeaving: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = AppStrings.discussionSearchingTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AppStrings.discussionSearchingSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        DiscussionCriteriaCard(languageName = languageName, cefrLevel = cefrLevel)
        AppSecondaryButton(
            text = AppStrings.discussionCancel,
            onClick = onCancel,
            enabled = !isLeaving,
        )
    }
}

@Composable
private fun DiscussionMatchedContent(
    partnerName: String,
    onStartCall: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = AppStrings.discussionMatchedTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = AppStrings.discussionCallWith(partnerName),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        AppPrimaryButton(
            text = AppStrings.discussionStartCall,
            onClick = onStartCall,
        )
    }
}

@Composable
private fun DiscussionCriteriaCard(
    languageName: String,
    cefrLevel: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            InfoRow(label = AppStrings.homeLabelLanguage, value = languageName)
            InfoRow(
                label = AppStrings.homeLabelLevel,
                value = AppStrings.homeCefrBadge(cefrLevel),
            )
        }
    }
}
