package ir.startup.zabanbaz.common.placement.data.datasource

import ir.startup.zabanbaz.common.placement.data.dto.PlacementAnswerDto
import ir.startup.zabanbaz.common.placement.data.dto.StartPlacementTestResponseDto
import ir.startup.zabanbaz.common.placement.data.dto.SubmitPlacementTestRequestDto
import ir.startup.zabanbaz.common.placement.data.dto.SubmitPlacementTestResponseDto
import ir.startup.zabanbaz.common.placement.data.dto.toDto
import ir.startup.zabanbaz.common.placement.domain.PlacementAnswer
import ir.startup.zabanbaz.common.placement.domain.PlacementResult
import ir.startup.zabanbaz.common.placement.domain.PlacementSession
import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class PlacementRemoteDataSource(
    private val apiService: ApiService,
) {
    suspend fun startTest(): PlacementSession =
        apiService.post(
            endpoint = "/placement/tests/",
            body = buildJsonObject { },
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(StartPlacementTestResponseDto.serializer(), body)
                .toDomain()
        }

    suspend fun submitTest(
        sessionId: Int,
        answers: List<PlacementAnswer>,
    ): PlacementResult {
        val request = SubmitPlacementTestRequestDto(
            answers = answers.map { it.toDto() },
        )
        return apiService.post(
            endpoint = "/placement/tests/$sessionId/submit/",
            body = request,
        ) { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(SubmitPlacementTestResponseDto.serializer(), body)
                .toDomain()
        }
    }

    suspend fun getTestResult(sessionId: Int): PlacementResult =
        apiService.get(endpoint = "/placement/tests/$sessionId/") { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(SubmitPlacementTestResponseDto.serializer(), body)
                .toDomain()
        }
}
