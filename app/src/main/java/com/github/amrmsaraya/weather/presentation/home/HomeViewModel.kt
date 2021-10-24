package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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

    val uiState = mutableStateOf(HomeUiState())
    val intent = MutableStateFlow<HomeIntent>(HomeIntent.Init)

    init {
        mapIntent()
        intent.value = HomeIntent.RestorePreferences(uiState.value)
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is HomeIntent.GetLocationForecast -> getLocationForecast(it, it.lat, it.lon)
                is HomeIntent.GetMapForecast -> getMapForecast(it)
                is HomeIntent.RestorePreferences -> restorePreferences(it)
                else -> Unit
            }
            intent.value = HomeIntent.Init
        }
    }

    private fun getLocationForecast(intent: HomeIntent, lat: Double, lon: Double) =
        viewModelScope.launch(dispatcher.default) {
            uiState.value = intent.uiState.copy(isLoading = true)
            val response = getCurrentForecast.execute(lat, lon)
            withContext(dispatcher.main) {
                uiState.value = when (response) {
                    is Response.Success -> intent.uiState.copy(forecast = response.result)
                    is Response.Error -> intent.uiState.copy(
                        forecast = response.result,
                        throwable = response.throwable
                    )
                }
            }
        }

    private fun getMapForecast(intent: HomeIntent) = viewModelScope.launch(dispatcher.default) {
        uiState.value = intent.uiState.copy(isLoading = true)
        val response = getCurrentForecast.execute()
        withContext(dispatcher.main) {
            uiState.value = when (response) {
                is Response.Success -> intent.uiState.copy(forecast = response.result)
                is Response.Error -> intent.uiState.copy(
                    forecast = response.result,
                    throwable = response.throwable
                )
            }
        }
    }

    fun restorePreferences(intent: HomeIntent) = viewModelScope.launch(dispatcher.default) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                uiState.value = intent.uiState.copy(settings = it)
            }
        }
    }
}
