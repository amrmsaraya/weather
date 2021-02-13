package com.github.amrmsaraya.weather.repositories

import android.util.Log
import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import com.github.amrmsaraya.weather.data.remote.RetrofitInstance
import com.github.amrmsaraya.weather.data.remote.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.io.IOException

class WeatherRepo(private val dao: WeatherDao) {

    private val retrofitService =
        RetrofitInstance.getRetrofitInstance().create(WeatherService::class.java)

    fun getCached(lat: Double, lon: Double): Flow<WeatherResponse> {
        return dao.getWeather(lat, lon)
    }

    suspend fun insert(weatherResponse: WeatherResponse) {
        dao.insertWeather(weatherResponse)
    }

    suspend fun delete(weatherResponse: WeatherResponse) {
        dao.deleteWeather(weatherResponse)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun getLive(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ): ResponseState {
        try {
            val response = retrofitService.getWeather(
                lat,
                lon,
                "minutely",
                units,
                lang,
                "22efb44963e22ae7005aac70558c7464"
            ).body()
            response
                ?.let { return ResponseState.Success(response) }
        } catch (e: IOException) {
            Log.e("Retrofit", "IOException, No Internet connection")
            return ResponseState.Error("No internet connection")
        } catch (e: HttpException) {
            Log.e("Retrofit", "HttpException, unexpected response")
            return ResponseState.Error("unexpected response")
        }
        return ResponseState.Empty
    }


    sealed class ResponseState {
        data class Success(val weatherResponse: WeatherResponse) : ResponseState()
        data class Error(val message: String) : ResponseState()
        object Empty : ResponseState()
    }
}

