package ir.startup.zabanbaz.feature.placement.presentation

import ir.startup.zabanbaz.common.placement.domain.PlacementQuestion
import ir.startup.zabanbaz.common.placement.domain.PlacementResult
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.OperationErrorState

data class PlacementUiState(
    override val operationError: AppOperationError = AppOperationError.None,
    val isLoading: Boolean = true,
    val sessionId: Int? = null,
    val questions: List<PlacementQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val answersByItemId: Map<Int, Int> = emptyMap(),
    val isSubmitting: Boolean = false,
    val result: PlacementResult? = null,
    val fieldError: String? = null,
) : BaseUiState(operationError), OperationErrorState {
    val currentQuestion: PlacementQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val isOnResultScreen: Boolean
        get() = result != null

    val canGoNext: Boolean
        get() {
            val question = currentQuestion ?: return false
            return answersByItemId.containsKey(question.itemId)
        }

    val isLastQuestion: Boolean
        get() = questions.isNotEmpty() && currentQuestionIndex == questions.lastIndex

    override fun copyWithOperationError(operationError: AppOperationError): BaseUiState =
        copy(operationError = operationError)
}
