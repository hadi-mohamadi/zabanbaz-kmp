package ir.startup.zabanbaz.feature.onboarding.di

import ir.startup.zabanbaz.feature.onboarding.presentation.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun onboardingPresentationModule(): Module = module {
    viewModel {
        OnboardingViewModel(get(), get(), get())
    }
}
