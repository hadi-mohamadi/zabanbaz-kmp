package ir.startup.zabanbaz.feature.home.presentation

import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.common.profile.domain.UpdateProfileDetailsUseCase
import ir.startup.zabanbaz.common.session.domain.LogoutUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateProfileDetailsUseCase: UpdateProfileDetailsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    fun onProfileRequested() {
        scope.launch {
            updateState { copy(isLoading = true) }
            safeCall(
                action = { getUserProfileUseCase() },
                onSuccess = { profile -> applyProfile(profile, isLoading = false) },
                onError = { updateState { copy(isLoading = false) } },
            )
        }
    }

    fun onEditProfile() {
        val profile = currentState.profile ?: return
        updateState {
            copy(
                isEditing = true,
                firstName = profile.firstName.orEmpty(),
                lastName = profile.lastName.orEmpty(),
                ageText = profile.age?.toString().orEmpty(),
                saveSucceeded = false,
            )
        }
    }

    fun onCancelEdit() {
        val profile = currentState.profile
        updateState {
            copy(
                isEditing = false,
                firstName = profile?.firstName.orEmpty(),
                lastName = profile?.lastName.orEmpty(),
                ageText = profile?.age?.toString().orEmpty(),
                saveSucceeded = false,
            )
        }
    }

    fun onFirstNameChanged(value: String) {
        updateState { copy(firstName = value, saveSucceeded = false) }
    }

    fun onLastNameChanged(value: String) {
        updateState { copy(lastName = value, saveSucceeded = false) }
    }

    fun onAgeChanged(value: String) {
        updateState { copy(ageText = value.filter { it.isDigit() }, saveSucceeded = false) }
    }

    fun onSaveProfile() {
        val age = currentState.ageText.toIntOrNull()
        scope.launch {
            updateState { copy(isSaving = true) }
            safeCall(
                action = {
                    updateProfileDetailsUseCase(
                        firstName = currentState.firstName,
                        lastName = currentState.lastName,
                        age = age,
                    )
                },
                onSuccess = { profile ->
                    updateState {
                        copy(
                            isSaving = false,
                            isEditing = false,
                            saveSucceeded = true,
                        )
                    }
                    applyProfile(profile, isLoading = false)
                },
                onError = { updateState { copy(isSaving = false) } },
            )
        }
    }

    fun onSaveAcknowledged() {
        updateState { copy(saveSucceeded = false) }
    }

    fun onLogout(onLoggedOut: () -> Unit) {
        scope.launch {
            safeCall(
                action = { logoutUseCase() },
                onSuccess = {
                    updateState { HomeUiState(isLoading = false) }
                    onLoggedOut()
                },
            )
        }
    }

    private fun applyProfile(profile: UserProfile, isLoading: Boolean) {
        val editing = currentState.isEditing
        updateState {
            copy(
                profile = profile,
                isLoading = isLoading,
                firstName = if (editing) firstName else profile.firstName.orEmpty(),
                lastName = if (editing) lastName else profile.lastName.orEmpty(),
                ageText = if (editing) ageText else profile.age?.toString().orEmpty(),
            )
        }
    }
}
