package ir.startup.zabanbaz.composeapp.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.profileui.presentation.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onProfileRequested()
    }

    LaunchedEffect(state.saveSucceeded) {
        if (state.saveSucceeded) {
            viewModel.onSaveHandled()
            onBack()
        }
    }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.profileTitle) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(AppStrings.profileBack)
                    }
                },
            )
        },
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 48.dp))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                state.profile?.username?.let { username ->
                    Text(
                        text = AppStrings.profileUsername(username),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                state.profile?.phone?.let { phone ->
                    Text(
                        text = AppStrings.profilePhone(phone),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                state.profile?.learningLanguageName?.let { language ->
                    Text(
                        text = AppStrings.profileLanguage(language),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                state.profile?.englishCefrLevel?.let { level ->
                    Text(
                        text = AppStrings.profileCefr(level),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = AppStrings.profileDetailsSection,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = viewModel::onFirstNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(AppStrings.profileFirstName) },
                    singleLine = true,
                    enabled = !state.isSaving,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(AppStrings.profileLastName) },
                    singleLine = true,
                    enabled = !state.isSaving,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.ageText,
                    onValueChange = viewModel::onAgeChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(AppStrings.profileAge) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !state.isSaving,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = viewModel::onSave,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving,
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.height(20.dp))
                    } else {
                        Text(AppStrings.profileSave)
                    }
                }
            }
        }
    }
}
