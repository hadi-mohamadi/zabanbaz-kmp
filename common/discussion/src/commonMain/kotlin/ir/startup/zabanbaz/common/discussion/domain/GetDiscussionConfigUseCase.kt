package ir.startup.zabanbaz.common.discussion.domain

class GetDiscussionConfigUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke(): DiscussionConfig = repository.getDiscussionConfig()
}
