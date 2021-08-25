package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest

interface RemoteDataSource {
    suspend fun getForecast(forecastRequest: ForecastRequest): Forecast
}
