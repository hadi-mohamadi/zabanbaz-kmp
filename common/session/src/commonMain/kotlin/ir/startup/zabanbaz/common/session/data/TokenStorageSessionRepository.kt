package ir.startup.zabanbaz.common.session.data

import ir.startup.zabanbaz.common.session.domain.SessionRepository
import ir.startup.zabanbaz.core.storage.TokenStorage

class TokenStorageSessionRepository(
    private val tokenStorage: TokenStorage,
) : SessionRepository {
    override suspend fun isLoggedIn(): Boolean = tokenStorage.isLoggedIn()

    override suspend fun clearSession() {
        tokenStorage.clearTokens()
    }
}
