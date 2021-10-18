package com.github.amrmsaraya.weather.presentation.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecastFromMap
import com.github.amrmsaraya.weather.domain.usecase.forecast.InsertForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import com.github.amrmsaraya.weather.domain.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val insertForecast: InsertForecast,
    private val getForecastFromMap: GetForecastFromMap,
    private val getCurrentForecast: GetCurrentForecast,
) : ViewModel() {

    val currentForecast = mutableStateOf(Forecast())

    fun savePreference(key: String, value: Int) = viewModelScope.launch {
        savePreference.execute(key, value)
    }

    fun insertForecast(forecast: Forecast) = viewModelScope.launch(Dispatchers.Default) {
        insertForecast.execute(forecast)
    }

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(Dispatchers.Default) {
        getForecastFromMap.execute(lat, lon)
    }

    fun getCurrentForecast(lat: Double, lon: Double) = viewModelScope.launch(Dispatchers.Default) {
        getCurrentForecast.execute(lat, lon)
    }

    fun getCurrentForecast() = viewModelScope.launch(Dispatchers.Default) {
        when (val forecastResponse = getCurrentForecast.execute()) {
            is Response.Success -> {
                currentForecast.value = forecastResponse.result
            }
            is Response.Error -> {
                forecastResponse.result?.let {
                    currentForecast.value = it
                }
            }
        }
    }
}
