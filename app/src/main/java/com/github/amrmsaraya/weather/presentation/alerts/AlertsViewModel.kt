package com.github.amrmsaraya.weather.presentation.alerts

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.domain.usecase.alert.DeleteAlert
import com.github.amrmsaraya.weather.domain.usecase.alert.GetAlerts
import com.github.amrmsaraya.weather.domain.usecase.alert.InsertAlert
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetIntPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getIntPreference: GetIntPreference,
    private val insertAlert: InsertAlert,
    private val deleteAlert: DeleteAlert,
    private val getAlerts: GetAlerts
) : ViewModel() {

    val accent = mutableStateOf(0)
    val alerts = mutableStateListOf<Alerts>()

    fun getPreference(key: String) = viewModelScope.launch {
        getIntPreference.execute(key).collect {
            if (key == "accent") {
                accent.value = it
            }
        }
    }

    fun insetAlert(alert: Alerts) = viewModelScope.launch {
        insertAlert.execute(alert)
    }

    fun deleteAlerts(alerts: List<Alerts>) = viewModelScope.launch {
        deleteAlert.execute(alerts)
    }

    fun getAlerts() = viewModelScope.launch {
        getAlerts.execute().collect {
            alerts.clear()
            alerts.addAll(it)
        }
    }
}
