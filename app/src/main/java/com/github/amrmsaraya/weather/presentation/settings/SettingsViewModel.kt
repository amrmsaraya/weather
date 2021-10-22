package com.github.amrmsaraya.weather.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.Settings
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.usecase.preferences.SavePreference
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePreference: SavePreference,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    init {
        restorePreferences()
    }

    val settings = mutableStateOf<Settings?>(null)

    fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        restorePreferences.execute().collect {
            withContext(dispatcher.main) {
                settings.value = it
            }
        }
    }

    fun savePreference(key: String, value: Int) = viewModelScope.launch(dispatcher.default) {
        savePreference.execute(key, value)
    }

    fun savePreference(key: String, value: Boolean) = viewModelScope.launch(dispatcher.default) {
        savePreference.execute(key, value)
    }
}
