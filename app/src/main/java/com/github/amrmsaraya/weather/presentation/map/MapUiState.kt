package com.github.amrmsaraya.weather.presentation.map

import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import kotlinx.coroutines.Job

data class MapUiState(
    val isLoading: Job? = null,
    val throwable: Throwable? = null,
    val forecast: Forecast = Forecast(),
)
