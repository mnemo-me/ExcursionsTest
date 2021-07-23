package com.example.excursionstest.domain.entities

data class Step (
    var id : Long,
    var name: String,
    var description: String,
    var images: List<String>,
    var audio: String
)