package ir.startup.zabanbaz.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initZabanbazKoin(
    isDebug: Boolean,
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {},
) {
    startKoin {
        appDeclaration()
        modules(*(registerSharedModules(isDebug) + extraModules).toTypedArray())
    }
}
