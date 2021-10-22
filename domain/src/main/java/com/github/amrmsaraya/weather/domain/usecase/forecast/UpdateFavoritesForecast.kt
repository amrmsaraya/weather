package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import kotlinx.coroutines.flow.first


class UpdateFavoritesForecast constructor(private val forecastRepo: ForecastRepo) {
    suspend fun execute() {
        val favorites = forecastRepo.getFavoriteForecasts().first()
        favorites.forEach {
            runCatching {
                val updatedForecast = forecastRepo.getForecast(it.lat, it.lon, true)
                println("FAVORITES: BEFORE UPDATE = $it")
                println("FAVORITES: AFTER UPDATE = $updatedForecast")
                forecastRepo.insertForecast(updatedForecast.copy(id = it.id))
            }
        }
    }
}
