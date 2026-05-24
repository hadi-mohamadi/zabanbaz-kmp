package ir.startup.zabanbaz.common.discussion.domain

class LeaveDiscussionMatchUseCase(
    private val repository: DiscussionRepository,
) {
    suspend operator fun invoke() {
        repository.leaveMatchQueue()
    }
}
