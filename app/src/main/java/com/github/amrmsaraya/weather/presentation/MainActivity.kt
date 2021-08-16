package com.github.amrmsaraya.weather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.theme.WeatherTheme
import com.github.amrmsaraya.weather.util.Navigation
import com.github.amrmsaraya.weather.util.Screens.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            App()
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun App() {
    var isDarkTheme by remember { mutableStateOf("") }
    val darkTheme = when (isDarkTheme) {
        stringResource(id = R.string.light) -> false
        stringResource(id = R.string.dark) -> true
        else -> isSystemInDarkTheme()
    }

    WeatherTheme(darkTheme = darkTheme, theme = "Default") {
        Surface(
            color = MaterialTheme.colors.surface,
        ) {
            val navController = rememberAnimatedNavController()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    BottomNavigation(
                        navController = navController,
                    )
                },
            ) { innerPadding ->
                Navigation(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    isDarkTheme = { isDarkTheme = it }
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route != Maps.route) {
        BottomNav(
            navController = navController,
            currentDestination = currentDestination
        )
    }

}

@ExperimentalFoundationApi
@Composable
fun BottomNav(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        val screens = listOf(Home, Favorites, Alerts, Settings)

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
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
