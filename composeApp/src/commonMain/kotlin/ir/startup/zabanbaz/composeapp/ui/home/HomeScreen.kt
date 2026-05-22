package ir.startup.zabanbaz.composeapp.ui.home

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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.composeapp.components.AppPrimaryButton
import ir.startup.zabanbaz.composeapp.components.AppSecondaryButton
import ir.startup.zabanbaz.composeapp.components.AppTextField
import ir.startup.zabanbaz.composeapp.components.AuthFormCard
import ir.startup.zabanbaz.composeapp.components.InfoRow
import ir.startup.zabanbaz.composeapp.components.ProfileAvatar
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onRetakePlacement: () -> Unit,
    onLoggedOut: () -> Unit,
    snackbarHostState: SnackbarHostState,
    refreshNonce: Int = 0,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contentAlpha = remember { Animatable(0f) }
    val scrollState = rememberScrollState()

    LaunchedEffect(refreshNonce) {
        viewModel.onProfileRequested()
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(state.saveSucceeded) {
        if (state.saveSucceeded) {
            snackbarHostState.showSnackbar(AppStrings.homeProfileSaved)
            viewModel.onSaveAcknowledged()
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
            HomeTopBar(
                onLogout = { viewModel.onLogout(onLoggedOut) },
                enabled = !state.isSaving,
            )

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
                ) {
                    if (profile != null) {
                        HomeProfileHeader(profile = profile)
                        Spacer(modifier = Modifier.height(20.dp))

                        AuthFormCard {
                            HomeAccountSection(
                                profile = profile,
                                isEditing = state.isEditing,
                                firstName = state.firstName,
                                lastName = state.lastName,
                                ageText = state.ageText,
                                isSaving = state.isSaving,
                                onEdit = viewModel::onEditProfile,
                                onCancel = viewModel::onCancelEdit,
                                onSave = viewModel::onSaveProfile,
                                onFirstNameChanged = viewModel::onFirstNameChanged,
                                onLastNameChanged = viewModel::onLastNameChanged,
                                onAgeChanged = viewModel::onAgeChanged,
                            )
                        }

                        if (profile.canRetakeEnglishPlacement) {
                            Spacer(modifier = Modifier.height(16.dp))
                            AuthFormCard {
                                HomePlacementSection(
                                    level = profile.englishCefrLevel,
                                    onRetake = onRetakePlacement,
                                    enabled = !state.isEditing && !state.isSaving,
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
private fun HomeTopBar(
    onLogout: () -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = AppStrings.appName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        TextButton(onClick = onLogout, enabled = enabled) {
            Text(
                text = AppStrings.homeLogout,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun HomeProfileHeader(profile: UserProfile) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileAvatar(profile = profile, size = 80.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = profile.fullName?.let { AppStrings.homeWelcome(it) }
                ?: profile.username?.let { AppStrings.homeWelcome("@$it") }
                ?: AppStrings.homeTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = AppStrings.homeSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        profile.englishCefrLevel?.let { level ->
            Spacer(modifier = Modifier.height(16.dp))
            CefrLevelBadge(level = level)
        }
    }
}

@Composable
private fun CefrLevelBadge(level: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Text(
            text = AppStrings.homeCefrBadge(level),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun HomeAccountSection(
    profile: UserProfile,
    isEditing: Boolean,
    firstName: String,
    lastName: String,
    ageText: String,
    isSaving: Boolean,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onAgeChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = AppStrings.homeAccountSection,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (!isEditing) {
            TextButton(onClick = onEdit, enabled = !isSaving) {
                Text(AppStrings.homeEditProfile)
            }
        }
    }

    if (isEditing) {
        Spacer(modifier = Modifier.height(16.dp))
        AppTextField(
            value = firstName,
            onValueChange = onFirstNameChanged,
            label = AppStrings.profileFirstName,
            enabled = !isSaving,
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppTextField(
            value = lastName,
            onValueChange = onLastNameChanged,
            label = AppStrings.profileLastName,
            enabled = !isSaving,
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppTextField(
            value = ageText,
            onValueChange = onAgeChanged,
            label = AppStrings.profileAge,
            enabled = !isSaving,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(20.dp))
        AppPrimaryButton(
            text = AppStrings.homeSaveChanges,
            onClick = onSave,
            isLoading = isSaving,
        )
        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving,
        ) {
            Text(AppStrings.homeCancelEdit)
        }
    } else {
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(label = AppStrings.homeLabelPhone, value = profile.phone)
        profile.username?.let {
            InfoRow(label = AppStrings.homeLabelUsername, value = "@$it")
        }
        profile.learningLanguageName?.let {
            InfoRow(label = AppStrings.homeLabelLanguage, value = it)
        }
        profile.sex?.let { sex ->
            val label = when (sex.lowercase()) {
                "male", "m" -> AppStrings.onboardingSexMale
                "female", "f" -> AppStrings.onboardingSexFemale
                else -> sex.replaceFirstChar { it.uppercase() }
            }
            InfoRow(label = AppStrings.homeLabelSex, value = label)
        }
        val name = profile.fullName
        if (!name.isNullOrBlank()) {
            InfoRow(label = AppStrings.profileDetailsSection, value = name)
        }
        profile.age?.let {
            InfoRow(label = AppStrings.profileAge, value = it.toString())
        }
    }
}

@Composable
private fun HomePlacementSection(
    level: String?,
    onRetake: () -> Unit,
    enabled: Boolean,
) {
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
    Spacer(modifier = Modifier.height(12.dp))
    InfoRow(
        label = AppStrings.homeLabelLevel,
        value = level?.let { AppStrings.homeCefrBadge(it) } ?: AppStrings.homeLevelNotSet,
    )
    Spacer(modifier = Modifier.height(20.dp))
    AppSecondaryButton(
        text = AppStrings.homeRetakeExam,
        onClick = onRetake,
        enabled = enabled,
    )
}
