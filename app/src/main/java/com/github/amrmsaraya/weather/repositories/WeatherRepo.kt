package com.github.amrmsaraya.weather.repositories

import androidx.lifecycle.LiveData
import com.github.amrmsaraya.weather.data.local.WeatherDao
import com.github.amrmsaraya.weather.data.models.WeatherResponse

class WeatherRepo(private val dao: WeatherDao) {
    fun getWeather(lat: Double, lon: Double): LiveData<WeatherResponse> {
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
}
