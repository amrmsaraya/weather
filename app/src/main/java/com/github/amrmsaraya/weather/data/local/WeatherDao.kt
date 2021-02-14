package com.github.amrmsaraya.weather.data.local

import androidx.room.*
import com.github.amrmsaraya.weather.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    @Delete
    suspend fun deleteWeather(weatherResponse: WeatherResponse)

    @Query("DELETE FROM weather")
    suspend fun deleteAll()

    @Query("SELECT * From weather WHERE lat = :lat AND lon = :lon ")
    suspend fun getLocationWeather(lat: Double, lon: Double): WeatherResponse

    @Query("SELECT * From weather")
    fun getAllWeather(): Flow<List<WeatherResponse>>

}
