package ir.startup.zabanbaz.feature.discussion.presentation

import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
import ir.startup.zabanbaz.common.discussion.domain.EndDiscussionSessionUseCase
import ir.startup.zabanbaz.common.discussion.domain.GetDiscussionConfigUseCase
import ir.startup.zabanbaz.common.discussion.domain.GetDiscussionSessionUseCase
import ir.startup.zabanbaz.common.discussion.domain.GetDiscussionSignalingEventsUseCase
import ir.startup.zabanbaz.common.discussion.domain.PostDiscussionSignalingEventUseCase
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventType
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DiscussionCallViewModel(
    private val sessionId: Int,
    private val getDiscussionSessionUseCase: GetDiscussionSessionUseCase,
    private val getDiscussionConfigUseCase: GetDiscussionConfigUseCase,
    private val postSignalingEventUseCase: PostDiscussionSignalingEventUseCase,
    private val getSignalingEventsUseCase: GetDiscussionSignalingEventsUseCase,
    private val endDiscussionSessionUseCase: EndDiscussionSessionUseCase,
) : BaseViewModel<DiscussionCallUiState>(DiscussionCallUiState()) {

    private var signalingJob: Job? = null

    init {
        startCall()
    }

    fun startCall() {
        signalingJob?.cancel()
        scope.launch {
            updateState {
                copy(
                    phase = DiscussionCallPhase.Loading,
                    statusMessage = STATUS_LOADING,
                )
            }
            safeCall(
                action = {
                    val session = getDiscussionSessionUseCase(sessionId)
                    val config = getDiscussionConfigUseCase()
                    session to config
                },
                onSuccess = { (session, config) ->
                    updateState {
                        copy(
                            partnerDisplayName = session.partner.displayName,
                            phase = DiscussionCallPhase.Connecting,
                            statusMessage = STATUS_CONNECTING,
                        )
                    }
                    runSignaling(session, config.signalingPollIntervalMs)
                },
                onError = {
                    updateState { copy(statusMessage = STATUS_FAILED) }
                },
            )
        }
    }

    fun onLeave(onFinished: () -> Unit) {
        signalingJob?.cancel()
        scope.launch {
            updateState { copy(isLeaving = true) }
            safeCall(
                action = {
                    runCatching {
                        postSignalingEventUseCase(
                            sessionId = sessionId,
                            type = SignalingEventType.Hangup,
                            payload = emptyMap(),
                        )
                    }
                    endDiscussionSessionUseCase(sessionId)
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

    private fun runSignaling(session: DiscussionSession, pollIntervalMs: Long) {
        signalingJob?.cancel()
        signalingJob = scope.launch {
            var sinceId = 0
            var offerSent = false
            var answerSent = false
            var signalingComplete = false

            if (session.isInitiator) {
                postSignalingEventUseCase(
                    sessionId = sessionId,
                    type = SignalingEventType.Offer,
                    payload = mapOf(
                        "type" to "offer",
                        "sdp" to placeholderSdp(sessionId, "offer"),
                    ),
                )
                offerSent = true
                updateState { copy(statusMessage = STATUS_WAITING_ANSWER) }
            } else {
                updateState { copy(statusMessage = STATUS_WAITING_OFFER) }
            }

            while (isActive && !signalingComplete) {
                delay(pollIntervalMs)
                var partnerLeft = false
                safeCall(
                    action = { getSignalingEventsUseCase(sessionId, sinceId) },
                    onSuccess = { page ->
                        sinceId = page.latestId
                        for (event in page.events) {
                            when (event.type) {
                                SignalingEventType.Offer -> {
                                    if (!session.isInitiator && !answerSent) {
                                        postSignalingEventUseCase(
                                            sessionId = sessionId,
                                            type = SignalingEventType.Answer,
                                            payload = mapOf(
                                                "type" to "answer",
                                                "sdp" to placeholderSdp(sessionId, "answer"),
                                            ),
                                        )
                                        answerSent = true
                                        signalingComplete = true
                                    }
                                }

                                SignalingEventType.Answer -> {
                                    if (session.isInitiator && offerSent) {
                                        signalingComplete = true
                                    }
                                }

                                SignalingEventType.Hangup -> {
                                    updateState {
                                        copy(
                                            phase = DiscussionCallPhase.PartnerLeft,
                                            statusMessage = STATUS_PARTNER_LEFT,
                                        )
                                    }
                                    partnerLeft = true
                                }

                                SignalingEventType.Ice -> Unit
                            }
                            if (partnerLeft) break
                        }
                        if (signalingComplete) {
                            updateState {
                                copy(
                                    phase = DiscussionCallPhase.SignalingComplete,
                                    statusMessage = STATUS_SIGNALING_COMPLETE,
                                )
                            }
                        }
                    },
                )
                if (partnerLeft) break
            }
        }
    }

    override fun onCleared() {
        signalingJob?.cancel()
        super.onCleared()
    }

    private companion object {
        const val STATUS_LOADING = "Preparing call…"
        const val STATUS_CONNECTING = "Connecting to your partner…"
        const val STATUS_WAITING_OFFER = "Waiting for connection details…"
        const val STATUS_WAITING_ANSWER = "Waiting for your partner to respond…"
        const val STATUS_SIGNALING_COMPLETE = "Connected — video will start in the next update."
        const val STATUS_PARTNER_LEFT = "Your partner left the discussion."
        const val STATUS_FAILED = "Could not start the call."

        fun placeholderSdp(sessionId: Int, type: String): String =
            "v=0\r\no=- $sessionId 0 IN IP4 127.0.0.1\r\ns=Zabanbaz $type\r\nt=0 0\r\n"
    }
}
