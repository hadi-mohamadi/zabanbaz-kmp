package ir.startup.zabanbaz.composeapp.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.components.AppPrimaryButton
import ir.startup.zabanbaz.composeapp.components.AppSecondaryButton
import ir.startup.zabanbaz.composeapp.components.AppTextField
import ir.startup.zabanbaz.composeapp.components.AuthFormCard
import ir.startup.zabanbaz.composeapp.components.InfoRow
import ir.startup.zabanbaz.composeapp.components.ProfileAvatar
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.profileui.presentation.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onRetakePlacement: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contentAlpha = remember { Animatable(0f) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.onProfileRequested()
        contentAlpha.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(state.saveSucceeded) {
        if (state.saveSucceeded) {
            snackbarHostState.showSnackbar(AppStrings.homeProfileSaved)
            viewModel.onSaveHandled()
            onBack()
        }
    }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    SplashBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha.value),
        ) {
            ProfileTopBar(onBack = onBack, enabled = !state.isSaving)

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                val profile = state.profile
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (profile != null) {
                        ProfileAvatar(profile = profile, size = 80.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = AppStrings.profileTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.profileSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        AuthFormCard {
                            Text(
                                text = AppStrings.homeAccountSection,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow(label = AppStrings.homeLabelPhone, value = profile.phone)
                            profile.learningLanguageName?.let {
                                InfoRow(label = AppStrings.homeLabelLanguage, value = it)
                            }
                            profile.englishCefrLevel?.let {
                                InfoRow(
                                    label = AppStrings.homeLabelLevel,
                                    value = AppStrings.homeCefrBadge(it),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        AuthFormCard {
                            Text(
                                text = AppStrings.profileDetailsSection,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = AppStrings.profileEditHint,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            AppTextField(
                                value = state.username,
                                onValueChange = viewModel::onUsernameChanged,
                                label = AppStrings.onboardingUsernameHint,
                                enabled = !state.isSaving,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(
                                value = state.firstName,
                                onValueChange = viewModel::onFirstNameChanged,
                                label = AppStrings.profileFirstName,
                                enabled = !state.isSaving,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(
                                value = state.lastName,
                                onValueChange = viewModel::onLastNameChanged,
                                label = AppStrings.profileLastName,
                                enabled = !state.isSaving,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(
                                value = state.ageText,
                                onValueChange = viewModel::onAgeChanged,
                                label = AppStrings.profileAge,
                                enabled = !state.isSaving,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            AppPrimaryButton(
                                text = AppStrings.profileSave,
                                onClick = viewModel::onSave,
                                isLoading = state.isSaving,
                            )
                            state.fieldError?.let { error ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        if (profile.canRetakeEnglishPlacement) {
                            Spacer(modifier = Modifier.height(16.dp))
                            AuthFormCard {
                                Text(
                                    text = AppStrings.homeLevelSection,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = AppStrings.homeRetakeExamSubtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                AppSecondaryButton(
                                    text = AppStrings.homeRetakeExam,
                                    onClick = onRetakePlacement,
                                    enabled = !state.isSaving,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileTopBar(
    onBack: () -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onBack, enabled = enabled) {
            Text(
                text = AppStrings.profileBack,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
