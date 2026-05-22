package ir.startup.zabanbaz.common.profile.data.datasource

import ir.startup.zabanbaz.common.profile.data.dto.ProfileDto
import ir.startup.zabanbaz.common.profile.data.dto.UpdateProfileDetailsRequestDto
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

    suspend fun patchCoreProfile(
        sex: String? = null,
        learningLanguageId: Int? = null,
        username: String? = null,
    ): UserProfile {
        val body = buildMap<String, Any> {
            sex?.let { put("sex", it) }
            learningLanguageId?.let { put("learning_language_id", it) }
            username?.trim()?.takeIf { it.isNotEmpty() }?.let { put("username", it) }
        }
        require(body.isNotEmpty()) { "At least one core profile field is required" }

        apiService.patch(
            endpoint = "/profile/core/",
            body = body,
        ) { _: JsonObject -> }

        return getProfile()
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
        apiService.patch(
            endpoint = "/profile/details/",
            body = dto,
        ) { _: JsonObject -> }

        return getProfile()
    }
}
