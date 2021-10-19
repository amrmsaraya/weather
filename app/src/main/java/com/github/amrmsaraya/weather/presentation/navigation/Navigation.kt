package com.github.amrmsaraya.weather.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.favorite_details.FavoriteDetailsScreen
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.home.HomeViewModel
import com.github.amrmsaraya.weather.presentation.map.Maps
import com.github.amrmsaraya.weather.presentation.navigation.Screens.*
import com.github.amrmsaraya.weather.presentation.settings.Settings
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
    NavHost(
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
                    navController.navigate("${FavoriteDetails.route}/${lat}/${lon}") {
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
                navArgument("lat") { type = NavType.StringType },
                navArgument("lon") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            FavoriteDetailsScreen(
                modifier = modifier,
                lat = backStackEntry.arguments?.getString("lat")?.toDouble() ?: 0.0,
                lon = backStackEntry.arguments?.getString("lon")?.toDouble() ?: 0.0,
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
