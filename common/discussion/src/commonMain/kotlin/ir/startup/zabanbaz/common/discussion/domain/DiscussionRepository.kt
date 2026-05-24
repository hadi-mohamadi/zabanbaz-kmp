package ir.startup.zabanbaz.common.discussion.domain

interface DiscussionRepository {
    suspend fun joinMatchQueue(): DiscussionMatchState
    suspend fun leaveMatchQueue()
    suspend fun getMatchStatus(): DiscussionMatchState
    suspend fun endSession(sessionId: Int): DiscussionSession
}
