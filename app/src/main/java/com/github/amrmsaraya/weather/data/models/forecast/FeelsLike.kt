package com.github.amrmsaraya.weather.data.models.forecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeelsLike(
    @SerialName("day")
    val day: Double = 0.0,
    @SerialName("night")
    val night: Double = 0.0,
    @SerialName("eve")
    val eve: Double = 0.0,
    @SerialName("morn")
    val morn: Double = 0.0
)
