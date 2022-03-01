package com.github.amrmsaraya.weather.util.enums

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class Theme(@StringRes val stringRes: Int) {
    DEFAULT(R.string.default_),
    LIGHT(R.string.light),
    DARK(R.string.dark)
}