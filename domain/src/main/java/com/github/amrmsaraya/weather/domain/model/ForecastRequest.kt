package com.github.amrmsaraya.weather.domain.model

import java.util.*

data class ForecastRequest(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val units: String = "metric",
    val lang: String = Locale.getDefault().language,
)
