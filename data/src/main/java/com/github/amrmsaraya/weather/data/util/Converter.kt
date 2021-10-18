package com.github.amrmsaraya.weather.data.util

import androidx.room.TypeConverter
import com.github.amrmsaraya.weather.data.model.forecast.AlertDTO
import com.github.amrmsaraya.weather.data.model.forecast.CurrentDTO
import com.github.amrmsaraya.weather.data.model.forecast.DailyDTO
import com.github.amrmsaraya.weather.data.model.forecast.HourlyDTO
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class Converter {

    @TypeConverter
    fun fromCurrent(current: CurrentDTO): String {
        return Json.encodeToString(current)
    }

    @TypeConverter
    fun toCurrent(string: String): CurrentDTO {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromHourly(hourly: List<HourlyDTO>): String {
        return Json.encodeToString(hourly)
    }

    @TypeConverter
    fun toHourly(string: String): List<HourlyDTO> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromDaily(daily: List<DailyDTO>): String {
        return Json.encodeToString(daily)
    }

    @TypeConverter
    fun toDaily(string: String): List<DailyDTO> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromAlerts(alerts: List<AlertDTO>): String {
        return Json.encodeToString(alerts)
    }

    @TypeConverter
    fun toAlerts(string: String): List<AlertDTO> {
        return Json.decodeFromString(string)
    }
}
