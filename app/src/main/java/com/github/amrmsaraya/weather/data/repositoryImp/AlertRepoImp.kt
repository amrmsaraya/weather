package com.github.amrmsaraya.weather.data.repositoryImp

import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.source.AlertLocalDataSource
import com.github.amrmsaraya.weather.domain.repository.AlertRepo

class AlertRepoImp(private val alertLocalDataSource: AlertLocalDataSource) : AlertRepo {
    override suspend fun insert(alert: Alerts) {
        alertLocalDataSource.insert(alert)
    }

    override suspend fun delete(uuid: String) {
        alertLocalDataSource.delete(uuid)
    }

    override suspend fun delete(alerts: List<Alerts>) {
        alertLocalDataSource.delete(alerts)
    }

    override suspend fun getAlert(uuid: String) = alertLocalDataSource.getAlert(uuid)

    override suspend fun getAlerts() = alertLocalDataSource.getAlerts()

}
