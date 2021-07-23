package com.example.excursionstest.domain.repositories

import com.example.excursionstest.domain.entities.Excursion
import com.example.excursionstest.domain.entities.Step

interface ExcursionRepository {
    fun getExcursion() : Excursion
    fun getStep() : Step
}