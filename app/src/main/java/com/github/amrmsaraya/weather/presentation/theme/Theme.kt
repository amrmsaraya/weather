package com.github.amrmsaraya.weather.presentation.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Blue,
    primaryVariant = Blue,
    secondary = Pink,
    secondaryVariant = Pink
)

private val LightColorPalette = lightColors(
    primary = Blue,
    primaryVariant = Blue,
    secondary = Pink,
    secondaryVariant = Pink
)


private val DarkColorPalette1 = darkColors(
    primary = Primary1,
    primaryVariant = Primary1,
    secondary = Secondary1,
    secondaryVariant = Secondary1
)

private val LightColorPalette1 = lightColors(
    primary = Primary1,
    primaryVariant = Primary1,
    secondary = Secondary1,
    secondaryVariant = Secondary1
)

private val DarkColorPalette2 = darkColors(
    primary = Primary2,
    primaryVariant = Primary2,
    secondary = Secondary2,
    secondaryVariant = Secondary2
)

private val LightColorPalette2 = lightColors(
    primary = Primary2,
    primaryVariant = Primary2,
    secondary = Secondary2,
    secondaryVariant = Secondary2
)

private val DarkColorPalette3 = darkColors(
    primary = Primary3,
    primaryVariant = Primary3,
    secondary = Secondary3,
    secondaryVariant = Secondary3
)

private val LightColorPalette3 = lightColors(
    primary = Primary3,
    primaryVariant = Primary3,
    secondary = Secondary3,
    secondaryVariant = Secondary3
)

private val DarkColorPalette4 = darkColors(
    primary = Primary4,
    primaryVariant = Primary4,
    secondary = Secondary4,
    secondaryVariant = Secondary4
)

private val LightColorPalette4 = lightColors(
    primary = Primary4,
    primaryVariant = Primary4,
    secondary = Secondary4,
    secondaryVariant = Secondary4
)

private val DarkColorPalette5 = darkColors(
    primary = Primary5,
    primaryVariant = Primary5,
    secondary = Secondary5,
    secondaryVariant = Secondary5
)

private val LightColorPalette5 = lightColors(
    primary = Primary5,
    primaryVariant = Primary5,
    secondary = Secondary5,
    secondaryVariant = Secondary5
)


val colorsList = listOf(
    ColorPalette(LightColorPalette, DarkColorPalette),
    ColorPalette(LightColorPalette1, DarkColorPalette1),
    ColorPalette(LightColorPalette2, DarkColorPalette2),
    ColorPalette(LightColorPalette3, DarkColorPalette3),
    ColorPalette(LightColorPalette4, DarkColorPalette4),
    ColorPalette(LightColorPalette5, DarkColorPalette5),
)

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val colors =
        if (darkTheme) colorsList[colorIndex].darkPalette else colorsList[colorIndex].lightPalette

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = colors.surface)
    }

    when (darkTheme) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


data class ColorPalette(
    val lightPalette: Colors,
    val darkPalette: Colors
)
