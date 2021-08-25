package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import javax.inject.Inject

class InsertAlert @Inject constructor(private val alertRepo: AlertRepo) {
    suspend fun execute(alert: Alerts) = alertRepo.insert(alert)
}
