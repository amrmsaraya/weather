package com.github.amrmsaraya.weather.presentation.map

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

sealed class MapIntent {
    object Idle : MapIntent()
    object GetCurrentForecast : MapIntent()
    data class GetForecastFromMap(val lat: Double, val lon: Double) : MapIntent()
    data class InsertForecast(val forecast: Forecast) : MapIntent()
}