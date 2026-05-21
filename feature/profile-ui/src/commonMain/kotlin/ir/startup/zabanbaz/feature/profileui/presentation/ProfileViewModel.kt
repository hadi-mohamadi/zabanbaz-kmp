package ir.startup.zabanbaz.feature.profileui.presentation

import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UpdateProfileDetailsUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateProfileDetailsUseCase: UpdateProfileDetailsUseCase,
) : BaseViewModel<ProfileUiState>(ProfileUiState()) {

    fun onProfileRequested() {
        scope.launch {
            updateState { copy(isLoading = true) }
            safeCall(
                action = { getUserProfileUseCase() },
                onSuccess = { profile ->
                    updateState {
                        copy(
                            profile = profile,
                            firstName = profile.firstName.orEmpty(),
                            lastName = profile.lastName.orEmpty(),
                            ageText = profile.age?.toString().orEmpty(),
                            isLoading = false,
                        )
                    }
                },
                onError = {
                    updateState { copy(isLoading = false) }
                },
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

    fun onSave() {
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
                            profile = profile,
                            firstName = profile.firstName.orEmpty(),
                            lastName = profile.lastName.orEmpty(),
                            ageText = profile.age?.toString().orEmpty(),
                            isSaving = false,
                            saveSucceeded = true,
                        )
                    }
                },
                onError = {
                    updateState { copy(isSaving = false) }
                },
            )
        }
    }

    fun onSaveHandled() {
        updateState { copy(saveSucceeded = false) }
    }
}
