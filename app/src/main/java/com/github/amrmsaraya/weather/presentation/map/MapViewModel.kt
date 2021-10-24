package com.github.amrmsaraya.weather.presentation.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetCurrentForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecastFromMap
import com.github.amrmsaraya.weather.domain.usecase.forecast.InsertForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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
    private val _uiState = mutableStateOf(MapUiState())
    val uiState: State<MapUiState> = _uiState
    val intent = MutableStateFlow<MapIntent>(MapIntent.Idle)

    init {
        mapIntent()
        intent.value = MapIntent.GetCurrentForecast
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is MapIntent.GetCurrentForecast -> getCurrentForecast()
                is MapIntent.GetForecastFromMap -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = getForecastFromMap(it.lat, it.lon))
                }
                is MapIntent.InsertForecast -> {
                    _uiState.value = _uiState.value.copy(isLoading = insertForecast(it.forecast))
                }
                is MapIntent.Idle -> Unit
            }
            intent.value = MapIntent.Idle
        }
    }

    private fun insertForecast(forecast: Forecast) = viewModelScope.launch(dispatcher.default) {
        insertForecast.execute(forecast)
        savePreference.execute("location", R.string.map)
        _uiState.value = _uiState.value.copy(isLoading = null)
    }

    private fun getForecastFromMap(lat: Double, lon: Double) =
        viewModelScope.launch(dispatcher.default) {
            getForecastFromMap.execute(lat, lon)
            _uiState.value = _uiState.value.copy(isLoading = null)
        }

    private fun getCurrentForecast() = viewModelScope.launch(dispatcher.default) {
        val response = getCurrentForecast.execute(false)
        withContext(dispatcher.main) {
            _uiState.value = when (response) {
                is Response.Success -> _uiState.value.copy(forecast = response.result)
                is Response.Error -> _uiState.value.copy(
                    forecast = response.result ?: Forecast(),
                    throwable = response.throwable
                )
            }
        }
    }
}
