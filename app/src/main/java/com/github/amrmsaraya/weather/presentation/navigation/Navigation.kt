package com.github.amrmsaraya.weather.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.amrmsaraya.weather.presentation.activity.MainViewModel
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.map.Maps
import com.github.amrmsaraya.weather.presentation.navigation.Screens.*
import com.github.amrmsaraya.weather.presentation.settings.Settings
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val viewModel: MainViewModel = viewModel()

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
