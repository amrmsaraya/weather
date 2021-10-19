package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.presentation.components.LoadingIndicator
import com.github.amrmsaraya.weather.presentation.home.HomeContent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun FavoriteDetailsScreen(
    modifier: Modifier,
    lat: Double,
    lon: Double,
    viewModel: FavoriteDetailsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val forecast by viewModel.forecast
    val settings by viewModel.settings
    var forecastRequested by remember { mutableStateOf(false) }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
        if (error.isNotEmpty()) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = error)
                viewModel.error.value = ""
            }
        }
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.isLoading.value = true
                viewModel.getForecast(lat, lon)
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    contentColor = MaterialTheme.colors.secondary
                )
            },
        ) {
            if (!forecastRequested) {
                viewModel.getForecast(lat, lon)
                forecastRequested = true
            }
            if (settings == null) LoadingIndicator()
            when (forecast.current.weather.isEmpty()) {
                true -> LoadingIndicator()
                false -> HomeContent(forecast, settings!!)
            }
        }
    }
}
