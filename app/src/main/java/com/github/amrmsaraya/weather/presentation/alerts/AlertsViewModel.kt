package com.github.amrmsaraya.weather.presentation.alerts

import androidx.compose.runtime.State
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

    private val _uiState = mutableStateOf(AlertsUiState())
    val uiState: State<AlertsUiState> = _uiState
    val intent = MutableStateFlow<AlertsIntent>(AlertsIntent.Idle)

    init {
        mapIntent()
        intent.value = AlertsIntent.GetAccent
        intent.value = AlertsIntent.GetAlerts
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is AlertsIntent.GetAccent -> getAccent()
                is AlertsIntent.GetAlerts -> getAlerts()
                is AlertsIntent.InsertAlert -> insertAlert(it.alert)
                is AlertsIntent.DeleteAlerts -> deleteAlerts(it.alerts)
                is AlertsIntent.Idle -> Unit
            }
        }
    }

    private fun getAccent() = viewModelScope.launch(dispatcher.default) {
        getIntPreference.execute("accent").collect {
            withContext(dispatcher.main) {
                _uiState.value = _uiState.value.copy(accent = it)
            }
        }
    }

    private fun insertAlert(alert: Alerts) =
        viewModelScope.launch(dispatcher.default) {
            insertAlert.execute(alert)
        }

    private fun deleteAlerts(alerts: List<Alerts>) =
        viewModelScope.launch(dispatcher.default) {
            deleteAlert.execute(alerts)
        }

    private fun getAlerts() = viewModelScope.launch(dispatcher.default) {
        getAlerts.execute().collect {
            withContext(dispatcher.main) {
                _uiState.value = _uiState.value.copy(alerts = it)
            }
        }
    }
}
