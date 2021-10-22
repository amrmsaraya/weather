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
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteForecasts: GetFavoriteForecasts,
    private val deleteForecast: DeleteForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    init {
        getFavoriteForecasts()
        restorePreferences()
    }

    val favorites = mutableStateListOf<Forecast>()
    val settings = mutableStateOf<Settings?>(null)

    private fun getFavoriteForecasts() = viewModelScope.launch(dispatcher.default) {
        val response = getFavoriteForecasts.execute()
        response.collect {
            withContext(dispatcher.main) {
                favorites.clear()
                favorites.addAll(it)
            }
        }
    }

    fun deleteForecast(list: List<Forecast>) = viewModelScope.launch(dispatcher.default) {
        deleteForecast.execute(list)
    }

    private fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        val preferences = restorePreferences.execute().first()
        withContext(dispatcher.main) {
            settings.value = preferences
        }
    }
}
