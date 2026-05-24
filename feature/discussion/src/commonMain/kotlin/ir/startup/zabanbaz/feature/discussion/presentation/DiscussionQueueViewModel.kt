package ir.startup.zabanbaz.feature.discussion.presentation

import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchStatus
import ir.startup.zabanbaz.common.discussion.domain.EndDiscussionSessionUseCase
import ir.startup.zabanbaz.common.discussion.domain.GetDiscussionMatchStatusUseCase
import ir.startup.zabanbaz.common.discussion.domain.JoinDiscussionMatchUseCase
import ir.startup.zabanbaz.common.discussion.domain.LeaveDiscussionMatchUseCase
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DiscussionQueueViewModel(
    private val joinDiscussionMatchUseCase: JoinDiscussionMatchUseCase,
    private val leaveDiscussionMatchUseCase: LeaveDiscussionMatchUseCase,
    private val getDiscussionMatchStatusUseCase: GetDiscussionMatchStatusUseCase,
    private val endDiscussionSessionUseCase: EndDiscussionSessionUseCase,
) : BaseViewModel<DiscussionQueueUiState>(DiscussionQueueUiState()) {

    private var pollingJob: Job? = null

    init {
        joinQueue()
    }

    fun joinQueue() {
        pollingJob?.cancel()
        scope.launch {
            updateState { copy(isLoading = true, fieldError = null) }
            safeCall(
                action = { joinDiscussionMatchUseCase() },
                onSuccess = { result ->
                    applyMatchState(result)
                    updateState { copy(isLoading = false) }
                    if (result.status == DiscussionMatchStatus.Queued) {
                        startPolling()
                    }
                },
                onError = {
                    updateState { copy(isLoading = false) }
                },
            )
        }
    }

    fun onLeave(onFinished: () -> Unit) {
        pollingJob?.cancel()
        scope.launch {
            updateState { copy(isLeaving = true) }
            safeCall(
                action = {
                    if (currentState.matchStatus == DiscussionMatchStatus.Matched) {
                        currentState.sessionId?.let { endDiscussionSessionUseCase(it) }
                    }
                    leaveDiscussionMatchUseCase()
                },
                onSuccess = {
                    updateState { copy(isLeaving = false) }
                    onFinished()
                },
                onError = {
                    updateState { copy(isLeaving = false) }
                    onFinished()
                },
            )
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            while (isActive && currentState.matchStatus == DiscussionMatchStatus.Queued) {
                delay(POLL_INTERVAL_MS)
                safeCall(
                    action = { getDiscussionMatchStatusUseCase() },
                    onSuccess = { result ->
                        applyMatchState(result)
                        if (result.status != DiscussionMatchStatus.Queued) {
                            pollingJob?.cancel()
                        }
                    },
                )
            }
        }
    }

    private fun applyMatchState(
        result: ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState,
    ) {
        updateState {
            copy(
                matchStatus = result.status,
                sessionId = result.sessionId,
                partnerDisplayName = result.partner?.displayName,
                learningLanguageName = result.learningLanguageName,
                englishCefrLevel = result.englishCefrLevel,
            )
        }
    }

    override fun onCleared() {
        pollingJob?.cancel()
        super.onCleared()
    }

    companion object {
        private const val POLL_INTERVAL_MS = 2_000L
    }
}
