package com.github.amrmsaraya.weather.presentation.alerts

import com.github.amrmsaraya.weather.domain.model.Alerts

sealed class AlertsIntent(open val uiState: AlertsUiState = AlertsUiState()) {

    object Init : AlertsIntent()
    data class GetAccent(override val uiState: AlertsUiState) : AlertsIntent(uiState)

    data class InsertAlert(
        override val uiState: AlertsUiState,
        val alert: Alerts
    ) : AlertsIntent(uiState)

    data class DeleteAlerts(
        override val uiState: AlertsUiState,
        val alerts: List<Alerts>
    ) : AlertsIntent(uiState)

    data class GetAlerts(override val uiState: AlertsUiState) : AlertsIntent(uiState)
}