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
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentForecast: GetCurrentForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    val uiState = mutableStateOf<UiState<Forecast>>(UiState())
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(dispatcher.default) {
        uiState.value = uiState.value.copy(error = "", isLoading = true)
        val response = getCurrentForecast.execute(lat, lon)
        withContext(dispatcher.main) {
            uiState.value = when (response) {
                is Response.Success -> UiState(data = response.result)
                is Response.Error -> when (response.result) {
                    null -> UiState()
                    else -> UiState(data = response.result, error = response.message)
                }
            }
        }
    }

    fun getForecast() = viewModelScope.launch(dispatcher.default) {
        uiState.value = uiState.value.copy(error = "", isLoading = true)
        val response = getCurrentForecast.execute()
        withContext(dispatcher.main) {
            uiState.value = when (response) {
                is Response.Success -> UiState(data = response.result)
                is Response.Error -> when (response.result) {
                    null -> UiState()
                    else -> UiState(data = response.result, error = response.message)
                }
            }
        }

    }

    fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                settings.value = it
            }
        }
    }
}
