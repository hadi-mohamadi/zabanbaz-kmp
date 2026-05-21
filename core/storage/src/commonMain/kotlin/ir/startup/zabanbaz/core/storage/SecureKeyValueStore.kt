package ir.startup.zabanbaz.core.storage

/**
 * Platform secure storage for JWT tokens (Android: EncryptedSharedPreferences, iOS: Keychain).
 */
expect class SecureKeyValueStore {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
    suspend fun remove(key: String)
    suspend fun clear()
}
