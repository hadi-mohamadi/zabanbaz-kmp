package ir.startup.zabanbaz.common.profile.domain

class UpdateProfileDetailsUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        firstName: String?,
        lastName: String?,
        age: Int?,
    ): UserProfile = repository.updateProfileDetails(
        firstName = firstName?.trim()?.takeIf { it.isNotEmpty() },
        lastName = lastName?.trim()?.takeIf { it.isNotEmpty() },
        age = age,
    )
}
