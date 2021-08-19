package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import javax.inject.Inject

class GetCurrentForecast @Inject constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forecastRequest: ForecastRequest) =
        forecastRepo.getCurrentForecast(forecastRequest)
}
