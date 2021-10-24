package com.github.amrmsaraya.weather.presentation.favorites

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

sealed class FavoritesIntent(open val uiState: FavoritesUiState = FavoritesUiState()) {

    object Init : FavoritesIntent()
    data class RestorePreferences(override val uiState: FavoritesUiState) : FavoritesIntent(uiState)
    data class GetFavoritesForecast(override val uiState: FavoritesUiState) :
        FavoritesIntent(uiState)

    data class DeleteForecasts(
        override val uiState: FavoritesUiState,
        val favorites: List<Forecast>
    ) : FavoritesIntent(uiState)
}