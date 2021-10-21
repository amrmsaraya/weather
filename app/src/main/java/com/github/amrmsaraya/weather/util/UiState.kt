package com.github.amrmsaraya.weather.util

data class UiState<T>(
    val data: T? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
