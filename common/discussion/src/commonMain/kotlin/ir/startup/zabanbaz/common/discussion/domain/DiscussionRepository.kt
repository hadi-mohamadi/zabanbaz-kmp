package ir.startup.zabanbaz.common.discussion.domain

interface DiscussionRepository {
    suspend fun joinMatchQueue(): DiscussionMatchState
    suspend fun leaveMatchQueue()
    suspend fun getMatchStatus(): DiscussionMatchState
    suspend fun getSession(sessionId: Int): DiscussionSession
    suspend fun endSession(sessionId: Int): DiscussionSession
    suspend fun getDiscussionConfig(): DiscussionConfig
    suspend fun postSignalingEvent(
        sessionId: Int,
        type: SignalingEventType,
        payload: Map<String, String>,
    )
    suspend fun getSignalingEvents(sessionId: Int, sinceId: Int): SignalingEventsPage
}
