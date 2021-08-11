package com.github.amrmsaraya.weather.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.settings.Settings
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@ExperimentalAnimationApi
@Composable
fun Navigation(modifier: Modifier = Modifier, navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.Home.name
    ) {
        composable(Screens.Home.name) {
            HomeScreen(modifier)
        }
        composable(Screens.Favorites.name) {
            Favorites(modifier)
        }
        composable(Screens.Alerts.name) {
            Alert(modifier)
        }
        composable(Screens.Settings.name) {
            Settings(modifier)
        }

    }

}
