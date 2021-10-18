package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo

class GetAlert(private val alertRepo: AlertRepo) {
    suspend fun execute(uuid: String): Alerts {
        return alertRepo.getAlert(uuid)
    }
}
