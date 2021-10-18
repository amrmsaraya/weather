package com.github.amrmsaraya.weather.data.remote

import com.github.amrmsaraya.weather.data.model.forecast.ForecastDTO
import io.ktor.client.*
import io.ktor.client.request.*
import java.util.*

class ApiService(private val client: HttpClient) {

    suspend fun getForecast(lat: Double, lon: Double): ForecastDTO = client.get("onecall") {
        parameter("lat", lat)
        parameter("lon", lon)
        parameter("units", "metric")
        parameter("lang", Locale.getDefault().language)
    }

}
