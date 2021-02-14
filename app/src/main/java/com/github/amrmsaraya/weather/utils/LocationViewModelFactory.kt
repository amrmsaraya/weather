package com.github.amrmsaraya.weather.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amrmsaraya.weather.presenter.viewModel.LocationViewModel
import com.github.amrmsaraya.weather.repositories.LocationRepo

class LocationViewModelFactory(private val locationRepo: LocationRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(locationRepo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
