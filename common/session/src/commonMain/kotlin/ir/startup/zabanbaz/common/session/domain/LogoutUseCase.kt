package ir.startup.zabanbaz.common.session.domain

class LogoutUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke() {
        sessionRepository.clearSession()
    }
}
