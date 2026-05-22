package ir.startup.zabanbaz.composeapp.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.composeapp.components.AppSecondaryButton
import ir.startup.zabanbaz.composeapp.components.HomeStatCard
import ir.startup.zabanbaz.composeapp.components.ProfileAvatar
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onRetakePlacement: () -> Unit,
    onLoggedOut: () -> Unit,
    snackbarHostState: SnackbarHostState,
    refreshNonce: Int = 0,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contentAlpha = remember { Animatable(0f) }
    val scrollState = rememberScrollState()
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(refreshNonce) {
        viewModel.onProfileRequested()
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
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
            HomeTopBar()

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
                        Spacer(modifier = Modifier.height(8.dp))
                        Box {
                            ProfileAvatar(
                                profile = profile,
                                size = 72.dp,
                                modifier = Modifier.clickable { menuExpanded = true },
                            )
                            HomeProfileMenu(
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
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = profile.fullName?.let { AppStrings.homeWelcome(it) }
                                ?: profile.username?.let { AppStrings.homeWelcome("@$it") }
                                ?: AppStrings.homeGreeting,
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
                        Spacer(modifier = Modifier.height(28.dp))

                        HomeStatCard(
                            label = AppStrings.homeLabelLevel,
                            value = profile.englishCefrLevel?.let { AppStrings.homeCefrBadge(it) }
                                ?: AppStrings.homeLevelNotSet,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        profile.username?.let { username ->
                            HomeStatCard(
                                label = AppStrings.homeLabelUsername,
                                value = "@$username",
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        profile.learningLanguageName?.let { language ->
                            HomeStatCard(
                                label = AppStrings.homeLabelLanguage,
                                value = language,
                            )
                        }

                        if (profile.canRetakeEnglishPlacement) {
                            Spacer(modifier = Modifier.height(24.dp))
                            AppSecondaryButton(
                                text = AppStrings.homeRetakeExam,
                                onClick = onRetakePlacement,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Text(
        text = AppStrings.appName,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
    )
}

@Composable
private fun HomeProfileMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.TopEnd,
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
                text = { Text(AppStrings.homeLogout) },
                onClick = onLogout,
            )
        }
    }
}
