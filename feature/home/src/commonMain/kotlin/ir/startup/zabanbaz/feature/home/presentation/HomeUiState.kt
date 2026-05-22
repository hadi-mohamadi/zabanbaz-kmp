package ir.startup.zabanbaz.feature.home.presentation

import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

data class HomeUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = true,
    val isEditing: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val ageText: String = "",
    val isSaving: Boolean = false,
    val saveSucceeded: Boolean = false,
    override val operationError: AppOperationError = AppOperationError.None,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
