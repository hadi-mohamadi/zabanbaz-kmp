package ir.startup.zabanbaz.feature.discussion.presentation

import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

enum class DiscussionCallPhase {
    Loading,
    Connecting,
    SignalingComplete,
    PartnerLeft,
}

data class DiscussionCallUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val phase: DiscussionCallPhase = DiscussionCallPhase.Loading,
    val partnerDisplayName: String? = null,
    val statusMessage: String = "",
    val isLeaving: Boolean = false,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
