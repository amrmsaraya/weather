package com.github.amrmsaraya.weather.data.source.remote

import com.github.amrmsaraya.weather.data.model.Forecast
import com.github.amrmsaraya.weather.data.model.ForecastRequest
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ApiService @Inject constructor(private val client: HttpClient) {

    suspend fun getWeather(request: ForecastRequest): Forecast = client.get("onecall") {
        parameter("lat", request.lat)
        parameter("lon", request.lon)
        parameter("units", request.units)
        parameter("lang", request.lang)
//        parameter("appid", request.appid)
    }

}
