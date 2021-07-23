package com.example.excursionstest.ui.player

import androidx.lifecycle.ViewModel
import com.example.excursionstest.domain.usecases.GetExcursionUseCase

class PlayerViewModel(val getExcursionUseCase: GetExcursionUseCase) : ViewModel() {

    val excursion = getExcursionUseCase()
    val step = excursion.steps[0]
}