package com.github.amrmsaraya.weather.repositories

import com.github.amrmsaraya.weather.data.local.AlarmDao
import com.github.amrmsaraya.weather.data.models.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmsRepo(private val alarmDao: AlarmDao) {
    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    fun queryAll(): Flow<List<Alarm>> {
        return alarmDao.queryAll()
    }
}
