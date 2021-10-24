package com.github.amrmsaraya.weather.presentation.favorite_details

sealed class FavoriteDetailsIntent {
    object Idle : FavoriteDetailsIntent()
    object RestorePreferences : FavoriteDetailsIntent()
    object ClearThrowable : FavoriteDetailsIntent()
    data class GetForecast(val id: Long) : FavoriteDetailsIntent()
}