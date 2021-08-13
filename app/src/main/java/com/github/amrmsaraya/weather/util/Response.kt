package com.github.amrmsaraya.weather.util

sealed class Response<out T> {
    data class Success<out T>(val result: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
    object Loading : Response<Nothing>()
}
