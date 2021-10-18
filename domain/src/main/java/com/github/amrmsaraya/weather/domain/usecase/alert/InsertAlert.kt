package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo

class InsertAlert(private val alertRepo: AlertRepo) {
    suspend fun execute(alert: Alerts) {
        alertRepo.insert(alert)
    }
}
