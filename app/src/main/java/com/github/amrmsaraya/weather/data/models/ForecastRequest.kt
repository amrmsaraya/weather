package com.github.amrmsaraya.weather.data.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ForecastRequest(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val units: String = "metric",
    val lang: String = Locale.getDefault().language,
)
