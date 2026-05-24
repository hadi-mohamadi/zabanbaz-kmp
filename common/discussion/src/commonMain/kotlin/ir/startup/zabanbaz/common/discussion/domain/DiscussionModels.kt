package ir.startup.zabanbaz.common.discussion.domain

enum class DiscussionMatchStatus {
    Idle,
    Queued,
    Matched,
}

data class DiscussionPartner(
    val userId: Int,
    val displayName: String,
)

data class DiscussionMatchState(
    val status: DiscussionMatchStatus,
    val sessionId: Int? = null,
    val partner: DiscussionPartner? = null,
    val learningLanguageName: String? = null,
    val englishCefrLevel: String? = null,
)

data class DiscussionSession(
    val sessionId: Int,
    val status: String,
    val partner: DiscussionPartner,
    val learningLanguageName: String,
    val englishCefrLevel: String,
    val createdAt: String,
)
