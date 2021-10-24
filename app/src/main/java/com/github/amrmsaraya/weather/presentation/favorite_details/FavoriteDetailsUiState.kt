package com.github.amrmsaraya.weather.presentation.favorite_details

import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

data class FavoriteDetailsUiState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val forecast: Forecast? = null,
    val settings: Settings? = null,
)
