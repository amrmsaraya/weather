package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.mapper.toForecast
import com.github.amrmsaraya.weather.data.remote.ApiService
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

class RemoteDataSourceImp(private val apiService: ApiService) : RemoteDataSource {

    override suspend fun getForecast(lat: Double, lon: Double): Forecast {
        return apiService.getForecast(lat, lon).toForecast()
    }
}
