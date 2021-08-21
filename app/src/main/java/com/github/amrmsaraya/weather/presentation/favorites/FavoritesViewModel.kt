package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.DeleteForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetFavoriteForecasts
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecast
import com.github.amrmsaraya.weather.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteForecasts: GetFavoriteForecasts,
    private val getForecast: GetForecast,
    private val deleteForecast: DeleteForecast,
) : ViewModel() {

    val forecasts = mutableStateListOf<Forecast>()

    fun getFavoriteForecasts() = viewModelScope.launch {
        when (val response = getFavoriteForecasts.execute()) {
            is Response.Success -> response.result.collect {
                forecasts.clear()
                forecasts.addAll(it)
            }
        }
    }

    fun deleteForecast(forecast: Forecast) = viewModelScope.launch {
        deleteForecast.execute(forecast)
    }


}
