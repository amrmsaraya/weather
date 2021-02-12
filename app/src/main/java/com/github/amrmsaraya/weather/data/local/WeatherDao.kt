package com.github.amrmsaraya.weather.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.amrmsaraya.weather.data.models.WeatherResponse

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    @Delete
    suspend fun deleteWeather(weatherResponse: WeatherResponse)

    @Query("DELETE FROM weather")
    suspend fun deleteAll()

    @Query("SELECT * From weather WHERE lat = :lat AND lon = :lon ")
    fun getWeather(lat: Double, lon: Double): LiveData<WeatherResponse>
}
