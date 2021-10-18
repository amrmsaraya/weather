package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo

class DeleteForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forecast: Forecast) {
        forecastRepo.deleteForecast(forecast)
    }

    suspend fun execute(list: List<Forecast>) {
        forecastRepo.deleteForecast(list)
    }
}
