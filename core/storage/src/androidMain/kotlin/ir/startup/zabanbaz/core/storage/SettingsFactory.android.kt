package ir.startup.zabanbaz.core.storage

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings

actual fun createSettings(): com.russhwolf.settings.Settings {
    val context = storageContext
    return SharedPreferencesSettings(
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE),
    )
}

private const val PREFS_FILE = "zabanbaz_settings"

/** Set from the Android Application before DI registration. */
lateinit var storageContext: Context
    private set

fun initStorageContext(context: Context) {
    storageContext = context.applicationContext
}
