package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo

class InsertForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forecast: Forecast) {
        forecastRepo.insertForecast(forecast)
    }
}
