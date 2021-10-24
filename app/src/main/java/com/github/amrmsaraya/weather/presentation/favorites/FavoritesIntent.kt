package com.github.amrmsaraya.weather.presentation.favorites

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

sealed class FavoritesIntent {
    object Idle : FavoritesIntent()
    object RestorePreferences : FavoritesIntent()
    object GetFavoritesForecast : FavoritesIntent()
    data class DeleteForecasts(val favorites: List<Forecast>) : FavoritesIntent()
}