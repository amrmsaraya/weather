package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentForecast: GetCurrentForecast,
    private val restorePreferences: RestorePreferences,
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val forecast: MutableState<Forecast> = mutableStateOf(Forecast())
    val error = mutableStateOf("")
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(Dispatchers.Default) {
        when (val forecastResponse = getCurrentForecast.execute(lat, lon)) {
            is Response.Success -> {
                isLoading.value = false
                forecast.value = forecastResponse.result
            }
            is Response.Error -> {
                isLoading.value = false
                when (forecastResponse.result) {
                    null -> Unit
                    else -> {
                        forecast.value = forecastResponse.result!!
                        error.value = forecastResponse.message
                    }
                }
            }
            else -> Unit
        }
    }

    fun getForecast() = viewModelScope.launch(Dispatchers.Default) {
        when (val response = getCurrentForecast.execute()) {
            is Response.Success -> {
                isLoading.value = false
                forecast.value = response.result
            }
            is Response.Error -> {
                isLoading.value = false
                when (response.result) {
                    null -> Unit
                    else -> {
                        forecast.value = response.result!!
                        error.value = response.message
                    }
                }
            }
        }
    }

    fun restorePreferences() = viewModelScope.launch {
        restorePreferences.execute().collect {
            settings.value = it
        }
    }
}
