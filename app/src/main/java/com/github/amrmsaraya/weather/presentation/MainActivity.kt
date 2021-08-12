package com.github.amrmsaraya.weather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.github.amrmsaraya.weather.presentation.theme.WeatherTheme
import com.github.amrmsaraya.weather.util.Navigation
import com.github.amrmsaraya.weather.util.Screens
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        // Keep splash screen for 1 seconds
        var keepSplashScreen = true
        splashScreen.setKeepVisibleCondition { keepSplashScreen }
        lifecycleScope.launchWhenStarted {
            delay(1000)
            keepSplashScreen = false
        }

        setContent {
            WeatherTheme {
                Surface(
                    color = MaterialTheme.colors.surface,
                ) {
                    val navController = rememberAnimatedNavController()
                    Scaffold(
                        bottomBar = { BottomNav(navController = navController) },
                    ) { innerPadding ->
                        Navigation(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BottomNav(navController: NavHostController) {
    BottomNavigation(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        var selectedScreen by remember {
            mutableStateOf(Screens.Home.name)
        }

        for (screen in Screens.values()) {
            BottomNavigationItem(
                icon = {
                    when (selectedScreen == screen.name) {
                        true -> Icon(imageVector = screen.activeIcon, contentDescription = null)
                        false -> Icon(imageVector = screen.inactiveIcon, contentDescription = null)
                    }
                },
                label = { Text(text = screen.name) },
                selected = selectedScreen == screen.name,
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = Color.Gray,
                onClick = {
                    if (selectedScreen != screen.name) {
                        navController.navigate(screen.name)
                        selectedScreen = screen.name
                    }
                }
            )
        }
    }
}
