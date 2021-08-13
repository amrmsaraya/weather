package com.github.amrmsaraya.domain

interface WeatherRepo {

    suspend fun getWeather(): Forecast
}
