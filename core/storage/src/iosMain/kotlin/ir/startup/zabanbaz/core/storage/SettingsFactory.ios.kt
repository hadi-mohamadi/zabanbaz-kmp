package ir.startup.zabanbaz.core.storage

import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults

actual fun createSettings(): com.russhwolf.settings.Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}
