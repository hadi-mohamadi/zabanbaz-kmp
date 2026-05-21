package ir.startup.zabanbaz.common.profile.domain

class GetUserProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): UserProfile = repository.getUserProfile()
}
