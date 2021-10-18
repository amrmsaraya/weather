package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo

class DeleteAlert(private val alertRepo: AlertRepo) {
    suspend fun execute(uuid: String) {
        alertRepo.delete(uuid)
    }

    suspend fun execute(alerts: List<Alerts>) {
        alertRepo.delete(alerts)
    }
}
