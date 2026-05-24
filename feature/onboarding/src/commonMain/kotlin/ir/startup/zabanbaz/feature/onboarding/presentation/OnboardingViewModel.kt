package ir.startup.zabanbaz.feature.onboarding.presentation

import ir.startup.zabanbaz.common.languages.domain.GetLanguagesUseCase
import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UpdateCoreProfileUseCase
import ir.startup.zabanbaz.core.errors.ClientErrorException
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getLanguagesUseCase: GetLanguagesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateCoreProfileUseCase: UpdateCoreProfileUseCase,
) : BaseViewModel<OnboardingUiState>(OnboardingUiState()) {

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        scope.launch {
            safeCall(
                action = {
                    updateState { copy(isLoadingLanguages = true, fieldError = null) }
                    val languages = getLanguagesUseCase()
                    val profile = runCatching { getUserProfileUseCase() }.getOrNull()
                    languages to profile
                },
                onSuccess = { (languages, profile) ->
                    val sex = profile?.sex?.let { value ->
                        when (value.lowercase()) {
                            "male" -> OnboardingSex.Male
                            "female" -> OnboardingSex.Female
                            else -> null
                        }
                    }
                    updateState {
                        copy(
                            languages = languages,
                            isLoadingLanguages = false,
                            selectedSex = sex,
                            selectedLanguageId = profile?.learningLanguageId,
                            username = profile?.username.orEmpty(),
                            fieldError = null,
                        )
                    }
                },
                onError = {
                    updateState {
                        copy(
                            isLoadingLanguages = false,
                            operationError = AppOperationError.None,
                        )
                    }
                },
            )
        }
    }

    fun onSexSelected(sex: OnboardingSex) {
        updateState { copy(selectedSex = sex, fieldError = null) }
    }

    fun onLanguageSelected(languageId: Int) {
        updateState { copy(selectedLanguageId = languageId, fieldError = null) }
    }

    fun onUsernameChanged(username: String) {
        updateState { copy(username = username, fieldError = null) }
    }

    fun onSubmit() {
        val sex = currentState.selectedSex
        val languageId = currentState.selectedLanguageId
        val username = currentState.username.trim()

        when {
            sex == null -> {
                updateState { copy(fieldError = "Please select your sex") }
                return
            }
            languageId == null -> {
                updateState { copy(fieldError = "Please select a language to learn") }
                return
            }
            !DISPLAY_NAME_REGEX.matches(username) -> {
                updateState {
                    copy(
                        fieldError = "Display name must be 2–32 characters (letters, numbers, spaces, _ -)",
                    )
                }
                return
            }
        }

        scope.launch {
            updateState { copy(isSubmitting = true, fieldError = null) }
            safeCall(
                action = {
                    updateCoreProfileUseCase(
                        sex = sex.apiValue,
                        learningLanguageId = languageId,
                        username = username,
                    )
                },
                onSuccess = {
                    updateState {
                        copy(
                            isSubmitting = false,
                            isComplete = true,
                            fieldError = null,
                        )
                    }
                },
                onError = { error ->
                    updateState {
                        copy(
                            isSubmitting = false,
                            fieldError = clientMessage(error),
                            operationError = AppOperationError.None,
                        )
                    }
                },
            )
            if (currentState.isSubmitting) {
                updateState { copy(isSubmitting = false) }
            }
        }
    }

    private fun clientMessage(error: Throwable): String =
        when (error) {
            is ClientErrorException -> error.message ?: "Request failed"
            else -> error.message ?: "Something went wrong"
        }

    private val OnboardingSex.apiValue: String
        get() = when (this) {
            OnboardingSex.Male -> "male"
            OnboardingSex.Female -> "female"
        }

    companion object {
        private val DISPLAY_NAME_REGEX = Regex("^[A-Za-z0-9_ \\-]{2,32}$")
    }
}
