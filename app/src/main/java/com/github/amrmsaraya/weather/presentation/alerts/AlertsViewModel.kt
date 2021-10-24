package com.github.amrmsaraya.weather.presentation.alerts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.usecase.alert.DeleteAlert
import com.github.amrmsaraya.weather.domain.usecase.alert.GetAlerts
import com.github.amrmsaraya.weather.domain.usecase.alert.InsertAlert
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetIntPreference
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getIntPreference: GetIntPreference,
    private val insertAlert: InsertAlert,
    private val deleteAlert: DeleteAlert,
    private val getAlerts: GetAlerts,
    private val dispatcher: IDispatchers
) : ViewModel() {

    val uiState = mutableStateOf(AlertsUiState())
    val intent = MutableStateFlow<AlertsIntent>(AlertsIntent.Init)

    init {
        mapIntent()
        intent.value = AlertsIntent.GetAccent(uiState.value)
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is AlertsIntent.GetAccent -> getAccent(it)
                is AlertsIntent.GetAlerts -> getAlerts(it)
                is AlertsIntent.InsertAlert -> insertAlert(it, it.alert)
                is AlertsIntent.DeleteAlerts -> deleteAlerts(it, it.alerts)
                AlertsIntent.Init -> Unit
            }
        }
    }

    private fun getAccent(alertsIntent: AlertsIntent) = viewModelScope.launch(dispatcher.default) {
        getIntPreference.execute("accent").collect {
            withContext(dispatcher.main) {
                uiState.value = alertsIntent.uiState.copy(accent = it)
                intent.value = AlertsIntent.GetAlerts(uiState.value)
            }
        }

    }

    private fun insertAlert(intent: AlertsIntent, alert: Alerts) =
        viewModelScope.launch(dispatcher.default) {
            insertAlert.execute(alert)
        }

    private fun deleteAlerts(intent: AlertsIntent, alerts: List<Alerts>) =
        viewModelScope.launch(dispatcher.default) {
            deleteAlert.execute(alerts)
        }

    private fun getAlerts(intent: AlertsIntent) = viewModelScope.launch(dispatcher.default) {
        getAlerts.execute().collect {
            withContext(dispatcher.main) {
                uiState.value = intent.uiState.copy(alerts = it)
            }
        }
    }
}
