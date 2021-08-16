package com.github.amrmsaraya.weather.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
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

@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: String = "Default",
    content: @Composable () -> Unit
) {
    var colors = LightColorPalette

    colors = when (theme) {
        "1" -> if (darkTheme) DarkColorPalette1 else LightColorPalette1
        else -> if (darkTheme) DarkColorPalette else LightColorPalette
    }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = colors.surface)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
