package com.github.amrmsaraya.weather.domain.model.forecast

data class Forecast(
    var id: Long = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String = "",
    val timezoneOffset: Int = 0,
    val current: Current = Current(),
    val hourly: List<Hourly> = listOf(),
    val daily: List<Daily> = listOf(),
    val alerts: List<Alert> = listOf()
)
