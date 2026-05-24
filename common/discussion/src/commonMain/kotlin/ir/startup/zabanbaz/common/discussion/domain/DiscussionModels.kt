package ir.startup.zabanbaz.common.discussion.domain

enum class DiscussionMatchStatus {
    Idle,
    Queued,
    Matched,
}

enum class SignalingEventType(val apiValue: String) {
    Offer("offer"),
    Answer("answer"),
    Ice("ice"),
    Hangup("hangup"),
    ;

    companion object {
        fun fromApi(value: String): SignalingEventType? =
            entries.find { it.apiValue == value }
    }
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
    val isInitiator: Boolean,
)

data class IceServer(
    val urls: List<String>,
    val username: String? = null,
    val credential: String? = null,
)

data class DiscussionConfig(
    val iceServers: List<IceServer>,
    val signalingPollIntervalMs: Long,
)

data class SignalingEvent(
    val id: Int,
    val type: SignalingEventType,
    val payload: Map<String, String>,
    val senderId: Int,
    val createdAt: String,
)

data class SignalingEventsPage(
    val events: List<SignalingEvent>,
    val latestId: Int,
)
