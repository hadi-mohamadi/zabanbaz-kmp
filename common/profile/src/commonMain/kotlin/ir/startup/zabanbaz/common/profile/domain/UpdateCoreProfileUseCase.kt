package ir.startup.zabanbaz.common.profile.domain

class UpdateCoreProfileUseCase(
    private val repository: ProfileRepository,
) {
    /** Full core profile setup (onboarding). */
    suspend operator fun invoke(
        sex: String,
        learningLanguageId: Int,
        username: String,
    ): UserProfile = repository.patchCoreProfile(
        sex = sex,
        learningLanguageId = learningLanguageId,
        username = username.trim().lowercase(),
    )

    /** Partial core profile update (e.g. username only from profile screen). */
    suspend fun patch(
        sex: String? = null,
        learningLanguageId: Int? = null,
        username: String? = null,
    ): UserProfile = repository.patchCoreProfile(
        sex = sex,
        learningLanguageId = learningLanguageId,
        username = username?.trim()?.lowercase(),
    )
}
