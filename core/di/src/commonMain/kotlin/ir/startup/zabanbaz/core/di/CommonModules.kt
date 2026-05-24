package ir.startup.zabanbaz.core.di

import ir.startup.zabanbaz.common.discussion.data.datasource.DiscussionRemoteDataSource
import ir.startup.zabanbaz.common.discussion.data.repository.DiscussionRepositoryImpl
import ir.startup.zabanbaz.common.discussion.domain.DiscussionRepository
import ir.startup.zabanbaz.common.discussion.domain.EndDiscussionSessionUseCase
import ir.startup.zabanbaz.common.discussion.domain.GetDiscussionMatchStatusUseCase
import ir.startup.zabanbaz.common.discussion.domain.JoinDiscussionMatchUseCase
import ir.startup.zabanbaz.common.discussion.domain.LeaveDiscussionMatchUseCase
import ir.startup.zabanbaz.common.placement.data.datasource.PlacementRemoteDataSource
import ir.startup.zabanbaz.common.placement.data.repository.PlacementRepositoryImpl
import ir.startup.zabanbaz.common.placement.domain.PlacementRepository
import ir.startup.zabanbaz.common.placement.domain.GetPlacementTestResultUseCase
import ir.startup.zabanbaz.common.placement.domain.StartPlacementTestUseCase
import ir.startup.zabanbaz.common.placement.domain.SubmitPlacementTestUseCase
import ir.startup.zabanbaz.common.session.domain.LogoutUseCase
import ir.startup.zabanbaz.common.languages.data.datasource.LanguagesRemoteDataSource
import ir.startup.zabanbaz.common.languages.data.repository.LanguagesRepositoryImpl
import ir.startup.zabanbaz.common.languages.domain.GetLanguagesUseCase
import ir.startup.zabanbaz.common.languages.domain.LanguagesRepository
import ir.startup.zabanbaz.common.profile.data.datasource.ProfileRemoteDataSource
import ir.startup.zabanbaz.common.profile.data.repository.ProfileRepositoryImpl
import ir.startup.zabanbaz.common.profile.domain.GetUserProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.ProfileRepository
import ir.startup.zabanbaz.common.profile.domain.UpdateCoreProfileUseCase
import ir.startup.zabanbaz.common.profile.domain.UpdateProfileDetailsUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(): Module = module {
    single { ProfileRemoteDataSource(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { UpdateCoreProfileUseCase(get()) }
    factory { UpdateProfileDetailsUseCase(get()) }

    single { LanguagesRemoteDataSource(get()) }
    single<LanguagesRepository> { LanguagesRepositoryImpl(get()) }
    factory { GetLanguagesUseCase(get()) }

    single { PlacementRemoteDataSource(get()) }
    single<PlacementRepository> { PlacementRepositoryImpl(get()) }
    factory { StartPlacementTestUseCase(get()) }
    factory { SubmitPlacementTestUseCase(get()) }
    factory { GetPlacementTestResultUseCase(get()) }

    single { DiscussionRemoteDataSource(get()) }
    single<DiscussionRepository> { DiscussionRepositoryImpl(get()) }
    factory { JoinDiscussionMatchUseCase(get()) }
    factory { LeaveDiscussionMatchUseCase(get()) }
    factory { GetDiscussionMatchStatusUseCase(get()) }
    factory { EndDiscussionSessionUseCase(get()) }

    factory { LogoutUseCase(get()) }
}
