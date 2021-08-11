package com.github.amrmsaraya.weather.util

import androidx.annotation.DrawableRes
import com.github.amrmsaraya.weather.R

enum class ForecastDetailsEnum (@DrawableRes val icon: Int) {
    Pressure(R.drawable.speedometer),
    Humidity(R.drawable.humidity),
    Wind(R.drawable.wind_speed),
    Cloud(R.drawable.cloud),
    UltraViolet(R.drawable.ultraviolet),
    Visibility(R.drawable.visibility),
}
