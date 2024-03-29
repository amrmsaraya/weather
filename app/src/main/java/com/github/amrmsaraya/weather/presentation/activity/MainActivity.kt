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
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.github.amrmsaraya.weather.presentation.navigation.BottomNav
import com.github.amrmsaraya.weather.presentation.navigation.Navigation
import com.github.amrmsaraya.weather.presentation.theme.WeatherTheme
import com.github.amrmsaraya.weather.util.LocaleHelper
import com.github.amrmsaraya.weather.util.enums.Language
import com.github.amrmsaraya.weather.util.enums.Theme
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
    val firstRun = viewModel.firstRun
    val settings = viewModel.settings
    val keepSplash = viewModel.keepSplash
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    LaunchedEffect(key1 = keepSplash) {
        onLoadSettings(viewModel.keepSplash)
        if (firstRun) {
            viewModel.setDefaultPreferences()
            viewModel.savePreference("firstRun", false)
        }
    }

    LaunchedEffect(key1 = settings) {
        when (settings?.language) {
            Language.ARABIC.ordinal -> {
                if (Locale.getDefault().language != "ar") {
                    LocaleHelper.setLocale(context, Locale("ar"))
                    onLocaleChange()
                }
            }
            Language.ENGLISH.ordinal -> {
                if (Locale.getDefault().language != "en") {
                    LocaleHelper.setLocale(context, Locale("en"))
                    onLocaleChange()
                }
            }
        }

    }

    val darkTheme by derivedStateOf {
        when (settings?.theme) {
            Theme.LIGHT.ordinal -> false
            Theme.DARK.ordinal -> true
            else -> isSystemInDarkTheme
        }
    }

    WeatherTheme(
        darkTheme = darkTheme,
        colorIndex = settings?.accent ?: 0,
        language = settings?.language ?: Language.ENGLISH.ordinal
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
        ) {
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    BottomNav(
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

