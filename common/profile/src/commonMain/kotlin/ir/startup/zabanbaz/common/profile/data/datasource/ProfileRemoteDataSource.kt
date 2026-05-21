package ir.startup.zabanbaz.common.profile.data.datasource

import ir.startup.zabanbaz.common.profile.data.dto.ProfileDto
import ir.startup.zabanbaz.common.profile.data.dto.UpdateCoreProfileRequestDto
import ir.startup.zabanbaz.common.profile.data.dto.UpdateProfileDetailsRequestDto
import ir.startup.zabanbaz.common.profile.data.dto.UpdateProfileDetailsResponseDto
import ir.startup.zabanbaz.common.profile.domain.UserProfile
import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class ProfileRemoteDataSource(
    private val apiService: ApiService,
) {
    suspend fun getProfile(): UserProfile =
        apiService.get(endpoint = "/profile/") { body: JsonObject ->
            HttpClientFactory.defaultJson
                .decodeFromJsonElement(ProfileDto.serializer(), body)
                .toDomain()
        }

    suspend fun updateCoreProfile(
        sex: String?,
        learningLanguageId: Int?,
        username: String?,
    ): UserProfile {
        val dto = UpdateCoreProfileRequestDto(
            sex = sex,
            learningLanguageId = learningLanguageId,
            username = username,
        )
        return apiService.patch(
            endpoint = "/profile/core/",
            body = dto,
        ) { body: JsonObject ->
            val partial = HttpClientFactory.defaultJson
                .decodeFromJsonElement(ProfileDto.serializer(), body)
            getProfile().copy(
                sex = partial.sex?.takeIf { it.isNotBlank() } ?: sex,
                learningLanguageId = partial.learningLanguageId ?: learningLanguageId,
                learningLanguageCode = partial.learningLanguageCode,
                learningLanguageName = partial.learningLanguageName,
                username = partial.username?.takeIf { it.isNotBlank() } ?: username,
            )
        }
    }

    suspend fun updateProfileDetails(
        firstName: String?,
        lastName: String?,
        age: Int?,
    ): UserProfile {
        val dto = UpdateProfileDetailsRequestDto(
            firstName = firstName,
            lastName = lastName,
            age = age,
        )
        return apiService.patch(
            endpoint = "/profile/details/",
            body = dto,
        ) { body: JsonObject ->
            val partial = HttpClientFactory.defaultJson
                .decodeFromJsonElement(UpdateProfileDetailsResponseDto.serializer(), body)
            getProfile().copy(
                firstName = partial.firstName?.takeIf { it.isNotBlank() } ?: firstName,
                lastName = partial.lastName?.takeIf { it.isNotBlank() } ?: lastName,
                age = partial.age ?: age,
            )
        }
    }
}
