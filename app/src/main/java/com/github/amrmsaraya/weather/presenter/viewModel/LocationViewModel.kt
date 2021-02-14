package com.github.amrmsaraya.weather.presenter.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.data.models.Location
import com.github.amrmsaraya.weather.repositories.LocationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepo: LocationRepo) : ViewModel() {

    fun insert(location: Location) = viewModelScope.launch {
        locationRepo.insert(location)
    }

    fun delete(location: Location) = viewModelScope.launch {
        locationRepo.delete(location)
    }

    fun getLocation(id: Int): Flow<Location> {
        return locationRepo.getLocation(id)
    }

}
