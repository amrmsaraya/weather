package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import javax.inject.Inject

class InsertForecast @Inject constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forecast: Forecast) =
        forecastRepo.insertForecast(forecast)
}
