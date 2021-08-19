package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentForecast: GetCurrentForecast,
) : ViewModel() {
    val isLoading = mutableStateOf(false)
    val forecast: MutableState<Forecast?> = mutableStateOf(null)

    fun getForecast(forecastRequest: ForecastRequest) = viewModelScope.launch {
        isLoading.value = true
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
                    else -> forecastResponse.result.collect {
                        forecast.value = it
                    }
                }
            }
        }
    }
}
