package com.github.amrmsaraya.weather.presentation.activity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.InsertForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetBooleanPreference
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import com.github.amrmsaraya.weather.domain.usecase.preferences.SetDefaultPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val restorePreferences: RestorePreferences,
    private val getBooleanPreference: GetBooleanPreference,
    private val setDefaultPreferences: SetDefaultPreferences,
    private val insertForecast: InsertForecast,
) : ViewModel() {

    val settings = mutableStateOf<Settings?>(null)
    val keepSplash = mutableStateOf(true)
    val firstRun = mutableStateOf(false)

    fun restorePreferences() = viewModelScope.launch {
        restorePreferences.execute().collect {
            settings.value = it
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
        setDefaultPreferences.execute(
            Settings(
                location = R.string.gps,
                language = if (Locale.getDefault().language == "ar") R.string.arabic else R.string.english,
                theme = R.string.default_,
                accent = 0,
                notifications = true,
                temperature = R.string.celsius,
                windSpeed = R.string.meter_sec,
            )
        )
        insertForecast.execute(Forecast(id = 1))
    }
}
