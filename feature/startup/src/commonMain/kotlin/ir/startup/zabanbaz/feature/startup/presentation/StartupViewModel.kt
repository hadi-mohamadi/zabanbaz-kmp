package ir.startup.zabanbaz.feature.startup.presentation

import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.session.domain.CheckSessionUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class StartupViewModel(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : BaseViewModel<StartupUiState>(StartupUiState()) {

    init {
        resolveDestination()
    }

    fun resolveDestination() {
        scope.launch {
            if (!checkSessionUseCase()) {
                setState(currentState.copy(destination = StartupDestination.Login))
                return@launch
            }

            safeCall(
                action = { getUserProfileUseCase() },
                onSuccess = { profile ->
                    setState(
                        currentState.copy(
                            destination = when {
                                !profile.isCoreComplete -> StartupDestination.Onboarding
                                profile.needsEnglishPlacement -> StartupDestination.Placement
                                else -> StartupDestination.Home
                            },
                        ),
                    )
                },
            )
        }
    }
}
