package com.github.amrmsaraya.weather.domain.util

sealed class Response<out T> {
    data class Success<out T>(val result: T) : Response<T>()
    data class Error<out T>(val message: String, val result: T?) : Response<T>()
    object Loading : Response<Nothing>()
    object None : Response<Nothing>()
}