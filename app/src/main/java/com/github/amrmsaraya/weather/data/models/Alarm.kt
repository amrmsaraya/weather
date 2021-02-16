package com.github.amrmsaraya.weather.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    val from: Long,
    val to: Long,
    val sound: Boolean,
    var isChecked: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
