package com.github.amrmsaraya.weather.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class ForecastIcons(@StringRes val nameId: Int, @DrawableRes val icon: Int) {
    Pressure(R.string.pressure, R.drawable.speedometer),
    Humidity(R.string.humidity, R.drawable.humidity),
    Wind(R.string.wind_speed, R.drawable.wind_speed),
    Cloud(R.string.cloud, R.drawable.cloud),
    UltraViolet(R.string.ultra_violet, R.drawable.ultraviolet),
    Visibility(R.string.visibility, R.drawable.visibility),
}
