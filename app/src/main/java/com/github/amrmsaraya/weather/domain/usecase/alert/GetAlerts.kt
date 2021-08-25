package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import javax.inject.Inject

class GetAlerts @Inject constructor(private val alertRepo: AlertRepo) {
    suspend fun execute() = alertRepo.getAlerts()
}
