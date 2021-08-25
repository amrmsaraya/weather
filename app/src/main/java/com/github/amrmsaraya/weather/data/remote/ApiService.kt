package com.github.amrmsaraya.weather.data.remote

import com.github.amrmsaraya.weather.data.models.forecast.Forecast
import com.github.amrmsaraya.weather.data.models.ForecastRequest
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ApiService @Inject constructor(private val client: HttpClient) {

    suspend fun getForecast(request: ForecastRequest): Forecast = client.get("onecall") {
        parameter("lat", request.lat)
        parameter("lon", request.lon)
        parameter("units", request.units)
        parameter("lang", request.lang)
    }

}
