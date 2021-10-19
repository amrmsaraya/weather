package com.github.amrmsaraya.weather.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.github.amrmsaraya.weather.presentation.alerts.Alert
import com.github.amrmsaraya.weather.presentation.alerts.AlertsViewModel
import com.github.amrmsaraya.weather.presentation.favorite_details.FavoriteDetailsScreen
import com.github.amrmsaraya.weather.presentation.favorite_details.FavoriteDetailsViewModel
import com.github.amrmsaraya.weather.presentation.favorites.Favorites
import com.github.amrmsaraya.weather.presentation.favorites.FavoritesViewModel
import com.github.amrmsaraya.weather.presentation.home.HomeScreen
import com.github.amrmsaraya.weather.presentation.home.HomeViewModel
import com.github.amrmsaraya.weather.presentation.map.MapViewModel
import com.github.amrmsaraya.weather.presentation.map.Maps
import com.github.amrmsaraya.weather.presentation.navigation.Screens.*
import com.github.amrmsaraya.weather.presentation.settings.Settings
import com.github.amrmsaraya.weather.presentation.settings.SettingsViewModel
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
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToMap = {
                    navController.navigate("${Maps.route}/true") {
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Favorites.route) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            Favorites(
                modifier = modifier,
                viewModel = viewModel,
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
            val viewModel = hiltViewModel<AlertsViewModel>()
            Alert(
                modifier = modifier,
                viewModel = viewModel,
                onBackPress = { navController.popBackStack() }
            )
        }
        composable(Settings.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            Settings(
                modifier = modifier,
                viewModel = viewModel,
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
            val viewModel = hiltViewModel<FavoriteDetailsViewModel>()
            FavoriteDetailsScreen(
                modifier = modifier,
                viewModel = viewModel,
                lat = backStackEntry.arguments?.getString("lat")?.toDouble() ?: 0.0,
                lon = backStackEntry.arguments?.getString("lon")?.toDouble() ?: 0.0,
            )
        }
        composable(
            route = "${Maps.route}/{isCurrent}",
            arguments = listOf(navArgument("isCurrent") { type = NavType.BoolType })
        ) { backStackEntry ->
            val viewModel = hiltViewModel<MapViewModel>()
            Maps(
                modifier = modifier,
                viewModel = viewModel,
                isCurrent = backStackEntry.arguments?.getBoolean("isCurrent") ?: true,
                onBackPress = { navController.popBackStack() }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BottomNav(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(Home, Favorites, Alerts, Settings)

    if (currentDestination?.route in screens.map { it.route }) {
        Column {
            Divider(thickness = (0.5).dp)
            BottomNavigationImpl(
                navController = navController,
                currentDestination = currentDestination,
                screens = screens
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BottomNavigationImpl(
    navController: NavHostController,
    currentDestination: NavDestination?,
    screens: List<Screens>
) {
    val context = LocalContext.current
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        for (screen in screens) {
            BottomNavigationItem(
                selected = currentDestination?.route == screen.route,
                icon = {
                    when (currentDestination?.route == screen.route) {
                        true -> Icon(imageVector = screen.activeIcon, contentDescription = null)
                        false -> Icon(imageVector = screen.inactiveIcon, contentDescription = null)
                    }
                },
                label = { Text(text = stringResource(id = screen.stringId)) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = Color.Gray,
                onClick = {
                    if (currentDestination?.route != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
