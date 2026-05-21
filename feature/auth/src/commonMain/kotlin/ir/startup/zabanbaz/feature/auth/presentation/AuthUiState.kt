package ir.startup.zabanbaz.feature.auth.presentation

import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

enum class AuthStep {
    EnterMobile,
    EnterCode,
}

enum class AuthStatus {
    Idle,
    Authenticated,
}

data class AuthUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val step: AuthStep = AuthStep.EnterMobile,
    val mobile: String = "",
    val code: String = "",
    val status: AuthStatus = AuthStatus.Idle,
    val isLoading: Boolean = false,
    val fieldError: String? = null,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
