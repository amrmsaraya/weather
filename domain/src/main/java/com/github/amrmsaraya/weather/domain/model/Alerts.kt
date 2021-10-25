package com.github.amrmsaraya.weather.domain.model

data class Alerts(
    val id: Long = 0,
    val from: Long = 0,
    val to: Long = 0,
    val isAlarm: Boolean = true,
    var workId: String = "",
)
