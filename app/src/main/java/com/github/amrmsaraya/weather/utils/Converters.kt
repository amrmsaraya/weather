package com.github.amrmsaraya.weather.utils

import androidx.room.TypeConverter
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.models.Current
import com.github.amrmsaraya.weather.data.models.Daily
import com.github.amrmsaraya.weather.data.models.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun fromCurrent(current: Current): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun toCurrent(string: String): Current {
        return Gson().fromJson(string, Current::class.java)
    }

    @TypeConverter
    fun fromHourly(hourly: List<Hourly>): String {
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun toHourly(string: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun fromDaily(daily: List<Daily>): String {
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun toDaily(string: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun fromAlerts(alerts: List<Alerts>?): String? {
        return Gson().toJson(alerts)
    }

    @TypeConverter
    fun toAlerts(string: String): List<Alerts>? {
        val type = object : TypeToken<List<Alerts>?>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return Gson().toJson(uuid)
    }

    @TypeConverter
    fun toUUID(string: String): UUID {
        return Gson().fromJson(string, UUID::class.java)
    }
}
