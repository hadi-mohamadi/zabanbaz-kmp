package ir.startup.zabanbaz.common.discussion.domain

class GetDiscussionSessionUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(sessionId: Int): DiscussionSession =
        repository.getSession(sessionId)
}
