package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import javax.inject.Inject

class DeleteAlert @Inject constructor(private val alertRepo: AlertRepo) {
    suspend fun execute(uuid: String) = alertRepo.delete(uuid)
    suspend fun execute(alerts: List<Alerts>) = alertRepo.delete(alerts)
}
