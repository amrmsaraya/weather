package com.github.amrmsaraya.weather.presenter.viewModel

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

    fun deleteAll() = viewModelScope.launch {
        repo.deleteAll()
    }

    fun getCachedWeather(lat: Double, lon: Double): Flow<WeatherResponse> {
        return repo.getCached(lat, lon)
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
