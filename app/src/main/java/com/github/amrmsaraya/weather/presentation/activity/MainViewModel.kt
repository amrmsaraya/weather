package com.github.amrmsaraya.weather.presentation.activity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetBooleanPreference
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetIntPreference
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import com.github.amrmsaraya.weather.domain.usecase.preferences.SetDefaultPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val getIntPreference: GetIntPreference,
    private val getBooleanPreference: GetBooleanPreference,
    private val setDefaultPreferences: SetDefaultPreferences
) : ViewModel() {

    val theme = mutableStateOf(R.string.default_)
    val accent = mutableStateOf(0)
    val keepSplash = mutableStateOf(true)
    val firstRun = mutableStateOf(false)

    fun getIntPreference(key: String) = viewModelScope.launch {
        getIntPreference.execute(key).collect {
            when (key) {
                "theme" -> theme.value = it
                "accent" -> accent.value = it
            }
            keepSplash.value = false
        }
    }

    fun getBooleanPreference(key: String) = viewModelScope.launch {
        getBooleanPreference.execute(key).collect {
            firstRun.value = it
        }
    }

    fun savePreference(key: String, value: String) = viewModelScope.launch {
        savePreference.execute(key, value)
    }

    fun savePreference(key: String, value: Boolean) = viewModelScope.launch {
        savePreference.execute(key, value)
    }

    fun setDefaultPreferences() = viewModelScope.launch {
        setDefaultPreferences.execute()
    }
}
