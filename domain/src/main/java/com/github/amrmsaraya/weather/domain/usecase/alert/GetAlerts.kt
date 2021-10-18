package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.model.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import kotlinx.coroutines.flow.Flow

class GetAlerts(private val alertRepo: AlertRepo) {
    suspend fun execute(): Flow<List<Alerts>> {
        return alertRepo.getAlerts()
    }
}
