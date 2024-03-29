package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.util.Response

class GetCurrentForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(forceUpdate: Boolean = true): Response<Forecast> {
        return runCatching {
            if (forceUpdate) {
                val cached = forecastRepo.getCurrentForecast()
                val response = forecastRepo.getCurrentForecast(cached.lat, cached.lon, true)
                forecastRepo.insertForecast(response.copy(id = 1))
            }
            Response.Success(forecastRepo.getCurrentForecast())
        }.getOrElse {
            runCatching {
                val response = forecastRepo.getCurrentForecast()
                Response.Error(it, response)
            }.getOrElse {
                Response.Error(it, null)
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
                Response.Error(it, response)
            }.getOrElse {
                Response.Error(it, null)
            }
        }
    }
}
