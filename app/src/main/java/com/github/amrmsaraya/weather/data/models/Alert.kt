package com.github.amrmsaraya.weather.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    @SerialName("sender_name")
    val senderName: String = "",
    @SerialName("event")
    val event: String = "",
    @SerialName("start")
    val start: Int = 0,
    @SerialName("end")
    val end: Int = 0,
    @SerialName("description")
    val description: String = "",
    @SerialName("tags")
    val tags: List<String> = listOf()
)
