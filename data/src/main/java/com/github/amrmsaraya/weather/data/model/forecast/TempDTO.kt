package com.github.amrmsaraya.weather.data.model.forecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TempDTO(
    @SerialName("day")
    val day: Double = 0.0,
    @SerialName("min")
    val min: Double = 0.0,
    @SerialName("max")
    val max: Double = 0.0,
    @SerialName("night")
    val night: Double = 0.0,
    @SerialName("eve")
    val eve: Double = 0.0,
    @SerialName("morn")
    val morn: Double = 0.0
)
