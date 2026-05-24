package ir.startup.zabanbaz.common.discussion.domain

class EndDiscussionSessionUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(sessionId: Int): DiscussionSession = repository.endSession(sessionId)
}
