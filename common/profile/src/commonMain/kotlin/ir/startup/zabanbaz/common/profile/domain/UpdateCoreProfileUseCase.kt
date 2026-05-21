package ir.startup.zabanbaz.common.profile.domain

class UpdateCoreProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        sex: String,
        learningLanguageId: Int,
        username: String,
    ): UserProfile = repository.updateCoreProfile(
        sex = sex,
        learningLanguageId = learningLanguageId,
        username = username,
    )
}
