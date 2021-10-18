package com.github.amrmsaraya.weather.domain.model.forecast

data class FeelsLike(
    val day: Double = 0.0,
    val night: Double = 0.0,
    val eve: Double = 0.0,
    val morn: Double = 0.0
)
