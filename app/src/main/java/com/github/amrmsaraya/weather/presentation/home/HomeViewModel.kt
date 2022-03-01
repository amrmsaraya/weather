package com.github.amrmsaraya.weather.presentation.home

import androidx.compose.runtime.State
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

    private val _uiState = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState
    val intent = MutableStateFlow<HomeIntent>(HomeIntent.Idle)

    init {
        mapIntent()
        intent.value = HomeIntent.RestorePreferences
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is HomeIntent.GetLocationForecast -> getLocationForecast(it.lat, it.lon)
                is HomeIntent.GetMapForecast -> getMapForecast()
                is HomeIntent.RestorePreferences -> restorePreferences()
                is HomeIntent.ClearThrowable -> clearThrowable()
                is HomeIntent.Idle -> Unit
            }
            intent.value = HomeIntent.Idle
        }
    }

    private fun getLocationForecast(lat: Double, lon: Double) =
        viewModelScope.launch(dispatcher.default) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val response = getCurrentForecast.execute(lat, lon)
            withContext(dispatcher.main) {
                _uiState.value = when (response) {
                    is Response.Success -> _uiState.value.copy(
                        forecast = response.result,
                        isLoading = false
                    )
                    is Response.Error -> _uiState.value.copy(
                        forecast = response.result,
                        throwable = response.throwable,
                        isLoading = false
                    )
                }
            }
        }

    private fun getMapForecast() = viewModelScope.launch(dispatcher.default) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val response = getCurrentForecast.execute()
        withContext(dispatcher.main) {
            _uiState.value = when (response) {
                is Response.Success -> _uiState.value.copy(
                    forecast = response.result,
                    isLoading = false
                )
                is Response.Error -> _uiState.value.copy(
                    forecast = response.result,
                    throwable = response.throwable,
                    isLoading = false
                )
            }
        }
    }

    private fun clearThrowable() {
        _uiState.value = _uiState.value.copy(throwable = null)
    }

    private fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(settings = it)
            }
        }
    }
}
