package com.github.amrmsaraya.weather.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.github.amrmsaraya.weather.data.models.Forecast
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertForecast(forecast: Forecast)

    @Delete
    suspend fun deleteForecast(forecast: Forecast)

    @Query("SELECT * FROM forecast WHERE id = 1")
    fun getCurrentForecast(): Flow<Forecast>

    @Query("SELECT * FROM forecast WHERE id = :id")
    fun getForecast(id: Int): Flow<Forecast>

    @Query("SELECT * FROM forecast WHERE id != 1")
    fun getAllForecasts(): Flow<List<Forecast>>

}
