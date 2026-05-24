package ir.startup.zabanbaz.common.discussion.data.datasource

import ir.startup.zabanbaz.common.discussion.data.dto.DiscussionMatchResponseDto
import ir.startup.zabanbaz.common.discussion.data.dto.DiscussionSessionResponseDto
import ir.startup.zabanbaz.common.discussion.domain.DiscussionMatchState
import ir.startup.zabanbaz.common.discussion.domain.DiscussionSession
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

    suspend fun endSession(sessionId: Int): DiscussionSession =
        apiService.post(
            endpoint = "/discussions/sessions/$sessionId/end/",
            body = buildJsonObject { },
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(DiscussionSessionResponseDto.serializer(), body)
                .toDomain()
        }
}
