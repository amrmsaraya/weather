package com.github.amrmsaraya.weather.util

data class UiState<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
    val isLoading: Boolean = false
)
