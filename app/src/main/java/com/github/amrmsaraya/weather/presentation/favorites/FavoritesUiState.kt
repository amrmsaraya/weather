package com.github.amrmsaraya.weather.presentation.favorites

import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

data class FavoritesUiState(
    val favorites: List<Forecast> = emptyList(),
    val settings: Settings? = null,
)
