package com.example.excursionstest.ui.step_list

import androidx.lifecycle.ViewModel
import com.example.excursionstest.domain.usecases.GetExcursionUseCase

class StepListViewModel(val getExcursionUseCase: GetExcursionUseCase) : ViewModel() {

    val excursion = getExcursionUseCase()
}