package com.github.amrmsaraya.weather.presentation.home

import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast

data class HomeUiState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val forecast: Forecast? = null,
    val settings: Settings? = null,
)
