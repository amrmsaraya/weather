package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import kotlinx.coroutines.flow.Flow

class GetFavoriteForecasts(private val forecastRepo: ForecastRepo) {
    suspend fun execute(): Flow<List<Forecast>> {
        return forecastRepo.getFavoriteForecasts()
    }
}
