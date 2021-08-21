package com.github.amrmsaraya.weather.data.repositoryImp

import android.util.Log
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.data.source.LocalDataSource
import com.github.amrmsaraya.weather.data.source.RemoteDataSource
import com.github.amrmsaraya.weather.domain.repository.ForecastRepo
import com.github.amrmsaraya.weather.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class ForecastRepoImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ForecastRepo {
    override suspend fun getForecast(id: Long): Response<Flow<Forecast>> {
        return try {
            val cachedForecast = localDataSource.getForecast(id).first()
            val forecast =
                remoteDataSource.getForecast(
                    ForecastRequest(
                        cachedForecast.lat,
                        cachedForecast.lon
                    )
                )
            localDataSource.insertForecast(forecast.copy(id = 1))
            Response.Success(localDataSource.getForecast(id))
        } catch (exception: Exception) {
            val localForecast = localDataSource.getForecast(id)
            when (localForecast.firstOrNull()) {
                null -> Response.Error("No cached data, please check your connection", null)
                else -> Response.Error("Please check your connection", localForecast)
            }
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
            val forecast = remoteDataSource.getForecast(forecastRequest)
            localDataSource.insertForecast(forecast)
            Response.Success(localDataSource.getCurrentForecast())
        } catch (exception: Exception) {
            val localForecast = localDataSource.getCurrentForecast()
            when (localForecast.firstOrNull()) {
                null -> Response.Error("No cached data, please check your connection", null)
                else -> Response.Error("Please check your connection", localForecast)
            }
        }
    }

    override suspend fun getCurrentForecast(forecastRequest: ForecastRequest): Response<Flow<Forecast>> {
        return try {
            val forecast = remoteDataSource.getForecast(forecastRequest)
            localDataSource.insertForecast(forecast.copy(id = 1))
            Log.e("REFRESH", "SUCCESS")
            return Response.Success(localDataSource.getCurrentForecast())
        } catch (exception: Exception) {
            val localForecast = localDataSource.getCurrentForecast()
            when (localForecast.firstOrNull()) {
                null -> Response.Error("No cached data, please check your connection", null)
                else -> Response.Error("Please check your connection", localForecast)
            }
        }
    }

    override suspend fun getCurrentForecast(): Response<Flow<Forecast>> {
        return try {
            val cachedForecast = localDataSource.getCurrentForecast().first()
            val forecast =
                remoteDataSource.getForecast(
                    ForecastRequest(
                        cachedForecast.lat,
                        cachedForecast.lon
                    )
                )
            localDataSource.insertForecast(forecast.copy(id = 1))
            Response.Success(localDataSource.getCurrentForecast())
        } catch (exception: Exception) {
            exception.printStackTrace()
            val localForecast = localDataSource.getCurrentForecast()
            when (localForecast.firstOrNull()) {
                null -> Response.Error("No cached data, please check your connection", null)
                else -> Response.Error("Please check your connection", localForecast)
            }
        }
    }

    override suspend fun getFavoriteForecasts(): Response<Flow<List<Forecast>>> {
        return Response.Success(localDataSource.getFavoriteForecasts())
    }
}
