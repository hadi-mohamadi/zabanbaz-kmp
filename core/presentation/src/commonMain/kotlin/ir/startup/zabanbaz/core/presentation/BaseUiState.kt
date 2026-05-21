package ir.startup.zabanbaz.core.presentation

interface OperationErrorState {
    fun copyWithOperationError(operationError: AppOperationError): BaseUiState
}

abstract class BaseUiState(
    open val operationError: AppOperationError = AppOperationError.None,
)

class InitialUiState(
    override val operationError: AppOperationError = AppOperationError.None,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState {
        return InitialUiState(operationError = operationError)
    }
}
