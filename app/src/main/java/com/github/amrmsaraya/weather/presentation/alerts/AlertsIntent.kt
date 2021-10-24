package com.github.amrmsaraya.weather.presentation.alerts

import com.github.amrmsaraya.weather.domain.model.Alerts

sealed class AlertsIntent {
    object Idle : AlertsIntent()
    object GetAccent : AlertsIntent()
    data class InsertAlert(val alert: Alerts) : AlertsIntent()
    data class DeleteAlerts(val alerts: List<Alerts>) : AlertsIntent()
    object GetAlerts : AlertsIntent()
}