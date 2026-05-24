package ir.startup.zabanbaz.common.discussion.domain

class GetDiscussionSignalingEventsUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(sessionId: Int, sinceId: Int): SignalingEventsPage =
        repository.getSignalingEvents(sessionId, sinceId)
}
