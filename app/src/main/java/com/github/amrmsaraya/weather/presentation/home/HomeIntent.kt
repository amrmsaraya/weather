package com.github.amrmsaraya.weather.presentation.home

sealed class HomeIntent {
    object Idle : HomeIntent()
    object GetMapForecast : HomeIntent()
    object RestorePreferences : HomeIntent()
    object ClearThrowable : HomeIntent()
    data class GetLocationForecast(val lat: Double, val lon: Double) : HomeIntent()
}