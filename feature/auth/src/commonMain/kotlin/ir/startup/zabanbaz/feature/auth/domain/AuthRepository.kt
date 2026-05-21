package ir.startup.zabanbaz.feature.auth.domain

interface AuthRepository {
    suspend fun requestVerificationCode(mobile: String)

    suspend fun verifyCode(mobile: String, code: String): AuthTokens
}
