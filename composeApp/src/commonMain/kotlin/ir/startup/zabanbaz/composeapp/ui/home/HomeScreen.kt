package ir.startup.zabanbaz.composeapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onLoggedOut: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onProfileRequested()
    }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.homeTitle) },
                actions = {
                    val profile = state.profile
                    if (profile != null) {
                        Box {
                            ProfileAvatarButton(
                                profile = profile,
                                onClick = { menuExpanded = true },
                            )
                            ProfileMenu(
                                expanded = menuExpanded,
                                onDismiss = { menuExpanded = false },
                                onProfile = {
                                    menuExpanded = false
                                    onNavigateToProfile()
                                },
                                onLogout = {
                                    menuExpanded = false
                                    viewModel.onLogout(onLoggedOut)
                                },
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                val profile = state.profile
                Text(
                    text = AppStrings.homeTitle,
                    style = MaterialTheme.typography.headlineMedium,
                )
                val greeting = profile?.username?.let { "@$it" } ?: profile?.phone
                greeting?.let { name ->
                    Text(
                        text = AppStrings.homeWelcome(name),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                profile?.englishCefrLevel?.let { level ->
                    Text(
                        text = AppStrings.homeCefrLevel(level),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                profile?.learningLanguageName?.let { language ->
                    Text(
                        text = AppStrings.homeLearningLanguage(language),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatarButton(
    profile: UserProfile,
    onClick: () -> Unit,
) {
    val initial = profile.username?.firstOrNull()?.uppercaseChar()
        ?: profile.phone.lastOrNull()
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial?.toString() ?: "?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun ProfileMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        DropdownMenuItem(
            text = { Text(AppStrings.homeMenuProfile) },
            onClick = onProfile,
        )
        DropdownMenuItem(
            text = { Text(AppStrings.homeMenuLogout) },
            onClick = onLogout,
        )
    }
}
