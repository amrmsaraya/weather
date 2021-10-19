package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailsViewModel @Inject constructor(
    private val getForecast: GetForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    init { restorePreferences() }

    val isLoading = mutableStateOf(false)
    val forecast: MutableState<Forecast> = mutableStateOf(Forecast())
    val error = mutableStateOf("")
    val settings = mutableStateOf<Settings?>(null)

    fun getForecast(lat: Double, lon: Double) = viewModelScope.launch(dispatcher) {
        when (val response = getForecast.execute(lat, lon)) {
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
        val preferences = restorePreferences.execute().first()
        withContext(Dispatchers.Main) {
            settings.value = preferences
        }
    }
}
