package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.domain.model.Alerts
import kotlinx.coroutines.flow.Flow

interface AlertDataSource {
    suspend fun insert(alert: Alerts)
    suspend fun delete(uuid: String)
    suspend fun delete(alerts: List<Alerts>)
    suspend fun getAlert(uuid: String): Alerts
    suspend fun getAlerts(): Flow<List<Alerts>>
}
