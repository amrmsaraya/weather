package com.github.amrmsaraya.weather.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class ForecastIcons(@StringRes val nameId: Int, @DrawableRes val icon: Int) {
    Pressure(R.string.pressure, R.drawable.speedometer),
    Cloud(R.string.cloud, R.drawable.cloud),
    Wind(R.string.wind, R.drawable.wind_speed),
    UltraViolet(R.string.ultra_violet, R.drawable.ultraviolet),
    Humidity(R.string.humidity, R.drawable.humidity),
    Visibility(R.string.visibility, R.drawable.visibility),
}
