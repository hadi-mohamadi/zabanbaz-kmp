package ir.startup.zabanbaz.common.profile.data.repository

import ir.startup.zabanbaz.common.profile.data.datasource.ProfileRemoteDataSource
import ir.startup.zabanbaz.common.profile.domain.ProfileRepository
import ir.startup.zabanbaz.common.profile.domain.UserProfile

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {
    override suspend fun getUserProfile(): UserProfile = remoteDataSource.getProfile()

    override suspend fun patchCoreProfile(
        sex: String?,
        learningLanguageId: Int?,
        username: String?,
    ): UserProfile = remoteDataSource.patchCoreProfile(
        sex = sex,
        learningLanguageId = learningLanguageId,
        username = username,
    )

    override suspend fun updateProfileDetails(
        firstName: String?,
        lastName: String?,
        age: Int?,
    ): UserProfile = remoteDataSource.updateProfileDetails(
        firstName = firstName,
        lastName = lastName,
        age = age,
    )
}
