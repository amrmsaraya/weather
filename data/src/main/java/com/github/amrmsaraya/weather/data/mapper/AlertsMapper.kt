package com.github.amrmsaraya.weather.data.mapper

import com.github.amrmsaraya.weather.data.model.AlertsDTO
import com.github.amrmsaraya.weather.domain.model.Alerts

fun AlertsDTO.toAlerts(): Alerts {
    return Alerts(
        id = id,
        from = from,
        to = to,
        isAlarm = isAlarm,
        workId = workId
    )
}

fun Alerts.toDTO(): AlertsDTO {
    return AlertsDTO(
        id = id,
        from = from,
        to = to,
        isAlarm = isAlarm,
        workId = workId
    )
}