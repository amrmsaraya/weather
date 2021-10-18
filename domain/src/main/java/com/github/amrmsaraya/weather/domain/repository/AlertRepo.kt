package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.domain.model.Alerts
import kotlinx.coroutines.flow.Flow

interface AlertRepo {
    suspend fun insert(alert: Alerts)
    suspend fun delete(uuid: String)
    suspend fun delete(alerts: List<Alerts>)
    suspend fun getAlert(uuid: String): Alerts
    suspend fun getAlerts(): Flow<List<Alerts>>
}
