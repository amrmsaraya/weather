package com.github.amrmsaraya.weather.data.model.forecast

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "forecast")
@Serializable
data class ForecastDTO(
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
    val current: CurrentDTO = CurrentDTO(),

    @SerialName("hourly")
    @ColumnInfo(name = "hourly")
    val hourly: List<HourlyDTO> = listOf(),

    @SerialName("daily")
    @ColumnInfo(name = "daily")
    val daily: List<DailyDTO> = listOf(),

    @SerialName("alerts")
    @ColumnInfo(name = "alerts")
    val alerts: List<AlertDTO> = listOf()
)
