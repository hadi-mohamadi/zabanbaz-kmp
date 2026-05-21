package ir.startup.zabanbaz.feature.home.presentation

import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.session.domain.LogoutUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    fun onProfileRequested() {
        scope.launch {
            updateState { copy(isLoading = true) }
            safeCall(
                action = { getUserProfileUseCase() },
                onSuccess = { profile ->
                    updateState { copy(profile = profile, isLoading = false) }
                },
                onError = {
                    updateState { copy(isLoading = false) }
                },
            )
        }
    }

    fun onLogout(onLoggedOut: () -> Unit) {
        scope.launch {
            safeCall(
                action = { logoutUseCase() },
                onSuccess = {
                    updateState { copy(profile = null, isLoading = false) }
                    onLoggedOut()
                },
            )
        }
    }
}
