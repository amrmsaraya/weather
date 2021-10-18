package com.github.amrmsaraya.weather.domain.model.forecast

data class Daily(
    val dt: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val moonrise: Int = 0,
    val moonset: Int = 0,
    val moonPhase: Double = 0.0,
    val temp: Temp,
    val feelsLike: FeelsLike = FeelsLike(),
    val pressure: Int = 0,
    val humidity: Int = 0,
    val dewPoint: Double = 0.0,
    val windSpeed: Double = 0.0,
    val windDeg: Int = 0,
    val windGust: Double = 0.0,
    val weather: List<Weather> = listOf(),
    val clouds: Int = 0,
    val pop: Double = 0.0,
    val uvi: Double = 0.0
)
