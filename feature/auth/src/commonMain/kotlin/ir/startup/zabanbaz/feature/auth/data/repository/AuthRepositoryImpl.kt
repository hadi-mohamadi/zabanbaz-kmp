package ir.startup.zabanbaz.feature.auth.data.repository

import ir.startup.zabanbaz.core.storage.TokenStorage
import ir.startup.zabanbaz.feature.auth.data.datasource.AuthRemoteDataSource
import ir.startup.zabanbaz.feature.auth.domain.AuthRepository
import ir.startup.zabanbaz.feature.auth.domain.AuthTokens

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun requestVerificationCode(mobile: String) {
        remoteDataSource.requestCode(mobile)
    }

    override suspend fun verifyCode(mobile: String, code: String): AuthTokens {
        val dto = remoteDataSource.verifyCode(mobile, code)
        tokenStorage.writeTokens(
            accessToken = dto.access,
            refreshToken = dto.refresh,
        )
        tokenStorage.writeUserId(dto.mobile)
        return AuthTokens(
            accessToken = dto.access,
            refreshToken = dto.refresh,
            mobile = dto.mobile,
            created = dto.created,
        )
    }
}
