package com.github.amrmsaraya.weather.domain.repository

import com.github.amrmsaraya.weather.domain.model.Alerts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAlertRepo : AlertRepo {
    val alertsList = mutableListOf<Alerts>()

    override suspend fun insert(alert: Alerts) {
        alertsList.add(alert)
    }

    override suspend fun delete(uuid: String) {
        alertsList.removeIf { it.workId == uuid }
    }

    override suspend fun delete(alerts: List<Alerts>) {
        alertsList.removeAll(alerts)
    }

    override suspend fun getAlert(uuid: String): Alerts {
        return alertsList.first { it.workId == uuid }
    }

    override suspend fun getAlerts(): Flow<List<Alerts>> {
        return flow {
            emit(alertsList)
        }
    }
}