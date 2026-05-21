package ir.startup.zabanbaz.feature.placement.presentation

import ir.startup.zabanbaz.common.placement.domain.PlacementAnswer
import ir.startup.zabanbaz.common.placement.domain.StartPlacementTestUseCase
import ir.startup.zabanbaz.common.placement.domain.SubmitPlacementTestUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class PlacementViewModel(
    private val startPlacementTestUseCase: StartPlacementTestUseCase,
    private val submitPlacementTestUseCase: SubmitPlacementTestUseCase,
) : BaseViewModel<PlacementUiState>(PlacementUiState()) {

    init {
        loadTest()
    }

    fun loadTest() {
        scope.launch {
            updateState {
                copy(
                    isLoading = true,
                    fieldError = null,
                    result = null,
                    currentQuestionIndex = 0,
                    answersByItemId = emptyMap(),
                )
            }
            safeCall(
                action = { startPlacementTestUseCase() },
                onSuccess = { session ->
                    updateState {
                        copy(
                            isLoading = false,
                            sessionId = session.sessionId,
                            questions = session.questions,
                        )
                    }
                },
                onError = {
                    updateState { copy(isLoading = false) }
                },
            )
        }
    }

    fun onOptionSelected(optionId: Int) {
        val question = currentState.currentQuestion ?: return
        updateState {
            copy(
                answersByItemId = answersByItemId + (question.itemId to optionId),
                fieldError = null,
            )
        }
    }

    fun onPrevious() {
        if (currentState.currentQuestionIndex > 0) {
            updateState { copy(currentQuestionIndex = currentQuestionIndex - 1) }
        }
    }

    fun onNext() {
        if (!currentState.canGoNext) {
            updateState { copy(fieldError = "Please select an answer") }
            return
        }
        if (currentState.isLastQuestion) {
            submitAnswers()
        } else {
            updateState {
                copy(
                    currentQuestionIndex = currentQuestionIndex + 1,
                    fieldError = null,
                )
            }
        }
    }

    private fun submitAnswers() {
        val sessionId = currentState.sessionId ?: return
        val unanswered = currentState.questions.filter { question ->
            !currentState.answersByItemId.containsKey(question.itemId)
        }
        if (unanswered.isNotEmpty()) {
            updateState { copy(fieldError = "Please answer all questions") }
            return
        }

        val answers = currentState.questions.map { question ->
            PlacementAnswer(
                itemId = question.itemId,
                optionId = currentState.answersByItemId.getValue(question.itemId),
            )
        }

        scope.launch {
            updateState { copy(isSubmitting = true, fieldError = null) }
            safeCall(
                action = { submitPlacementTestUseCase(sessionId, answers) },
                onSuccess = { result ->
                    updateState {
                        copy(
                            isSubmitting = false,
                            result = result,
                            fieldError = null,
                        )
                    }
                },
                onError = {
                    updateState { copy(isSubmitting = false) }
                },
            )
        }
    }

    fun selectedOptionIdForCurrentQuestion(): Int? {
        val question = currentState.currentQuestion ?: return null
        return currentState.answersByItemId[question.itemId]
    }
}
