package ir.startup.zabanbaz.common.session.domain

class CheckSessionUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): Boolean = sessionRepository.isLoggedIn()
}
