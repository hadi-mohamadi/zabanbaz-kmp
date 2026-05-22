package ir.startup.zabanbaz.composeapp.ui.placement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.common.placement.domain.PlacementOption
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.placement.presentation.PlacementViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlacementScreen(
    onComplete: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: PlacementViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    when {
        state.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator()
                Text(
                    text = AppStrings.placementLoading,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        state.isOnResultScreen -> {
            val result = state.result!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = AppStrings.placementResultTitle,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = result.cefrLevel,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
                Text(
                    text = AppStrings.placementResultScore(
                        result.scoreCorrect,
                        result.highestConsecutiveLevel,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(AppStrings.placementContinue)
                }
            }
        }

        else -> {
            val question = state.currentQuestion
            if (question == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(AppStrings.placementEmpty)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = viewModel::loadTest) {
                        Text(AppStrings.retry)
                    }
                }
                return
            }

            val selectedOptionId = viewModel.selectedOptionIdForCurrentQuestion()
            val progress = (state.currentQuestionIndex + 1).toFloat() / state.questions.size.coerceAtLeast(1)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            ) {
                Text(
                    text = AppStrings.placementTitle,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = AppStrings.placementProgress(
                        state.currentQuestionIndex + 1,
                        state.questions.size,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = question.prompt,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                question.options.forEach { option ->
                    PlacementOptionRow(
                        option = option,
                        selected = selectedOptionId == option.id,
                        enabled = !state.isSubmitting,
                        onSelect = { viewModel.onOptionSelected(option.id) },
                    )
                }

                state.fieldError?.let { error ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (state.currentQuestionIndex > 0) {
                        OutlinedButton(
                            onClick = viewModel::onPrevious,
                            enabled = !state.isSubmitting,
                        ) {
                            Text(AppStrings.placementBack)
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = viewModel::onNext,
                        enabled = !state.isSubmitting,
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator()
                        } else {
                            val label = if (state.isLastQuestion) {
                                AppStrings.placementSubmit
                            } else {
                                AppStrings.placementNext
                            }
                            Text(label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlacementOptionRow(
    option: PlacementOption,
    selected: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                enabled = enabled,
                onClick = onSelect,
                role = Role.RadioButton,
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled,
        )
        Text(
            text = option.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
