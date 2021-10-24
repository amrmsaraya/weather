package com.github.amrmsaraya.weather.presentation.favorite_details

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

    val uiState = mutableStateOf(FavoriteDetailsUiState())
    val intent = MutableStateFlow<FavoriteDetailsIntent>(FavoriteDetailsIntent.Init)

    init {
        mapIntent()
        intent.value = FavoriteDetailsIntent.RestorePreferences(uiState.value)
    }

    private fun mapIntent() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is FavoriteDetailsIntent.GetForecast -> getForecast(it, it.id)
                is FavoriteDetailsIntent.RestorePreferences -> restorePreferences(it)
                else -> Unit
            }
            intent.value = FavoriteDetailsIntent.Init
        }
    }

    fun getForecast(intent: FavoriteDetailsIntent, id: Long) =
        viewModelScope.launch(dispatcher.default) {
            uiState.value = intent.uiState.copy(isLoading = true)
            val response = getForecast.execute(id)
            withContext(dispatcher.main) {
                uiState.value = when (response) {
                    is Response.Success -> intent.uiState.copy(forecast = response.result)
                    is Response.Error -> intent.uiState.copy(
                        forecast = response.result,
                        throwable = response.throwable
                    )
                }
            }
        }

    private fun restorePreferences(intent: FavoriteDetailsIntent) =
        viewModelScope.launch(dispatcher.default) {
            val settings = restorePreferences.execute().first()
            withContext(Dispatchers.Main) {
                uiState.value = intent.uiState.copy(settings = settings)
            }
        }

}
