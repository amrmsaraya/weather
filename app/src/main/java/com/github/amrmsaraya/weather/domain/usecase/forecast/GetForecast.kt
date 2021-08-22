package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import javax.inject.Inject

class GetForecast @Inject constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute(id: Long) = forecastRepo.getForecast(id)
    suspend fun execute(forecastRequest: ForecastRequest) =
        forecastRepo.getForecast(forecastRequest)
}
