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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentForecast: GetCurrentForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    init {
       restorePreferences()
    }

    val isLoading = mutableStateOf(false)
    val forecast: MutableState<Forecast> = mutableStateOf(Forecast())
    val error = mutableStateOf("")
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(dispatcher) {
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
            else -> isLoading.value = false
        }
    }

    fun getForecast() = viewModelScope.launch(dispatcher) {
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

    private fun restorePreferences() = viewModelScope.launch(dispatcher) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                settings.value = it
            }
        }
    }
}
