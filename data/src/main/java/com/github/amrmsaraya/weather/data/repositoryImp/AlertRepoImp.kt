package com.github.amrmsaraya.weather.data.repositoryImp

import com.github.amrmsaraya.weather.data.source.AlertDataSource
import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import kotlinx.coroutines.flow.Flow

class AlertRepoImp(private val alertDataSource: AlertDataSource) : AlertRepo {
    override suspend fun insert(alert: Alerts) {
        alertDataSource.insert(alert)
    }

    override suspend fun delete(uuid: String) {
        alertDataSource.delete(uuid)
    }

    override suspend fun delete(alerts: List<Alerts>) {
        alertDataSource.delete(alerts)
    }

    override suspend fun getAlert(uuid: String): Alerts {
        return alertDataSource.getAlert(uuid)
    }

    override suspend fun getAlerts(): Flow<List<Alerts>> {
        return alertDataSource.getAlerts()
    }
}
