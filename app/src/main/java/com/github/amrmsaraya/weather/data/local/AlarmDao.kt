package com.github.amrmsaraya.weather.data.local

import androidx.room.*
import com.github.amrmsaraya.weather.data.models.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    suspend fun getAlarmList(): List<Alarm>

    @Query("SELECT * FROM alarms")
    fun queryAll(): Flow<List<Alarm>>
}
