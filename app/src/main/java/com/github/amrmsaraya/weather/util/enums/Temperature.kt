package com.github.amrmsaraya.weather.util.enums

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class Temperature(@StringRes val stringRes: Int) {
    Celsius(R.string.celsius),
    Kelvin(R.string.kelvin),
    Fahrenheit(R.string.fahrenheit)
}