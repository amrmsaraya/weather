package com.github.amrmsaraya.weather.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Alarm
import com.github.amrmsaraya.weather.repositories.AlarmsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlarmViewModel(private val alarmsRepo: AlarmsRepo) : ViewModel() {
    suspend fun insert(alarm: Alarm) = viewModelScope.launch {
        alarmsRepo.insert(alarm)
    }

    suspend fun delete(alarm: Alarm) = viewModelScope.launch {
        alarmsRepo.delete(alarm)
    }

    suspend fun update(alarm: Alarm) = viewModelScope.launch {
        alarmsRepo.update(alarm)
    }
    suspend fun getAlarmList(): List<Alarm> {
        return alarmsRepo.getAlarmList()
    }

    fun queryAll(): Flow<List<Alarm>> {
        return alarmsRepo.queryAll()
    }
}
