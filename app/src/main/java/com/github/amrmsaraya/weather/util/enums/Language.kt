package com.github.amrmsaraya.weather.util.enums

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class Language(@StringRes val stringRes: Int) {
    ENGLISH(R.string.english),
    ARABIC(R.string.arabic)
}