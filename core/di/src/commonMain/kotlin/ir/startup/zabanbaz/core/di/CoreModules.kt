package ir.startup.zabanbaz.core.di

import ir.startup.zabanbaz.common.session.data.TokenStorageSessionRepository
import ir.startup.zabanbaz.common.session.domain.CheckSessionUseCase
import ir.startup.zabanbaz.common.session.domain.SessionRepository
import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import ir.startup.zabanbaz.core.networking.NetworkConstants
import ir.startup.zabanbaz.core.storage.AppSettingsStorage
import ir.startup.zabanbaz.core.storage.TokenStorage
import ir.startup.zabanbaz.core.storage.createSecureKeyValueStore
import ir.startup.zabanbaz.core.storage.createSettings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val API_BASE_URL_QUALIFIER = "apiBaseUrl"

fun coreModule(apiBaseUrl: String): Module = module {
    single(named(API_BASE_URL_QUALIFIER)) { apiBaseUrl }

    single { createSettings() }
    single { createSecureKeyValueStore() }
    single { TokenStorage(get(), get()) }
    single { AppSettingsStorage(get()) }

    single { HttpClientFactory.create() }
    single {
        ApiService(
            httpClient = get(),
            baseUrl = get(named(API_BASE_URL_QUALIFIER)),
            tokenStorage = get(),
            appSettingsStorage = get(),
        )
    }

    single<SessionRepository> { TokenStorageSessionRepository(get()) }
    single { CheckSessionUseCase(get()) }
}

fun apiBaseUrl(@Suppress("UNUSED_PARAMETER") isDebug: Boolean): String =
    NetworkConstants.API_BASE_URL
