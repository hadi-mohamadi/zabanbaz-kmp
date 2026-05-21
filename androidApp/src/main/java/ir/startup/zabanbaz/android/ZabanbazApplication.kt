package ir.startup.zabanbaz.android

import android.app.Application
import ir.startup.zabanbaz.composeapp.di.presentationModule
import ir.startup.zabanbaz.core.di.initZabanbazKoin
import ir.startup.zabanbaz.core.storage.initStorageContext
import org.koin.android.ext.koin.androidContext

class ZabanbazApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initStorageContext(this)
        initZabanbazKoin(
            isDebug = BuildConfig.DEBUG,
            extraModules = listOf(presentationModule()),
        ) {
            androidContext(this@ZabanbazApplication)
        }
    }
}
