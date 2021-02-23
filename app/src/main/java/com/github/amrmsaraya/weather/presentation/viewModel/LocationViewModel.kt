package com.github.amrmsaraya.weather.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.repositories.LocationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepo: LocationRepo) : ViewModel() {

    fun insert(location: Location) = viewModelScope.launch {
        locationRepo.insert(location)
    }

    fun delete(location: Location) = viewModelScope.launch {
        locationRepo.delete(location)
    }

    suspend fun getCurrentLocation(): Location {
        return locationRepo.getCurrentLocation()
    }

    fun getLocation(id: Int): Flow<Location> {
        return locationRepo.getLocation(id)
    }

    fun getAllLocations(): Flow<List<Location>> {
        return locationRepo.getAllLocations()
    }

}
