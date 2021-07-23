package com.example.excursionstest.di

import com.example.excursionstest.data.repositories.ExcursionRepositoryImpl
import com.example.excursionstest.domain.usecases.GetExcursionUseCase
import com.example.excursionstest.domain.usecases.GetStepUseCase
import com.example.excursionstest.ui.main_screen.MainScreenViewModel
import com.example.excursionstest.ui.player.PlayerViewModel
import com.example.excursionstest.ui.step_list.StepListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { ExcursionRepositoryImpl() }
}

val useCaseModule = module {
    single { GetExcursionUseCase(get()) }
    single { GetStepUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MainScreenViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { StepListViewModel(get()) }
}