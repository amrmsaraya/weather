package com.github.amrmsaraya.weather.presentation.alerts

import com.github.amrmsaraya.weather.domain.model.Alerts

data class AlertsUiState(
    val alerts: List<Alerts> = emptyList(),
    val accent: Int = 0
)
