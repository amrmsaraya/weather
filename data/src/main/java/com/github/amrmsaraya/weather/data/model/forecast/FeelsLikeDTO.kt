package com.github.amrmsaraya.weather.data.model.forecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeDTO(
    @SerialName("day")
    val day: Double = 0.0,
    @SerialName("night")
    val night: Double = 0.0,
    @SerialName("eve")
    val eve: Double = 0.0,
    @SerialName("morn")
    val morn: Double = 0.0
)
