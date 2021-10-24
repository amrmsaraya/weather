package com.github.amrmsaraya.weather.util

import androidx.annotation.StringRes

data class UiState<T>(
    val data: T? = null,
    @StringRes val error: Int? = null,
    val isLoading: Boolean = false
)
