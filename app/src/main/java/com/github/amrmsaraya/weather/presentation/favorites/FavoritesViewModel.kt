package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.DeleteForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetFavoriteForecasts
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteForecasts: GetFavoriteForecasts,
    private val deleteForecast: DeleteForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val favorites = mutableStateListOf<Forecast>()
    val settings = mutableStateOf<Settings?>(null)

    fun getFavoriteForecasts() = viewModelScope.launch(dispatcher) {
        val response = getFavoriteForecasts.execute()
        response.collect {
            withContext(Dispatchers.Main) {
                favorites.clear()
                favorites.addAll(it)
            }
        }
    }

    fun deleteForecast(list: List<Forecast>) = viewModelScope.launch(dispatcher) {
        deleteForecast.execute(list)
    }

    fun restorePreferences() = viewModelScope.launch(dispatcher) {
        restorePreferences.execute().collect {
            withContext(Dispatchers.Main) {
                settings.value = it
            }
        }
    }
}
