package com.github.amrmsaraya.weather.presenter.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import com.github.amrmsaraya.weather.repositories.WeatherApiRepo
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import kotlinx.coroutines.launch

class WeatherViewModel(private val context: Context, private val repo: WeatherRepo) : ViewModel() {

    private val weatherApiRepo = WeatherApiRepo(context)

    fun insert(weatherResponse: WeatherResponse) = viewModelScope.launch {
        repo.insert(weatherResponse)
    }

    fun delete(weatherResponse: WeatherResponse) = viewModelScope.launch {
        repo.delete(weatherResponse)
    }

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    fun getCachedWeather(lat: Double, lon: Double): LiveData<WeatherResponse> {
        return repo.getWeather(lat, lon)
    }

    fun getApiWeather(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ): LiveData<WeatherResponse> {
        return weatherApiRepo.getWeatherData(lat, lon, units, lang)
    }

}
