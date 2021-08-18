package com.github.amrmsaraya.weather.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val restorePreferences: RestorePreferences,
) : ViewModel() {

    val location = mutableStateOf(R.string.gps)
    val language = mutableStateOf(R.string.english)
    val theme = mutableStateOf(R.string.default_)
    val accent = mutableStateOf(0)
    val notifications = mutableStateOf(true)
    val temperature = mutableStateOf(R.string.celsius)
    val windSpeed = mutableStateOf(R.string.meter_sec)

    fun restorePreferences() = viewModelScope.launch {
        restorePreferences.execute().collect { settings ->
            location.value = settings.location
            language.value = settings.language
            theme.value = settings.theme
            accent.value = settings.accent
            notifications.value = settings.notifications
            temperature.value = settings.temperature
            windSpeed.value = settings.windSpeed
        }
    }

    fun savePreference(key: String, value: Int) = viewModelScope.launch {
        savePreference.execute(key, value)
    }

    fun savePreference(key: String, value: Boolean) = viewModelScope.launch {
        savePreference.execute(key, value)
    }
}
