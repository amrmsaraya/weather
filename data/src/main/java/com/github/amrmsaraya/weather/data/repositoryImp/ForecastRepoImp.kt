package com.github.amrmsaraya.weather.data.repositoryImp

import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import kotlinx.coroutines.flow.Flow

class ForecastRepoImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ForecastRepo {

    override suspend fun insertForecast(forecast: Forecast) {
        localDataSource.insertForecast(forecast)
    }

    override suspend fun deleteForecast(forecast: Forecast) {
        localDataSource.deleteForecast(forecast)
    }

    override suspend fun deleteForecast(list: List<Forecast>) {
        localDataSource.deleteForecast(list)
    }

    override suspend fun getForecast(lat: Double, lon: Double, forceUpdate: Boolean): Forecast {
        return when (forceUpdate) {
            true -> remoteDataSource.getForecast(lat, lon)
            false -> localDataSource.getForecast(lat, lon)
        }
    }

    override suspend fun getCurrentForecast(
        lat: Double,
        lon: Double,
        forceUpdate: Boolean
    ): Forecast {
        return when (forceUpdate) {
            true -> remoteDataSource.getForecast(lat, lon)
            false -> localDataSource.getCurrentForecast()
        }
    }

    override suspend fun getCurrentForecast(): Forecast {
        return localDataSource.getCurrentForecast()
    }

    override suspend fun getFavoriteForecasts(): Flow<List<Forecast>> {
        return localDataSource.getFavoriteForecasts()
    }
}
