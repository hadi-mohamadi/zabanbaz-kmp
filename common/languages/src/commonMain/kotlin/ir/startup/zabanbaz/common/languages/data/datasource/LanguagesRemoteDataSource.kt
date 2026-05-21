package ir.startup.zabanbaz.common.languages.data.datasource

import ir.startup.zabanbaz.common.languages.data.dto.LanguageDto
import ir.startup.zabanbaz.common.languages.domain.Language
import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement

class LanguagesRemoteDataSource(
    private val apiService: ApiService,
) {
    suspend fun getLanguages(): List<Language> =
        apiService.getJsonArray(
            endpoint = "/languages/",
            skipAuth = true,
        ) { array: JsonArray ->
            array.map { element ->
                HttpClientFactory.defaultJson
                    .decodeFromJsonElement(LanguageDto.serializer(), element)
                    .toDomain()
            }
        }
}
