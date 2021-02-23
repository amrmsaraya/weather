package com.github.amrmsaraya.weather.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repo: WeatherRepo) : ViewModel() {

    private val _weatherResponse =
        MutableStateFlow<WeatherRepo.ResponseState>(WeatherRepo.ResponseState.Empty)

    val weatherResponse = _weatherResponse


    fun insert(weatherResponse: WeatherResponse) = viewModelScope.launch {
        repo.insert(weatherResponse)
    }

    fun delete(weatherResponse: WeatherResponse) = viewModelScope.launch {
        repo.delete(weatherResponse)
    }

    fun deleteCurrent() = viewModelScope.launch {
        repo.deleteCurrent()
    }

    suspend fun getCachedLocationWeather(lat: Double, lon: Double): WeatherResponse {
        return repo.getCachedLocationWeather(lat, lon)
    }

    fun getAllCachedWeather(): Flow<List<WeatherResponse>> {
        return repo.getAllCachedWeather()
    }

    fun getLiveWeather(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ) {
        viewModelScope.launch {
            _weatherResponse.value = repo.getLive(lat, lon, units, lang)
        }
    }

}
