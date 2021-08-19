package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.data.remote.ApiService
import com.github.amrmsaraya.weather.data.source.RemoteDataSource

class RemoteDataSourceImp(private val apiService: ApiService) : RemoteDataSource {

    override suspend fun getForecast(forecastRequest: ForecastRequest): Forecast {
        return apiService.getForecast(forecastRequest)
    }
}
