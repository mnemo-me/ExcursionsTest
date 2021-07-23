package com.example.excursionstest.domain.usecases

import com.example.excursionstest.data.repositories.ExcursionRepositoryImpl

class GetExcursionUseCase(val excursionRepository: ExcursionRepositoryImpl) {

    operator fun invoke() = excursionRepository.getExcursion()
}