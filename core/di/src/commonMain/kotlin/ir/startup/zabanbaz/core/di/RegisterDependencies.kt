package ir.startup.zabanbaz.core.di

import ir.startup.zabanbaz.feature.auth.di.authModule
import org.koin.core.module.Module

/**
 * Registers all shared (commonMain) dependencies in live_app order:
 * core → common → features (data/domain).
 */
fun registerSharedModules(isDebug: Boolean): List<Module> = listOf(
    coreModule(apiBaseUrl(isDebug)),
    commonModule(),
    authModule(),
)
