package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.github.amrmsaraya.weather.data.models.Settings
import com.github.amrmsaraya.weather.domain.usecase.forecast.DeleteForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetFavoriteForecasts
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.util.Response
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
    val settings = mutableStateOf(Settings())

    fun getFavoriteForecasts() = viewModelScope.launch {
        when (val response = getFavoriteForecasts.execute()) {
            is Response.Success -> response.result.collect {
                favorites.clear()
                favorites.addAll(it)
            }
        }
    }

    fun deleteForecast(forecast: Forecast) = viewModelScope.launch {
        deleteForecast.execute(forecast)
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
