package com.github.amrmsaraya.weather.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeelsLike(
    @SerialName("day")
    val day: Double,
    @SerialName("night")
    val night: Double,
    @SerialName("eve")
    val eve: Double,
    @SerialName("morn")
    val morn: Double
)
