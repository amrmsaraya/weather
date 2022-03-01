package com.github.amrmsaraya.weather.util.enums

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class Location(@StringRes val stringRes: Int) {
    GPS(R.string.gps),
    MAP(R.string.map)
}