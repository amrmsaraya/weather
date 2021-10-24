package com.github.amrmsaraya.weather.presentation.favorite_details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.amrmsaraya.weather.presentation.components.LoadingIndicator
import com.github.amrmsaraya.weather.presentation.home.HomeContent
import com.github.amrmsaraya.weather.presentation.home.NoInternetConnection
import com.github.amrmsaraya.weather.util.toStringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun FavoriteDetailsScreen(
    modifier: Modifier,
    id: Long,
    viewModel: FavoriteDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val settings by viewModel.settings
    var swipeRefresh by remember { mutableStateOf(false) }
    val swipeRefreshState =
        rememberSwipeRefreshState(if (!uiState.isLoading) uiState.isLoading else swipeRefresh)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.getForecast(id)
    }

    settings?.let { setting ->
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState
        ) {
            uiState.throwable?.toStringResource()?.let {
                val error = stringResource(it)
                scope.launch {
                    viewModel.uiState.value = viewModel.uiState.value.copy(throwable = null)
                    scaffoldState.snackbarHostState.showSnackbar(error)
                }
            }

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    swipeRefresh = true
                    viewModel.getForecast(id)
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
                    when (it.current.weather.isNotEmpty()) {
                        true -> HomeContent(it, setting)
                        false -> NoInternetConnection { viewModel.getForecast(id) }
                    }
                } ?: LoadingIndicator()
            }
        }
    }
}
