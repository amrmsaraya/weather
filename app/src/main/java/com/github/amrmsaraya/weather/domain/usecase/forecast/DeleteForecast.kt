package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import javax.inject.Inject

class DeleteForecast @Inject constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forecast: Forecast) =
        forecastRepo.deleteForecast(forecast)

    suspend fun execute(list: List<Forecast>) =
        forecastRepo.deleteForecast(list)
}
