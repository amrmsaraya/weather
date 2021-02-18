package com.github.amrmsaraya.weather.repositories

import androidx.lifecycle.LiveData
import com.github.amrmsaraya.weather.data.local.AlarmDao
import com.github.amrmsaraya.weather.data.models.Alarm
import kotlinx.coroutines.flow.Flow
import java.util.*

class AlarmsRepo(private val alarmDao: AlarmDao) {
    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    suspend fun getAlarm(id: UUID): Alarm {
        return alarmDao.getAlarm(id)
    }

    suspend fun getAlarmList(): List<Alarm> {
        return alarmDao.getAlarmList()
    }

    fun queryAll(): Flow<List<Alarm>> {
        return alarmDao.queryAll()
    }
}
