package com.github.amrmsaraya.weather.util

import com.github.amrmsaraya.weather.R
import io.ktor.client.features.*
import java.io.IOException

fun Throwable.toStringResource(): Int? {
    return when (this) {
        is IOException -> R.string.check_your_connection_and_try_again
        is ResponseException -> R.string.you_have_exceeded_limit
        else -> null
    }
}