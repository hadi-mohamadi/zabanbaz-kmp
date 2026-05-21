package ir.startup.zabanbaz.feature.startup.di

import ir.startup.zabanbaz.feature.startup.presentation.StartupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun startupPresentationModule(): Module = module {
    viewModel { StartupViewModel(get(), get()) }
}
