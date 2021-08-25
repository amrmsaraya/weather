package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.data.models.Settings
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
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
    val settings = mutableStateOf(Settings())

    fun getForecast(forecastRequest: ForecastRequest) = viewModelScope.launch {
        when (val forecastResponse = getCurrentForecast.execute(forecastRequest)) {
            is Response.Success -> {
                isLoading.value = false
                forecastResponse.result.collect {
                    forecast.value = it
                }
            }
            is Response.Error -> {
                isLoading.value = false
                when (forecastResponse.result) {
                    null -> Unit
                    else -> {
                        forecast.value = forecastResponse.result.first()
                        error.value = forecastResponse.message
                    }
                }
            }
            else -> Unit
        }
    }

    fun getForecast() = viewModelScope.launch {
        when (val response = getCurrentForecast.execute()) {
            is Response.Success -> {
                isLoading.value = false
                response.result.collect {
                    forecast.value = it
                }
            }
            is Response.Error -> {
                isLoading.value = false
                when (response.result) {
                    null -> Unit
                    else -> response.result.collect {
                        forecast.value = it
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
