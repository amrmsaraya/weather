package com.github.amrmsaraya.weather.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amrmsaraya.weather.presentation.viewModel.WeatherViewModel
import com.github.amrmsaraya.weather.repositories.WeatherRepo

class WeatherViewModelFactory(private val weatherRepo: WeatherRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
