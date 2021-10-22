package com.github.amrmsaraya.weather.util.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface IDispatchers {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}