package com.github.amrmsaraya.weather.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alerts(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "start")
    val from: Long,
    @ColumnInfo(name = "end")
    val to: Long,
    @ColumnInfo(name = "type")
    val isAlarm: Boolean,
    @ColumnInfo(name = "work_id")
    var workId: String,
)
