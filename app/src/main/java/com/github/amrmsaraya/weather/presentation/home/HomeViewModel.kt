package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.UiState
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

    val uiState = mutableStateOf<UiState<Forecast>>(UiState())
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(dispatcher) {
        uiState.value = when (val response = getCurrentForecast.execute(lat, lon)) {
            is Response.Success -> UiState(data = response.result)
            is Response.Error -> when (response.result) {
                null -> UiState(error = response.message)
                else -> UiState(data = response.result, error = response.message)
            }
            else -> UiState()
        }
    }

    fun getForecast() = viewModelScope.launch(dispatcher) {
        uiState.value = when (val response = getCurrentForecast.execute()) {
            is Response.Success ->UiState(data = response.result)
            is Response.Error -> when (response.result) {
                null -> UiState(error = response.message)
                else -> UiState(data = response.result, error = response.message)
            }
            else ->  UiState()
        }
    }

    fun restorePreferences() = viewModelScope.launch(dispatcher) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                settings.value = it
            }
        }
    }
}
