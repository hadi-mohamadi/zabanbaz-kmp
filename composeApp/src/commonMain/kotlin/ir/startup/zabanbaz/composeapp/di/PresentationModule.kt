package ir.startup.zabanbaz.composeapp.di

import ir.startup.zabanbaz.feature.auth.presentation.AuthViewModel
import ir.startup.zabanbaz.feature.discussion.presentation.DiscussionCallViewModel
import ir.startup.zabanbaz.feature.discussion.presentation.DiscussionQueueViewModel
import ir.startup.zabanbaz.feature.home.presentation.HomeViewModel
import ir.startup.zabanbaz.feature.onboarding.presentation.OnboardingViewModel
import ir.startup.zabanbaz.feature.placement.presentation.PlacementViewModel
import ir.startup.zabanbaz.feature.profileui.presentation.ProfileViewModel
import ir.startup.zabanbaz.feature.startup.presentation.StartupViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** Shared ViewModel registrations for Android and iOS (Compose Multiplatform). */
fun presentationModule(): Module = module {
    viewModel { StartupViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get(), get()) }
    viewModel { PlacementViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { DiscussionQueueViewModel(get(), get(), get(), get()) }
    viewModel { (sessionId: Int) ->
        DiscussionCallViewModel(sessionId, get(), get(), get(), get(), get())
    }
    viewModel { ProfileViewModel(get(), get(), get()) }
}
