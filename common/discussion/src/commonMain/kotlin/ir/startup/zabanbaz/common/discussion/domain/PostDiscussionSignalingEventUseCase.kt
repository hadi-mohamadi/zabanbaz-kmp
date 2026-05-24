package ir.startup.zabanbaz.common.discussion.domain

class PostDiscussionSignalingEventUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(
        sessionId: Int,
        type: SignalingEventType,
        payload: Map<String, String>,
    ) {
        repository.postSignalingEvent(sessionId, type, payload)
    }
}
