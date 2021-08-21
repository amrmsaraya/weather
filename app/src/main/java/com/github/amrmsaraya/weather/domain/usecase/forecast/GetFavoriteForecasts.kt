package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import javax.inject.Inject

class GetFavoriteForecasts @Inject constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute() = forecastRepo.getFavoriteForecasts()
}
