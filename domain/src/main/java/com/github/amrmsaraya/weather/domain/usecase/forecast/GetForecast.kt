package com.github.amrmsaraya.weather.domain.usecase.forecast

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.domain.util.Response

class GetForecast(private val forecastRepo: ForecastRepo) {
    suspend fun execute(id: Long): Response<Forecast> {
        return runCatching {
            val cached = forecastRepo.getLocalForecast(id)
            val response = forecastRepo.getRemoteForecast(cached.lat, cached.lon)
            forecastRepo.insertForecast(response.copy(id = id))
            Response.Success(forecastRepo.getLocalForecast(id))
        }.getOrElse {
            runCatching {
                val response = forecastRepo.getLocalForecast(id)
                Response.Error("Please check your connection", response)
            }.getOrElse {
                it.printStackTrace()
                Response.Error(
                    "No cached data, please check your connection",
                    null
                )
            }
        }
    }
}