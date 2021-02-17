package com.github.amrmsaraya.weather.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alarms")
data class Alarm(
    val start: Long,
    val end: Long,
    val type: String,
    var isChecked: Boolean = true,
    var isNotified: Boolean = false,
    var workId: UUID = UUID.randomUUID(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
