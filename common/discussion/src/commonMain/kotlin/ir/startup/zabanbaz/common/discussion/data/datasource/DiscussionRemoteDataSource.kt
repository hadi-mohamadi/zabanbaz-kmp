package ir.startup.zabanbaz.common.discussion.data.datasource

import ir.startup.zabanbaz.common.discussion.data.dto.DiscussionConfigResponseDto
import ir.startup.zabanbaz.common.discussion.data.dto.DiscussionMatchResponseDto
import ir.startup.zabanbaz.common.discussion.data.dto.DiscussionSessionResponseDto
import ir.startup.zabanbaz.common.discussion.data.dto.PostSignalingEventRequestDto
import ir.startup.zabanbaz.common.discussion.data.dto.SignalingEventsResponseDto
import ir.startup.zabanbaz.common.discussion.data.dto.toJsonObject
import ir.startup.zabanbaz.common.discussion.domain.DiscussionConfig
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventType
import ir.startup.zabanbaz.common.discussion.domain.SignalingEventsPage
import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class DiscussionRemoteDataSource(
    private val apiService: ApiService,
) {
    suspend fun joinMatchQueue(): DiscussionMatchState =
        apiService.post(
            endpoint = "/discussions/match/",
            body = buildJsonObject { },
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionMatchResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun leaveMatchQueue() {
        apiService.delete(endpoint = "/discussions/match/")
    }

    suspend fun getMatchStatus(): DiscussionMatchState =
        apiService.get(endpoint = "/discussions/match/status/") { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionMatchResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun getSession(sessionId: Int): DiscussionSession =
        apiService.get(endpoint = "/discussions/sessions/$sessionId/") { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionSessionResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun endSession(sessionId: Int): DiscussionSession =
        apiService.post(
            endpoint = "/discussions/sessions/$sessionId/end/",
            body = buildJsonObject { },
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionSessionResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun getDiscussionConfig(): DiscussionConfig =
        apiService.get(endpoint = "/discussions/config/") { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionConfigResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun postSignalingEvent(
        sessionId: Int,
        type: SignalingEventType,
        payload: Map<String, String>,
    ) {
        val request = PostSignalingEventRequestDto(
            type = type.apiValue,
            payload = payload.toJsonObject(),
        )
        apiService.post(
            endpoint = "/discussions/sessions/$sessionId/signaling/",
            body = request,
        ) { _: JsonObject -> }
    }

    suspend fun getSignalingEvents(sessionId: Int, sinceId: Int): SignalingEventsPage =
        apiService.get(
            endpoint = "/discussions/sessions/$sessionId/signaling/",
            query = mapOf("since" to sinceId.toString()),
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(SignalingEventsResponseDto.serializer(), body)
                .toDomain()
        }
}
