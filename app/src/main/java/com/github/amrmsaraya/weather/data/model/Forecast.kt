package com.github.amrmsaraya.weather.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @SerialName("lat")
    @ColumnInfo(name = "lat")
    val lat: Double,

    @SerialName("lon")
    @ColumnInfo(name = "lon")
    val lon: Double,

    @SerialName("timezone")
    @ColumnInfo(name = "timezone")
    val timezone: String,

    @SerialName("timezone_offset")
    @ColumnInfo(name = "timezone_offset")
    val timezoneOffset: Int,

    @SerialName("current")
    @ColumnInfo(name = "current")
    val current: Current,

    @SerialName("hourly")
    @ColumnInfo(name = "hourly")
    val hourly: List<Hourly>,

    @SerialName("daily")
    @ColumnInfo(name = "daily")
    val daily: List<Daily>,

    @SerialName("alerts")
    @ColumnInfo(name = "alerts")
    val alerts: List<Alert> = listOf()
)
