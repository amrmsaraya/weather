package com.github.amrmsaraya.weather.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastRequest(
    val lat: Double,
    val lon: Double,
    val units: String = "metric",
    val lang: String = "en",
)
