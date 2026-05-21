package ir.startup.zabanbaz.common.session.domain

interface SessionRepository {
    suspend fun isLoggedIn(): Boolean

    suspend fun clearSession()
}
