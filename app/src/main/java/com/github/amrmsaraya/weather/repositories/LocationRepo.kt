package com.github.amrmsaraya.weather.repositories

import com.github.amrmsaraya.weather.data.local.LocationDao
import com.github.amrmsaraya.weather.data.models.Location
import kotlinx.coroutines.flow.Flow

class LocationRepo(private val locationDao: LocationDao) {
    suspend fun insert(location: Location) {
        locationDao.insertLocation(location)
    }

    suspend fun delete(location: Location) {
        locationDao.deleteLocation(location)
    }

    fun getLocation(id: Int): Flow<Location> {
        return locationDao.getLocation(id)
    }

    fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations()
    }
}
