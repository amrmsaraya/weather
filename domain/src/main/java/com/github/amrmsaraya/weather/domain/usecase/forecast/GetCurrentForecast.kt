package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.util.Response

class GetCurrentForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(): Response<Forecast> {
        return runCatching {
            val cached = forecastRepo.getCurrentForecast()
            val response = forecastRepo.getCurrentForecast(cached.lat, cached.lon, true)
            forecastRepo.insertForecast(response.copy(id = 1))
            Response.Success(forecastRepo.getCurrentForecast())
        }.getOrElse {
            runCatching {
                val response = forecastRepo.getCurrentForecast()
                Response.Error("Please check your connection", response)
            }.getOrElse {
                Response.Error(
                    "No cached data, please check your connection",
                    null
                )
            }
        }
    }

    suspend fun execute(lat: Double, lon: Double): Response<Forecast> {
        return runCatching {
            val response = forecastRepo.getCurrentForecast(lat, lon, true)
            forecastRepo.insertForecast(response.copy(id = 1))
            val cached = forecastRepo.getCurrentForecast()
            Response.Success(cached)
        }.getOrElse {
            runCatching {
                val response = forecastRepo.getCurrentForecast()
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
