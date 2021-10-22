package com.github.amrmsaraya.weather.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.github.amrmsaraya.weather.data.model.forecast.ForecastDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertForecast(forecast: ForecastDTO)

    @Delete
    suspend fun deleteForecast(forecast: ForecastDTO)

    @Delete
    suspend fun deleteForecast(list: List<ForecastDTO>)

    @Query("SELECT * FROM forecast WHERE id = :id")
    fun getForecast(id: Long): ForecastDTO

    @Query("SELECT * FROM forecast WHERE id = 1")
    fun getCurrentForecast(): ForecastDTO

    @Query("SELECT * FROM forecast WHERE id != 1")
    fun getFavoriteForecasts(): Flow<List<ForecastDTO>>
}
