package ir.startup.zabanbaz.feature.onboarding.presentation

import ir.startup.zabanbaz.common.languages.domain.Language
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

enum class OnboardingSex {
    Male,
    Female,
}

data class OnboardingUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val languages: List<Language> = emptyList(),
    val isLoadingLanguages: Boolean = true,
    val selectedSex: OnboardingSex? = null,
    val selectedLanguageId: Int? = null,
    val username: String = "",
    val isSubmitting: Boolean = false,
    val fieldError: String? = null,
    val isComplete: Boolean = false,
) : BaseUiState(operationError), OperationErrorState {
    val selectedLanguageCode: String?
        get() = languages.find { it.id == selectedLanguageId }?.code

    val needsPlacementAfterComplete: Boolean
        get() = selectedLanguageCode.equals("en", ignoreCase = true)

    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
