package com.github.amrmsaraya.weather.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    @SerialName("dt")
    val dt: Int,
    @SerialName("sunrise")
    val sunrise: Int,
    @SerialName("sunset")
    val sunset: Int,
    @SerialName("moonrise")
    val moonrise: Int,
    @SerialName("moonset")
    val moonset: Int,
    @SerialName("moon_phase")
    val moonPhase: Double,
    @SerialName("temp")
    val temp: Temp,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    @SerialName("weather")
    val weather: List<Weather>,
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("pop")
    val pop: Double,
    @SerialName("uvi")
    val uvi: Double
)
