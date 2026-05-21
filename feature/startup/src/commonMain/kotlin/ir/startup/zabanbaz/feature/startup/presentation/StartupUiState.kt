package ir.startup.zabanbaz.feature.startup.presentation

import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

enum class StartupDestination {
    Loading,
    Login,
    Onboarding,
    Placement,
    Home,
}

data class StartupUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val destination: StartupDestination = StartupDestination.Loading,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
