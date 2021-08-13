package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.data.models.Forecast
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertForecast(forecast: Forecast)
    suspend fun deleteForecast(forecast: Forecast)
    suspend fun getCurrentForecast(): Flow<Forecast>
    suspend fun getAllForecasts(): Flow<List<Forecast>>
}
