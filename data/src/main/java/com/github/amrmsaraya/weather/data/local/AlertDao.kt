package com.github.amrmsaraya.weather.data.local

import androidx.room.*
import com.github.amrmsaraya.weather.data.model.AlertsDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertsDTO)

    @Delete
    suspend fun delete(alerts: List<AlertsDTO>)

    @Query("DELETE FROM alerts WHERE work_id = :uuid")
    suspend fun delete(uuid: String)

    @Query("SELECT *  FROM alerts WHERE work_id = :uuid")
    suspend fun getAlert(uuid: String): AlertsDTO

    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertsDTO>>
}
