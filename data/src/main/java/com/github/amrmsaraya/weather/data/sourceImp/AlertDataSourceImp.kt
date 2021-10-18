package com.github.amrmsaraya.weather.data.sourceImp

import com.github.amrmsaraya.weather.data.local.AlertDao
import com.github.amrmsaraya.weather.data.mapper.toAlerts
import com.github.amrmsaraya.weather.data.mapper.toDTO
import com.github.amrmsaraya.weather.data.source.AlertDataSource
import com.github.amrmsaraya.weather.domain.model.Alerts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlertDataSourceImp(private val alertDao: AlertDao) : AlertDataSource {
    override suspend fun insert(alert: Alerts) {
        alertDao.insert(alert.toDTO())
    }

    override suspend fun delete(uuid: String) {
        alertDao.delete(uuid)
    }

    override suspend fun delete(alerts: List<Alerts>) {
        alertDao.delete(alerts.map { it.toDTO() })
    }

    override suspend fun getAlert(uuid: String): Alerts {
        return alertDao.getAlert(uuid).toAlerts()
    }

    override suspend fun getAlerts(): Flow<List<Alerts>> {
        return alertDao.getAlerts().map { list -> list.map { it.toAlerts() } }
    }
}
