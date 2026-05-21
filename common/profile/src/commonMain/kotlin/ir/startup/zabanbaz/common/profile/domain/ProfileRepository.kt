package ir.startup.zabanbaz.common.profile.domain

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfile

    suspend fun updateCoreProfile(
        sex: String,
        learningLanguageId: Int,
        username: String,
    ): UserProfile

    suspend fun updateProfileDetails(
        firstName: String?,
        lastName: String?,
        age: Int?,
    ): UserProfile
}
