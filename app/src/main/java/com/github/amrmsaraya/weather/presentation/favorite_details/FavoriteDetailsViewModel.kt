package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.UiState
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import com.github.amrmsaraya.weather.util.toStringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailsViewModel @Inject constructor(
    private val getForecast: GetForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    init {
        restorePreferences()
    }

    val uiState = mutableStateOf<UiState<Forecast>>(UiState())
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(id: Long) = viewModelScope.launch(dispatcher.default) {
        uiState.value = uiState.value.copy(error = null, isLoading = true)
        val response = getForecast.execute(id)
        withContext(dispatcher.main) {
            uiState.value = when (response) {
                is Response.Success -> UiState(data = response.result)
                is Response.Error -> when (response.result) {
                    null -> UiState()
                    else -> UiState(
                        data = response.result,
                        error = response.throwable.toStringResource()
                    )
                }
            }
        }
    }

    private fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        val preferences = restorePreferences.execute().first()
        withContext(Dispatchers.Main) {
            settings.value = preferences
        }
    }
}
