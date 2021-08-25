package com.github.amrmsaraya.weather.data.local

import androidx.room.*
import com.github.amrmsaraya.weather.data.models.Alerts
import com.github.amrmsaraya.weather.data.models.forecast.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: Alerts)

    @Delete
    suspend fun delete(alerts: List<Alerts>)

    @Query("DELETE FROM alerts WHERE work_id = :uuid")
    suspend fun delete(uuid: String)

    @Query("SELECT *  FROM alerts WHERE work_id = :uuid")
    suspend fun getAlert(uuid: String): Alerts

    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<Alerts>>
}
