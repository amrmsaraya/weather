package com.github.amrmsaraya.weather.presentation.alerts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.usecase.preferences.GetIntPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getIntPreference: GetIntPreference,
) : ViewModel() {

    val accent = mutableStateOf(0)

    fun getPreference(key: String) = viewModelScope.launch {
        getIntPreference.execute(key).collect {
            if (key == "accent") {
                accent.value = it
            }
        }
    }
}
