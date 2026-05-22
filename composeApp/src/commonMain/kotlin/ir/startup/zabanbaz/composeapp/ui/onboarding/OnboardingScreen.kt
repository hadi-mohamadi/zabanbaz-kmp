package ir.startup.zabanbaz.composeapp.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.onboarding.presentation.OnboardingSex
import ir.startup.zabanbaz.feature.onboarding.presentation.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onNavigateToPlacement: () -> Unit,
    onNavigateToHome: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isComplete, state.needsPlacementAfterComplete) {
        if (state.isComplete) {
            if (state.needsPlacementAfterComplete) {
                onNavigateToPlacement()
            } else {
                onNavigateToHome()
            }
        }
    }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Text(
            text = AppStrings.onboardingTitle,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = AppStrings.onboardingSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
        )

        Text(
            text = AppStrings.onboardingSexLabel,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            FilterChip(
                selected = state.selectedSex == OnboardingSex.Male,
                onClick = { viewModel.onSexSelected(OnboardingSex.Male) },
                label = { Text(AppStrings.onboardingSexMale) },
                modifier = Modifier.padding(end = 8.dp),
            )
            FilterChip(
                selected = state.selectedSex == OnboardingSex.Female,
                onClick = { viewModel.onSexSelected(OnboardingSex.Female) },
                label = { Text(AppStrings.onboardingSexFemale) },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = AppStrings.onboardingLanguageLabel,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoadingLanguages) {
            CircularProgressIndicator()
        } else {
            var languageMenuExpanded by remember { mutableStateOf(false) }
            val selectedLanguage = state.languages.find { it.id == state.selectedLanguageId }

            ExposedDropdownMenuBox(
                expanded = languageMenuExpanded,
                onExpandedChange = { languageMenuExpanded = !languageMenuExpanded },
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = selectedLanguage?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = !state.isSubmitting,
                        ),
                    label = { Text(AppStrings.onboardingLanguageHint) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageMenuExpanded)
                    },
                    enabled = !state.isSubmitting,
                )
                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false },
                ) {
                    state.languages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.name) },
                            onClick = {
                                viewModel.onLanguageSelected(language.id)
                                languageMenuExpanded = false
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(AppStrings.onboardingUsernameHint) },
            singleLine = true,
            enabled = !state.isSubmitting && !state.isLoadingLanguages,
        )

        state.fieldError?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSubmitting && !state.isLoadingLanguages,
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator()
            } else {
                Text(AppStrings.onboardingContinue)
            }
        }
    }
}
