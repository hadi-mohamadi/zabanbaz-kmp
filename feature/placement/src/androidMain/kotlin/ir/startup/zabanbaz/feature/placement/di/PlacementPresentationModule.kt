package ir.startup.zabanbaz.feature.placement.di

import ir.startup.zabanbaz.feature.placement.presentation.PlacementViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun placementPresentationModule(): Module = module {
    viewModel {
        PlacementViewModel(get(), get())
    }
}
