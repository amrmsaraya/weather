package com.github.amrmsaraya.weather.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amrmsaraya.weather.presenter.viewModel.SharedViewModel

class SharedViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
