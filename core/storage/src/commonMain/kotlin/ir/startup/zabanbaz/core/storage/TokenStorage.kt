package ir.startup.zabanbaz.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class TokenStorage(
    private val secureStore: SecureKeyValueStore,
    private val settings: Settings,
) {
    suspend fun readAccessToken(): String? = secureStore.getString(StorageKeys.ACCESS_TOKEN)

    suspend fun readRefreshToken(): String? = secureStore.getString(StorageKeys.REFRESH_TOKEN)

    suspend fun writeTokens(accessToken: String, refreshToken: String?) {
        secureStore.putString(StorageKeys.ACCESS_TOKEN, accessToken)
        if (!refreshToken.isNullOrEmpty()) {
            secureStore.putString(StorageKeys.REFRESH_TOKEN, refreshToken)
        }
        setLoginFlag(true)
    }

    suspend fun clearTokens() {
        secureStore.remove(StorageKeys.ACCESS_TOKEN)
        secureStore.remove(StorageKeys.REFRESH_TOKEN)
        setLoginFlag(false)
    }

    suspend fun isLoggedIn(): Boolean = settings.getBooleanOrNull(StorageKeys.IS_LOGGED_IN) == true

    suspend fun writeUserId(userId: String) {
        settings[StorageKeys.USER_ID] = userId
    }

    suspend fun readUserId(): String? = settings.getStringOrNull(StorageKeys.USER_ID)

    private suspend fun setLoginFlag(value: Boolean) {
        settings[StorageKeys.IS_LOGGED_IN] = value
    }
}
