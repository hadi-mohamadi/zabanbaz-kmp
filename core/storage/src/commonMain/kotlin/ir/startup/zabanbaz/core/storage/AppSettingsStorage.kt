package ir.startup.zabanbaz.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class AppSettingsStorage(
    private val settings: Settings,
) {
    fun getLanguageCode(): String? = settings.getStringOrNull(StorageKeys.APP_LANGUAGE_CODE)

    fun setLanguageCode(code: String) {
        settings[StorageKeys.APP_LANGUAGE_CODE] = code
    }

    /** Value for the `Accept-Language` HTTP header. */
    fun acceptLanguageHeader(): String = getLanguageCode() ?: DEFAULT_LANGUAGE_CODE

    companion object {
        const val DEFAULT_LANGUAGE_CODE = "fa"
    }
}
