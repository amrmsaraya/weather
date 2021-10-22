package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
    val uiState by viewModel.uiState
    val settings by viewModel.settings

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.getForecast(lat, lon)
    }

    settings?.let { setting ->
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState
        ) {
            if (uiState.error.isNotEmpty()) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(uiState.error)
                }
            }
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.uiState.value =
                        viewModel.uiState.value.copy(isLoading = true, error = "")
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
                uiState.data?.let {
                    HomeContent(it, setting)
                } ?: LoadingIndicator()
            }
        }
    }
}
