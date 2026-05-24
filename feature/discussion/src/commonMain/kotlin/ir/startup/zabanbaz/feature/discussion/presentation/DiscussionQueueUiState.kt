package ir.startup.zabanbaz.feature.discussion.presentation

import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchStatus
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

data class DiscussionQueueUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val isLoading: Boolean = true,
    val matchStatus: DiscussionMatchStatus = DiscussionMatchStatus.Idle,
    val sessionId: Int? = null,
    val partnerDisplayName: String? = null,
    val learningLanguageName: String? = null,
    val englishCefrLevel: String? = null,
    val fieldError: String? = null,
    val isLeaving: Boolean = false,
) : BaseUiState(operationError), OperationErrorState {
    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
