package com.github.amrmsaraya.weather.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class WeatherResponse(

    @PrimaryKey
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezone_offset: Int,
    @SerializedName("current") val current: Current,
    @SerializedName("hourly") val hourly: List<Hourly>,
    @SerializedName("daily") val daily: List<Daily>,
    @SerializedName("alerts") var alerts: List<Alerts>?
)
