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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteForecasts: GetFavoriteForecasts,
    private val deleteForecast: DeleteForecast,
    private val restorePreferences: RestorePreferences,
) : ViewModel() {

    val favorites = mutableStateListOf<Forecast>()
    val settings = mutableStateOf<Settings?>(null)

    fun getFavoriteForecasts() = viewModelScope.launch {
        val response = getFavoriteForecasts.execute()
        response.collect {
            favorites.clear()
            favorites.addAll(it)
        }
    }

    fun deleteForecast(list: List<Forecast>) = viewModelScope.launch {
        deleteForecast.execute(list)
    }

    fun restorePreferences() = viewModelScope.launch {
        restorePreferences.execute().collect {
            settings.value = it
        }
    }

}
