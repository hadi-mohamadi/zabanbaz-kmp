package ir.startup.zabanbaz.feature.home.di

import ir.startup.zabanbaz.feature.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun homePresentationModule(): Module = module {
    viewModel {
        HomeViewModel(get(), get())
    }
}
