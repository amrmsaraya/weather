package com.github.amrmsaraya.weather.presentation.favorite_details

sealed class FavoriteDetailsIntent(open val uiState: FavoriteDetailsUiState = FavoriteDetailsUiState()) {

    object Init : FavoriteDetailsIntent()
    data class RestorePreferences(override val uiState: FavoriteDetailsUiState) :
        FavoriteDetailsIntent(uiState)

    data class GetForecast(
        override val uiState: FavoriteDetailsUiState,
        val id: Long,
    ) : FavoriteDetailsIntent(uiState)
}