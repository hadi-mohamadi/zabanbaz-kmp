package ir.startup.zabanbaz.core.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSUserDefaults

/**
 * iOS secure storage via Keychain would be preferable for production;
 * NSUserDefaults is used here for parity with a minimal KMP setup.
 */
actual class SecureKeyValueStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual suspend fun getString(key: String): String? = withContext(Dispatchers.Default) {
        defaults.stringForKey(key)
    }

    actual suspend fun putString(key: String, value: String) = withContext(Dispatchers.Default) {
        defaults.setObject(value, forKey = key)
        defaults.synchronize()
    }

    actual suspend fun remove(key: String) = withContext(Dispatchers.Default) {
        defaults.removeObjectForKey(key)
        defaults.synchronize()
    }

    actual suspend fun clear() = withContext(Dispatchers.Default) {
        listOf(StorageKeys.ACCESS_TOKEN, StorageKeys.REFRESH_TOKEN).forEach { remove(it) }
    }
}
