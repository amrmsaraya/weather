package com.github.amrmsaraya.weather.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.map.Maps
import com.github.amrmsaraya.weather.presentation.settings.Settings
import com.github.amrmsaraya.weather.util.Screens.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Home.route
    ) {
        composable(Home.route) {
            HomeScreen(modifier)
        }
        composable(Favorites.route) {
            Favorites(modifier)
        }
        composable(Alerts.route) {
            Alert(modifier)
        }
        composable(Settings.route) {
            Settings(
                modifier,
                onMapClick = {
                    navController.navigate(Maps.route) {
                        popUpTo(Settings.route)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Maps.route) {
            Maps(modifier)
        }

    }

}
