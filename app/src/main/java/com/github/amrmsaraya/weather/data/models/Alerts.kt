package com.github.amrmsaraya.weather.data.models

import com.google.gson.annotations.SerializedName

data class Alerts(

    @SerializedName("sender_name") val sender_name: String = "",
    @SerializedName("event") val event: String = "",
    @SerializedName("start") val start: Int = 0,
    @SerializedName("end") val end: Int = 0,
    @SerializedName("description") val description: String = ""
)
