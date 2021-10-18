package com.github.amrmsaraya.weather.domain.model.forecast

data class Hourly(
    val dt: Int = 0,
    val temp: Double = 0.0,
    val feelsLike: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val dewPoint: Double = 0.0,
    val uvi: Double = 0.0,
    val clouds: Int = 0,
    val visibility: Int = 0,
    val windSpeed: Double = 0.0,
    val windDeg: Int = 0,
    val windGust: Double = 0.0,
    val weather: List<Weather> = listOf(),
    val pop: Double = 0.0,
)
