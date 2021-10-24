package com.github.amrmsaraya.weather.presentation.home

sealed class HomeIntent(open val uiState: HomeUiState = HomeUiState()) {

    object Init : HomeIntent()
    data class GetMapForecast(override val uiState: HomeUiState) : HomeIntent(uiState)
    data class RestorePreferences(override val uiState: HomeUiState) : HomeIntent(uiState)
    data class GetLocationForecast(
        override val uiState: HomeUiState,
        val lat: Double,
        val lon: Double
    ) : HomeIntent(uiState)
}