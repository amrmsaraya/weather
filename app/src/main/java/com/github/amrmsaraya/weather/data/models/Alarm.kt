package com.github.amrmsaraya.weather.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey()
    val id: UUID,
    val start: Long,
    val end: Long,
    val type: String,
    var workId: UUID = UUID(0, 0),
)
