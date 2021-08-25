package com.github.amrmsaraya.weather.data.models

import com.github.amrmsaraya.weather.R
import java.util.*

data class Settings(
    val location: Int = R.string.gps,
    val language: Int = if (Locale.getDefault().language == "ar") R.string.arabic else R.string.english,
    val theme: Int = R.string.default_,
    val accent: Int = 0,
    val notifications: Boolean = true,
    val temperature: Int = R.string.celsius,
    val windSpeed: Int = R.string.meter_sec,
)
