package com.github.amrmsaraya.weather.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amrmsaraya.weather.repositories.WeatherRepo
import com.github.amrmsaraya.weather.presenter.viewModel.WeatherViewModel

class WeatherViewModelFactory(private val context: Context, private val repo: WeatherRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(context, repo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
