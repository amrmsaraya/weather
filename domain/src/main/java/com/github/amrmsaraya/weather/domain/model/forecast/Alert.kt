package com.github.amrmsaraya.weather.domain.model.forecast

data class Alert(
    val senderName: String = "",
    val event: String = "",
    val start: Int = 0,
    val end: Int = 0,
    val description: String = "",
    val tags: List<String> = listOf()
)
