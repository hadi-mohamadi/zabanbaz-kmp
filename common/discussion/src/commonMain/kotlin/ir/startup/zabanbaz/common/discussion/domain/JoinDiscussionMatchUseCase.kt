package ir.startup.zabanbaz.common.discussion.domain

class JoinDiscussionMatchUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(): DiscussionMatchState = repository.joinMatchQueue()
}
