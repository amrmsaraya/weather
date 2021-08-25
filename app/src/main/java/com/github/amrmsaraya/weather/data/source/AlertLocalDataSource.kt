package com.github.amrmsaraya.weather.data.source

import com.github.amrmsaraya.weather.data.models.Alerts
import kotlinx.coroutines.flow.Flow
import java.util.*

interface AlertLocalDataSource {
    suspend fun insert(alert: Alerts)
    suspend fun delete(uuid:String)
    suspend fun delete(alerts: List<Alerts>)
    suspend fun getAlert(uuid: String): Alerts
    suspend fun getAlerts(): Flow<List<Alerts>>
}
