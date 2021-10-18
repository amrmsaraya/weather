package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

interface RemoteDataSource {
    suspend fun getForecast(lat: Double, lon: Double): Forecast
}
