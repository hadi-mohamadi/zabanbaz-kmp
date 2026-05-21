package ir.startup.zabanbaz.feature.profileui.di

import ir.startup.zabanbaz.feature.profileui.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun profilePresentationModule(): Module = module {
    viewModel {
        ProfileViewModel(get(), get())
    }
}
