package ir.startup.zabanbaz.feature.auth.domain

class VerifyCodeUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(mobile: String, code: String): AuthTokens =
        authRepository.verifyCode(mobile, code)
}
