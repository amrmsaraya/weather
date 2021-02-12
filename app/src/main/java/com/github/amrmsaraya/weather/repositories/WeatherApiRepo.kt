package com.github.amrmsaraya.weather.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.data.remote.RetrofitInstance
import com.github.amrmsaraya.weather.data.remote.WeatherService
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import retrofit2.HttpException
import java.io.IOException

class WeatherApiRepo(private val context: Context) {

    private val retrofitService =
        RetrofitInstance.getRetrofitInstance().create(WeatherService::class.java)

    fun getWeatherData(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ): LiveData<WeatherResponse> {
        return liveData {
            try {
                val response = retrofitService.getWeather(
                    lat,
                    lon,
                    "minutely",
                    units,
                    lang,
                    context.getString(R.string.open_weather_map_app_id)
                )
                response.body()?.let { emit(it) }
            } catch (e: IOException) {
                Log.e("Retrofit", "IOException, No Internet connection")
                Toast.makeText(
                    context,
                    "No internet connection",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: HttpException) {
                Log.e("Retrofit", "HttpException, unexpected response")
            }
        }
    }
}
