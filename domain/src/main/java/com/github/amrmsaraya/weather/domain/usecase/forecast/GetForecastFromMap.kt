package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo

class GetForecastFromMap(private val forecastRepo: ForecastRepo) {
    suspend fun execute(lat: Double, lon: Double) {
        runCatching { forecastRepo.getRemoteForecast(lat, lon) }
            .onSuccess { forecastRepo.insertForecast(it) }
            .onFailure { forecastRepo.insertForecast(Forecast(lat = lat, lon = lon)) }
    }
}