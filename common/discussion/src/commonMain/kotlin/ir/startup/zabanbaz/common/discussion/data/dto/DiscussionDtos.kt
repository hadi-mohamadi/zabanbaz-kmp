package ir.startup.zabanbaz.common.discussion.data.dto

import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchStatus
import ir.startup.zabanbaz.common.discussion.domain.DiscussionPartner
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
) {
    fun toDomain(): DiscussionSession = DiscussionSession(
        sessionId = sessionId,
        status = status,
        partner = partner.toDomain(),
        learningLanguageName = learningLanguageName,
        englishCefrLevel = englishCefrLevel,
        createdAt = createdAt,
    )
}
