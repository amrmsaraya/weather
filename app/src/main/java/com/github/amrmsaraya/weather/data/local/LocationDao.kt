package com.github.amrmsaraya.weather.data.local

import androidx.room.*
import com.github.amrmsaraya.weather.data.models.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)

    @Query("SELECT * From location WHERE id = :id")
    fun getLocation(id: Int): Flow<Location>

    @Query("SELECT * From location")
    fun getAllLocations(): Flow<List<Location>>
}
