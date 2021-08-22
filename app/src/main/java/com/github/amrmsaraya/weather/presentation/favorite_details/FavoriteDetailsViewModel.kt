package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.data.models.Settings
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailsViewModel @Inject constructor(
    private val getForecast: GetForecast,
    private val restorePreferences: RestorePreferences,
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val forecast: MutableState<Forecast> = mutableStateOf(Forecast())
    val error = mutableStateOf("")
    val settings = mutableStateOf(Settings())

    fun getForecast(id: Long) = viewModelScope.launch {
        when (val response = getForecast.execute(id)) {
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
