package com.github.amrmsaraya.weather.domain.usecase.alert

import com.github.amrmsaraya.weather.domain.repository.AlertRepo
import javax.inject.Inject

class GetAlert @Inject constructor(private val alertRepo: AlertRepo) {
    suspend fun execute(uuid: String) = alertRepo.getAlert(uuid)
}
