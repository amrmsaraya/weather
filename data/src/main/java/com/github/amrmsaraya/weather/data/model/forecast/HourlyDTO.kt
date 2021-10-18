package com.github.amrmsaraya.weather.data.model.forecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyDTO(
    @SerialName("dt")
    val dt: Int = 0,
    @SerialName("temp")
    val temp: Double = 0.0,
    @SerialName("feels_like")
    val feelsLike: Double = 0.0,
    @SerialName("pressure")
    val pressure: Int = 0,
    @SerialName("humidity")
    val humidity: Int = 0,
    @SerialName("dew_point")
    val dewPoint: Double = 0.0,
    @SerialName("uvi")
    val uvi: Double = 0.0,
    @SerialName("clouds")
    val clouds: Int = 0,
    @SerialName("visibility")
    val visibility: Int = 0,
    @SerialName("wind_speed")
    val windSpeed: Double = 0.0,
    @SerialName("wind_deg")
    val windDeg: Int = 0,
    @SerialName("wind_gust")
    val windGust: Double = 0.0,
    @SerialName("weather")
    val weather: List<WeatherDTO> = listOf(),
    @SerialName("pop")
    val pop: Double = 0.0,
)
