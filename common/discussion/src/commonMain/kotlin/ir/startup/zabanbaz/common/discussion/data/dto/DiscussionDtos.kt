package ir.startup.zabanbaz.common.discussion.data.dto

import ir.startup.zabanbaz.common.discussion.domain.DiscussionConfig
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchStatus
import ir.startup.zabanbaz.common.discussion.domain.DiscussionPartner
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
import ir.startup.zabanbaz.common.discussion.domain.IceServer
import ir.startup.zabanbaz.common.discussion.domain.SignalingEvent
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventType
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventsPage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class DiscussionPartnerDto(
    @SerialName("user_id") val userId: Int,
    @SerialName("display_name") val displayName: String,
) {
    fun toDomain(): DiscussionPartner = DiscussionPartner(
        userId = userId,
        displayName = displayName,
    )
}

@Serializable
data class DiscussionMatchResponseDto(
    val status: String,
    @SerialName("session_id") val sessionId: Int? = null,
    val partner: DiscussionPartnerDto? = null,
    @SerialName("learning_language_name") val learningLanguageName: String? = null,
    @SerialName("english_cefr_level") val englishCefrLevel: String? = null,
) {
    fun toDomain(): DiscussionMatchState = DiscussionMatchState(
        status = when (status) {
            "queued" -> DiscussionMatchStatus.Queued
            "matched" -> DiscussionMatchStatus.Matched
            else -> DiscussionMatchStatus.Idle
        },
        sessionId = sessionId,
        partner = partner?.toDomain(),
        learningLanguageName = learningLanguageName,
        englishCefrLevel = englishCefrLevel,
    )
}

@Serializable
data class DiscussionSessionResponseDto(
    @SerialName("session_id") val sessionId: Int,
    val status: String,
    val partner: DiscussionPartnerDto,
    @SerialName("learning_language_name") val learningLanguageName: String,
    @SerialName("english_cefr_level") val englishCefrLevel: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_initiator") val isInitiator: Boolean = false,
) {
    fun toDomain(): DiscussionSession = DiscussionSession(
        sessionId = sessionId,
        status = status,
        partner = partner.toDomain(),
        learningLanguageName = learningLanguageName,
        englishCefrLevel = englishCefrLevel,
        createdAt = createdAt,
        isInitiator = isInitiator,
    )
}

@Serializable
data class IceServerDto(
    val urls: List<String>,
    val username: String? = null,
    val credential: String? = null,
) {
    fun toDomain(): IceServer = IceServer(
        urls = urls,
        username = username,
        credential = credential,
    )
}

@Serializable
data class DiscussionConfigResponseDto(
    @SerialName("ice_servers") val iceServers: List<IceServerDto>,
    @SerialName("signaling_poll_interval_ms") val signalingPollIntervalMs: Long,
) {
    fun toDomain(): DiscussionConfig = DiscussionConfig(
        iceServers = iceServers.map { it.toDomain() },
        signalingPollIntervalMs = signalingPollIntervalMs,
    )
}

@Serializable
data class SignalingEventDto(
    val id: Int,
    val type: String,
    val payload: JsonObject,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("created_at") val createdAt: String,
) {
    fun toDomain(): SignalingEvent? {
        val eventType = SignalingEventType.fromApi(type) ?: return null
        return SignalingEvent(
            id = id,
            type = eventType,
            payload = payload.toStringMap(),
            senderId = senderId,
            createdAt = createdAt,
        )
    }
}

@Serializable
data class SignalingEventsResponseDto(
    val events: List<SignalingEventDto>,
    @SerialName("latest_id") val latestId: Int,
) {
    fun toDomain(): SignalingEventsPage = SignalingEventsPage(
        events = events.mapNotNull { it.toDomain() },
        latestId = latestId,
    )
}

@Serializable
data class PostSignalingEventRequestDto(
    val type: String,
    val payload: JsonObject,
)

fun Map<String, String>.toJsonObject(): JsonObject = buildJsonObject {
    forEach { (key, value) -> put(key, value) }
}

private fun JsonObject.toStringMap(): Map<String, String> =
    mapValues { (_, value) ->
        when (value) {
            is JsonPrimitive -> value.content
            else -> value.toString()
        }
    }

fun sessionDescriptionPayload(type: String, sdp: String): JsonObject = buildJsonObject {
    put("type", type)
    put("sdp", sdp)
}
