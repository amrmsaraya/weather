package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.local.AlertDao
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.source.AlertLocalDataSource

class AlertLocalDataSourceImp(private val alertDao: AlertDao) : AlertLocalDataSource {
    override suspend fun insert(alert: Alerts) {
        alertDao.insert(alert)
    }

    override suspend fun delete(uuid: String) {
        alertDao.delete(uuid)
    }

    override suspend fun delete(alerts: List<Alerts>) {
        alertDao.delete(alerts)
    }

    override suspend fun getAlert(uuid: String) = alertDao.getAlert(uuid)

    override suspend fun getAlerts() = alertDao.getAlerts()

}
