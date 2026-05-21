package ir.startup.zabanbaz.common.profile.data.dto

import ir.startup.zabanbaz.common.profile.domain.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val phone: String = "",
    val sex: String? = null,
    @SerialName("learning_language_id") val learningLanguageId: Int? = null,
    @SerialName("learning_language_code") val learningLanguageCode: String? = null,
    @SerialName("learning_language_name") val learningLanguageName: String? = null,
    val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    val age: Int? = null,
    @SerialName("english_cefr_level") val englishCefrLevel: String? = null,
) {
    fun toDomain(): UserProfile = UserProfile(
        phone = phone,
        sex = sex?.takeIf { it.isNotBlank() },
        learningLanguageId = learningLanguageId,
        learningLanguageCode = learningLanguageCode?.takeIf { it.isNotBlank() },
        learningLanguageName = learningLanguageName?.takeIf { it.isNotBlank() },
        username = username?.takeIf { it.isNotBlank() },
        firstName = firstName?.takeIf { it.isNotBlank() },
        lastName = lastName?.takeIf { it.isNotBlank() },
        age = age,
        englishCefrLevel = englishCefrLevel?.takeIf { it.isNotBlank() },
    )
}

@Serializable
data class UpdateCoreProfileRequestDto(
    val sex: String? = null,
    @SerialName("learning_language_id") val learningLanguageId: Int? = null,
    val username: String? = null,
)

@Serializable
data class UpdateProfileDetailsRequestDto(
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    val age: Int? = null,
)

@Serializable
data class UpdateProfileDetailsResponseDto(
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    val age: Int? = null,
)
