package ir.startup.zabanbaz.feature.auth.domain

class RequestVerificationCodeUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(mobile: String) {
        authRepository.requestVerificationCode(mobile)
    }
}
