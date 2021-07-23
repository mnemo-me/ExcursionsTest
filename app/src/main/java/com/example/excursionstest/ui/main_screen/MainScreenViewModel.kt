package com.example.excursionstest.ui.main_screen

import androidx.lifecycle.ViewModel
import com.example.excursionstest.domain.usecases.GetStepUseCase

class MainScreenViewModel(val getStepUseCase: GetStepUseCase) : ViewModel() {

    val step = getStepUseCase()

}