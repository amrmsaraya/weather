package com.github.amrmsaraya.weather.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
    @SerialName("current")
    val current: Current,
    @SerialName("hourly")
    val hourly: List<Hourly>,
    @SerialName("daily")
    val daily: List<Daily>,
    @SerialName("alerts")
    val alerts: List<Alert> = listOf()
)
