package com.github.amrmsaraya.weather.data.models

import com.github.amrmsaraya.weather.R

data class Settings(
    val location: Int = R.string.gps,
    val language: Int = R.string.english,
    val theme: Int = R.string.default_,
    val accent: Int = 0,
    val notifications: Boolean = true,
    val temperature: Int = R.string.celsius,
    val windSpeed: Int = R.string.meter_sec,
)
