package com.github.amrmsaraya.weather.presentation.favorites

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.amrmsaraya.weather.domain.model.forecast.Forecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.DeleteForecast
import com.github.amrmsaraya.weather.domain.usecase.forecast.GetFavoriteForecasts
import com.github.amrmsaraya.weather.domain.usecase.preferences.RestorePreferences
import com.github.amrmsaraya.weather.util.dispatchers.IDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteForecasts: GetFavoriteForecasts,
    private val deleteForecast: DeleteForecast,
    private val restorePreferences: RestorePreferences,
    private val dispatcher: IDispatchers
) : ViewModel() {

    val uiState = mutableStateOf(FavoritesUiState())
    val intent = MutableStateFlow<FavoritesIntent>(FavoritesIntent.Init)

    init {
        mapIntents()
        intent.value = FavoritesIntent.RestorePreferences(uiState.value)
    }

    private fun mapIntents() = viewModelScope.launch {
        intent.collect {
            when (it) {
                is FavoritesIntent.DeleteForecasts -> deleteForecast(it.favorites)
                is FavoritesIntent.GetFavoritesForecast -> getFavoriteForecasts(it)
                is FavoritesIntent.RestorePreferences -> restorePreferences(it)
                FavoritesIntent.Init -> Unit
            }
            intent.value = FavoritesIntent.Init
        }
    }


    private fun getFavoriteForecasts(intent: FavoritesIntent) =
        viewModelScope.launch(dispatcher.default) {
            val response = getFavoriteForecasts.execute()
            response.collect {
                withContext(dispatcher.main) {
                    uiState.value = intent.uiState.copy(favorites = it)
                }
            }
        }

    private fun deleteForecast(list: List<Forecast>) = viewModelScope.launch(dispatcher.default) {
        deleteForecast.execute(list)
    }

    private fun restorePreferences(favoritesIntent: FavoritesIntent) =
        viewModelScope.launch(dispatcher.default) {
            restorePreferences.execute().collect {
                withContext(Dispatchers.Main) {
                    uiState.value = favoritesIntent.uiState.copy(settings = it)
                    intent.value = FavoritesIntent.GetFavoritesForecast(uiState.value)
                }
            }
        }
}
