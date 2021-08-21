package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImp(private val weatherDao: WeatherDao) : LocalDataSource {

    override suspend fun insertForecast(forecast: Forecast) = weatherDao.insertForecast(forecast)

    override suspend fun deleteForecast(forecast: Forecast) = weatherDao.deleteForecast(forecast)

    override suspend fun getForecast(id: Long): Flow<Forecast> = weatherDao.getForecast(id)

    override suspend fun getCurrentForecast(): Flow<Forecast> = weatherDao.getCurrentForecast()

    override suspend fun getFavoriteForecasts(): Flow<List<Forecast>> = weatherDao.getFavoriteForecasts()
}
