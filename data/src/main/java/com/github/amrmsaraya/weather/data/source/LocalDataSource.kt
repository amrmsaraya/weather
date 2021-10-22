package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertForecast(forecast: Forecast)
    suspend fun deleteForecast(forecast: Forecast)
    suspend fun deleteForecast(list: List<Forecast>)
    suspend fun getForecast(id:Long): Forecast
    suspend fun getCurrentForecast(): Forecast
    suspend fun getFavoriteForecasts(): Flow<List<Forecast>>
}
