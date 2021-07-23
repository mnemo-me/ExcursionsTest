package com.example.excursionstest.domain.usecases

import com.example.excursionstest.data.repositories.ExcursionRepositoryImpl

class GetStepUseCase(val excursionRepository: ExcursionRepositoryImpl) {
    operator fun invoke() = excursionRepository.getStep()
}