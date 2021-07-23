package com.example.excursionstest.domain.entities

data class Excursion (
    var id: Long,
    var name: String,
    var steps: List<Step>
)