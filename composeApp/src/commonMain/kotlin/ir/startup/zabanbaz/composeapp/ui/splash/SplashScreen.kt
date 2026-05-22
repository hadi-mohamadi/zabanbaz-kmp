package ir.startup.zabanbaz.composeapp.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.components.AppLoadingIndicator
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.components.ZabanbazLogoMark
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.startup.presentation.StartupDestination
import ir.startup.zabanbaz.feature.startup.presentation.StartupViewModel
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private val SplashMinDisplayDuration = 4.seconds

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToPlacement: () -> Unit,
    onNavigateToHome: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: StartupViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contentAlpha = remember { Animatable(0f) }
    val contentScale = remember { Animatable(0.92f) }
    val splashStart = remember { TimeSource.Monotonic.markNow() }

    OperationErrorEffects(
        state = state,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
    )

    LaunchedEffect(state.destination) {
        val destination = state.destination
        if (destination == StartupDestination.Loading) return@LaunchedEffect

        val remaining = SplashMinDisplayDuration - splashStart.elapsedNow()
        if (remaining.isPositive()) {
            delay(remaining)
        }

        when (destination) {
            StartupDestination.Loading -> Unit
            StartupDestination.Login -> onNavigateToLogin()
            StartupDestination.Onboarding -> onNavigateToOnboarding()
            StartupDestination.Placement -> onNavigateToPlacement()
            StartupDestination.Home -> onNavigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        contentScale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
    }

    SplashBackground(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha.value),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(contentScale.value)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ZabanbazLogoMark()
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = AppStrings.splashTagline,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AppLoadingIndicator(modifier = Modifier.width(128.dp))
                Text(
                    text = AppStrings.splashLoading,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
