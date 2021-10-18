package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepo {
    suspend fun insertForecast(forecast: Forecast)
    suspend fun deleteForecast(forecast: Forecast)
    suspend fun deleteForecast(list: List<Forecast>)
    suspend fun getForecast(lat: Double, lon: Double, forceUpdate: Boolean): Forecast
    suspend fun getCurrentForecast(lat: Double, lon: Double, forceUpdate: Boolean): Forecast
    suspend fun getCurrentForecast(): Forecast
    suspend fun getFavoriteForecasts(): Flow<List<Forecast>>
}
