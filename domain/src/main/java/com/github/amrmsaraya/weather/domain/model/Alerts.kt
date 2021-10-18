package com.github.amrmsaraya.weather.domain.model

data class Alerts(
    val id: Long = 0,
    val from: Long,
    val to: Long,
    val isAlarm: Boolean,
    var workId: String,
)
