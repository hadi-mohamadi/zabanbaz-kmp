package ir.startup.zabanbaz.feature.profileui.presentation

import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.common.profile.domain.UpdateCoreProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UpdateProfileDetailsUseCase
import ir.startup.zabanbaz.core.errors.ClientErrorException
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateCoreProfileUseCase: UpdateCoreProfileUseCase,
    private val updateProfileDetailsUseCase: UpdateProfileDetailsUseCase,
) : BaseViewModel<ProfileUiState>(ProfileUiState()) {

    fun onProfileRequested() {
        scope.launch {
            updateState { copy(isLoading = true, fieldError = null) }
            safeCall(
                action = { getUserProfileUseCase() },
                onSuccess = { profile -> applyProfile(profile, isLoading = false) },
                onError = { updateState { copy(isLoading = false) } },
            )
        }
    }

    fun onUsernameChanged(value: String) {
        updateState { copy(username = value, fieldError = null, saveSucceeded = false) }
    }

    fun onFirstNameChanged(value: String) {
        updateState { copy(firstName = value, fieldError = null, saveSucceeded = false) }
    }

    fun onLastNameChanged(value: String) {
        updateState { copy(lastName = value, fieldError = null, saveSucceeded = false) }
    }

    fun onAgeChanged(value: String) {
        updateState { copy(ageText = value.filter { it.isDigit() }, fieldError = null, saveSucceeded = false) }
    }

    fun onSave() {
        val profile = currentState.profile ?: return
        val username = currentState.username.trim()
        val previousUsername = profile.username.orEmpty().trim()
        val usernameChanged = username != previousUsername

        if (usernameChanged && !DISPLAY_NAME_REGEX.matches(username)) {
            updateState {
                copy(
                    fieldError = "Display name must be 2–32 characters (letters, numbers, spaces, _ -)",
                )
            }
            return
        }

        val age = currentState.ageText.toIntOrNull()
        scope.launch {
            updateState { copy(isSaving = true, fieldError = null) }
            safeCall(
                action = {
                    if (usernameChanged) {
                        updateCoreProfileUseCase.patch(username = username)
                    }
                    updateProfileDetailsUseCase(
                        firstName = currentState.firstName,
                        lastName = currentState.lastName,
                        age = age,
                    )
                },
                onSuccess = { updated ->
                    updateState {
                        copy(
                            isSaving = false,
                            saveSucceeded = true,
                        )
                    }
                    applyProfile(updated, isLoading = false)
                },
                onError = { error ->
                    updateState {
                        copy(
                            isSaving = false,
                            fieldError = clientMessage(error),
                            operationError = AppOperationError.None,
                        )
                    }
                },
            )
        }
    }

    fun onSaveHandled() {
        updateState { copy(saveSucceeded = false) }
    }

    private fun applyProfile(profile: UserProfile, isLoading: Boolean) {
        updateState {
            copy(
                profile = profile,
                username = profile.username.orEmpty(),
                firstName = profile.firstName.orEmpty(),
                lastName = profile.lastName.orEmpty(),
                ageText = profile.age?.toString().orEmpty(),
                isLoading = isLoading,
            )
        }
    }

    private fun clientMessage(error: Throwable): String =
        when (error) {
            is ClientErrorException -> error.message ?: "Request failed"
            else -> error.message ?: "Something went wrong"
        }

    companion object {
        private val DISPLAY_NAME_REGEX = Regex("^[A-Za-z0-9_ \\-]{2,32}$")
    }
}
