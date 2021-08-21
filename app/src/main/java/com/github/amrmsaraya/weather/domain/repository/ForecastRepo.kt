package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.util.Response
import kotlinx.coroutines.flow.Flow

interface ForecastRepo {
    suspend fun insertForecast(forecast: Forecast)
    suspend fun deleteForecast(forecast: Forecast)
    suspend fun getForecast(id: Long): Response<Flow<Forecast>>
    suspend fun getForecast(forecastRequest: ForecastRequest): Response<Flow<Forecast>>
    suspend fun getCurrentForecast(forecastRequest: ForecastRequest): Response<Flow<Forecast>>
    suspend fun getCurrentForecast(): Response<Flow<Forecast>>
    suspend fun getFavoriteForecasts(): Response<Flow<List<Forecast>>>
}
