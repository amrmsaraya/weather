package com.github.amrmsaraya.weather.presentation.alerts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.component.AnimatedVisibilityFade
import com.github.amrmsaraya.weather.presentation.component.FABScaffold
import com.github.amrmsaraya.weather.presentation.favorites.EmptyListIndicator
import com.github.amrmsaraya.weather.presentation.favorites.FavoritesList
import com.github.amrmsaraya.weather.presentation.home.Daily
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun Alert(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()

    FABScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        onFABClick = { /*TODO*/ }
    ) {
        val favorites = remember {
            mutableStateListOf<Daily>()
        }

        LaunchedEffect(key1 = null) {
            delay(1000)
            favorites.add(
                Daily(
                    "32",
                    "16",
                    "Clear Sky",
                    "Tomorrow",
                    R.drawable.clear_day,
                    "Giza"
                )
            )
        }

        AnimatedVisibilityFade(favorites.isEmpty()) {
            EmptyListIndicator(Icons.Outlined.Notifications)
        }
        AnimatedVisibilityFade(favorites.isNotEmpty()) {
            FavoritesList(
                list = favorites,
                onClick = { }
            )
        }
    }
}
