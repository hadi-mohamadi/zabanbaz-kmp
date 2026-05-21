package ir.startup.zabanbaz.feature.auth.di

import ir.startup.zabanbaz.feature.auth.data.datasource.AuthRemoteDataSource
import ir.startup.zabanbaz.feature.auth.data.repository.AuthRepositoryImpl
import ir.startup.zabanbaz.feature.auth.domain.AuthRepository
import ir.startup.zabanbaz.feature.auth.domain.RequestVerificationCodeUseCase
import ir.startup.zabanbaz.feature.auth.domain.VerifyCodeUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

/** Data/domain layer for auth (parity with live_app register_auth_dependencies). */
fun authModule(): Module = module {
    single { AuthRemoteDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory { RequestVerificationCodeUseCase(get()) }
    factory { VerifyCodeUseCase(get()) }
}
