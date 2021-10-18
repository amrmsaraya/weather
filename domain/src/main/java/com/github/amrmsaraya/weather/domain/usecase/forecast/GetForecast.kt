package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.util.Response

class GetForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(lat: Double, lon: Double): Response<Forecast> {
        return runCatching {
            val response = forecastRepo.getForecast(lat, lon, true)
            val cached = forecastRepo.getForecast(lat, lon, false)
            forecastRepo.insertForecast(response.copy(id = cached.id))
            Response.Success(forecastRepo.getForecast(lat, lon, false))
        }.getOrElse {
            runCatching {
                val response = forecastRepo.getForecast(lat, lon, false)
                Response.Error("Please check your connection", response)
            }.getOrElse {
                Response.Error(
                    "No cached data, please check your connection",
                    null
                )
            }
        }
    }
}