package ir.startup.zabanbaz.common.discussion.domain

class GetDiscussionMatchStatusUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(): DiscussionMatchState = repository.getMatchStatus()
}
