package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.mapper.toDTO
import com.github.amrmsaraya.weather.data.mapper.toForecast
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImp(private val weatherDao: WeatherDao) : LocalDataSource {
    override suspend fun insertForecast(forecast: Forecast) {
        weatherDao.insertForecast(forecast.toDTO())
    }

    override suspend fun deleteForecast(forecast: Forecast) {
        weatherDao.deleteForecast(forecast.toDTO())
    }

    override suspend fun deleteForecast(list: List<Forecast>) {
        weatherDao.deleteForecast(list.map { it.toDTO() })
    }

    override suspend fun getForecast(id:Long): Forecast {
        return weatherDao.getForecast(id).toForecast()
    }

    override suspend fun getCurrentForecast(): Forecast {
        return weatherDao.getCurrentForecast().toForecast()
    }

    override suspend fun getFavoriteForecasts(): Flow<List<Forecast>> {
        return weatherDao.getFavoriteForecasts().map { list -> list.map { it.toForecast() } }
    }
}
