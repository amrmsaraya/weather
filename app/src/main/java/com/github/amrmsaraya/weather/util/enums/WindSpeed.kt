package com.github.amrmsaraya.weather.util.enums

import androidx.annotation.StringRes
import com.github.amrmsaraya.weather.R

enum class WindSpeed(@StringRes val stringRes: Int) {
    METER_SECOND(R.string.meter_sec),
    MILE_HOUR(R.string.mile_hour)
}