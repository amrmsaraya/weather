package com.github.amrmsaraya.weather.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amrmsaraya.weather.presentation.viewModel.AlarmViewModel
import com.github.amrmsaraya.weather.repositories.AlarmsRepo

class AlarmViewModelFactory(private val alarmsRepo: AlarmsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(alarmsRepo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
