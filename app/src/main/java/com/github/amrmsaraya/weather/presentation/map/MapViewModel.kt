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
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val insertForecast: InsertForecast,
    private val getForecastFromMap: GetForecastFromMap,
    private val getCurrentForecast: GetCurrentForecast,
    private val dispatcher: IDispatchers,
) : ViewModel() {

    init {
        getCurrentForecast()
    }

    val currentForecast = mutableStateOf(Forecast())

    fun savePreference(key: String, value: Int) = viewModelScope.launch(dispatcher.default) {
        savePreference.execute(key, value)
    }

    fun insertForecast(forecast: Forecast) = viewModelScope.launch(dispatcher.default) {
        insertForecast.execute(forecast)
    }

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(dispatcher.default) {
        getForecastFromMap.execute(lat, lon)
    }

    private fun getCurrentForecast() = viewModelScope.launch(dispatcher.default) {
        val forecastResponse = getCurrentForecast.execute(false)
        withContext(dispatcher.main) {
            currentForecast.value = when (forecastResponse) {
                is Response.Success -> forecastResponse.result
                is Response.Error -> forecastResponse.result ?: Forecast()
                else -> Forecast()
            }
        }
    }
}
