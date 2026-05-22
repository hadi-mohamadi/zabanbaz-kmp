package ir.startup.zabanbaz.composeapp.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.startup.zabanbaz.composeapp.components.AppPrimaryButton
import ir.startup.zabanbaz.composeapp.components.AppTextField
import ir.startup.zabanbaz.composeapp.components.AuthFormCard
import ir.startup.zabanbaz.composeapp.components.AuthStepIndicator
import ir.startup.zabanbaz.composeapp.components.SplashBackground
import ir.startup.zabanbaz.composeapp.components.ZabanbazLogoMark
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.composeapp.presentation.OperationErrorEffects
import ir.startup.zabanbaz.feature.auth.presentation.AuthStatus
import ir.startup.zabanbaz.feature.auth.presentation.AuthStep
import ir.startup.zabanbaz.feature.auth.presentation.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onAuthenticated: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contentAlpha = remember { Animatable(0f) }
    val scrollState = rememberScrollState()

    LaunchedEffect(state.status) {
        if (state.status == AuthStatus.Authenticated) {
            onAuthenticated()
        }
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
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
                .alpha(contentAlpha.value)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ZabanbazLogoMark(size = 56.dp)
            Spacer(modifier = Modifier.height(24.dp))

            AuthFormCard {
                Text(
                    text = AppStrings.loginTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (state.step == AuthStep.EnterMobile) {
                        AppStrings.loginSubtitle
                    } else {
                        AppStrings.loginCodeSubtitle
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(20.dp))

                AuthStepIndicator(
                    currentStep = state.step,
                    mobileLabel = AppStrings.loginStepPhone,
                    codeLabel = AppStrings.loginStepCode,
                )
                Spacer(modifier = Modifier.height(24.dp))

                AnimatedContent(
                    targetState = state.step,
                    transitionSpec = {
                        if (targetState == AuthStep.EnterCode) {
                            (slideInHorizontally { it / 3 } + fadeIn()).togetherWith(
                                slideOutHorizontally { -it / 3 } + fadeOut(),
                            )
                        } else {
                            (slideInHorizontally { -it / 3 } + fadeIn()).togetherWith(
                                slideOutHorizontally { it / 3 } + fadeOut(),
                            )
                        }
                    },
                    label = "loginStep",
                ) { step ->
                    when (step) {
                        AuthStep.EnterMobile -> LoginMobileStep(
                            mobile = state.mobile,
                            isLoading = state.isLoading,
                            onMobileChanged = viewModel::onMobileChanged,
                            onRequestCode = viewModel::onRequestCode,
                        )
                        AuthStep.EnterCode -> LoginCodeStep(
                            mobile = state.mobile,
                            code = state.code,
                            isLoading = state.isLoading,
                            onCodeChanged = viewModel::onCodeChanged,
                            onVerifyCode = viewModel::onVerifyCode,
                            onBackToMobile = viewModel::onBackToMobile,
                        )
                    }
                }

                state.fieldError?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginMobileStep(
    mobile: String,
    isLoading: Boolean,
    onMobileChanged: (String) -> Unit,
    onRequestCode: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AppTextField(
            value = mobile,
            onValueChange = onMobileChanged,
            label = AppStrings.loginMobileHint,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        AppPrimaryButton(
            text = AppStrings.loginRequestCode,
            onClick = onRequestCode,
            isLoading = isLoading,
        )
    }
}

@Composable
private fun LoginCodeStep(
    mobile: String,
    code: String,
    isLoading: Boolean,
    onCodeChanged: (String) -> Unit,
    onVerifyCode: () -> Unit,
    onBackToMobile: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Text(
                text = mobile,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
        AppTextField(
            value = code,
            onValueChange = onCodeChanged,
            label = AppStrings.loginCodeHint,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        AppPrimaryButton(
            text = AppStrings.loginVerify,
            onClick = onVerifyCode,
            isLoading = isLoading,
        )
        TextButton(
            onClick = onBackToMobile,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        ) {
            Text(
                text = AppStrings.loginBack,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
