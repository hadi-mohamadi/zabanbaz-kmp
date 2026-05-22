package ir.startup.zabanbaz.common.profile.domain

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfile

    /** Partial update — only non-null fields are sent to PATCH /profile/core/ */
    suspend fun patchCoreProfile(
        sex: String? = null,
        learningLanguageId: Int? = null,
        username: String? = null,
    ): UserProfile

    suspend fun updateProfileDetails(
        firstName: String?,
        lastName: String?,
        age: Int?,
    ): UserProfile
}
