package ir.startup.zabanbaz.common.discussion.data.repository

import ir.startup.zabanbaz.common.discussion.data.datasource.DiscussionRemoteDataSource
import ir.startup.zabanbaz.common.discussion.domain.DiscussionConfig
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionRepository
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventType
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventsPage

class DiscussionRepositoryImpl(
    private val remote: DiscussionRemoteDataSource,
) : DiscussionRepository {
    override suspend fun joinMatchQueue(): DiscussionMatchState = remote.joinMatchQueue()

    override suspend fun leaveMatchQueue() {
        remote.leaveMatchQueue()
    }

    override suspend fun getMatchStatus(): DiscussionMatchState = remote.getMatchStatus()

    override suspend fun getSession(sessionId: Int): DiscussionSession = remote.getSession(sessionId)

    override suspend fun endSession(sessionId: Int): DiscussionSession = remote.endSession(sessionId)

    override suspend fun getDiscussionConfig(): DiscussionConfig = remote.getDiscussionConfig()

    override suspend fun postSignalingEvent(
        sessionId: Int,
        type: SignalingEventType,
        payload: Map<String, String>,
    ) {
        remote.postSignalingEvent(sessionId, type, payload)
    }

    override suspend fun getSignalingEvents(sessionId: Int, sinceId: Int): SignalingEventsPage =
        remote.getSignalingEvents(sessionId, sinceId)
}
