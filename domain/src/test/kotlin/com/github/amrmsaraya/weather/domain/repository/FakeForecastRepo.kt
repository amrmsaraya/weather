package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.domain.model.forecast.Current
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakeForecastRepo : ForecastRepo {

    val forecastList = mutableListOf<Forecast>()

    override suspend fun insertForecast(forecast: Forecast) {

        // Simulate OnConflictStrategy.REPLACE for Room
        val result = forecastList.map {
            if (it.id == forecast.id) forecast else it
        }
        if (result.toMutableList() == forecastList) {
            forecastList.add(forecast)
        } else {
            forecastList.clear()
            forecastList.addAll(result)
        }
    }

    override suspend fun deleteForecast(forecast: Forecast) {
        val result = forecastList.remove(forecast)
        if (!result) {
            throw NoSuchElementException()
        }
    }

    override suspend fun deleteForecast(list: List<Forecast>) {
        list.forEach {
            val result = forecastList.remove(it)
            if (!result) {
                throw NoSuchElementException()
            }
        }
    }

    override suspend fun getLocalForecast(id: Long): Forecast {
        val forecast = forecastList.firstOrNull { it.id == id }
        if (forecast != null) {
            return forecast
        } else {
            throw NoSuchElementException()
        }
    }

    override suspend fun getRemoteForecast(lat: Double, lon: Double): Forecast {
        return Forecast(
            lat = lat,
            lon = lon,
            current = Current(temp = 28.6)
        )
    }

    override suspend fun getCurrentForecast(
        lat: Double,
        lon: Double,
        forceUpdate: Boolean
    ): Forecast {
        return Forecast(
            id = 1,
            lat = lat,
            lon = lon,
            current = Current(temp = 28.6)
        )
    }

    override suspend fun getCurrentForecast(): Forecast {
        val forecast = forecastList.firstOrNull { it.id == 1L }
        if (forecast != null) {
            return forecast
        } else {
            throw NoSuchElementException()
        }
    }

    override suspend fun getFavoriteForecasts(): Flow<List<Forecast>> {
        return flow {
            emit(forecastList.filter { it.id != 1L })
        }
    }
}