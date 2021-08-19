package com.github.amrmsaraya.weather.data.repositoryImp

import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class ForecastRepoImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ForecastRepo {
    override suspend fun getForecastFromApi(forecastRequest: ForecastRequest): Forecast {
        return remoteDataSource.getForecast(forecastRequest)

    }

    override suspend fun getForecast(id: Int): Response<Flow<Forecast>> {
        val forecast = localDataSource.getForecast(id)
        return when (forecast.firstOrNull()) {
            null -> Response.Error("No Cached Data, please check your connection", null)
            else -> Response.Error("No internet connection, cached data", forecast)
        }
    }

    override suspend fun insertForecast(forecast: Forecast) {
        localDataSource.insertForecast(forecast)
    }

    override suspend fun deleteForecast(forecast: Forecast) {
        localDataSource.deleteForecast(forecast)
    }

    override suspend fun getForecast(forecastRequest: ForecastRequest): Response<Flow<Forecast>> {
        return try {
            val forecast = getForecastFromApi(forecastRequest)
            localDataSource.insertForecast(forecast)
            Response.Success(localDataSource.getCurrentForecast())
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    val localForecast = localDataSource.getCurrentForecast()
                    when (localForecast.firstOrNull()) {
                        null -> Response.Error("Connection Error", null)
                        else -> Response.Error("Coonection Error, Cached data", localForecast)
                    }
                }
                else -> Response.None
            }
        }
    }

    override suspend fun getCurrentForecast(forecastRequest: ForecastRequest): Response<Flow<Forecast>> {
        return try {
            val forecast = getForecastFromApi(forecastRequest)
            localDataSource.insertForecast(forecast.copy(id = 1))
            Response.Success(localDataSource.getCurrentForecast())
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    val localForecast = localDataSource.getCurrentForecast()
                    when (localForecast.firstOrNull()) {
                        null -> Response.Error("Connection Error", null)
                        else -> Response.Error("Coonection Error, Cached data", localForecast)
                    }
                }
                else -> Response.None
            }
        }
    }

    override suspend fun getAllForecasts(): Response<Flow<Forecast>> {
        TODO("Not yet implemented")
    }
}
