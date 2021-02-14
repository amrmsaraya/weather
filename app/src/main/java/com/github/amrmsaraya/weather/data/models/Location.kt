package com.github.amrmsaraya.weather.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    val lat: Double,
    val lon: Double,
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
