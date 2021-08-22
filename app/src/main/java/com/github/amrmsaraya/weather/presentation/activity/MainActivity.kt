package com.github.amrmsaraya.weather.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.amrmsaraya.weather.R
import com.github.amrmsaraya.weather.presentation.navigation.Navigation
import com.github.amrmsaraya.weather.presentation.navigation.Screens
import com.github.amrmsaraya.weather.presentation.navigation.Screens.*
import com.github.amrmsaraya.weather.presentation.theme.WeatherTheme
import com.github.amrmsaraya.weather.util.LocaleHelper
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        var keepSplash by mutableStateOf(true)
        splash.setKeepVisibleCondition { keepSplash }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            App(
                onLoadSettings = { keepSplash = it },
                onLocaleChange = { recreate() }
            )
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val context: Context = LocaleHelper.setLocale(newBase, Locale.getDefault())
        super.attachBaseContext(context)
    }
}

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun App(
    viewModel: MainViewModel = viewModel(),
    onLocaleChange: () -> Unit,
    onLoadSettings: (Boolean) -> Unit
) {
    viewModel.getBooleanPreference("firstRun")
    viewModel.restorePreferences()

    val firstRun by viewModel.firstRun
    val settings by viewModel.settings

    SideEffect {
        onLoadSettings(viewModel.keepSplash.value)
        if (firstRun) {
            viewModel.setDefaultPreferences()
            viewModel.savePreference("firstRun", false)
        }
    }

    when (settings.language) {
        R.string.arabic -> {
            if (Locale.getDefault().language != "ar") {
                LocaleHelper.setLocale(LocalContext.current, Locale("ar"))
                onLocaleChange()
            }
        }
        else -> {
            if (Locale.getDefault().language != "en") {
                LocaleHelper.setLocale(LocalContext.current, Locale("en"))
                onLocaleChange()
            }
        }
    }

    val darkTheme = when (settings.theme) {
        R.string.light -> false
        R.string.dark -> true
        else -> isSystemInDarkTheme()
    }

    WeatherTheme(darkTheme = darkTheme, colorIndex = settings.accent) {
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
                    navController = navController
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

    val screens = listOf(Home, Favorites, Alerts, Settings)

    if (currentDestination?.route in screens.map { it.route }) {
        BottomNav(
            navController = navController,
            currentDestination = currentDestination,
            screens = screens
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun BottomNav(
    navController: NavHostController,
    currentDestination: NavDestination?,
    screens: List<Screens>
) {
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
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
