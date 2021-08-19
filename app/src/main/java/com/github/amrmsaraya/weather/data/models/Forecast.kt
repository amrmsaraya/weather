package com.github.amrmsaraya.weather.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "forecast")
@Serializable
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @SerialName("lat")
    @ColumnInfo(name = "lat")
    val lat: Double = 0.0,

    @SerialName("lon")
    @ColumnInfo(name = "lon")
    val lon: Double = 0.0,

    @SerialName("timezone")
    @ColumnInfo(name = "timezone")
    val timezone: String = "",

    @SerialName("timezone_offset")
    @ColumnInfo(name = "timezone_offset")
    val timezoneOffset: Int = 0,

    @SerialName("current")
    @ColumnInfo(name = "current")
    val current: Current = Current(),

    @SerialName("hourly")
    @ColumnInfo(name = "hourly")
    val hourly: List<Hourly> = listOf(),

    @SerialName("daily")
    @ColumnInfo(name = "daily")
    val daily: List<Daily> = listOf(),

    @SerialName("alerts")
    @ColumnInfo(name = "alerts")
    val alerts: List<Alert> = listOf()
)
