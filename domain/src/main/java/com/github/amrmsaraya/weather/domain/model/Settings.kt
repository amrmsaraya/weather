package com.github.amrmsaraya.weather.domain.model

data class Settings(
    val location: Int,
    val language: Int,
    val theme: Int,
    val accent: Int,
    val notifications: Boolean,
    val temperature: Int,
    val windSpeed: Int,
    val versionCode: Int
)
