package com.github.amrmsaraya.weather.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.favorite_details.FavoriteDetailsScreen
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.map.Maps
import com.github.amrmsaraya.weather.presentation.navigation.Screens.*
import com.github.amrmsaraya.weather.presentation.settings.Settings
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Home.route
    ) {
        composable(Home.route) {
            HomeScreen(
                modifier = modifier,
                onNavigateToMap = {
                    navController.navigate("${Maps.route}/true") {
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Favorites.route) {
            Favorites(
                modifier = modifier,
                onItemClick = { lat, lon ->
                    navController.navigate("${FavoriteDetails.route}/${lat.toFloat()}/${lon.toFloat()}") {
                        popUpTo(Favorites.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToMap = {
                    navController.navigate("${Maps.route}/false") {
                        popUpTo(Favorites.route)
                        launchSingleTop = true
                    }
                },
                onBackPress = { navController.popBackStack() }
            )
        }
        composable(Alerts.route) {
            Alert(
                modifier = modifier,
                onBackPress = { navController.popBackStack() }
            )
        }
        composable(Settings.route) {
            Settings(
                modifier = modifier,
                onMapClick = {
                    navController.navigate("${Maps.route}/true") {
                        popUpTo(Settings.route)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "${FavoriteDetails.route}/{lat}/{lon}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            FavoriteDetailsScreen(
                modifier = modifier,
                lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0,
                lon = backStackEntry.arguments?.getFloat("lon")?.toDouble() ?: 0.0,
            )
        }
        composable(
            route = "${Maps.route}/{isCurrent}",
            arguments = listOf(navArgument("isCurrent") { type = NavType.BoolType })
        ) { backStackEntry ->
            Maps(
                modifier = modifier,
                isCurrent = backStackEntry.arguments?.getBoolean("isCurrent") ?: true,
                onBackPress = { navController.popBackStack() }
            )
        }
    }
}
