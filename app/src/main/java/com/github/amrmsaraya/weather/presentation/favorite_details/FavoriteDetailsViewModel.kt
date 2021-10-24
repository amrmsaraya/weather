package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetForecast
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.domain.util.Response
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailsViewModel @Inject constructor(
    private val getForecast: GetForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    private val _uiState = mutableStateOf(FavoriteDetailsUiState())
    val uiState: State<FavoriteDetailsUiState> = _uiState

    val intent = MutableStateFlow<FavoriteDetailsIntent>(FavoriteDetailsIntent.Idle)

    init {
        mapIntent()
        intent.value = FavoriteDetailsIntent.RestorePreferences
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is FavoriteDetailsIntent.GetForecast -> getForecast(it.id)
                is FavoriteDetailsIntent.RestorePreferences -> restorePreferences()
                is FavoriteDetailsIntent.ClearThrowable -> clearThrowable()
                is FavoriteDetailsIntent.Idle -> Unit
            }
            intent.value = FavoriteDetailsIntent.Idle
        }
    }

    private fun getForecast(id: Long) = viewModelScope.launch(dispatcher.default) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val response = getForecast.execute(id)
        withContext(dispatcher.main) {
            _uiState.value = when (response) {
                is Response.Success -> _uiState.value.copy(
                    forecast = response.result,
                    isLoading = false
                )
                is Response.Error -> _uiState.value.copy(
                    forecast = response.result,
                    throwable = response.throwable,
                    isLoading = false
                )
            }
        }
    }

    private fun clearThrowable() {
        _uiState.value = _uiState.value.copy(throwable = null)
    }

    fun restorePreferences() = viewModelScope.launch(dispatcher.default) {
        val settings = restorePreferences.execute().first()
        withContext(Dispatchers.Main) {
            _uiState.value = _uiState.value.copy(settings = settings)
        }
    }
}
