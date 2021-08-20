package com.github.amrmsaraya.weather.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savePreference: SavePreference
) : ViewModel() {
    fun savePreference(key: String, value: Int) = viewModelScope.launch {
        savePreference.execute(key, value)
    }
}
