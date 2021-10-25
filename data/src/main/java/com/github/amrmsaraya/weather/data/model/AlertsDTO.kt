package com.github.amrmsaraya.weather.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertsDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "start")
    val from: Long = 0,
    @ColumnInfo(name = "end")
    val to: Long = 0,
    @ColumnInfo(name = "type")
    val isAlarm: Boolean = true,
    @ColumnInfo(name = "work_id")
    var workId: String = "",
)
