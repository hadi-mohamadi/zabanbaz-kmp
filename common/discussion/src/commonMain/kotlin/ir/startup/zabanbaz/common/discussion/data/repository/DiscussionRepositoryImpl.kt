package ir.startup.zabanbaz.common.discussion.data.repository

import ir.startup.zabanbaz.common.discussion.data.datasource.DiscussionRemoteDataSource
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionRepository
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession

class DiscussionRepositoryImpl(
    private val remote: DiscussionRemoteDataSource,
) : DiscussionRepository {
    override suspend fun joinMatchQueue(): DiscussionMatchState = remote.joinMatchQueue()

    override suspend fun leaveMatchQueue() {
        remote.leaveMatchQueue()
    }

    override suspend fun getMatchStatus(): DiscussionMatchState = remote.getMatchStatus()

    override suspend fun endSession(sessionId: Int): DiscussionSession = remote.endSession(sessionId)
}
