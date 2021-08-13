package com.github.amrmsaraya.weather.util

import androidx.room.TypeConverter
import com.github.amrmsaraya.weather.data.models.Alert
import com.github.amrmsaraya.weather.data.models.Current
import com.github.amrmsaraya.weather.data.models.Daily
import com.github.amrmsaraya.weather.data.models.Hourly
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class Converter {

    @TypeConverter
    fun fromCurrent(current: Current): String {
        return Json.encodeToString(current)
    }

    @TypeConverter
    fun toCurrent(string: String): Current {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromHourly(hourly: List<Hourly>): String {
        return Json.encodeToString(hourly)
    }

    @TypeConverter
    fun toHourly(string: String): List<Hourly> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromDaily(daily: List<Daily>): String {
        return Json.encodeToString(daily)
    }

    @TypeConverter
    fun toDaily(string: String): List<Daily> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromAlerts(alerts: List<Alert>): String {
        return Json.encodeToString(alerts)
    }

    @TypeConverter
    fun toAlerts(string: String): List<Alert> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return Json.encodeToString(uuid)
    }

    @TypeConverter
    fun toUUID(string: String): UUID {
        return Json.decodeFromString(string)
    }
}
